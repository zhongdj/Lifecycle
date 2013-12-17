package net.madz.lifecycle.syntax;

import net.madz.lifecycle.syntax.basic.BasicTestSuite;
import net.madz.lifecycle.syntax.lm.LMSyntaxTestSuite;
import net.madz.lifecycle.syntax.register.RegisterSyntaxTestSuite;
import net.madz.lifecycle.syntax.relation.RelationSyntaxTestSuite;
import net.madz.lifecycle.syntax.state.StateSyntaxTestSuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ BasicTestSuite.class, LMSyntaxTestSuite.class, RegisterSyntaxTestSuite.class,
        RelationSyntaxTestSuite.class, StateSyntaxTestSuite.class, })
public class SyntaxTestSuite {}
