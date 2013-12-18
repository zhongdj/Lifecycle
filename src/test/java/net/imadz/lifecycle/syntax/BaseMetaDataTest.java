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
package net.imadz.lifecycle.syntax;

import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.LogManager;

import org.junit.BeforeClass;

import net.imadz.common.ConsoleLoggingTestBase;
import net.imadz.lifecycle.SyntaxErrors;
import net.imadz.lifecycle.meta.builder.StateMachineMetaBuilder;
import net.imadz.utils.BundleUtils;
import net.imadz.verification.VerificationFailure;

public class BaseMetaDataTest extends ConsoleLoggingTestBase {

    @BeforeClass
    public static void setLogLevel() throws SecurityException, FileNotFoundException, IOException {
        LogManager.getLogManager().readConfiguration(new FileInputStream("target/test-classes/lifecycle_logging.properties"));
    }

    public BaseMetaDataTest() {
        super();
    }

    protected String getMessage(String errorCode, Object... args) {
        return BundleUtils.getBundledMessage(StateMachineMetaBuilder.class, SyntaxErrors.SYNTAX_ERROR_BUNDLE, errorCode, args);
    }

    protected void assertFailure(VerificationFailure failure, String errorCode, Object... args) {
        System.out.println("ExpectedErrorCode:" + errorCode + ",ActualErrorCode:" + failure.getErrorCode());
        assertEquals(errorCode, failure.getErrorCode());
        final String expectedMessage = getMessage(errorCode, args);
        System.out.println("ExpectedMessages:" + expectedMessage + "\n" + "  FailureMessage:" + failure.getErrorMessage(null));
        assertEquals(expectedMessage, failure.getErrorMessage(null));
    }
}