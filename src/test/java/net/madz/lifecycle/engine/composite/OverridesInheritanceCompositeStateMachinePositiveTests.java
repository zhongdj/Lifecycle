package net.madz.lifecycle.engine.composite;

import org.junit.Test;

public class OverridesInheritanceCompositeStateMachinePositiveTests extends EngineCoreCompositeStateMachineMetadata {

    @Test
    public void should_be_ok_if_invoke_T1_when_contract_in_Draft() {
        final Contract contract = new Contract();
        assertState(ContractLifecycle.States.Draft.class, contract);
        final OverridesComposite object = new OverridesComposite(contract);
        assertState(SM1_Overrides.States.S0.class, object);
        object.doActionT2();
        assertState(SM1_Overrides.States.S1.CStates.CS0.class, object);
        object.doActionT1();
        assertState(SM1_Overrides.States.S2.CStates.CS2.class, object);
        object.doActionT3();
        assertState(SM1_Overrides.States.S3.class, object);
    }

    @Test
    public void should_be_ok_if_invoke_T2_when_contract_in_Draft() {
        final Contract contract = new Contract();
        assertState(ContractLifecycle.States.Draft.class, contract);
        final OverridesComposite object = new OverridesComposite(contract);
        assertState(SM1_Overrides.States.S0.class, object);
        object.doActionT2();
        assertState(SM1_Overrides.States.S1.CStates.CS0.class, object);
        object.doActionT2();
        assertState(SM1_Overrides.States.S2.CStates.CS2.class, object);
        object.doActionT2();
        assertState(SM1_Overrides.States.S3.class, object);
    }

    @Test
    public void should_be_ok_if_invoke_T1_when_contract_in_Expired() {
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
        object.doActionT1();
        assertState(SM1_Overrides.States.S2.CStates.CS2.class, object);
        object.doActionT2();
        assertState(SM1_Overrides.States.S3.class, object);
    }

    @Test
    public void should_be_ok_if_invoke_T2_when_contract_in_Expired() {
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
        object.doActionT2();
        assertState(SM1_Overrides.States.S2.CStates.CS2.class, object);
        object.doActionT2();
        assertState(SM1_Overrides.States.S3.class, object);
    }
}
