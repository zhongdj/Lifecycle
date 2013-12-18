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
package net.imadz.lifecycle.meta.type;

import net.imadz.lifecycle.meta.MetaType;
import net.imadz.lifecycle.meta.Recoverable;
import net.imadz.lifecycle.meta.object.FunctionMetadata;

public interface StateMetadata extends Recoverable, MetaType<StateMetadata> {

    public static enum StateTypeEnum {
        Running,
        Waiting,
        Stopped,
        Corrupted,
        Common
    }

    StateTypeEnum getType();

    /* ////////////////////////////////////////////////////////////////// */
    /* //////////////////////////Basic Properties /////////////////////// */
    /* ////////////////////////////////////////////////////////////////// */
    StateMachineMetadata getStateMachine();

    String getSimpleName();

    boolean isInitial();

    boolean isFinal();

    /* ////////////////////////////////////////////////////////////////// */
    /* ////////////////////////Transition Related /////////////////////// */
    /* ////////////////////////////////////////////////////////////////// */
    TransitionMetadata[] getPossibleLeavingTransitions();

    TransitionMetadata[] getPossibleReachingTransitions();

    TransitionMetadata getTransition(Object transitionKey);

    boolean isTransitionValid(Object transitionKey);

    /* ////////////////////////////////////////////////////////////////// */
    /* //////////////////////////Dependency Part //////////////////////// */
    /* ////////////////////////////////////////////////////////////////// */
    boolean hasInboundWhiles();

    /**
     * @return related state dependencies, expected to be used post-state-change
     *         validation
     */
    RelationConstraintMetadata[] getDeclaredInboundWhiles();

    boolean hasValidWhiles();

    /**
     * @return related state dependencies, expected to be used pre-state-change
     *         validation, which will validate the validity of the state. Once
     *         the state is not valid, transitions will fail until the state
     *         has been fixed by synchronizationTransition.
     * 
     *         And if parent object life cycle exists, then this state should be
     *         valid ONLY in a subset of parent life cycle states, so does the
     *         parent object, the validation will go up along the parent's
     *         parent recursively.
     * 
     */
    RelationConstraintMetadata[] getValidWhiles();

    /* ////////////////////////////////////////////////////////////////// */
    /* //////////////////////////Composite State///////////////////////// */
    /* ////////////////////////////////////////////////////////////////// */
    boolean isCompositeState();

    StateMetadata getOwningState();

    StateMachineMetadata getCompositeStateMachine();

    /* For Shortcut State inside a composite state */
    StateMetadata getLinkTo();

    FunctionMetadata[] getDeclaredFunctionMetadata();

    FunctionMetadata getDeclaredFunctionMetadata(Object functionKey);

    boolean hasMultipleStateCandidatesOn(Object transtionKey);

    FunctionMetadata getFunctionMetadata(Object functionKey);

    RelationConstraintMetadata[] getDeclaredValidWhiles();

    RelationConstraintMetadata[] getInboundWhiles();

    void setType(StateTypeEnum type);
}
