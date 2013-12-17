package net.madz.lifecycle.syntax.lm;

import net.madz.lifecycle.syntax.lm.callback.CallbackTestSuite;
import net.madz.lifecycle.syntax.lm.condition.LMSyntaxConditionTestSuite;
import net.madz.lifecycle.syntax.lm.relation.LMSyntaxRelationTestSuite;
import net.madz.lifecycle.syntax.lm.stateindicator.StateIndicatorTestSuite;
import net.madz.lifecycle.syntax.lm.transition.TransitionTestSuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ LMSyntaxNegativeTest.class, LMSyntaxPositiveTest.class, CallbackTestSuite.class,
        LMSyntaxConditionTestSuite.class, LMSyntaxRelationTestSuite.class, StateIndicatorTestSuite.class,
        TransitionTestSuite.class })
public class LMSyntaxTestSuite {}
