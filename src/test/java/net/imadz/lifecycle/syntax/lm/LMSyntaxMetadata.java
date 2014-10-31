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
package net.imadz.lifecycle.syntax.lm;

import net.imadz.lifecycle.annotations.Transition;
import net.imadz.lifecycle.annotations.LifecycleMeta;
import net.imadz.lifecycle.annotations.StateMachine;
import net.imadz.lifecycle.annotations.StateSet;
import net.imadz.lifecycle.annotations.Event;
import net.imadz.lifecycle.annotations.EventSet;
import net.imadz.lifecycle.annotations.action.Corrupt;
import net.imadz.lifecycle.annotations.action.Recover;
import net.imadz.lifecycle.annotations.action.Redo;
import net.imadz.lifecycle.annotations.state.Final;
import net.imadz.lifecycle.annotations.state.Initial;
import net.imadz.lifecycle.syntax.BaseMetaDataTest;
import net.imadz.lifecycle.syntax.lm.LMSyntaxMetadata.PS1.Events.S1_X;
import net.imadz.lifecycle.syntax.lm.LMSyntaxMetadata.S2.Events.NS1_X;
import net.imadz.lifecycle.syntax.lm.LMSyntaxMetadata.S2.Events.NS1_Y;
import net.imadz.lifecycle.syntax.lm.LMSyntaxMetadata.S2.Events.NS1_Z;
import net.imadz.lifecycle.syntax.lm.LMSyntaxMetadata.S3.Events.S3_X;
import net.imadz.lifecycle.syntax.lm.LMSyntaxMetadata.S3.Events.S3_Y;
import net.imadz.lifecycle.syntax.lm.LMSyntaxMetadata.S3.Events.S3_Z;

public class LMSyntaxMetadata extends BaseMetaDataTest {

    @StateMachine
    static interface PS1 {

        @StateSet
        static interface States {

            @Initial
            @Transition(event = S1_X.class, value = { S1_B.class })
            static interface S1_A {}
            @Final
            static interface S1_B {}
        }
        @EventSet
        static interface Events {

            static interface S1_X {}
        }
    }
    @LifecycleMeta(PS1.class)
    static interface PLM_1 {

        public String getState();

        @Event(S1_X.class)
        void test();
    }
    @StateMachine
    static interface S2 {

        @StateSet
        static interface States {

            @Initial
            @Transition(event = NS1_X.class, value = { NS1_B.class })
            static interface NS1_A {}
            @Transition(event = NS1_Y.class, value = { NS1_C.class })
            static interface NS1_B {}
            @Transition(event = NS1_Z.class, value = { NS1_C.class })
            static interface NS1_C {}
            @Final
            static interface NS1_D {}
        }
        @EventSet
        static interface Events {

            static interface NS1_X {}
            static interface NS1_Y {}
            static interface NS1_Z {}
        }
    }
    // Positive: all events are covered by methods
    @LifecycleMeta(S2.class)
    static interface PLM_2 {

        @Event(NS1_X.class)
        void m1();

        @Event(NS1_Y.class)
        void m2();

        @Event(NS1_Z.class)
        void m3();

        public String getState();
    }
    // Positive: all events are covered by methods, using default method
    // name
    @LifecycleMeta(S2.class)
    static interface PLM_3 {

        @Event
        public void nS1_X();

        @Event
        public void nS1_Y();

        @Event
        public void nS1_Z();

        public String getState();
    }
    // Event NS1_Z has no binding method in LM
    @LifecycleMeta(S2.class)
    static interface NLM_1 {

        @Event(NS1_X.class)
        public void m1();

        @Event(NS1_Y.class)
        public void m2();
    }
    // Event NS1_Z has no method in LM
    @LifecycleMeta(S2.class)
    static interface NLM_2 {

        @Event(NS1_X.class)
        public void m1();

        @Event(NS1_X.class)
        public void m2();

        @Event(NS1_Y.class)
        public void m3();
    }
    @LifecycleMeta(S2.class)
    static interface NLM_3 {

        @Event
        public void nS1_Xyz(); // Method nS1_Xyz can not bind to any event
                               // in S2.

        @Event
        public void nS1_X();

        @Event
        public void nS1_Y();

        @Event
        public void nS1_Z();
    }
    @LifecycleMeta(S2.class)
    static interface NLM_4 {

        // Use other state machine's event
        @Event(S1_X.class)
        public void nS1_X();

        @Event
        public void nS1_Y();

        @Event
        public void nS1_Z();
    }
    @StateMachine
    static interface S3 {

        @StateSet
        static interface States {

            @Initial
            @Transition(event = S3_X.class, value = { S3_B.class })
            static interface S3_A {}
            @Transition(event = S3_Y.class, value = { S3_C.class })
            static interface S3_B {}
            @Transition(event = S3_Z.class, value = { S3_D.class })
            static interface S3_C {}
            @Final
            static interface S3_D {}
        }
        @EventSet
        static interface Events {

            @Corrupt
            static interface S3_X {}
            @Redo
            static interface S3_Y {}
            @Recover
            static interface S3_Z {}
        }
    }
    // Positive LM: Corrupt, Redo, Recover event can bind to only 1 method.
    @LifecycleMeta(S3.class)
    static interface PLM_4 {

        public String getState();

        @Event
        void s3_X();

        @Event
        void s3_Y();

        @Event
        void s3_Z();
    }
    // Negative LM: Redo event binds to more than 1 method
    @LifecycleMeta(S3.class)
    static interface NLM_5 {

        @Event
        void s3_X();

        @Event
        void s3_Y();

        @Event(S3_Y.class)
        void s3_Y2();

        @Event
        void s3_Z();
    }
}
