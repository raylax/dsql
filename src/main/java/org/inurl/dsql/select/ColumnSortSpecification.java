package org.inurl.dsql.select;

import java.util.Objects;

import org.inurl.dsql.SortSpecification;
import org.inurl.dsql.SqlColumn;

public class ColumnSortSpecification implements SortSpecification {
    private final String tableAlias;
    private final SqlColumn<?> column;
    private final boolean isDescending;

    public ColumnSortSpecification(String tableAlias, SqlColumn<?> column) {
        this(tableAlias, column, false);
    }

    private ColumnSortSpecification(String tableAlias, SqlColumn<?> column, boolean isDescending) {
        this.tableAlias = Objects.requireNonNull(tableAlias);
        this.column = Objects.requireNonNull(column);
        this.isDescending = isDescending;
    }

    @Override
    public SortSpecification descending() {
        return new ColumnSortSpecification(tableAlias, column, true);
    }

    @Override
    public String orderByName() {
        return tableAlias + "." + column.name();
    }

    @Override
    public boolean isDescending() {
        return isDescending;
    }
}
