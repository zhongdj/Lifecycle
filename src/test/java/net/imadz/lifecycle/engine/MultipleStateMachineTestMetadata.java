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

import net.imadz.lifecycle.annotations.Function;
import net.imadz.lifecycle.annotations.Functions;
import net.imadz.lifecycle.annotations.StateMachine;
import net.imadz.lifecycle.annotations.StateSet;
import net.imadz.lifecycle.annotations.EventSet;
import net.imadz.lifecycle.annotations.relation.InboundWhile;
import net.imadz.lifecycle.annotations.relation.InboundWhiles;
import net.imadz.lifecycle.annotations.relation.Parent;
import net.imadz.lifecycle.annotations.relation.RelateTo;
import net.imadz.lifecycle.annotations.relation.RelationSet;
import net.imadz.lifecycle.annotations.relation.ValidWhile;
import net.imadz.lifecycle.annotations.relation.ValidWhiles;
import net.imadz.lifecycle.annotations.state.End;
import net.imadz.lifecycle.annotations.state.Initial;

public class MultipleStateMachineTestMetadata extends EngineTestBase {

    @StateMachine
    public static interface PCPurchaseOrderStateMachine {

        @StateSet
        static interface States {

            @Initial
            @Functions({ @Function(event = Events.Confirm.class, value = Confirmed.class),
                    @Function(event = Events.Abort.class, value = Aborted.class) })
            static interface Draft {}
            @Functions({ @Function(event = Events.Start.class, value = Ongoing.class),
                    @Function(event = Events.Abort.class, value = Aborted.class) })
            static interface Confirmed {}
            @Functions({ @Function(event = Events.Complete.class, value = Completed.class),
                    @Function(event = Events.Abort.class, value = Aborted.class) })
            static interface Ongoing {}
            @End
            static interface Completed {}
            @End
            static interface Aborted {}
        }
        @EventSet
        static interface Events {

            static interface Confirm {}
            static interface Start {}
            static interface Complete {}
            static interface Abort {}
        }
    }
    @StateMachine
    public static interface PCManufactoringOrderStateMachine {

        @StateSet
        static interface States {

            @Initial
            @Function(event = Events.ConfirmBOM.class, value = BOMConfirmed.class)
            static interface Draft {}
            @InboundWhile(relation = Relations.PurchaseOrder.class, on = PCPurchaseOrderStateMachine.States.Confirmed.class)
            @Function(event = Events.Plan.class, value = Planned.class)
            @ValidWhile(relation = Relations.PurchaseOrder.class, on = PCPurchaseOrderStateMachine.States.Ongoing.class)
            static interface BOMConfirmed {}
            @InboundWhile(relation = Relations.PurchaseOrder.class, on = PCPurchaseOrderStateMachine.States.Ongoing.class)
            @Function(event = Events.MakeOSROMComplete.class, value = OSROMReady.class)
            @ValidWhile(relation = Relations.PurchaseOrder.class, on = PCPurchaseOrderStateMachine.States.Ongoing.class)
            static interface Planned {}
            @InboundWhile(relation = Relations.PurchaseOrder.class, on = PCPurchaseOrderStateMachine.States.Ongoing.class)
            @Function(event = Events.StartAssembling.class, value = AssemblingOnProductLine.class)
            @ValidWhile(relation = Relations.PurchaseOrder.class, on = PCPurchaseOrderStateMachine.States.Ongoing.class)
            static interface OSROMReady {}
            @InboundWhile(relation = Relations.PurchaseOrder.class, on = PCPurchaseOrderStateMachine.States.Ongoing.class)
            @Function(event = Events.StartDebugging.class, value = DebuggingOnProductLine.class)
            @ValidWhile(relation = Relations.PurchaseOrder.class, on = PCPurchaseOrderStateMachine.States.Ongoing.class)
            static interface AssemblingOnProductLine {}
            @InboundWhile(relation = Relations.PurchaseOrder.class, on = PCPurchaseOrderStateMachine.States.Ongoing.class)
            @Function(event = Events.PackageComplete.class, value = Packaged.class)
            @ValidWhile(relation = Relations.PurchaseOrder.class, on = PCPurchaseOrderStateMachine.States.Ongoing.class)
            static interface DebuggingOnProductLine {}
            @InboundWhile(relation = Relations.PurchaseOrder.class, on = PCPurchaseOrderStateMachine.States.Ongoing.class)
            @Function(event = Events.TransferToLogistics.class, value = Done.class)
            @ValidWhile(relation = Relations.PurchaseOrder.class, on = PCPurchaseOrderStateMachine.States.Ongoing.class)
            static interface Packaged {}
            @End
            static interface Done {}
        }
        @EventSet
        static interface Events {

