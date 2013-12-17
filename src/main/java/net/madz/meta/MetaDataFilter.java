package net.madz.meta;

import net.madz.common.DottedPath;

/**
 * 
 * Mechanism to exclude ("filter-out") unwanted meta-definitions.
 * 
 */
public interface MetaDataFilter {

    /**
     * Determine if the specified MetaData passes this filter (that is, it
     * should not be excluded)
     * 
     * @param metaData
     *            Meta-definition to check
     * @return true if the meta-data can be used, false if it should be filtered
     *         out
     */
    public boolean canInclude(Object metaData);

    /**
     * 
     * @param metaData
     *            Meta data to filter
     * @param path
     *            Original name
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
            for ( MetaDataFilter filter : filters ) {
                if ( !filter.canInclude(metaData) ) {
                    return false;
                }
            }
            return true;
        }

        public DottedPath getFilteredName(Object metaData, DottedPath path) {
            for ( MetaDataFilter filter : filters ) {
                path = filter.getFilteredName(metaData, path);
            }
            return path;
        }

        @Override
        public int hashCode() {
            int hashCode = 0;
            for ( MetaDataFilter filter : filters ) {
                hashCode = ( hashCode * 3 ) + filter.hashCode();
            }
            return hashCode;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder(filters.length * 23);
            boolean first = true;
            for ( MetaDataFilter filter : filters ) {
                if ( !first )
                    sb.append("->");
                else
                    first = false;
                sb.append(filter);
            }
            return "Filters[" + sb.toString() + "]";
        }
    }
}
