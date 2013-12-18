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
package net.imadz.common;

import net.imadz.common.DottedPath;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class DottedPathTest {

    @Test
    public void should_know_dotted_path_details_for_one_level() {
        DottedPath path = DottedPath.parse("a");
        assertThat(path.size(), is(1));
        assertThat(path.getAbsoluteName(), is("a"));
        assertThat(path.getName(), is("a"));
    }

    @Test
    public void should_know_the_parent_of_one_level_is_empty() {
        DottedPath path = DottedPath.parse("a");
        assertThat(path.getParent().isPresent(), is(Boolean.FALSE));
    }

    @Test
    public void should_know_dotted_path_details_for_two_levels() {
        DottedPath path = DottedPath.parse("a.b");
        assertThat(path.size(), is(2));
        assertThat(path.getName(), is("b"));
        assertThat(path.getAbsoluteName(), is("a.b"));
    }

    @Test
    public void should_know_dotted_path_details_for_multiple_levels() {
        DottedPath path = DottedPath.parse("a.b.c.d");
        assertThat(path.size(), is(4));
        assertThat(path.getName(), is("d"));
        assertThat(path.getAbsoluteName(), is("a.b.c.d"));
        assertThat(path.getParent().get().getAbsoluteName(), is("a.b.c"));
    }
}