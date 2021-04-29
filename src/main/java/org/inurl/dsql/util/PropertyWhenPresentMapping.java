package org.inurl.dsql.util;

import java.util.Objects;
import java.util.function.Supplier;

import org.inurl.dsql.SqlColumn;

public class PropertyWhenPresentMapping extends PropertyMapping {
    private final Supplier<?> valueSupplier;

    private PropertyWhenPresentMapping(SqlColumn<?> column, String property, Supplier<?> valueSupplier) {
        super(column, property);
        this.valueSupplier = Objects.requireNonNull(valueSupplier);
    }

    public boolean shouldRender() {
        return valueSupplier.get() != null;
    }

    @Override
    public <R> R accept(ColumnMappingVisitor<R> visitor) {
        return visitor.visit(this);
    }

    public static PropertyWhenPresentMapping of(SqlColumn<?> column, String property, Supplier<?> valueSupplier) {
        return new PropertyWhenPresentMapping(column, property, valueSupplier);
    }
}
