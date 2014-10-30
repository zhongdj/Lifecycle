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

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Logger;

import net.imadz.lifecycle.LifecycleLockStrategry;
import net.imadz.lifecycle.annotations.CompositeState;
import net.imadz.lifecycle.annotations.Function;
import net.imadz.lifecycle.annotations.Functions;
import net.imadz.lifecycle.annotations.LifecycleLock;
import net.imadz.lifecycle.annotations.LifecycleMeta;
import net.imadz.lifecycle.annotations.StateMachine;
import net.imadz.lifecycle.annotations.StateSet;
import net.imadz.lifecycle.annotations.Event;
import net.imadz.lifecycle.annotations.EventSet;
import net.imadz.lifecycle.annotations.relation.InboundWhile;
import net.imadz.lifecycle.annotations.relation.InboundWhiles;
import net.imadz.lifecycle.annotations.relation.Parent;
import net.imadz.lifecycle.annotations.relation.RelateTo;
import net.imadz.lifecycle.annotations.relation.Relation;
import net.imadz.lifecycle.annotations.relation.RelationSet;
import net.imadz.lifecycle.annotations.relation.ValidWhile;
import net.imadz.lifecycle.annotations.relation.ValidWhiles;
import net.imadz.lifecycle.annotations.state.End;
import net.imadz.lifecycle.annotations.state.Initial;
import net.imadz.lifecycle.annotations.state.LifecycleOverride;
import net.imadz.lifecycle.annotations.state.ShortCut;
import net.imadz.verification.VerificationException;

import org.junit.BeforeClass;

public class LifecycleLockTestMetadata extends EngineTestBase {

    @BeforeClass
    public static void setup() throws VerificationException {
        registerMetaFromClass(LifecycleLockTestMetadata.class);
    }

    @StateMachine
    static interface LockingStateMachine {

        @StateSet
        static interface States {

            @Initial
            @Function(event = LockingStateMachine.Events.Start.class, value = Started.class)
            static interface Created {}
            @Functions({ @Function(event = LockingStateMachine.Events.Stop.class, value = Stopped.class),
                    @Function(event = LockingStateMachine.Events.Cancel.class, value = Canceled.class) })
            static interface Started {}
            @End
            static interface Stopped {}
            @End
            static interface Canceled {}
        }
        @EventSet
        static interface Events {

            static interface Start {}
            static interface Stop {}
            static interface Cancel {}
        }
    }
    static interface ILockingReactiveObject {

        public abstract int getCounter();

        public abstract void start();

        public abstract void stop();

        public abstract void cancel();
    }
    @LifecycleMeta(LockingStateMachine.class)
    static class SynchronizedLockingReactiveObject extends ReactiveObject implements ILockingReactiveObject {

        public SynchronizedLockingReactiveObject() {
            initialState(LockingStateMachine.States.Created.class.getSimpleName());
        }

        private volatile AtomicInteger counter = new AtomicInteger(0);

        @Override
        public int getCounter() {
            return counter.intValue();
        }

        @Override
        @Event
        public synchronized void start() {
            counter.incrementAndGet();
        }

        @Override
        @Event
        public synchronized void stop() {
            counter.incrementAndGet();
        }

        @Override
        @Event
        public synchronized void cancel() {
            counter.incrementAndGet();
        }
    }
    public static class SimpleLock implements LifecycleLockStrategry {

        private static Logger logger = Logger.getLogger("Lifecycle Framework");
        private static volatile int depth = 0;
        private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

        @Override
        public void lockRead(Object reactiveObject) {
            logLockingMethod(reactiveObject, "lockRead: ");
            lock.readLock().lock();
            depth++;
        }

        private void logLockingMethod(Object reactiveObject, String methodName) {
            final StringBuilder builder = getIndent();
            builder.append(methodName + reactiveObject);
            logger.fine(builder.toString());
        }

        private StringBuilder getIndent() {
            StringBuilder builder = new StringBuilder();
            for ( int i = 0; i < depth; i++ ) {
                builder.append("\t");
            }
            return builder;
        }

        @Override
        public void unlockRead(Object targetReactiveObject) {
            depth--;
            logLockingMethod(targetReactiveObject, "unlockRead: ");
            lock.readLock().unlock();
        }

        @Override
        public void lockWrite(Object reactiveObject) {
            logLockingMethod(reactiveObject, "lockWrite: ");
            lock.writeLock().lock();
            depth++;
        }

