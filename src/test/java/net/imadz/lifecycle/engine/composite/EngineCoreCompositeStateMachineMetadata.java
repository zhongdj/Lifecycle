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
package net.imadz.lifecycle.engine.composite;

import net.imadz.lifecycle.annotations.CompositeState;
import net.imadz.lifecycle.annotations.Event;
import net.imadz.lifecycle.annotations.EventSet;
import net.imadz.lifecycle.annotations.LifecycleMeta;
import net.imadz.lifecycle.annotations.StateMachine;
import net.imadz.lifecycle.annotations.StateSet;
import net.imadz.lifecycle.annotations.Transition;
import net.imadz.lifecycle.annotations.Transitions;
import net.imadz.lifecycle.annotations.relation.RelateTo;
import net.imadz.lifecycle.annotations.relation.Relation;
import net.imadz.lifecycle.annotations.relation.RelationSet;
import net.imadz.lifecycle.annotations.relation.ValidWhile;
import net.imadz.lifecycle.annotations.state.Final;
import net.imadz.lifecycle.annotations.state.Initial;
import net.imadz.lifecycle.annotations.state.LifecycleOverride;
import net.imadz.lifecycle.annotations.state.ShortCut;
import net.imadz.lifecycle.engine.EngineTestBase;
import net.imadz.verification.VerificationException;
import org.junit.BeforeClass;

public class EngineCoreCompositeStateMachineMetadata extends EngineTestBase {

  @BeforeClass
  public static void registerLifecycleMetadata() throws VerificationException {
    registerMetaFromClass(EngineCoreCompositeStateMachineMetadata.class);
  }

  // ///////////////////////////////////////////////////////////////////////////////
  // Non Relational (Composite State Machine without inheritance)
  // ///////////////////////////////////////////////////////////////////////////////
  @StateMachine
  static interface OrderLifecycle {

    @StateSet
    static interface States {

      @Initial
      @Transition(event = Events.Start.class, value = Started.class)
      static interface Created {}

      @CompositeState
      @Transition(event = Events.Cancel.class, value = Canceled.class)
      static interface Started {

        @StateSet
        static interface SubStates {

          @Initial
          @Transition(event = OrderLifecycle.States.Started.SubEvents.DoProduce.class, value = Producing.class)
          static interface OrderCreated {}

          @Transition(event = OrderLifecycle.States.Started.SubEvents.DoDeliver.class, value = Delivering.class)
          static interface Producing {}

          @Transition(event = OrderLifecycle.States.Started.SubEvents.ConfirmComplete.class, value = Done.class)
          static interface Delivering {}

          @Final
          @ShortCut(OrderLifecycle.States.Finished.class)
          static interface Done {}
        }

        @EventSet
        static interface SubEvents {

          static interface DoProduce {}

          static interface DoDeliver {}

          static interface ConfirmComplete {}
        }
      }

      @Final
      static interface Finished {}

      @Final
      static interface Canceled {}
    }

    @EventSet
    static interface Events {

      static interface Start {}

      static interface Cancel {}
    }
  }

  public abstract static class ProductBase extends ReactiveObject {}

  @LifecycleMeta(OrderLifecycle.class)
  public static class ProductOrder extends ProductBase {

    public ProductOrder() {
      initialState(OrderLifecycle.States.Created.class.getSimpleName());
    }

    @Event
    public void start() {
    }

    @Event
    public void cancel() {
    }

    @Event
    public void doProduce() {
    }

    @Event
    public void doDeliver() {
    }

    @Event
    public void confirmComplete() {
    }
  }

  // ///////////////////////////////////////////////////////////////////////////////
  // Relational
  // ///////////////////////////////////////////////////////////////////////////////
  @StateMachine
  static interface ContractLifecycle {

    @StateSet
    static interface States {

