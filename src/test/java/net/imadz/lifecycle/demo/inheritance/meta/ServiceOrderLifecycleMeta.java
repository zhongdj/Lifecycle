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

import net.imadz.lifecycle.annotations.StateMachine;
import net.imadz.lifecycle.annotations.StateSet;
import net.imadz.lifecycle.annotations.TransitionSet;
import net.imadz.lifecycle.annotations.relation.ErrorMessage;
import net.imadz.lifecycle.annotations.relation.InboundWhile;
import net.imadz.lifecycle.annotations.relation.InboundWhiles;
import net.imadz.lifecycle.annotations.relation.RelationSet;
import net.imadz.lifecycle.demo.inheritance.meta.ServiceOrderLifecycleMeta.Relations.ConcreteTruckResource;
import net.imadz.lifecycle.demo.inheritance.meta.ServiceOrderLifecycleMeta.Relations.PlantResource;
import net.imadz.lifecycle.demo.inheritance.meta.ServiceOrderLifecycleMeta.Relations.SummaryPlan;

@StateMachine
public interface ServiceOrderLifecycleMeta extends OrderLifecycleMeta {

    @StateSet
    public class States extends OrderLifecycleMeta.States {

        @InboundWhiles({ @InboundWhile(relation = SummaryPlan.class, on = { SummaryPlanLifecycleMeta.States.Ongoing.class }, otherwise = {
                @ErrorMessage(states = { SummaryPlanLifecycleMeta.States.VolumeLeftEmpty.class }, bundle = "scheduling", code = "100-0002"),
                @ErrorMessage(states = { SummaryPlanLifecycleMeta.States.Done.class }, bundle = "scheduling", code = "100-0003") }) })
        public static class Created extends OrderLifecycleMeta.States.Created {}
        @InboundWhiles({
                @InboundWhile(relation = PlantResource.class,
                        on = { PlantResourceLifecycleMeta.States.Idle.class, PlantResourceLifecycleMeta.States.Busy.class }),
                @InboundWhile(relation = ConcreteTruckResource.class, on = { ConcreteTruckResourceLifecycleMeta.States.Idle.class,
                        ConcreteTruckResourceLifecycleMeta.States.Busy.class }) })
        public static class Queued extends OrderLifecycleMeta.States.Queued {}
    }
    @TransitionSet
    public class Transitions extends OrderLifecycleMeta.Transitions {}
    @RelationSet
    public static class Relations {

        public static class SummaryPlan {}
        // default to @Relation("plantResource")
        public static class PlantResource {}
        // default to @Relation("concreteTruckResource")
        public static class ConcreteTruckResource {}
    }
}