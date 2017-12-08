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
package net.imadz.lifecycle.engine.callback;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PreStateChangePositiveTests extends CallbackTestMetadata {

  @Test
  public void should_increase_counter_if_any_event_method_invoked() {
    final PreCallbackFromAnyToAny o = new PreCallbackFromAnyToAny();
    assertEquals(0, o.getCallbackInvokeCounter());
    o.start();
    assertEquals(1, o.getCallbackInvokeCounter());
    o.finish();
    assertEquals(2, o.getCallbackInvokeCounter());
  }

  @Test
  public void should_increase_counter_if_event_method_invoked_when_state_is_started() {
    final PreCallbackFromStartToAny o = new PreCallbackFromStartToAny();
    assertEquals(0, o.getCallbackInvokeCounter());
    o.start();
    assertEquals(0, o.getCallbackInvokeCounter());
    o.finish();
    assertEquals(1, o.getCallbackInvokeCounter());
  }

  @Test
  public void should_increase_counter_if_event_method_invoked_when_next_state_is_started() {
    final PreCallbackFromAnyToStart o = new PreCallbackFromAnyToStart();
    assertEquals(0, o.getCallbackInvokeCounter());
    o.start();
    assertEquals(1, o.getCallbackInvokeCounter());
    o.finish();
    assertEquals(1, o.getCallbackInvokeCounter());
  }

  @Test
  public void should_increase_counter_if_event_method_invoked_when_from_and_to_state_matched() {
    final OrderWithSpecifiedPreFromToCallback bigOrder = new OrderWithSpecifiedPreFromToCallback();
    assertEquals(0, bigOrder.getCount());
    bigOrder.pay();
    assertEquals(1, bigOrder.getCount());
    bigOrder.deliver();
    assertEquals(2, bigOrder.getCount());
    bigOrder.install();
    assertEquals(3, bigOrder.getCount());
  }
}
