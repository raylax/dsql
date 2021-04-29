package org.inurl.dsql;

/**
 * Defines attributes of columns that are necessary for rendering an order by expression.
 *
 * @author Jeff Butler
 *
 */
public interface SortSpecification {
    /**
     * Returns a new instance of the SortSpecification that should render as descending in an
     * ORDER BY clause.
     *
     * @return new instance of SortSpecification
     */
    SortSpecification descending();

    /**
     * Return the phrase that should be written into a rendered order by clause. This should
     * NOT include the "DESC" word for descending sort specifications.
     *
     * @return the order by phrase
     */
    String orderByName();

    /**
     * Return true if the sort order is descending.
     *
     * @return true if the SortSpecification should render as descending
     */
    boolean isDescending();
}
