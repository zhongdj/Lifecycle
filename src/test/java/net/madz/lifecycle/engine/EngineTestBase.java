package net.madz.lifecycle.engine;

import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.LogManager;

import org.junit.BeforeClass;

import net.madz.bcel.intercept.DefaultStateMachineRegistry;
import net.madz.bcel.intercept.LifecycleInterceptor;
import net.madz.common.ConsoleLoggingTestBase;
import net.madz.lifecycle.LifecycleCommonErrors;
import net.madz.lifecycle.LifecycleException;
import net.madz.lifecycle.annotations.LifecycleMeta;
import net.madz.lifecycle.annotations.StateIndicator;
import net.madz.lifecycle.annotations.Transition;
import net.madz.lifecycle.annotations.relation.Relation;
import net.madz.lifecycle.engine.CoreFuntionTestMetadata.Customer;
import net.madz.lifecycle.engine.CoreFuntionTestMetadata.InternetServiceLifecycleMeta;
import net.madz.lifecycle.engine.CoreFuntionTestMetadata.ServiceProviderLifecycle;
import net.madz.utils.BundleUtils;
import net.madz.verification.VerificationException;

public class EngineTestBase extends ConsoleLoggingTestBase {
    @BeforeClass
    public static void setLogLevel() throws SecurityException, FileNotFoundException, IOException {
        LogManager.getLogManager().readConfiguration(new FileInputStream("target/test-classes/lifecycle_logging.properties"));
    }
    public abstract static class ReactiveObject {

        @StateIndicator
        private String state = null;

        protected void initialState(String stateName) {
            if ( null == state ) {
                this.state = stateName;
            } else {
                throw new IllegalStateException("Cannot call initialState method after state had been intialized.");
            }
        }

        public String getState() {
            return state;
        }
    }
    @LifecycleMeta(InternetServiceLifecycleMeta.class)
    public static class BaseService<T extends BaseServiceProvider> extends ReactiveObject {

        private Customer customer;

        public BaseService(Customer customer) {
            initialState(InternetServiceLifecycleMeta.States.New.class.getSimpleName());
            this.customer = customer;
        }

        private T provider;

        public T getProvider() {
            return provider;
        }

        public void setProvider(T provider) {
            this.provider = provider;
        }

        @Relation(InternetServiceLifecycleMeta.Relations.CustomerRelation.class)
        public Customer getCustomer() {
            return customer;
        }

        public void setCustomer(Customer customer) {
            this.customer = customer;
        }

        @Transition
        void start() {}

        @Transition
        void end() {}
    }
    @LifecycleMeta(ServiceProviderLifecycle.class)
    public static class BaseServiceProvider extends ReactiveObject {

        public BaseServiceProvider() {
            initialState(ServiceProviderLifecycle.States.ServiceAvailable.class.getSimpleName());
        }

        @Transition
        void shutdown() {}
    }

    protected static void registerMetaFromClass(final Class<?> metadataClas) throws VerificationException {
        for ( Class<?> cursorClass = metadataClas; null != cursorClass; cursorClass = cursorClass.getSuperclass() ) {
            for ( final Class<?> c : cursorClass.getDeclaredClasses() ) {
                for ( final Annotation a : c.getDeclaredAnnotations() ) {
                    if ( LifecycleMeta.class == a.annotationType() ) {
                        System.out.println("Registering Lifecycle class:  " + c);
                        DefaultStateMachineRegistry.getInstance().registerLifecycleMeta(c);
                        break;
                    }
                }
            }
        }
    }

    protected static void assertLifecycleError(LifecycleException e, final String expectedErrorCode, final Object... messageVars) {
        System.out.println();
        System.out.println("expected error code: " + expectedErrorCode);
        System.out.println("  actual error code: " + e.getErrorCode());
        assertEquals(expectedErrorCode, e.getErrorCode());
        final String expectedMessage = BundleUtils.getBundledMessage(LifecycleInterceptor.class, LifecycleCommonErrors.BUNDLE, expectedErrorCode, messageVars);
        System.out.println("expected error message: " + expectedMessage);
        System.out.println("  actual error message: " + e.getMessage());
        assertEquals(expectedMessage, e.getMessage());
        throw e;
    }

    public EngineTestBase() {
        super();
    }

    protected void assertState(final Class<?> stateClass, final ReactiveObject product) {
        assertEquals(stateClass.getSimpleName(), product.getState());
    }

    protected void assertInvalidStateErrorByValidWhile(final LifecycleException e, final ReactiveObject relationObject, final ReactiveObject itself,
            final Class<?>... validStates) {
        final ArrayList<String> validNames = new ArrayList<>();
        for ( Class<?> validstate : validStates ) {
            validNames.add(validstate.getSimpleName());
        }
        try {
            assertLifecycleError(e, LifecycleCommonErrors.STATE_INVALID, itself, itself.getState(), relationObject, relationObject.getState(),
                    Arrays.toString(validNames.toArray()));
        } catch (LifecycleException ex) {
            throw ex;
        }
    }

    /**
     * e,
     * LifecycleCommonErrors.VIOLATE_INBOUND_WHILE_RELATION_CONSTRAINT,
     * KeyBoardLifecycleMetadataPreValidateCondition.Transitions.PressAnyKey.
     * class,
     * KeyBoardLifecycleMetadataPreValidateCondition.States.Broken.class.
     * getSimpleName(),
     * keyboard,
     * power,
     * power.getState(),
     * inboundWhileDottedPath(KeyBoardLifecycleMetadataPreValidateCondition.
     * States.Broken.class,
     * PowerRelation.class));
     */
    protected void assertViolateInboundWhileRelationConstraint(final LifecycleException e, final Class<?> transitionKey, final Class<?> nextState,
            final ReactiveObject itself, final ReactiveObject relationObject, final Class<?>... validStates) {
        final ArrayList<String> validNames = new ArrayList<>();
        for ( Class<?> validstate : validStates ) {
            validNames.add(validstate.getSimpleName());
        }
        try {
            assertLifecycleError(e, LifecycleCommonErrors.VIOLATE_INBOUND_WHILE_RELATION_CONSTRAINT, transitionKey.getSimpleName(), nextState.getSimpleName(),
                    itself, relationObject, relationObject.getState(), Arrays.toString(validNames.toArray()));
        } catch (LifecycleException ex) {
            throw ex;
        }
    }
}