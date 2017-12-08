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
package net.imadz.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * PrintWriter that can return its parent writer.
 * <p>
 * This class is typically used to print to strings, and therefore prefers being
 * wrapper by a StringWriter.
 * <p>
 * <h3>Usage</h3>
 * <p>
 * <pre>
 *    Exception e = ...;
 *
 *    StringPrintWriter strWriter= new StringPrintWriter();
 *    e.printStackTrace( strWriter );
 *    System.out.println( "Exception is: " + strWriter );
 * </pre>
 */
public class StringPrintWriter extends PrintWriter {

  private final Writer base;

  /**
   * Default constructor
   */
  public StringPrintWriter() {
    this(new StringWriter());
  }

  /**
   * Constructor that wraps any arbitrary Writer.
   */
  public StringPrintWriter(Writer base) {
    super(base);
    this.base = base;
  }

  /**
   * Underlying Writer that this StringPrintWriter is writing to.
   *
   * @return Writer based in to constructor, or the StringWriter created by
   * the default constructor
   */
  public Writer getBaseWriter() {
    return this.base;
  }

  /**
   * Returns the String representation of the base writer
   *
   * @return base.toString()
   */
  @Override
  public String toString() {
    return base.toString();
  }
}
