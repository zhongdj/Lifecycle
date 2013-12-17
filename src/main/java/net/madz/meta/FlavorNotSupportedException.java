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
package net.madz.meta;

import net.madz.verification.VerificationRuntimeException;

/**
 * Exception thrown when a flavor is requested from an object that either does
 * not support that flavor.
 */
public class FlavorNotSupportedException extends VerificationRuntimeException {

    private final static long serialVersionUID = 1L;

    /**
     * Constructor
     * 
     * @param flavorMetaData
     *            Meta-data object of the flavor
     * @param flavorKey
     *            Flavor key (typically a name or class)
     */
    public FlavorNotSupportedException(Object flavorMetaData, Object flavorKey) {
        super(flavorMetaData, "flavorNotSupported." + toString(flavorKey), "Flavor %s not supported by %s", flavorKey, flavorMetaData);
    }

    /**
     * Convert a flavor key to a String
     */
    private final static String toString(Object flavorKey) {
        if ( flavorKey instanceof Class ) {
            return ( (Class<?>) flavorKey ).getSimpleName();
        }
        return flavorKey.toString();
    }
}