        @Override
        public void unlockWrite(Object targetReactiveObject) {
            depth--;
            logLockingMethod(targetReactiveObject, "unlockWrite: ");
            lock.writeLock().unlock();
        }
    }
    @LifecycleMeta(LockingStateMachine.class)
    @LifecycleLock(SimpleLock.class)
    static class SimpleLockingReactiveObject extends ReactiveObject implements ILockingReactiveObject {

        public SimpleLockingReactiveObject() {
            initialState(LockingStateMachine.States.Created.class.getSimpleName());
        }

        private volatile AtomicInteger counter = new AtomicInteger(0);

        public int getCounter() {
            return counter.intValue();
        }

        @Event
        public void start() {
            counter.incrementAndGet();
        }

        @Event
        public void stop() {
            counter.incrementAndGet();
        }

        @Event
        public void cancel() {
            counter.incrementAndGet();
        }
    }
    // ///////////////////////////////////////////////////////
    // Hierarchical Relations
    // ///////////////////////////////////////////////////////
    @StateMachine
    static interface InformativeStateMachine {

        @StateSet
        static interface States {

            @Initial
            @Functions({ @Function(event = Events.LogicalDelete.class, value = Recycled.class),
                    @Function(event = Events.Confirm.class, value = Confirmed.class) })
            static interface Draft {}
            @Function(event = Events.Finish.class, value = Finished.class)
            static interface Confirmed {}
            @End
            static interface Finished {}
            @Functions({ @Function(event = Events.PutBack.class, value = Draft.class),
                    @Function(event = Events.PhysicalDelete.class, value = Vanished.class) })
            static interface Recycled {}
            @End
            static interface Vanished {}
        }
        @EventSet
        static interface Events {

            static interface Confirm {}
            static interface LogicalDelete {}
            static interface PutBack {}
            static interface PhysicalDelete {}
            static interface File {}
            static interface Finish {}
        }
    }
    @StateMachine
    static interface CustomerStateMachine extends InformativeStateMachine {

        @StateSet
        static interface States extends InformativeStateMachine.States {

            @CompositeState
            @LifecycleOverride
            static interface Confirmed extends InformativeStateMachine.States.Confirmed {

                @StateSet
                static interface ConfirmedStates {

                    @Initial
                    @Functions({ @Function(event = Events.Suspend.class, value = ServiceSuspended.class),
                            @Function(event = Events.TerminateService.class, value = ServiceExpired.class),
                            @Function(event = Events.Cancel.class, value = Canceled.class) })
                    static interface InService {}
                    @Function(event = Events.Resume.class, value = InService.class)
                    static interface ServiceSuspended {}
                    @Functions({ @Function(event = Events.Renew.class, value = InService.class),
                            @Function(event = Events.Disconnect.class, value = Disconnected.class),
                            @Function(event = Events.Abandon.class, value = Abandoned.class) })
                    static interface ServiceExpired {}
                    @Functions({ @Function(event = Events.Abandon.class, value = Abandoned.class),
                            @Function(event = Events.Disconnect.class, value = Disconnected.class) })
                    static interface Canceled {}
                    @End
                    @ShortCut(InformativeStateMachine.States.Finished.class)
                    static interface Disconnected {}
                    @End
                    @ShortCut(InformativeStateMachine.States.Recycled.class)
                    static interface Abandoned {}
                }
                @EventSet
                static interface Events {

                    static interface Abandon {}
                    static interface Suspend {}
                    static interface Resume {}
                    static interface Renew {}
                    static interface Cancel {}
                    static interface TerminateService {}
                    static interface Disconnect {}
                }
            }
        }
    }
    @StateMachine
    static interface ContractStateMachine extends InformativeStateMachine {

        @StateSet
        static interface States extends InformativeStateMachine.States {

            @CompositeState
            @LifecycleOverride
            static interface Confirmed extends InformativeStateMachine.States.Confirmed {

                @StateSet
                static interface ConfirmedStates {

