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
package net.imadz.lifecycle.demo.process;

import net.imadz.lifecycle.annotations.Transition;
import net.imadz.lifecycle.annotations.Transitions;
import net.imadz.lifecycle.annotations.StateMachine;
import net.imadz.lifecycle.annotations.StateSet;
import net.imadz.lifecycle.annotations.EventSet;
import net.imadz.lifecycle.annotations.action.Corrupt;
import net.imadz.lifecycle.annotations.action.Fail;
import net.imadz.lifecycle.annotations.action.Recover;
import net.imadz.lifecycle.annotations.action.Redo;
import net.imadz.lifecycle.annotations.action.Timeout;
import net.imadz.lifecycle.annotations.state.Corrupted;
import net.imadz.lifecycle.annotations.state.End;
import net.imadz.lifecycle.annotations.state.Initial;
import net.imadz.lifecycle.annotations.state.Running;
import net.imadz.lifecycle.annotations.state.Stopped;
import net.imadz.lifecycle.demo.process.IDownloadProcessLifecycleMeta.Events.Activate;
import net.imadz.lifecycle.demo.process.IDownloadProcessLifecycleMeta.Events.Deactive;
import net.imadz.lifecycle.demo.process.IDownloadProcessLifecycleMeta.Events.Err;
import net.imadz.lifecycle.demo.process.IDownloadProcessLifecycleMeta.Events.Finish;
import net.imadz.lifecycle.demo.process.IDownloadProcessLifecycleMeta.Events.Pause;
import net.imadz.lifecycle.demo.process.IDownloadProcessLifecycleMeta.Events.Prepare;
import net.imadz.lifecycle.demo.process.IDownloadProcessLifecycleMeta.Events.Receive;
import net.imadz.lifecycle.demo.process.IDownloadProcessLifecycleMeta.Events.Remove;
import net.imadz.lifecycle.demo.process.IDownloadProcessLifecycleMeta.Events.Restart;
import net.imadz.lifecycle.demo.process.IDownloadProcessLifecycleMeta.Events.Resume;
import net.imadz.lifecycle.demo.process.IDownloadProcessLifecycleMeta.Events.Start;

@StateMachine
public interface IDownloadProcessLifecycleMeta {

    @StateSet
    static class States {

        @Initial
        @Transitions({ @Transition(event = Prepare.class, value = Queued.class), @Transition(event = Remove.class, value = Removed.class) })
        static class New {}
        @Running
        @Transitions({ @Transition(event = Pause.class, value = Paused.class), @Transition(event = Start.class, value = Started.class),
                @Transition(event = Deactive.class, value = InactiveQueued.class), @Transition(event = Remove.class, value = Removed.class) })
        static class Queued {}
        @Running
        @Transitions({ @Transition(event = Pause.class, value = Paused.class), @Transition(event = Receive.class, value = Started.class),
                @Transition(event = Deactive.class, value = InactiveStarted.class), @Transition(event = Err.class, value = Failed.class),
                @Transition(event = Finish.class, value = Finished.class), @Transition(event = Remove.class, value = Removed.class) })
        static class Started {}
        @Corrupted(recoverPriority = 1)
        @Transitions({ @Transition(event = Activate.class, value = Queued.class), @Transition(event = Remove.class, value = Removed.class) })
        static class InactiveQueued {}
        @Corrupted(recoverPriority = 0)
        @Transitions({ @Transition(event = Activate.class, value = Queued.class), @Transition(event = Remove.class, value = Removed.class) })
        static class InactiveStarted {}
        @Stopped
        @Transitions({ @Transition(event = Resume.class, value = New.class), @Transition(event = Restart.class, value = New.class),
                @Transition(event = Remove.class, value = Removed.class) })
        static class Paused {}
        @Stopped
        @Transitions({ @Transition(event = Restart.class, value = New.class), @Transition(event = Resume.class, value = New.class),
                @Transition(event = Remove.class, value = Removed.class), })
        static class Failed {}
        @Stopped
        @Transitions({ @Transition(event = Restart.class, value = New.class), @Transition(event = Remove.class, value = Removed.class), })
        static class Finished {}
        @End
        static class Removed {}
    }
    @EventSet
    static class Events {

        @Recover
        @Timeout(3000L)
        static class Activate {}
        @Corrupt
        @Timeout(3000L)
        static class Deactive {}
        @Fail
        @Timeout(3000L)
        static class Err {}
        static class Prepare {}
        static class Start {}
        static class Resume {}
        static class Pause {}
        static class Finish {}
        static class Receive {}
        @Redo
        @Timeout(3000L)
        static class Restart {}
        static class Remove {}
    }
}
