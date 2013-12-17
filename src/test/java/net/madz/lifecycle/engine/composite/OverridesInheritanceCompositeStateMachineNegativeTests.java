package net.madz.lifecycle.engine.composite;

import net.madz.lifecycle.LifecycleCommonErrors;
import net.madz.lifecycle.LifecycleException;

import org.junit.Test;

public class OverridesInheritanceCompositeStateMachineNegativeTests extends EngineCoreCompositeStateMachineMetadata {

    @Test(expected = LifecycleException.class)
    public void should_throw_002_9001_if_invoke_T1_while_contract_Active() {
        final Contract contract = new Contract();
        assertState(ContractLifecycle.States.Draft.class, contract);
        final OverridesComposite object = new OverridesComposite(contract);
        assertState(SM1_Overrides.States.S0.class, object);
        object.doActionT2();
        assertState(SM1_Overrides.States.S1.CStates.CS0.class, object);
        contract.activate();
        assertState(ContractLifecycle.States.Active.class, contract);
        try {
            object.doActionT1();
        } catch (LifecycleException e) {
            assertInvalidStateErrorByValidWhile(e, contract, object, ContractLifecycle.States.Expired.class, ContractLifecycle.States.Draft.class);
            throw e;
        }
    }

    @Test(expected = LifecycleException.class)
    public void should_throw_002_9001_if_invoke_T2_while_contract_Active() {
        final Contract contract = new Contract();
        assertState(ContractLifecycle.States.Draft.class, contract);
        final OverridesComposite object = new OverridesComposite(contract);
        assertState(SM1_Overrides.States.S0.class, object);
        object.doActionT2();
        assertState(SM1_Overrides.States.S1.CStates.CS0.class, object);
        contract.activate();
        assertState(ContractLifecycle.States.Active.class, contract);
        try {
            object.doActionT2();
        } catch (LifecycleException e) {
            assertInvalidStateErrorByValidWhile(e, contract, object, ContractLifecycle.States.Expired.class, ContractLifecycle.States.Draft.class);
            throw e;
        }
    }

    @Test(expected = LifecycleException.class)
    public void should_throw_002_9001_if_invoke_T6_while_contract_Active() {
        final Contract contract = new Contract();
        assertState(ContractLifecycle.States.Draft.class, contract);
        contract.activate();
        assertState(ContractLifecycle.States.Active.class, contract);
        final OverridesComposite object = new OverridesComposite(contract);
        assertState(SM1_Overrides.States.S0.class, object);
        object.doActionT2();
        assertState(SM1_Overrides.States.S1.CStates.CS0.class, object);
        try {
            object.doActionT6();
        } catch (LifecycleException e) {
            assertInvalidStateErrorByValidWhile(e, contract, object, ContractLifecycle.States.Expired.class, ContractLifecycle.States.Draft.class);
            throw e;
        }
    }

    @Test(expected = LifecycleException.class)
    public void should_throw_002_9001_if_invoke_T6_while_contract_Canceled() {
        final Contract contract = new Contract();
        assertState(ContractLifecycle.States.Draft.class, contract);
        contract.activate();
        assertState(ContractLifecycle.States.Active.class, contract);
        contract.cancel();
        assertState(ContractLifecycle.States.Canceled.class, contract);
        final OverridesComposite object = new OverridesComposite(contract);
        assertState(SM1_Overrides.States.S0.class, object);
        object.doActionT2();
        assertState(SM1_Overrides.States.S1.CStates.CS0.class, object);
        try {
            object.doActionT6();
        } catch (LifecycleException e) {
            assertInvalidStateErrorByValidWhile(e, contract, object, ContractLifecycle.States.Expired.class, ContractLifecycle.States.Draft.class);
            throw e;
        }
    }

    @Test(expected = LifecycleException.class)
    public void should_throw_002_9000_if_invoke_T6_while_contract_Expired() {
        final Contract contract = new Contract();
        assertState(ContractLifecycle.States.Draft.class, contract);
        contract.activate();
        assertState(ContractLifecycle.States.Active.class, contract);
        contract.expire();
        assertState(ContractLifecycle.States.Expired.class, contract);
        final OverridesComposite object = new OverridesComposite(contract);
        assertState(SM1_Overrides.States.S0.class, object);
        object.doActionT2();
        assertState(SM1_Overrides.States.S1.CStates.CS0.class, object);
        try {
            object.doActionT6();
        } catch (LifecycleException e) {
            assertLifecycleError(e, LifecycleCommonErrors.ILLEGAL_TRANSITION_ON_STATE, SM1_Overrides.Transitions.T6.class,
                    SM1_Overrides.States.S1.CStates.CS0.class.getSimpleName(), object);
        }
    }

    @Test(expected = LifecycleException.class)
    public void should_throw_002_9000_if_invoke_T6_while_contract_Draft() {
        final Contract contract = new Contract();
        assertState(ContractLifecycle.States.Draft.class, contract);
        final OverridesComposite object = new OverridesComposite(contract);
        assertState(SM1_Overrides.States.S0.class, object);
        object.doActionT2();
        assertState(SM1_Overrides.States.S1.CStates.CS0.class, object);
        try {
            object.doActionT6();
        } catch (LifecycleException e) {
            assertLifecycleError(e, LifecycleCommonErrors.ILLEGAL_TRANSITION_ON_STATE, SM1_Overrides.Transitions.T6.class,
                    SM1_Overrides.States.S1.CStates.CS0.class.getSimpleName(), object);
        }
    }
}