                    @Initial
                    @Functions({ @Function(event = Events.StartService.class, value = ServiceStarted.class),
                            @Function(event = Events.TerminateService.class, value = Terminated.class) })
                    @InboundWhile(on = { InformativeStateMachine.States.Draft.class, CustomerStateMachine.States.Confirmed.ConfirmedStates.InService.class },
                            relation = Relations.Customer.class)
                    @ValidWhile(on = { CustomerStateMachine.States.Confirmed.ConfirmedStates.InService.class }, relation = Relations.Customer.class)
                    static interface Effective {}
                    @Function(event = Events.AbortService.class, value = ServiceAborted.class)
                    @ValidWhile(on = { CustomerStateMachine.States.Confirmed.ConfirmedStates.InService.class }, relation = Relations.Customer.class)
                    @InboundWhile(on = { CustomerStateMachine.States.Confirmed.ConfirmedStates.InService.class }, relation = Relations.Customer.class)
                    static interface ServiceStarted {}
                    @Function(event = Events.Invalidate.class, value = Uneffective.class)
                    @InboundWhile(on = { CustomerStateMachine.States.Confirmed.ConfirmedStates.InService.class }, relation = Relations.Customer.class)
                    static interface ServiceAborted {}
                    @End
                    @ShortCut(InformativeStateMachine.States.Finished.class)
                    static interface Terminated {}
                    @End
                    @ShortCut(InformativeStateMachine.States.Draft.class)
                    static interface Uneffective {}
                }
                @EventSet
                static interface Events {

                    static interface Invalidate {}
                    static interface StartService {}
                    static interface AbortService {}
                    static interface TerminateService {}
                }
                @RelationSet
                static interface Relations {

                    @Parent
                    @RelateTo(CustomerStateMachine.class)
                    static interface Customer {}
                }
            }
        }
    }
    @StateMachine
    static interface OrderStateMachine extends InformativeStateMachine {

        @StateSet
        static interface States extends InformativeStateMachine.States {

            @CompositeState
            @LifecycleOverride
            static interface Confirmed extends InformativeStateMachine.States.Confirmed {

                @StateSet
                static interface ConfirmedStates {

                    @Initial
                    @InboundWhile(on = { ContractStateMachine.States.Confirmed.ConfirmedStates.ServiceStarted.class }, relation = Relations.Contract.class)
                    @ValidWhile(on = { ContractStateMachine.States.Confirmed.ConfirmedStates.ServiceStarted.class }, relation = Relations.Contract.class)
                    @Functions({ @Function(event = Events.StartProduce.class, value = Manufactoring.class),
                            @Function(event = Events.Dequeue.class, value = Dequeued.class) })
                    static interface Queued {}
                    @InboundWhiles({
                            @InboundWhile(on = { ContractStateMachine.States.Confirmed.ConfirmedStates.ServiceStarted.class },
                                    relation = Relations.Contract.class),
                            @InboundWhile(on = { ResourceStateMachine.States.OfficialRunning.RunningStates.Idle.class }, relation = Relations.Resource.class) })
                    @ValidWhiles({ @ValidWhile(on = { ContractStateMachine.States.Confirmed.ConfirmedStates.ServiceStarted.class },
                            relation = Relations.Contract.class) })
                    @Function(event = Events.StartPackage.class, value = Packaging.class)
                    static interface Manufactoring {}
                    @InboundWhile(on = { ContractStateMachine.States.Confirmed.ConfirmedStates.ServiceStarted.class }, relation = Relations.Contract.class)
                    @ValidWhile(on = { ContractStateMachine.States.Confirmed.ConfirmedStates.ServiceStarted.class }, relation = Relations.Contract.class)
                    @Function(event = Events.Deliver.class, value = { Delivering.class })
                    static interface Packaging {}
                    @InboundWhile(on = { ContractStateMachine.States.Confirmed.ConfirmedStates.ServiceStarted.class }, relation = Relations.Contract.class)
                    @ValidWhile(on = { ContractStateMachine.States.Confirmed.ConfirmedStates.ServiceStarted.class }, relation = Relations.Contract.class)
                    @Function(event = Events.Complete.class, value = Completed.class)
                    static interface Delivering {}
                    @End
                    @ShortCut(InformativeStateMachine.States.Finished.class)
                    static interface Completed {}
                    @End
                    @ShortCut(InformativeStateMachine.States.Draft.class)
                    static interface Dequeued {}
                }
                @EventSet
                static interface Events {

                    static interface Deliver {}
                    static interface Dequeue {}
                    static interface StartProduce {}
                    static interface StartPackage {}
                    static interface Complete {}
                }
                @RelationSet
                static interface Relations {

                    @Parent
                    @RelateTo(ContractStateMachine.class)
                    static interface Contract {}
                    @RelateTo(ResourceStateMachine.class)
                    static interface Resource {}
                }
            }
        }
    }
    @StateMachine
    static interface ResourceStateMachine {

