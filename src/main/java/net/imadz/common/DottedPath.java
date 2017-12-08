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


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


/**
 * Represents a hierarchical name in which each level is separated by a dot.
 * This class is loosely modeled after java.io.File
 */
public class DottedPath implements Iterable<DottedPath> {

  private final DottedPath parent;
  private final String name, absoluteName;
  private List<String> paths;

  /**
   * Constructor
   */
  public DottedPath(String name) {
    this(null, name);
  }

  private DottedPath(DottedPath parent, String name) {
    this.parent = parent;
    this.name = name;
    this.absoluteName = null != this.parent ? this.parent.absoluteName + "." + this.name : this.name;
    this.paths = makePaths();
  }

  private List<String> makePaths() {
    if (null == this.parent) {
      final ArrayList<String> arrayList = new ArrayList<String>();
      arrayList.add(this.name);
      return arrayList;
    }
    List<String> paths = new ArrayList<String>(this.parent.paths);
    paths.add(this.name);
    return paths;
  }

  public static DottedPath parse(String path) {
    List<String> segments = Arrays.asList(path.split("\\."));
    DottedPath head = new DottedPath(segments.get(0));
    List<String> tail = segments.subList(1, segments.size());
    return head.append(tail);
  }

  public static DottedPath append(DottedPath parent, String segment) {
    return parent != null ? parent.append(segment) : new DottedPath(segment);
  }

  public DottedPath append(String segment) {
    return new DottedPath(this, segment);
  }

  public DottedPath append(List<String> segments) {
    if (segments.isEmpty()) {
      return this;
    }
    return this.append(segments.get(0)).append(segments.subList(1, segments.size()));
  }

  @Override
  public boolean equals(Object obj) {
    return (obj instanceof DottedPath) && ((DottedPath) obj).absoluteName.equals(this.absoluteName);
  }

  @Override
  public int hashCode() {
    return this.absoluteName.hashCode();
  }

  /**
   * Number of elements from the head element to this element
   */
  public int size() {
    return null != parent ? parent.size() + 1 : 1;
  }

  private List<DottedPath> toList() {
    if (null == this.parent) {
      final List<DottedPath> result = new ArrayList<DottedPath>();
      result.add(this);
      return result;
    }
    ArrayList<DottedPath> dottedPaths = new ArrayList<DottedPath>(this.parent.toList());
    dottedPaths.add(this);
    return dottedPaths;
  }

  @Override
  public Iterator<DottedPath> iterator() {
    return toList().iterator();
  }

  /**
   * Parent element in path
   */
  public DottedPath getParent() {
    return this.parent;
  }

  /**
   * Fully qualified name of this element
   *
   * @return getParent().getAbsoluteName() + "." + getName();
   */
  public String getAbsoluteName() {
    return this.absoluteName;
  }

  /**
   * Local name of this element
   */
  public String getName() {
    return this.name;
  }

  /**
   * Absolute name of this path using the specified separator.
   *
   * @param separator String to append between each path level
   * @return StringBuilder passed in or created
   */
  public StringBuilder toString(StringBuilder sb, final String separator) {
    for (int i = 0; i < paths.size(); i++) {
      sb.append(paths.get(i));
      if (i + 1 < paths.size()) {
        sb.append(separator);
      }
    }
    return sb;
  }

  /**
   * @return absolute name
   */
  @Override
  public String toString() {
    return this.absoluteName;
  }
}
