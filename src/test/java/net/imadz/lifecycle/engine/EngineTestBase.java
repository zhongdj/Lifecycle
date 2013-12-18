/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright (c) 2013-2020 Madz. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License"). You
 * may not use this file except in compliance with the License. You can
 * obtain a copy of the License at
 * https://raw.github.com/zhongdj/Lifecycle/master/License.txt
 * . See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 * 
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 * 
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license." If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above. However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package net.imadz.lifecycle.engine;

import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.LogManager;

import org.junit.BeforeClass;

import net.imadz.bcel.intercept.DefaultStateMachineRegistry;
import net.imadz.bcel.intercept.LifecycleInterceptor;
import net.imadz.common.ConsoleLoggingTestBase;
import net.imadz.lifecycle.LifecycleCommonErrors;
import net.imadz.lifecycle.LifecycleException;
import net.imadz.lifecycle.annotations.LifecycleMeta;
import net.imadz.lifecycle.annotations.StateIndicator;
import net.imadz.lifecycle.annotations.Transition;
import net.imadz.lifecycle.annotations.relation.Relation;
import net.imadz.lifecycle.engine.CoreFuntionTestMetadata.Customer;
import net.imadz.lifecycle.engine.CoreFuntionTestMetadata.InternetServiceLifecycleMeta;
import net.imadz.lifecycle.engine.CoreFuntionTestMetadata.ServiceProviderLifecycle;
import net.imadz.utils.BundleUtils;
import net.imadz.verification.VerificationException;

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