        @StateSet
        static interface States {

            @Initial
            @Function(event = Events.Test.class, value = TestRunning.class)
            static interface New {}
            @Functions({ @Function(event = Events.GoLive.class, value = OfficialRunning.class),
                    @Function(event = Events.ConfirmMalfunction.class, value = Malfunctioning.class) })
            static interface TestRunning {}
            @Functions({ @Function(event = Events.Repair.class, value = Repairing.class),
                    @Function(event = Events.Deprecate.class, value = Deprecated.class) })
            static interface Malfunctioning {}
            @Function(event = Events.ConfirmMalfunction.class, value = Malfunctioning.class)
            @CompositeState
            static interface OfficialRunning {

                @StateSet
                static interface RunningStates {

                    @Initial
                    @Function(event = RunningEvents.Acquire.class, value = Busy.class)
                    static interface Idle {}
                    @Functions({ @Function(event = RunningEvents.Fail.class, value = Failing.class),
                            @Function(event = RunningEvents.Release.class, value = Idle.class) })
                    static interface Busy {}
                    @End
                    @ShortCut(Malfunctioning.class)
                    static interface Failing {}
                }
                @EventSet
                static interface RunningEvents {

                    static interface Acquire {}
                    static interface Release {}
                    static interface Fail {}
                }
            }
            @Function(event = Events.Test.class, value = TestRunning.class)
            static interface Repairing {}
            @End
            static interface Deprecated {}
        }
        @EventSet
        static interface Events {

            static interface Test {}
            static interface ConfirmMalfunction {}
            static interface Repair {}
            static interface GoLive {}
            static interface Deprecate {}
        }
    }
    @LifecycleMeta(InformativeStateMachine.class)
    @LifecycleLock(SimpleLock.class)
    static class InformativeObject extends ReactiveObject {

        public InformativeObject() {
            initialState(InformativeStateMachine.States.Draft.class.getSimpleName());
        }

        @Event
        public void confirm() {}

        @Event
        public void logicalDelete() {}

        @Event
        public void putBack() {}

        @Event
        public void physicalDelete() {}

        @Event
        public void file() {}

        @Event
        public void finish() {}
    }
    @LifecycleMeta(CustomerStateMachine.class)
    @LifecycleLock(SimpleLock.class)
    static class CustomerObject extends InformativeObject {

        @Event
        public void abandon() {}

        @Event
        public void suspend() {}

        @Event
        public void resume() {}

        @Event
        public void renew() {}

        @Event
        public void cancel() {}

        @Event
        public void terminateService() {}

        @Event
        public void disconnect() {}
    }
    @LifecycleMeta(ContractStateMachine.class)
    @LifecycleLock(SimpleLock.class)
    static class ContractObject extends InformativeObject {

        @Relation(ContractStateMachine.States.Confirmed.Relations.Customer.class)
        private CustomerObject customer;

        public ContractObject(final CustomerObject customer) {
            super();
            this.customer = customer;
        }

        @Event
        public void invalidate() {}

        @Event
        public void startService() {}

        @Event
        public void abortService() {}

        @Event
        public void terminateService() {}
    }
    @LifecycleMeta(OrderStateMachine.class)
    @LifecycleLock(SimpleLock.class)
    static class OrderObject extends InformativeObject {

        @Relation
        private ContractObject contract;

        public OrderObject(ContractObject contract) {
            this.contract = contract;
        }

        @Event
        public void deliver() {}

        @Event
        public void dequeue() {}

        @Event
        public void startProduce(@Relation(OrderStateMachine.States.Confirmed.Relations.Resource.class) ResourceObject resource) {}

        @Event
        public void startPackage() {}

        @Event
        public void complete() {}
    }
    @LifecycleMeta(ResourceStateMachine.class)
    @LifecycleLock(SimpleLock.class)
    static class ResourceObject extends ReactiveObject {

        public ResourceObject() {
            initialState(ResourceStateMachine.States.New.class.getName());
        }

        @Event
        public void test() {}

        @Event
        public void confirmMalfunction() {}

        @Event
        public void repair() {}

        @Event
        public void goLive() {}

        @Event
        public void deprecate() {}

        @Event
        public void acquire() {}

        @Event
        public void release() {}

        @Event
        public void fail() {}
    }
}
