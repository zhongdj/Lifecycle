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
package net.imadz.lifecycle.demo.standalone;

import net.imadz.lifecycle.annotations.LifecycleMeta;
import net.imadz.lifecycle.annotations.StateIndicator;
import net.imadz.lifecycle.annotations.Event;
import net.imadz.lifecycle.demo.standalone.ServiceableLifecycleMeta.Events.Cancel;
import net.imadz.lifecycle.demo.standalone.ServiceableLifecycleMeta.Events.Finish;
import net.imadz.lifecycle.demo.standalone.ServiceableLifecycleMeta.Events.Schedule;
import net.imadz.lifecycle.demo.standalone.ServiceableLifecycleMeta.Events.Start;

/**
 * This interface is the business interface for some domain.
 * ONLY two things to be noticed:
 * 1. Provide the @StateIndicator
 * 1.1 Directly put @StateIndicator on getter method or on field, and state
 * setter will never open for application
 * 1.2 Or to put @StateIndicator on Type definition, and provide a state
 * indicator property name.
 * 
 * 2. Specify actions corresponding to the transitions defined at life cycle
 * meta data with @Event
 * 2.1 Leave @Event with default value while the action method name equals
 * with the transition class simple name
 * 2.2 Specify the transition value with the defined transition class when their
 * names are not equal
 * 
 * @author Barry
 * 
 */
@LifecycleMeta(ServiceableLifecycleMeta.class)
public interface IServiceOrder {

    @Event(Schedule.class)
    void allocateResources(final long summaryPlanId, final long truckResourceId, final long plangResourceId);

    @Event(Start.class)
    void confirmStart();

    @Event(Finish.class)
    void confirmFinish();

    @Event(Cancel.class)
    void cancel();

    @StateIndicator
    String getServiceOrderState();
}
