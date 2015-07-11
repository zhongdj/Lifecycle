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

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import net.imadz.common.Dumpable;
import net.imadz.common.Dumper;
import net.imadz.common.ParameterString;
import net.imadz.util.StringUtil;
import net.imadz.util.json.io.JsonWriter;

/**
 * Collection of VerificationFailures for a meta object.
 */
public class VerificationFailureSet implements Iterable<VerificationFailure>, Dumpable, Cloneable {

    @SuppressWarnings("unchecked")
    public final static VerificationFailureSet NULL_SET = new VerificationFailureSet((Set<VerificationFailure>) Collections.EMPTY_SET);
    // private final static long serialVersionUID = 1L;
    private final Set<VerificationFailure> failureSet;

    private VerificationFailureSet(Set<VerificationFailure> failureSet) {
        this.failureSet = failureSet;
    }

    /**
     * Constructor
     */
    public VerificationFailureSet() {
        failureSet = new LinkedHashSet<VerificationFailure>();
    }

    @Override
    public VerificationFailureSet clone() {
        return new VerificationFailureSet(new LinkedHashSet<VerificationFailure>(this.failureSet));
    }

    /**
     * Remove all failures from the failure set
     */
    public void clear() {
        failureSet.clear();
    }

    /**
     * Add a verification failure to this set
     * 
     * argument metaData
     *            Schema, entity, field, or action with error
     * argument errorKey
     *            error message key
     * argument defaultErrorMessage
     *            Message to display if message key is not found
     * argument details
     *            Message error parameters
     */
    public VerificationFailureSet add(Object source, String errorKey, String defaultErrorMessage, Object... details) {
        add(new VerificationFailure(source, errorKey, defaultErrorMessage, details));
        return this;
    }

    /**
     * Add a verification failure to this set
     * 
     * argument metaData
     *            Schema, entity, field, or action with error
     * argument errorKey
     *            error message key
     * argument defaultErrorMessage
     *            Message to display if message key is not found
     * argument details
     *            Message error parameters
     */
    public VerificationFailureSet add(Throwable e, Object source, String errorKey, String defaultErrorMessage, Object... details) {
        add(new VerificationFailure(e, source, errorKey, defaultErrorMessage, details));
        return this;
    }

    /**
     * Add verification failure to this set
     * 
     * argument failure
     *            VerificationFailure to add
     * @return this set
     */
    public VerificationFailureSet add(VerificationFailure failure) {
        failureSet.add(failure);
        return this;
    }

    /**
     * Add verification failures from the exception to this set
     */
    public VerificationFailureSet add(VerificationException failure) {
        return addAll(failure.getVerificationFailureSet());
    }

    /**
     * Add verification failures from the exception to this set
     */
    public VerificationFailureSet add(VerificationRuntimeException failure) {
        return addAll(failure.getVerificationFailureSet());
    }

    /**
     * Add verification failures from the set to this set
     */
    public VerificationFailureSet addAll(VerificationFailureSet set) {
        this.failureSet.addAll(set.failureSet);
        return this;
    }

    @Override
    public Iterator<VerificationFailure> iterator() {
        return failureSet.iterator();
    }

    /**
     * Throws a VerificationException if this verification set contains any
     * errors.
     * 
     * @throws VerificationException
     */
    public void assertNoFailures() throws VerificationException {
        if ( size() > 0 ) {
            throw new VerificationException(this);
        }
    }

    /**
     * Throws a VerificationRuntimeException if this verification set contains
     * any errors.
     * 
     * @throws VerificationRuntimeException
     */
    public void throwRuntimeException() {
        if ( size() > 0 ) {
            throw new VerificationRuntimeException(this);
        }
    }

    /**
     * Number of verification failures in this set
     */
    public int size() {
        return this.failureSet.size();
    }

    /**
     * Does this verification set contain any verification failures?
     */
    public boolean hasVerificationFailures() {
        return !this.failureSet.isEmpty();
    }

    /**
     * Called by the VerificationException and VerificationRuntimeException to
     * set the exception message
     */
    String getMessage() {
        return StringUtil.toString(failureSet, ", ");
    }

    @Override
    public String toString() {
        ParameterString sb = new ParameterString(getClass().getSimpleName());
        for ( VerificationFailure failure : this.failureSet ) {
            sb.append(failure.getErrorKey());
        }
        return sb.toString();
    }

    /**
     * Return the VerificationFailureSet if it is not null, or create a new one
     * if it is.
     */
    public final static VerificationFailureSet get(VerificationFailureSet set) {
        if ( null == set || NULL_SET == set ) {
            set = new VerificationFailureSet();
        }
        return set;
    }

    @Override
    public void dump(Dumper dumper) {
        for ( VerificationFailure failure : this.failureSet ) {
            failure.dump(dumper);
        }
    }

    public void writeJson(JsonWriter writer) throws IOException {
        writer.startAnonymousArray("Errors");
        try {
            for ( VerificationFailure failure : this.failureSet ) {
                failure.writeJson(writer);
            }
        } finally {
            writer.endArray();
        }
    }
}
