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
package net.imadz.lifecycle;

public class LifecycleCommonErrors {

    /**
     * @param {0} transition simple name or transitionKey Class,
     *        When @Transition(value=T.class) then transitionKeyClass will be
     *        the value other than simple Name
     * @param {1} state name
     * @param {2} target object
     */
    public static final String ILLEGAL_TRANSITION_ON_STATE = "002-9000";
    /**
     * @param {0} target object
     * @param {1} target object's state
     * @param {2} relation object
     * @param {3} relation state
     * @param {4} relation definition
     */
    public static final String STATE_INVALID = "002-9001";
    /**
     * @param {0} transition
     * @param {1} target object next state
     * @param {2} target object
     * @param {3} relation object
     * @param {4} relation object's state
     * @param {5} inbound constraint definition
     */
    public static final String VIOLATE_INBOUND_WHILE_RELATION_CONSTRAINT = "002-9002";
    public static final String BUNDLE = "lifecycle_common";
    /**
     * @param {0} Method object
     */
    public static final String CALLBACK_EXCEPTION_OCCOURRED = "002-9003";
    /**
     * @param {0} Relation class
     * @param {1} @nullable value
     * @param {2} State class
     */
    public static final String VALID_WHILE_RELATION_TARGET_IS_NULL = "002-9004";
    /**
     * @param {0} Relation class
     * @param {1} @nullable value
     * @param {2} State class
     */
    public static final String INBOUND_WHILE_RELATION_TARGET_IS_NULL = "002-9005";

    private LifecycleCommonErrors() {}
}
