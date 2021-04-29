package org.inurl.dsql.util;

import java.util.Objects;

import org.inurl.dsql.SqlColumn;

public class PropertyMapping extends AbstractColumnMapping {
    private final String property;

    protected PropertyMapping(SqlColumn<?> column, String property) {
        super(column);
        this.property = Objects.requireNonNull(property);
    }

    public String property() {
        return property;
    }

    @Override
    public <R> R accept(ColumnMappingVisitor<R> visitor) {
        return visitor.visit(this);
    }

    public static PropertyMapping of(SqlColumn<?> column, String property) {
        return new PropertyMapping(column, property);
    }
}
