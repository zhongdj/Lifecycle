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
import net.imadz.lifecycle.annotations.action.ConditionSet;
import net.imadz.lifecycle.annotations.action.Conditional;
import net.imadz.lifecycle.annotations.action.ConditionalEvent;
import net.imadz.lifecycle.annotations.state.End;
import net.imadz.lifecycle.annotations.state.Initial;
import net.imadz.lifecycle.demo.inheritance.meta.SummaryPlanLifecycleMeta.Conditions.VolumeMeasurable;
import net.imadz.lifecycle.demo.inheritance.meta.SummaryPlanLifecycleMeta.States.Ongoing;
import net.imadz.lifecycle.demo.inheritance.meta.SummaryPlanLifecycleMeta.States.VolumeLeftEmpty;
import net.imadz.lifecycle.demo.inheritance.meta.SummaryPlanLifecycleMeta.Events.AdjustTotalVolume;
import net.imadz.lifecycle.demo.inheritance.meta.SummaryPlanLifecycleMeta.Events.ConfirmFinish;
import net.imadz.lifecycle.demo.inheritance.meta.SummaryPlanLifecycleMeta.Events.CreateServiceOrder;
import net.imadz.lifecycle.demo.inheritance.meta.SummaryPlanLifecycleMeta.Utils.VolumeMeasurableEvent;

@StateMachine
public interface SummaryPlanLifecycleMeta {

    @StateSet
    public static class States {

        @Functions({ @Function(transition = CreateServiceOrder.class, value = { Ongoing.class, VolumeLeftEmpty.class }),
                @Function(transition = ConfirmFinish.class, value = Done.class),
                @Function(transition = AdjustTotalVolume.class, value = { Ongoing.class, VolumeLeftEmpty.class }) })
        @Initial
        public static class Ongoing {}
        @Functions({ @Function(transition = ConfirmFinish.class, value = Done.class),
                @Function(transition = AdjustTotalVolume.class, value = { Ongoing.class, VolumeLeftEmpty.class }) })
        public static class VolumeLeftEmpty {}
        @End
        public static class Done {}
    }
    @EventSet
    public static class Events {

        @Conditional(judger = VolumeMeasurableEvent.class, condition = VolumeMeasurable.class)
        public static class CreateServiceOrder {}
        @Conditional(judger = VolumeMeasurableEvent.class, condition = VolumeMeasurable.class)
        public static class AdjustTotalVolume {}
        public static class ConfirmFinish {}
    }
    @ConditionSet
    public static class Conditions {

        public static interface VolumeMeasurable {

            boolean isVolumeLeft();
        }
    }
    static class Utils {

        public static class VolumeMeasurableEvent implements ConditionalEvent<VolumeMeasurable> {

            public Class<?> doConditionJudge(VolumeMeasurable measurable) {
                if ( !measurable.isVolumeLeft() ) {
                    return VolumeLeftEmpty.class;
                } else {
                    return Ongoing.class;
                }
            }
        }
    }
}
