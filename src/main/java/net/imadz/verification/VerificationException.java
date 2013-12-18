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
package net.imadz.verification;

import net.imadz.common.Dumpable;
import net.imadz.common.Dumper;

/**
 * Exception caused by a single VerificationFailure or a VerificationFailureSet.
 */
public class VerificationException extends Exception implements Dumpable {

    private final static long serialVersionUID = 1L;
    private final VerificationFailureSet verificationSet;

    /**
     * Convenience constructor
     */
    public VerificationException(VerificationFailureSet verificationSet) {
        this(null, verificationSet);
    }

    /**
     * Full constructor
     * 
     * @param cause
     *            Throwable that has triggered this exception
     * @param verificationSet
     *            Verification failures associated with the exception
     */
    public VerificationException(Throwable cause, VerificationFailureSet verificationSet) {
        super(verificationSet.getMessage(), cause);
        this.verificationSet = verificationSet;
    }

    /**
     * Convenience constructor
     */
    public VerificationException(Throwable cause, VerificationFailure failure) {
        this(cause, new VerificationFailureSet().add(failure));
    }

    /**
     * Convenience constructor
     */
    public VerificationException(VerificationFailure failure) {
        this(null, new VerificationFailureSet().add(failure));
    }

    /**
     * Convenience constructor
     */
    public VerificationException(Object metaData, String errorKey, String defaultErrorMessage, Object... details) {
        this(null, new VerificationFailureSet().add(metaData, errorKey, defaultErrorMessage, details));
    }

    /**
     * Convenience constructor
     */
    public VerificationException(Throwable cause, Object metaData, String errorKey, String defaultErrorMessage, Object... details) {
        this(cause, new VerificationFailureSet().add(cause, metaData, errorKey, defaultErrorMessage, details));
    }

    /**
     * VerificationFailureSet associated with this exception
     */
    public VerificationFailureSet getVerificationFailureSet() {
        return this.verificationSet;
    }

    @Override
    public void dump(Dumper dumper) {
        dumper.println("VerificationException");
        dumper.dump(verificationSet);
    }

    /**
     * Convert this VerificationException into a VerificationRuntimeException
     */
    public VerificationRuntimeException toRuntimeException() {
        VerificationRuntimeException newException = new VerificationRuntimeException(getCause(), this.verificationSet);
        newException.setStackTrace(this.getStackTrace());
        return newException;
    }
}
