package net.madz.lifecycle.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import net.madz.lifecycle.LifecycleCommonErrors;
import net.madz.lifecycle.LifecycleException;

import org.junit.Test;

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
        for ( int i = 0; i < 100; i++ ) {
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
            if ( i % 2 == 0 ) {
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
                        assertFalse(( null != e1 && null != e2 ) || ( null == e1 && null == e2 ));
                        final LifecycleException e = null != e1 ? e1 : e2;
                        System.out.println(e.toString());
                        assertEquals(LifecycleCommonErrors.ILLEGAL_TRANSITION_ON_STATE, e.getErrorCode());
                        assertEquals(2, object.getCounter());
                        return null;
                    } catch (Exception e) {
                        return e;
                    }
                }
            };
            final Future<Exception> f3 = executorService.submit(c3);
            if ( null != f3.get() ) {
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
                            while ( indicator.intValue() != 3 ) {
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
                        while ( indicator.intValue() != 2 ) {
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
