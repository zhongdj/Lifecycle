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
import static org.junit.Assert.assertFalse;
import net.imadz.lifecycle.annotations.Function;
import net.imadz.lifecycle.annotations.Functions;
import net.imadz.lifecycle.annotations.LifecycleMeta;
import net.imadz.lifecycle.annotations.StateMachine;
import net.imadz.lifecycle.annotations.StateSet;
import net.imadz.lifecycle.annotations.Event;
import net.imadz.lifecycle.annotations.EventSet;
import net.imadz.lifecycle.annotations.state.End;
import net.imadz.lifecycle.annotations.state.Initial;
import net.imadz.lifecycle.engine.ReturnTypeTests.ReturnTypeStateMachine.Events.FinishThis;
import net.imadz.lifecycle.engine.ReturnTypeTests.ReturnTypeStateMachine.Events.JustDoIt;

import org.junit.Test;

public class ReturnTypeTests extends EngineTestBase {

    @StateMachine
    static interface ReturnTypeStateMachine {

        @StateSet
        static interface States {

            @Initial
            @Function(transition = JustDoIt.class, value = OnTheFly.class)
            static interface New {}
            @Functions({ @Function(transition = JustDoIt.class, value = OnTheFly.class), @Function(transition = FinishThis.class, value = Done.class) })
            static interface OnTheFly {}
            @End
            static interface Done {}
        }
        @EventSet
        static interface Events {

            static interface JustDoIt {}
            static interface FinishThis {}
        }
    }
    @LifecycleMeta(ReturnTypeStateMachine.class)
    public static class ReturnTypes extends ReactiveObject {

        public ReturnTypes() {
            initialState(ReturnTypeStateMachine.States.New.class.getSimpleName());
        }

        @Event(JustDoIt.class)
        public boolean returnBoolean() {
            return false;
        }

        @Event(JustDoIt.class)
        public byte returnByte() {
            return 1;
        }

        @Event(JustDoIt.class)
        public short returnShort() {
            return 1;
        }

        @Event(JustDoIt.class)
        public int returnInt() {
            return 1;
        }

        @Event(JustDoIt.class)
        public long returnLong() {
            return 1L;
        }

        @Event(JustDoIt.class)
        public float returnFloat() {
            return 1.0F;
        }

        @Event(JustDoIt.class)
        public double returnDouble() {
            return 1.0D;
        }

        @Event(JustDoIt.class)
        public char returnChar() {
            return 'a';
        }

        @Event(JustDoIt.class)
        public Object returnObject() {
            return new Object();
        }

        @Event(FinishThis.class)
        public void doFinish() {}
        // public Object doObject() {
        // InterceptorController<ReturnTypes, Object> controller = new
        // InterceptorController<>();
        // InterceptContext<ReturnTypes, Object> context = new
        // InterceptContext<ReturnTypeTests.ReturnTypes,
        // Object>(ReturnTypes.class, this, "doObject",
        // new Class[0], new Object[0]);
        // return controller.exec(context, new Callable<Object>() {
        //
        // @Override
        // public Object call() throws Exception {
        // return doObject$Impl();
        // }
        // });
        // }
        //
        // public Object doObject$Impl() {
        // return new Object();
        // }
    }

    @Test
    public void should_support_8_wrapper_classes_and_Object_as_return_type_with_bcel_bytecode_transform() {
        final ReturnTypes type = new ReturnTypes();
        assertFalse(type.returnBoolean());
        assertEquals((byte) 1, type.returnByte());
        assertEquals('a', type.returnChar());
        assertEquals(1.0D, type.returnDouble(), 0D);
        assertEquals(1.0F, type.returnFloat(), 0F);
        assertEquals(1, type.returnInt());
        assertEquals(1L, type.returnLong());
        assertEquals((short) 1, type.returnShort());
        type.returnObject();
        type.doFinish();
    }
}
