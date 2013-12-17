package net.madz.common;

import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class ConsoleLoggingTestBase {

    @Rule
    public TestRule loggerRule = new TestRule() {
    
            @Override
            public Statement apply(Statement base, Description description) {
                return new LoggerStatement(base, description);
            }
        };

    protected static class LoggerStatement extends Statement {
    
            private Statement base;
            private Description description;
    
            public LoggerStatement(Statement base, Description description) {
                this.base = base;
                this.description = description;
            }
    
            @Override
            public void evaluate() throws Throwable {
                final String displayName = description.getDisplayName();
                System.out.println();
                System.out.println();
                System.out.println("########################################################################################################################");
                System.out.println("Processing test: " + displayName);
                base.evaluate();
                System.out.println();
                System.out.println("Finish test: " + displayName);
                System.out.println("########################################################################################################################");
            }
        }

    public ConsoleLoggingTestBase() {
        super();
    }
}