            static interface TransferToLogistics {}
            static interface PackageComplete {}
            static interface StartDebugging {}
            static interface StartAssembling {}
            static interface MakeOSROMComplete {}
            static interface Plan {}
            static interface ConfirmBOM {}
        }
        @RelationSet
        static interface Relations {

            @Parent
            @RelateTo(PCPurchaseOrderStateMachine.class)
            public static interface PurchaseOrder {}
        }
    }
    @StateMachine
    public static interface PCLogisticOrderStateMachine {

        @StateSet
        static interface States {

            @Initial
            @Function(event = Events.Confirm.class, value = Confirmed.class)
            static interface Draft {}
            @InboundWhiles({ @InboundWhile(relation = Relations.PurchaseOrder.class, on = PCPurchaseOrderStateMachine.States.Ongoing.class),
                    @InboundWhile(relation = Relations.ManufactureOrder.class, on = PCManufactoringOrderStateMachine.States.Packaged.class) })
            @Function(event = Events.Schedule.class, value = Scheduled.class)
            @ValidWhile(relation = Relations.PurchaseOrder.class, on = PCPurchaseOrderStateMachine.States.Ongoing.class)
            static interface Confirmed {}
            @InboundWhiles({ @InboundWhile(relation = Relations.PurchaseOrder.class, on = PCPurchaseOrderStateMachine.States.Ongoing.class),
                    @InboundWhile(relation = Relations.ManufactureOrder.class, on = PCManufactoringOrderStateMachine.States.Packaged.class) })
            @Function(event = Events.DoPickup.class, value = Picked.class)
            @ValidWhiles({ @ValidWhile(relation = Relations.PurchaseOrder.class, on = PCPurchaseOrderStateMachine.States.Ongoing.class),
                    @ValidWhile(relation = Relations.ManufactureOrder.class, on = PCManufactoringOrderStateMachine.States.Packaged.class) })
            static interface Scheduled {}
            @InboundWhiles({ @InboundWhile(relation = Relations.PurchaseOrder.class, on = PCPurchaseOrderStateMachine.States.Ongoing.class),
                    @InboundWhile(relation = Relations.ManufactureOrder.class, on = PCManufactoringOrderStateMachine.States.Packaged.class) })
            @Function(event = Events.StartTransport.class, value = Transporting.class)
            @ValidWhiles({ @ValidWhile(relation = Relations.PurchaseOrder.class, on = PCPurchaseOrderStateMachine.States.Ongoing.class),
                    @ValidWhile(relation = Relations.ManufactureOrder.class, on = PCManufactoringOrderStateMachine.States.Done.class) })
            static interface Picked {}
            @InboundWhiles({ @InboundWhile(relation = Relations.PurchaseOrder.class, on = PCPurchaseOrderStateMachine.States.Ongoing.class),
                    @InboundWhile(relation = Relations.ManufactureOrder.class, on = PCManufactoringOrderStateMachine.States.Done.class) })
            @Function(event = Events.CustomerConfirmReceive.class, value = Received.class)
            @ValidWhiles({ @ValidWhile(relation = Relations.PurchaseOrder.class, on = PCPurchaseOrderStateMachine.States.Ongoing.class),
                    @ValidWhile(relation = Relations.ManufactureOrder.class, on = PCManufactoringOrderStateMachine.States.Done.class) })
            static interface Transporting {}
            @End
            static interface Received {}
        }
        @EventSet
        static interface Events {

            static interface Schedule {}
            static interface CustomerConfirmReceive {}
            static interface StartTransport {}
            static interface DoPickup {}
            static interface Confirm {}
        }
        @RelationSet
        static interface Relations {

            @Parent
            @RelateTo(PCPurchaseOrderStateMachine.class)
            public static interface PurchaseOrder {}
            @RelateTo(PCManufactoringOrderStateMachine.class)
            static interface ManufactureOrder {}
        }
    }
}