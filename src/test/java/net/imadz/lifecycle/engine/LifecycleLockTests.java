/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright (c) 2013-2020 Madz. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License"). You
 * may not use this file except in compliance with the License. You can
 * obtain a copy of the License at
 * https://raw.github.com/zhongdj/Lifecycle/master/License.txt
 * . See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 * 
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 * 
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license." If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above. However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package net.imadz.lifecycle.engine;

import net.imadz.lifecycle.LifecycleCommonErrors;
import net.imadz.lifecycle.LifecycleException;
import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

public class LifecycleLockTests extends LifecycleLockTestMetadata {

  @Test
  public void should_support_synchronized_lock() throws Throwable {
    doConcurrent(SynchronizedLockingReactiveObject.class);
  }

  @Test
  public void should_support_simple_lock() throws Throwable {
    doConcurrent(SimpleLockingReactiveObject.class);
  }

  private void doConcurrent(Class<? extends ILockingReactiveObject> klass) throws Throwable {
    final ExecutorService executorService = Executors.newFixedThreadPool(7);
    for (int i = 0; i < 100; i++) {
      final ILockingReactiveObject object = klass.newInstance();
      object.start();
      Callable<LifecycleException> c1 = new Callable<LifecycleException>() {

        @Override
        public LifecycleException call() throws Exception {
          try {
            object.stop();
            return null;
          } catch (LifecycleException e) {
            return e;
          }
        }
      };
      Callable<LifecycleException> c2 = new Callable<LifecycleException>() {

        @Override
        public LifecycleException call() throws Exception {
          try {
            object.cancel();
            return null;
          } catch (LifecycleException e) {
            return e;
          }
        }
      };
      if (i % 2 == 0) {
        Callable<LifecycleException> temp = c1;
        c1 = c2;
        c2 = temp;
      }
      final Future<LifecycleException> f1 = executorService.submit(c1);
      final Future<LifecycleException> f2 = executorService.submit(c2);
      final Callable<Exception> c3 = new Callable<Exception>() {

        @Override
        public Exception call() throws Exception {
          try {
            final LifecycleException e1 = f1.get();
            final LifecycleException e2 = f2.get();
            assertFalse((null != e1 && null != e2) || (null == e1 && null == e2));
            final LifecycleException e = null != e1 ? e1 : e2;
            System.out.println(e.toString());
            assertEquals(LifecycleCommonErrors.ILLEGAL_EVENT_ON_STATE, e.getErrorCode());
            assertEquals(2, object.getCounter());
            return null;
          } catch (Exception e) {
            return e;
          }
        }
      };
      final Future<Exception> f3 = executorService.submit(c3);
      if (null != f3.get()) {
        fail(f3.get().getMessage());
      }
    }
  }

  @Test
  public void should_support_relational_locking() {
    final CustomerObject customer = new CustomerObject();
    final ContractObject contract = new ContractObject(customer);
    final OrderObject order = new OrderObject(contract);
    final ResourceObject resource = new ResourceObject();
    customer.confirm();
    contract.confirm();
    contract.startService();
    order.confirm();
    resource.test();
    resource.goLive();
    order.startProduce(resource);
    order.startPackage();
    order.deliver();
    order.complete();
    assertState(OrderStateMachine.States.Finished.class, order);
  }

  @Test(expected = LifecycleException.class)
  public void should_throw_state_invalid_exception_if_relational_locking_concurrent_cancel_parent_first() throws Throwable {
    final CustomerObject customer = new CustomerObject();
    customer.confirm();
    final ContractObject contract = new ContractObject(customer);
    final OrderObject order = new OrderObject(contract);
    final ResourceObject resource = new ResourceObject();
    resource.test();
    resource.goLive();
    final ReentrantLock lock = new ReentrantLock();
    final Condition condition = lock.newCondition();
    final AtomicInteger indicator = new AtomicInteger(1);
    final Callable<LifecycleException> c1 = new Callable<LifecycleException>() {

      @Override
      public LifecycleException call() throws Exception {
        try {
          contract.confirm();
          contract.startService();
          order.confirm();
          order.startProduce(resource);
          order.startPackage();
          { // control test flow
            try {
              lock.lock();
              indicator.incrementAndGet();
              condition.signal();
              while (indicator.intValue() != 3) {
                condition.await();
              }
            } finally {
              lock.unlock();
            }
          }
          order.deliver();
          order.complete();
          assertState(OrderStateMachine.States.Finished.class, order);
          return null;
        } catch (LifecycleException e) {
          return e;
        }
      }
    };
    final Callable<Void> c2 = new Callable<Void>() {

      @Override
      public Void call() throws Exception {
        { // control test flow
          try {
            lock.lock();
            while (indicator.intValue() != 2) {
              condition.await();
            }
          } finally {
            lock.unlock();
          }
        }
        customer.cancel();
        { // control test flow
          try {
            lock.lock();
            indicator.incrementAndGet();
            condition.signal();
          } finally {
            lock.unlock();
          }
        }
        return null;
      }
    };
    final ExecutorService executorService = Executors.newFixedThreadPool(2);
    final Future<LifecycleException> f1 = executorService.submit(c1);
    executorService.submit(c2);
    assertInvalidStateErrorByValidWhile(f1.get(), customer, contract, CustomerStateMachine.States.Confirmed.ConfirmedStates.InService.class);
  }
}