      @Initial
      @Transitions({@Transition(event = ContractLifecycle.Events.Activate.class, value = Active.class),
          @Transition(event = ContractLifecycle.Events.Cancel.class, value = Canceled.class)})
      static interface Draft {}

      @Transitions({@Transition(event = ContractLifecycle.Events.Expire.class, value = Expired.class),
          @Transition(event = ContractLifecycle.Events.Cancel.class, value = Canceled.class)})
      static interface Active {}

      @Final
      static interface Expired {}

      @Final
      static interface Canceled {}
    }

    @EventSet
    static interface Events {

      static interface Activate {}

      static interface Expire {}

      static interface Cancel {}
    }
  }

  @LifecycleMeta(ContractLifecycle.class)
  public static class Contract extends ReactiveObject {

    public Contract() {
      initialState(ContractLifecycle.States.Draft.class.getSimpleName());
    }

    @Event
    public void activate() {
    }

    @Event
    public void expire() {
    }

    @Event
    public void cancel() {
    }
  }

  // ///////////////////////////////////////////////////////////////////////////////
  // Relational Case 1.
  // ///////////////////////////////////////////////////////////////////////////////
  @StateMachine
  static interface RelationalOrderLifecycleReferencingOuterRelation {

    @StateSet
    static interface States {

      @Initial
      @Transition(event = Events.Start.class, value = Started.class)
      @ValidWhile(on = {ContractLifecycle.States.Active.class}, relation = Relations.Contract.class)
      static interface Created {}

      @CompositeState
      @Transition(event = Events.Cancel.class, value = Canceled.class)
      static interface Started {

        @StateSet
        static interface SubStates {

          @Initial
          @Transition(event = RelationalOrderLifecycleReferencingOuterRelation.States.Started.SubEvents.DoProduce.class,
              value = Producing.class)
          @ValidWhile(on = {ContractLifecycle.States.Active.class},
              relation = RelationalOrderLifecycleReferencingOuterRelation.Relations.Contract.class)
          static interface OrderCreated {}

          @Transition(event = RelationalOrderLifecycleReferencingOuterRelation.States.Started.SubEvents.DoDeliver.class,
              value = Delivering.class)
          @ValidWhile(on = {ContractLifecycle.States.Active.class},
              relation = RelationalOrderLifecycleReferencingOuterRelation.Relations.Contract.class)
          static interface Producing {}

          @Transition(event = RelationalOrderLifecycleReferencingOuterRelation.States.Started.SubEvents.ConfirmComplete.class,
              value = Done.class)
          @ValidWhile(on = {ContractLifecycle.States.Active.class},
              relation = RelationalOrderLifecycleReferencingOuterRelation.Relations.Contract.class)
          static interface Delivering {}

          @Final
          @ShortCut(RelationalOrderLifecycleReferencingOuterRelation.States.Finished.class)
          // Ignoring : @ValidWhile(on = {
          // ContractLifecycle.States.Active.class }, relation =
          // Relations.Contract.class)
          static interface Done {}
        }

        @EventSet
        static interface SubEvents {

          static interface DoProduce {}

          static interface DoDeliver {}

          static interface ConfirmComplete {}
        }
      }

      @Final
      static interface Finished {}

      @Final
      static interface Canceled {}
    }

    @EventSet
    static interface Events {

      static interface Start {}

      static interface Cancel {}
    }

    @RelationSet
    static interface Relations {

      @RelateTo(ContractLifecycle.class)
      static interface Contract {}
    }
  }

  @LifecycleMeta(RelationalOrderLifecycleSharingValidWhile.class)
  public static class ProductOrderSharingValidWhile extends ProductBase {

    private Contract contract;

    public ProductOrderSharingValidWhile(Contract contract) {
      this.contract = contract;
      initialState(RelationalOrderLifecycleSharingValidWhile.States.Created.class.getSimpleName());
    }

    @Relation(RelationalOrderLifecycleSharingValidWhile.Relations.Contract.class)
    public Contract getContract() {
      return this.contract;
    }

