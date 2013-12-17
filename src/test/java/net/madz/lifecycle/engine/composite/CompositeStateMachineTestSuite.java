package net.madz.lifecycle.engine.composite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


@RunWith(Suite.class)
@SuiteClasses({ NonOverridedInheritanceCompositeStateMachineNegativeTests.class, NonOverridedInheritanceCompositeStateMachinePositiveTests.class,
        OverridesInheritanceCompositeStateMachineNegativeTests.class, OverridesInheritanceCompositeStateMachinePositiveTests.class,
        StandaloneCompositeStateMachineNegativeTests.class, StandaloneCompositeStateMachinePositiveTests.class })
public class CompositeStateMachineTestSuite {}
