package net.madz.lifecycle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.LogManager;

import net.madz.lifecycle.engine.EngineTestSuite;
import net.madz.lifecycle.semantics.SemanticsTestSuite;
import net.madz.lifecycle.syntax.SyntaxTestSuite;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ SyntaxTestSuite.class, SemanticsTestSuite.class, EngineTestSuite.class })
public class AllTestSuites {

    @BeforeClass
    public static void setLogLevel() throws SecurityException, FileNotFoundException, IOException {
        LogManager.getLogManager().readConfiguration(new FileInputStream("target/test-classes/lifecycle_logging.properties"));
    }
}
