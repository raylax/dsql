package org.inurl.dsql.util;

import java.util.Objects;
import java.util.function.Function;

import org.inurl.dsql.SqlColumn;

public abstract class AbstractColumnMapping {
    protected final SqlColumn<?> column;

    protected AbstractColumnMapping(SqlColumn<?> column) {
        this.column = Objects.requireNonNull(column);
    }

    public String columnName() {
        return column.name();
    }

    public <R> R mapColumn(Function<SqlColumn<?>, R> mapper) {
        return mapper.apply(column);
    }

    public abstract <R> R accept(ColumnMappingVisitor<R> visitor);
}
