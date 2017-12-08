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
package net.imadz.meta;

import net.imadz.common.DottedPath;

/**
 * Mechanism to exclude ("filter-out") unwanted meta-definitions.
 */
public interface MetaDataFilter {

  /**
   * Determine if the specified MetaData passes this filter (that is, it
   * should not be excluded)
   *
   * @param metaData Meta-definition to check
   * @return true if the meta-data can be used, false if it should be filtered
   * out
   */
  public boolean canInclude(Object metaData);

  /**
   * @param metaData Meta data to filter
   * @param path     Original name
   * @return Filtered name (or original name if not filtered)
   */
  public DottedPath getFilteredName(Object metaData, DottedPath path);

  public final static MetaDataFilter NULL_FILTER = new MetaDataFilter() {

    public boolean canInclude(Object metaData) {
      return true;
    }

    public DottedPath getFilteredName(Object metaData, DottedPath path) {
      return path;
    }

    public String toString() {
      return "NullFilter";
    }
  };

  /**
   * Simple MetaDataFilter that chains together multiple filters
   */
  public static class MetaDataFilterSet implements MetaDataFilter {

    private final MetaDataFilter[] filters;

    /**
     * Constructor
     */
    public MetaDataFilterSet(MetaDataFilter... filters) {
      this.filters = filters;
    }

    @Override
    public boolean canInclude(final Object metaData) {
      for (MetaDataFilter filter : filters) {
        if (!filter.canInclude(metaData)) {
          return false;
        }
      }
      return true;
    }

    public DottedPath getFilteredName(Object metaData, DottedPath path) {
      for (MetaDataFilter filter : filters) {
        path = filter.getFilteredName(metaData, path);
      }
      return path;
    }

    @Override
    public int hashCode() {
      int hashCode = 0;
      for (MetaDataFilter filter : filters) {
        hashCode = (hashCode * 3) + filter.hashCode();
      }
      return hashCode;
    }

    @Override
    public String toString() {
      final StringBuilder sb = new StringBuilder(filters.length * 23);
      boolean first = true;
      for (MetaDataFilter filter : filters) {
        if (!first) {
          sb.append("->");
        } else {
          first = false;
        }
        sb.append(filter);
      }
      return "Filters[" + sb.toString() + "]";
    }
  }
}
