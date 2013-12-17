package net.madz.lifecycle.syntax.basic;

import net.madz.lifecycle.syntax.basic.transition.TransitionSyntaxTestSuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ CompositeStateMachineNegativeTests.class, ConditionSetTest.class,
        StateSetAndTransitionSetSyntaxNegativeTest.class, StateSetSyntaxPositiveTest.class, TransitionSyntaxTestSuite.class })
public class BasicTestSuite {}
