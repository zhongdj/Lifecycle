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
package net.madz.lifecycle.engine;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Logger;

import net.madz.lifecycle.LifecycleLockStrategry;
import net.madz.lifecycle.annotations.CompositeState;
import net.madz.lifecycle.annotations.Function;
import net.madz.lifecycle.annotations.Functions;
import net.madz.lifecycle.annotations.LifecycleLock;
import net.madz.lifecycle.annotations.LifecycleMeta;
import net.madz.lifecycle.annotations.StateMachine;
import net.madz.lifecycle.annotations.StateSet;
import net.madz.lifecycle.annotations.Transition;
import net.madz.lifecycle.annotations.TransitionSet;
import net.madz.lifecycle.annotations.relation.InboundWhile;
import net.madz.lifecycle.annotations.relation.InboundWhiles;
import net.madz.lifecycle.annotations.relation.Parent;
import net.madz.lifecycle.annotations.relation.RelateTo;
import net.madz.lifecycle.annotations.relation.Relation;
import net.madz.lifecycle.annotations.relation.RelationSet;
import net.madz.lifecycle.annotations.relation.ValidWhile;
import net.madz.lifecycle.annotations.relation.ValidWhiles;
import net.madz.lifecycle.annotations.state.End;
import net.madz.lifecycle.annotations.state.Initial;
import net.madz.lifecycle.annotations.state.LifecycleOverride;
import net.madz.lifecycle.annotations.state.ShortCut;
import net.madz.verification.VerificationException;

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
            @Function(transition = LockingStateMachine.Transitions.Start.class, value = Started.class)
            static interface Created {}
            @Functions({ @Function(transition = LockingStateMachine.Transitions.Stop.class, value = Stopped.class),
                    @Function(transition = LockingStateMachine.Transitions.Cancel.class, value = Canceled.class) })
            static interface Started {}
            @End
            static interface Stopped {}
            @End
            static interface Canceled {}
        }
        @TransitionSet
        static interface Transitions {

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
        @Transition
        public synchronized void start() {
            counter.incrementAndGet();
        }

        @Override
        @Transition
        public synchronized void stop() {
            counter.incrementAndGet();
        }

        @Override
        @Transition
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

        @Transition
        public void start() {
            counter.incrementAndGet();
        }

        @Transition
        public void stop() {
            counter.incrementAndGet();
        }

        @Transition
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
            @Functions({ @Function(transition = Transitions.LogicalDelete.class, value = Recycled.class),
                    @Function(transition = Transitions.Confirm.class, value = Confirmed.class) })
            static interface Draft {}
            @Function(transition = Transitions.Finish.class, value = Finished.class)
            static interface Confirmed {}
            @End
            static interface Finished {}
            @Functions({ @Function(transition = Transitions.PutBack.class, value = Draft.class),
                    @Function(transition = Transitions.PhysicalDelete.class, value = Vanished.class) })
            static interface Recycled {}
            @End
            static interface Vanished {}
        }
        @TransitionSet
        static interface Transitions {

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
                    @Functions({ @Function(transition = Transitions.Suspend.class, value = ServiceSuspended.class),
                            @Function(transition = Transitions.TerminateService.class, value = ServiceExpired.class),
                            @Function(transition = Transitions.Cancel.class, value = Canceled.class) })
                    static interface InService {}
                    @Function(transition = Transitions.Resume.class, value = InService.class)
                    static interface ServiceSuspended {}
                    @Functions({ @Function(transition = Transitions.Renew.class, value = InService.class),
                            @Function(transition = Transitions.Disconnect.class, value = Disconnected.class),
                            @Function(transition = Transitions.Abandon.class, value = Abandoned.class) })
                    static interface ServiceExpired {}
                    @Functions({ @Function(transition = Transitions.Abandon.class, value = Abandoned.class),
                            @Function(transition = Transitions.Disconnect.class, value = Disconnected.class) })
                    static interface Canceled {}
                    @End
                    @ShortCut(InformativeStateMachine.States.Finished.class)
                    static interface Disconnected {}
                    @End
                    @ShortCut(InformativeStateMachine.States.Recycled.class)
                    static interface Abandoned {}
                }
                @TransitionSet
                static interface Transitions {

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
                    @Functions({ @Function(transition = Transitions.StartService.class, value = ServiceStarted.class),
                            @Function(transition = Transitions.TerminateService.class, value = Terminated.class) })
                    @InboundWhile(on = { InformativeStateMachine.States.Draft.class, CustomerStateMachine.States.Confirmed.ConfirmedStates.InService.class },
                            relation = Relations.Customer.class)
                    @ValidWhile(on = { CustomerStateMachine.States.Confirmed.ConfirmedStates.InService.class }, relation = Relations.Customer.class)
                    static interface Effective {}
                    @Function(transition = Transitions.AbortService.class, value = ServiceAborted.class)
                    @ValidWhile(on = { CustomerStateMachine.States.Confirmed.ConfirmedStates.InService.class }, relation = Relations.Customer.class)
                    @InboundWhile(on = { CustomerStateMachine.States.Confirmed.ConfirmedStates.InService.class }, relation = Relations.Customer.class)
                    static interface ServiceStarted {}
                    @Function(transition = Transitions.Invalidate.class, value = Uneffective.class)
                    @InboundWhile(on = { CustomerStateMachine.States.Confirmed.ConfirmedStates.InService.class }, relation = Relations.Customer.class)
                    static interface ServiceAborted {}
                    @End
                    @ShortCut(InformativeStateMachine.States.Finished.class)
                    static interface Terminated {}
                    @End
                    @ShortCut(InformativeStateMachine.States.Draft.class)
                    static interface Uneffective {}
                }
                @TransitionSet
                static interface Transitions {

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
                    @Functions({ @Function(transition = Transitions.StartProduce.class, value = Manufactoring.class),
                            @Function(transition = Transitions.Dequeue.class, value = Dequeued.class) })
                    static interface Queued {}
                    @InboundWhiles({
                            @InboundWhile(on = { ContractStateMachine.States.Confirmed.ConfirmedStates.ServiceStarted.class },
                                    relation = Relations.Contract.class),
                            @InboundWhile(on = { ResourceStateMachine.States.OfficialRunning.RunningStates.Idle.class }, relation = Relations.Resource.class) })
                    @ValidWhiles({ @ValidWhile(on = { ContractStateMachine.States.Confirmed.ConfirmedStates.ServiceStarted.class },
                            relation = Relations.Contract.class) })
                    @Function(transition = Transitions.StartPackage.class, value = Packaging.class)
                    static interface Manufactoring {}
                    @InboundWhile(on = { ContractStateMachine.States.Confirmed.ConfirmedStates.ServiceStarted.class }, relation = Relations.Contract.class)
                    @ValidWhile(on = { ContractStateMachine.States.Confirmed.ConfirmedStates.ServiceStarted.class }, relation = Relations.Contract.class)
                    @Function(transition = Transitions.Deliver.class, value = { Delivering.class })
                    static interface Packaging {}
                    @InboundWhile(on = { ContractStateMachine.States.Confirmed.ConfirmedStates.ServiceStarted.class }, relation = Relations.Contract.class)
                    @ValidWhile(on = { ContractStateMachine.States.Confirmed.ConfirmedStates.ServiceStarted.class }, relation = Relations.Contract.class)
                    @Function(transition = Transitions.Complete.class, value = Completed.class)
                    static interface Delivering {}
                    @End
                    @ShortCut(InformativeStateMachine.States.Finished.class)
                    static interface Completed {}
                    @End
                    @ShortCut(InformativeStateMachine.States.Draft.class)
                    static interface Dequeued {}
                }
                @TransitionSet
                static interface Transitions {

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
            @Function(transition = Transitions.Test.class, value = TestRunning.class)
            static interface New {}
            @Functions({ @Function(transition = Transitions.GoLive.class, value = OfficialRunning.class),
                    @Function(transition = Transitions.ConfirmMalfunction.class, value = Malfunctioning.class) })
            static interface TestRunning {}
            @Functions({ @Function(transition = Transitions.Repair.class, value = Repairing.class),
                    @Function(transition = Transitions.Deprecate.class, value = Deprecated.class) })
            static interface Malfunctioning {}
            @Function(transition = Transitions.ConfirmMalfunction.class, value = Malfunctioning.class)
            @CompositeState
            static interface OfficialRunning {

                @StateSet
                static interface RunningStates {

                    @Initial
                    @Function(transition = RunningTransitions.Acquire.class, value = Busy.class)
                    static interface Idle {}
                    @Functions({ @Function(transition = RunningTransitions.Fail.class, value = Failing.class),
                            @Function(transition = RunningTransitions.Release.class, value = Idle.class) })
                    static interface Busy {}
                    @End
                    @ShortCut(Malfunctioning.class)
                    static interface Failing {}
                }
                @TransitionSet
                static interface RunningTransitions {

                    static interface Acquire {}
                    static interface Release {}
                    static interface Fail {}
                }
            }
            @Function(transition = Transitions.Test.class, value = TestRunning.class)
            static interface Repairing {}
            @End
            static interface Deprecated {}
        }
        @TransitionSet
        static interface Transitions {

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

        @Transition
        public void confirm() {}

        @Transition
        public void logicalDelete() {}

        @Transition
        public void putBack() {}

        @Transition
        public void physicalDelete() {}

        @Transition
        public void file() {}

        @Transition
        public void finish() {}
    }
    @LifecycleMeta(CustomerStateMachine.class)
    @LifecycleLock(SimpleLock.class)
    static class CustomerObject extends InformativeObject {

        @Transition
        public void abandon() {}

        @Transition
        public void suspend() {}

        @Transition
        public void resume() {}

        @Transition
        public void renew() {}

        @Transition
        public void cancel() {}

        @Transition
        public void terminateService() {}

        @Transition
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

        @Transition
        public void invalidate() {}

        @Transition
        public void startService() {}

        @Transition
        public void abortService() {}

        @Transition
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

        @Transition
        public void deliver() {}

        @Transition
        public void dequeue() {}

        @Transition
        public void startProduce(@Relation(OrderStateMachine.States.Confirmed.Relations.Resource.class) ResourceObject resource) {}

        @Transition
        public void startPackage() {}

        @Transition
        public void complete() {}
    }
    @LifecycleMeta(ResourceStateMachine.class)
    @LifecycleLock(SimpleLock.class)
    static class ResourceObject extends ReactiveObject {

        public ResourceObject() {
            initialState(ResourceStateMachine.States.New.class.getName());
        }

        @Transition
        public void test() {}

        @Transition
        public void confirmMalfunction() {}

        @Transition
        public void repair() {}

        @Transition
        public void goLive() {}

        @Transition
        public void deprecate() {}

        @Transition
        public void acquire() {}

        @Transition
        public void release() {}

        @Transition
        public void fail() {}
    }
}