    @Event
    public void start() {
    }

    @Event
    public void cancel() {
    }

    @Event
    public void doProduce() {
    }

    @Event
    public void doDeliver() {
    }

    @Event
    public void confirmComplete() {
    }
  }

  // ///////////////////////////////////////////////////////////////////////////////
  // Relational Case 2.
  // ///////////////////////////////////////////////////////////////////////////////
  @StateMachine
  static interface RelationalOrderLifecycleSharingValidWhile {

    @StateSet
    static interface States {

      @Initial
      @Transition(event = Events.Start.class, value = Started.class)
      @ValidWhile(on = {ContractLifecycle.States.Active.class}, relation = Relations.Contract.class)
      static interface Created {}

      @CompositeState
      @Transition(event = Events.Cancel.class, value = Canceled.class)
      @ValidWhile(on = {ContractLifecycle.States.Active.class}, relation = Relations.Contract.class)
      static interface Started {

        @StateSet
        static interface SubStates {

          @Initial
          @Transition(event = RelationalOrderLifecycleSharingValidWhile.States.Started.SubEvents.DoProduce.class, value = Producing.class)
          static interface OrderCreated {}

          @Transition(event = RelationalOrderLifecycleSharingValidWhile.States.Started.SubEvents.DoDeliver.class, value = Delivering.class)
          static interface Producing {}

          @Transition(event = RelationalOrderLifecycleSharingValidWhile.States.Started.SubEvents.ConfirmComplete.class, value = Done.class)
          static interface Delivering {}

          @Final
          @ShortCut(RelationalOrderLifecycleSharingValidWhile.States.Finished.class)
          static interface Done {}
        }

        @EventSet
        static interface SubEvents {

          static interface DoProduce {}

          static interface DoDeliver {}

          static interface ConfirmComplete {}
        }
      }

      @Final
      static interface Finished {}

      @Final
      static interface Canceled {}
    }

    @EventSet
    static interface Events {

      static interface Start {}

      static interface Cancel {}
    }

    @RelationSet
    static interface Relations {

      @RelateTo(ContractLifecycle.class)
      static interface Contract {}
    }
  }

  @LifecycleMeta(RelationalOrderLifecycleReferencingOuterRelation.class)
  public static class ProductOrderOuterRelation extends ProductBase {

    private final Contract contract;

    public ProductOrderOuterRelation(Contract contract) {
      initialState(RelationalOrderLifecycleReferencingOuterRelation.States.Created.class.getSimpleName());
      this.contract = contract;
    }

    @Relation(RelationalOrderLifecycleReferencingOuterRelation.Relations.Contract.class)
    public Contract getContract() {
      return this.contract;
    }

    @Event
    public void start() {
    }

    @Event
    public void cancel() {
    }

    @Event
    public void doProduce() {
    }

    @Event
    public void doDeliver() {
    }

    @Event
    public void confirmComplete() {
    }
  }

  @StateMachine
  static interface RelationalOrderLifecycleReferencingInnerValidWhile {

    @StateSet
    static interface States {

      @Initial
      @Transition(event = Events.Start.class, value = Started.class)
      static interface Created {}

      @CompositeState
      @Transition(event = Events.Cancel.class, value = Canceled.class)
      static interface Started {

        @StateSet
        static interface SubStates {

          @Initial
          @Transition(event = RelationalOrderLifecycleReferencingInnerValidWhile.States.Started.SubEvents.DoProduce.class,
              value = Producing.class)
          @ValidWhile(on = {ContractLifecycle.States.Active.class}, relation = Relations.Contract.class)
          static interface OrderCreated {}

          @Transition(event = RelationalOrderLifecycleReferencingInnerValidWhile.States.Started.SubEvents.DoDeliver.class,
              value = Delivering.class)
          static interface Producing {}

