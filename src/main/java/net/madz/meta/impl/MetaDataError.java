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
package net.madz.meta.impl;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import net.madz.meta.FlavorMetaData;
import net.madz.meta.KeySet;
import net.madz.meta.MetaData;
import net.madz.meta.MetaDataFilter;

/**
 * Special Handler that records errors encountered during meta-data processing.
 * 
 * This class is instantiated automatically by the <tt>MetaDataHandlerMap</tt>
 * class.
 */
public class MetaDataError<M extends MetaData> implements FlavorMetaData<M> {

    private final LinkedList<String> errors = new LinkedList<String>();
    private final static KeySet KEY_SET = new KeySet(MetaDataError.class);

    /**
     * Default constructor
     */
    public MetaDataError() {}

    @Override
    public KeySet getKeySet() {
        return KEY_SET;
    }

    /**
     * Add an error to the error list
     */
    public void addError(Throwable e) {
        // Flatten exceptions caused by reflection
        while ( e instanceof InvocationTargetException ) {
            e = ( (InvocationTargetException) e ).getTargetException();
        }
        if ( e instanceof IllegalStateException ) {
            errors.add(e.getMessage());
        } else {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PrintStream pout = new PrintStream(out);
            e.printStackTrace(pout);
            errors.add(out.toString());
        }
    }

    /**
     * List of all error messages
     * 
     * @return List of all error strings
     */
    public List<String> getErrors() {
        return Collections.unmodifiableList(this.errors);
    }

    @Override
    public String toString() {
        return "Errors" + errors;
    }

    @Override
    public MetaDataError<M> filter(MetaData owner, MetaDataFilter filter, boolean lazyFilter) {
        return this;
    }
}
