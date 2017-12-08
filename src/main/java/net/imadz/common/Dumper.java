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

import net.imadz.util.StringPrintWriter;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;

public final class Dumper {

  private static final String INDENT = "   ";
  private final PrintWriter pw;
  private final StringBuilder sb;
  private final String indent;
  private boolean beginningOfLine = true;
  private Dumper parent;
  private Dumper child;

  public Dumper() {
    this(new StringPrintWriter());
  }

  public Dumper(OutputStream out) {
    this(new PrintWriter(out, true));
  }

  public Dumper(PrintWriter pw) {
    this(pw, "");
  }

  private Dumper(PrintWriter pw, String indent) {
    this.pw = pw;
    this.indent = indent;
    this.sb = new StringBuilder();
  }

  private Dumper(PrintWriter pw, String indent, Dumper parent) {
    this.pw = pw;
    this.indent = indent;
    this.parent = parent;
    this.sb = new StringBuilder();
  }

  public Dumper indent() {
    return push();
  }

  public Dumper undent() {
    return pop();
  }

  public Dumper push() {
    if (child == null) {
      child = new Dumper(pw, indent + INDENT, this);
    }
    return child;
  }

  public Dumper pop() {
    return parent != null ? parent : this;
  }

  public Dumper print(Object o) {
    if (beginningOfLine) {
      sb.append(indent);
      pw.print(indent);
    }
    if (null == o) {
      sb.append("null");
      pw.print("null");
    } else {
      sb.append(o);
      pw.print(o);
    }
    pw.flush();
    beginningOfLine = false;
    return this;
  }

  public Dumper println() {
    sb.append("\n");
    pw.print("\n");
    pw.flush();
    beginningOfLine = true;
    return this;
  }

  public Dumper println(Object o) {
    print(o);
    return println();
  }

  public Dumper printf(String format, Object... values) {
    return print(String.format(format, values));
  }

  public Dumper printfln(String format, Object... values) {
    return println(String.format(format, values));
  }

  public Dumper dump(Map<?, ?> map) {
    println("{");
    for (Entry<?, ?> entry : map.entrySet()) {
      indent().print(entry.getKey()).print(" = ").dump(entry.getValue());
    }
    println("}");
    return this;
  }

  public Dumper dump(Iterable<?> iterable) {
    println("{");
    for (Object element : iterable) {
      indent().dump(element);
    }
    println("}");
    return this;
  }

  public Dumper dump(Object[] array) {
    return dump(Arrays.asList(array));
  }

  public Dumper dump(Object o) {
    if (null == o) {
      println("null");
    } else if (o instanceof Dumpable) {
      ((Dumpable) o).dump(this);
    } else if (o instanceof Iterable) {
      return dump((Iterable<?>) o);
    } else if (o instanceof Map) {
      return dump((Map<?, ?>) o);
    } else if (o.getClass().isArray()) {
      return dump((Object[]) o);
    } else if (o instanceof Throwable) {
      StringPrintWriter spw = new StringPrintWriter();
      ((Throwable) o).printStackTrace(spw);
      for (String line : spw.toString().split("\n")) {
        println(line);
      }
    } else {
      println(o);
    }
    return this;
  }

  @Override
  public String toString() {
    return sb.toString();
  }

  /**
   * Convert a dumpable object to a string.
   */
  public final static String toString(Object obj) {
    if (null != obj) {
      return new Dumper().dump(obj).toString();
    }
    return null;
  }

  public String getIndentString() {
    return indent;
  }
}
