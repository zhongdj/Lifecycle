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
package net.imadz.lifecycle.demo.inheritance.meta;

import net.imadz.lifecycle.annotations.Function;
import net.imadz.lifecycle.annotations.Functions;
import net.imadz.lifecycle.annotations.StateMachine;
import net.imadz.lifecycle.annotations.StateSet;
import net.imadz.lifecycle.annotations.EventSet;
import net.imadz.lifecycle.annotations.relation.InboundWhile;
import net.imadz.lifecycle.annotations.relation.RelationSet;
import net.imadz.lifecycle.annotations.relation.ValidWhile;
import net.imadz.lifecycle.annotations.state.End;
import net.imadz.lifecycle.demo.inheritance.meta.PlantScheduleOrderLifecycleMeta.Relations.ServiceOrder;
import net.imadz.lifecycle.demo.inheritance.meta.PlantScheduleOrderLifecycleMeta.Events.Finish;
import net.imadz.lifecycle.demo.inheritance.meta.PlantScheduleOrderLifecycleMeta.Events.Start;

@StateMachine(parentOn = ServiceOrderLifecycleMeta.class)
public interface PlantScheduleOrderLifecycleMeta extends OrderLifecycleMeta {

    @StateSet
    static class States extends OrderLifecycleMeta.States {

        @InboundWhile(relation = ServiceOrder.class, on = OrderLifecycleMeta.States.Queued.class)
        // Default @ValidWhile(relation="serviceOrder", on =
        // {ServiceOrderLifecycleMeta.States.Queued.class})
        @Functions({ @Function(transition = Start.class, value = Ongoing.class) })
        static class Queued extends OrderLifecycleMeta.States.Queued {}
        @InboundWhile(relation = ServiceOrder.class, on = { ServiceOrderLifecycleMeta.States.Ongoing.class })
        // Default @ValidWhile(IServiceOrder.States.Ongoing.class)
        @Functions({ @Function(transition = Finish.class, value = Finished.class) })
        static class Ongoing extends OrderLifecycleMeta.States.Ongoing {}
        @End
        @InboundWhile(relation = ServiceOrder.class, on = { ServiceOrderLifecycleMeta.States.Ongoing.class })
        @ValidWhile(relation = ServiceOrder.class, on = { ServiceOrderLifecycleMeta.States.Ongoing.class, ServiceOrderLifecycleMeta.States.Finished.class })
        // Default @Functions({})
        static class Finished extends OrderLifecycleMeta.States.Finished {}
    }
    @EventSet
    public static class Events {

        public static class Start {}
        public static class Finish {}
    }
    @RelationSet
    public static class Relations {

        public static class ServiceOrder {}
    }
}