          @Transition(event = RelationalOrderLifecycleReferencingInnerValidWhile.States.Started.SubEvents.ConfirmComplete.class,
              value = Done.class)
          static interface Delivering {}

          @Final
          @ShortCut(RelationalOrderLifecycleReferencingInnerValidWhile.States.Finished.class)
          static interface Done {}
        }

        @EventSet
        static interface SubEvents {

          static interface DoProduce {}

          static interface DoDeliver {}

          static interface ConfirmComplete {}
        }

        @RelationSet
        static interface Relations {

          @RelateTo(ContractLifecycle.class)
          static interface Contract {}
        }
      }

      @Final
      static interface Finished {}

      @Final
      static interface Canceled {}
    }

    @EventSet
    static interface Events {

      static interface Start {}

      static interface Cancel {}
    }
  }

  // ///////////////////////////////////////////////////////////////////////////////
  // Relational Case 3.
  // ///////////////////////////////////////////////////////////////////////////////
  @LifecycleMeta(RelationalOrderLifecycleReferencingInnerValidWhile.class)
  public static class ProductOrderInnerValidWhile extends ProductBase {

    private final Contract contract;

    public ProductOrderInnerValidWhile(Contract contract) {
      initialState(RelationalOrderLifecycleReferencingInnerValidWhile.States.Created.class.getSimpleName());
      this.contract = contract;
    }

    @Relation(RelationalOrderLifecycleReferencingInnerValidWhile.States.Started.Relations.Contract.class)
    public Contract getContract() {
      return contract;
    }

    @Event
    public void start() {
    }

    @Event
    public void cancel() {
    }

    @Event
    public void doProduce() {
    }

    @Event
    public void doDeliver() {
    }

    @Event
    public void confirmComplete() {
    }
  }

  // /////////////////////////////////////////////////////////////////////////////////////////////////////
  // Part II: composite state machine with inheritance) According to Image
  // File:
  // Composite State Machine Visibility Scope.png
  // /////////////////////////////////////////////////////////////////////////////////////////////////////
  @StateMachine
  public static interface SM2 {

    @StateSet
    static interface States {

      @Initial
      @Transition(event = SM2.Events.T6.class, value = SM2.States.S1.class)
      static interface S0 {}

      @CompositeState
      @Transition(event = SM2.Events.T6.class, value = SM2.States.S2.class)
      @ValidWhile(relation = SM2.Relations.R6.class, on = {ContractLifecycle.States.Active.class})
      static interface S1 {

        @StateSet
        static interface CStates {

          @Initial
          @Transition(event = SM2.States.S1.CEvents.T4.class, value = SM2.States.S1.CStates.CS1.class)
          @ValidWhile(relation = SM2.States.S1.CRelations.R4.class, on = {ContractLifecycle.States.Expired.class})
          static interface CS0 {}

          @Final
          @ShortCut(SM2.States.S2.class)
          static interface CS1 {}
        }

        @EventSet
        static interface CEvents {

          static interface T4 {}
        }

        @RelationSet
        static interface CRelations {

          @RelateTo(ContractLifecycle.class)
          static interface R4 {}
        }
      }

      @CompositeState
      @Transition(event = SM2.Events.T6.class, value = SM2.States.S3.class)
      static interface S2 {

        @StateSet
        static interface CStates {

          @Initial
          @Transition(event = SM2.States.S2.CEvents.T5.class, value = SM2.States.S2.CStates.CS3.class)
          static interface CS2 {}

          @Final
          @ShortCut(SM2.States.S3.class)
          static interface CS3 {}
        }

        @EventSet
        static interface CEvents {

          static interface T5 {}
        }

        @RelationSet
        static interface CRelations {

          @RelateTo(ContractLifecycle.class)
          static interface R5 {}
        }
      }

      @Final
      static interface S3 {}
    }

    @EventSet
    static interface Events {

      static interface T6 {}
    }

    @RelationSet
    static interface Relations {

