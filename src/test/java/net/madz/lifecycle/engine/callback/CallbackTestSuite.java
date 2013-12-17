package net.madz.lifecycle.engine.callback;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


@RunWith(Suite.class)
@SuiteClasses({ CallbackExtendedAndOverrideTests.class, PostStateChangePositiveTests.class, PreStateChangePositiveTests.class })
public class CallbackTestSuite {}
