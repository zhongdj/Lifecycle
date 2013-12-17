package net.madz.lifecycle.engine.composite;

import net.madz.lifecycle.LifecycleCommonErrors;
import net.madz.lifecycle.LifecycleException;

import org.junit.Test;


public class NonOverridedInheritanceCompositeStateMachineNegativeTests extends EngineCoreCompositeStateMachineMetadata {

    @Test(expected = LifecycleException.class)
    public void should_throw_002_9001_if_invoke_transition_method_while_contract_canceled() {
        final Contract contract = new Contract();
        {
            assertState(ContractLifecycle.States.Draft.class, contract);
            contract.activate(); // Draft from Owning State, Active From Owning
            assertState(ContractLifecycle.States.Active.class, contract);
        }
        final NoOverrideComposite noOverride = new NoOverrideComposite(contract);
        {
            assertState(SM1_No_Overrides.States.S0.class, noOverride);
            noOverride.doActionT2();
            assertState(SM1_No_Overrides.States.S1.CStates.CS0.class, noOverride);
        }
        {
            contract.cancel();
            assertState(ContractLifecycle.States.Canceled.class, contract);
        }
        try {
            noOverride.doActionT1();
        } catch (LifecycleException e) {
            assertInvalidStateErrorByValidWhile(e, contract, noOverride, ContractLifecycle.States.Draft.class, ContractLifecycle.States.Active.class,
                    ContractLifecycle.States.Expired.class);
        }
    }

    @Test(expected = LifecycleException.class)
    public void should_throw_002_9000_if_invoke_illegal_transition_T3_from_other_composite_state_machine_of_owning_state_machine() {
        final Contract contract = new Contract();
        {
            assertState(ContractLifecycle.States.Draft.class, contract);
            contract.activate(); // Draft from Owning State, Active From Owning
            assertState(ContractLifecycle.States.Active.class, contract);
        }
        final NoOverrideComposite noOverride = new NoOverrideComposite(contract);
        {
            assertState(SM1_No_Overrides.States.S0.class, noOverride);
            noOverride.doActionT2();
            assertState(SM1_No_Overrides.States.S1.CStates.CS0.class, noOverride);
        }
        try {
            noOverride.doActionT3();
        } catch (LifecycleException e) {
            assertLifecycleError(e, LifecycleCommonErrors.ILLEGAL_TRANSITION_ON_STATE, SM1_No_Overrides.States.S2.CTransitions.T3.class,
                    SM1_No_Overrides.States.S1.CStates.CS0.class.getSimpleName(), noOverride);
        }
    }

    @Test(expected = LifecycleException.class)
    public void should_throw_002_9000_if_invoke_illegal_transition_T5_of_non_extended_composite_state_machine_of_owning_state_machine_super_state_machine() {
        final Contract contract = new Contract();
        {
            assertState(ContractLifecycle.States.Draft.class, contract);
            contract.activate(); // Draft from Owning State, Active From Owning
            assertState(ContractLifecycle.States.Active.class, contract);
        }
        final NoOverrideComposite noOverride = new NoOverrideComposite(contract);
        {
            assertState(SM1_No_Overrides.States.S0.class, noOverride);
            noOverride.doActionT2();
            assertState(SM1_No_Overrides.States.S1.CStates.CS0.class, noOverride);
        }
        try {
            noOverride.doActionT5();
        } catch (LifecycleException e) {
            assertLifecycleError(e, LifecycleCommonErrors.ILLEGAL_TRANSITION_ON_STATE, SM2.States.S2.CTransitions.T5.class,
                    SM1_No_Overrides.States.S1.CStates.CS0.class.getSimpleName(), noOverride);
        }
    }

}