      @RelateTo(ContractLifecycle.class)
      static interface R6 {}
    }
  }

  @StateMachine
  public static interface SM1_No_Overrides extends SM2 {

    @StateSet
    static interface States extends SM2.States {

      @Initial
      @Transition(event = SM1_No_Overrides.Events.T2.class, value = SM1_No_Overrides.States.S1.class)
      @LifecycleOverride
      static interface S0 extends SM2.States.S0 {}

      @CompositeState
      @Transition(event = SM1_No_Overrides.Events.T2.class, value = SM1_No_Overrides.States.S2.class)
      @ValidWhile(relation = SM1_No_Overrides.Relations.R2.class, on = {ContractLifecycle.States.Draft.class})
      static interface S1 extends SM2.States.S1 {

        @StateSet
        static interface CStates extends SM2.States.S1.CStates {

          @Initial
          @Transition(event = SM1_No_Overrides.States.S1.CEvents.T1.class, value = SM1_No_Overrides.States.S1.CStates.CS1.class)
          static interface CS0 extends SM2.States.S1.CStates.CS0 {}

          @Final
          @ShortCut(SM1_No_Overrides.States.S2.class)
          static interface CS1 {}
        }

        @EventSet
        static interface CEvents extends SM2.States.S1.CEvents {

          static interface T1 {}
        }

        @RelationSet
        static interface CRelations extends SM2.States.S1.CRelations {

          @RelateTo(ContractLifecycle.class)
          static interface R1 {}
        }
      }

      @CompositeState
      @Transition(event = SM1_No_Overrides.Events.T2.class, value = SM1_No_Overrides.States.S3.class)
      static interface S2 extends SM2.States.S2 {

        @StateSet
        static interface CStates {

          @Initial
          @Transition(event = SM1_No_Overrides.States.S2.CEvents.T3.class, value = SM1_No_Overrides.States.S2.CStates.CS3.class)
          static interface CS2 extends SM2.States.S2.CStates.CS2 {}

          @Final
          @ShortCut(SM1_No_Overrides.States.S3.class)
          static interface CS3 extends SM2.States.S2.CStates.CS3 {}
        }

        @EventSet
        static interface CEvents {

          static interface T3 {}
        }

        @RelationSet
        static interface CRelations {

          @RelateTo(ContractLifecycle.class)
          static interface R3 {}
        }
      }

      @Final
      static interface S3 extends SM2.States.S3 {}
    }

    @EventSet
    static interface Events extends SM2.Events {

      static interface T2 {}
    }

    @RelationSet
    static interface Relations extends SM2.Relations {

      @RelateTo(ContractLifecycle.class)
      static interface R2 {}
    }
  }

  @StateMachine
  public static interface SM1_Overrides extends SM2 {

    @StateSet
    static interface States extends SM2.States {

      @Initial
      @Transition(event = SM1_Overrides.Events.T2.class, value = SM1_Overrides.States.S1.class)
      @LifecycleOverride
      static interface S0 extends SM2.States.S0 {}

      @CompositeState
      @Transition(event = SM1_Overrides.Events.T2.class, value = SM1_Overrides.States.S2.class)
      @ValidWhile(relation = SM1_Overrides.Relations.R2.class, on = {ContractLifecycle.States.Draft.class})
      @LifecycleOverride
      static interface S1 extends SM2.States.S1 {

        @StateSet
        static interface CStates {

          @Initial
          @Transition(event = SM1_Overrides.States.S1.CEvents.T1.class, value = SM1_Overrides.States.S1.CStates.CS1.class)
          @ValidWhile(relation = SM1_Overrides.States.S1.CRelations.R1.class, on = {ContractLifecycle.States.Expired.class})
          static interface CS0 {}

          @Final
          @ShortCut(SM1_Overrides.States.S2.class)
          static interface CS1 {}
        }

        @EventSet
        static interface CEvents {

          static interface T1 {}
        }

