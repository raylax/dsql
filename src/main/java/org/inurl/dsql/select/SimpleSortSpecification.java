package org.inurl.dsql.select;

import java.util.Objects;

import org.inurl.dsql.SortSpecification;

/**
 * This class is used for an order by phrase where there is no suitable column name
 * to use (for example a calculated column or an aggregate column).
 *
 * @author Jeff Butler
 */
public class SimpleSortSpecification implements SortSpecification {

    private final String name;
    private final boolean isDescending;

    private SimpleSortSpecification(String name) {
        this(name, false);
    }

    private SimpleSortSpecification(String name, boolean isDescending) {
        this.name = Objects.requireNonNull(name);
        this.isDescending = isDescending;
    }

    @Override
    public SortSpecification descending() {
        return new SimpleSortSpecification(name, true);
    }

    @Override
    public String orderByName() {
        return name;
    }

    @Override
    public boolean isDescending() {
        return isDescending;
    }

    public static SimpleSortSpecification of(String name) {
        return new SimpleSortSpecification(name);
    }
}