        @RelationSet
        static interface CRelations {

          @RelateTo(ContractLifecycle.class)
          static interface R1 {}
        }
      }

      @CompositeState
      @Transition(event = SM1_Overrides.Events.T2.class, value = SM1_Overrides.States.S3.class)
      @LifecycleOverride
      static interface S2 extends SM2.States.S2 {

        @StateSet
        static interface CStates {

          @Initial
          @Transition(event = SM1_Overrides.States.S2.CEvents.T3.class, value = SM1_Overrides.States.S2.CStates.CS3.class)
          static interface CS2 {}

          @Final
          @ShortCut(SM1_Overrides.States.S3.class)
          static interface CS3 {}
        }

        @EventSet
        static interface CEvents {

          static interface T3 {}
        }

        @RelationSet
        static interface CRelations {

          @RelateTo(ContractLifecycle.class)
          static interface R3 {}
        }
      }

      @Final
      static interface S3 extends SM2.States.S3 {}
    }

    @EventSet
    static interface Events extends SM2.Events {

      static interface T2 {}
    }

    @RelationSet
    static interface Relations extends SM2.Relations {

      @RelateTo(ContractLifecycle.class)
      static interface R2 {}
    }
  }

  @LifecycleMeta(SM2.class)
  public static class Base extends ReactiveObject {

    private final Contract contract;

    public Base(Contract contract) {
      initialState(SM2.States.S0.class.getSimpleName());
      this.contract = contract;
    }

    @Relation(SM2.Relations.R6.class)
    public Contract getContract() {
      return contract;
    }

    @Relation(SM2.States.S1.CRelations.R4.class)
    public Contract getContract2() {
      return contract;
    }

    @Relation(SM2.States.S2.CRelations.R5.class)
    public Contract getContract3() {
      return contract;
    }

    @Event(SM2.Events.T6.class)
    public void doActionT6() {
    }

    @Event(SM2.States.S1.CEvents.T4.class)
    public void doActionT4() {
    }

    @Event(SM2.States.S2.CEvents.T5.class)
    public void doActionT5() {
    }
  }

  @LifecycleMeta(SM1_No_Overrides.class)
  public static class NoOverrideComposite extends Base {

    public NoOverrideComposite(Contract contract) {
      super(contract);
    }

    @Event(SM1_No_Overrides.Events.T2.class)
    public void doActionT2() {
    }

    @Event(SM1_No_Overrides.States.S1.CEvents.T1.class)
    public void doActionT1() {
    }

    @Event(SM1_No_Overrides.States.S2.CEvents.T3.class)
    public void doActionT3() {
    }

    @Relation(SM1_No_Overrides.Relations.R2.class)
    public Contract getContractR2() {
      return getContract();
    }

    @Relation(SM1_No_Overrides.States.S1.CRelations.R1.class)
    public Contract getContractR1() {
      return getContract();
    }

    @Relation(SM1_No_Overrides.States.S2.CRelations.R3.class)
    public Contract getContractR3() {
      return getContract();
    }
  }

  @LifecycleMeta(SM1_Overrides.class)
  public static class OverridesComposite extends Base {

    public OverridesComposite(Contract contract) {
      super(contract);
    }

    @Event(SM1_Overrides.Events.T2.class)
    public void doActionT2() {
    }

    @Event(SM1_Overrides.States.S1.CEvents.T1.class)
    public void doActionT1() {
    }

    @Event(SM1_Overrides.States.S2.CEvents.T3.class)
    public void doActionT3() {
    }

    @Relation(SM1_Overrides.Relations.R2.class)
    public Contract getContractR2() {
      return getContract();
    }

    @Relation(SM1_Overrides.States.S1.CRelations.R1.class)
    public Contract getContractR1() {
      return getContract();
    }

    @Relation(SM1_Overrides.States.S2.CRelations.R3.class)
    public Contract getContractR3() {
      return getContract();
    }
  }
}