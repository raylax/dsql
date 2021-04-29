package org.inurl.dsql.util;

import java.util.Objects;
import java.util.function.Supplier;

import org.inurl.dsql.SqlColumn;

public class ValueMapping<T> extends AbstractColumnMapping {

    private final Supplier<T> valueSupplier;
    // keep a reference to the column so we don't lose the type
    private final SqlColumn<T> localColumn;

    private ValueMapping(SqlColumn<T> column, Supplier<T> valueSupplier) {
        super(column);
        this.valueSupplier = Objects.requireNonNull(valueSupplier);
        localColumn = Objects.requireNonNull(column);
    }

    public Object value() {
        return localColumn.convertParameterType(valueSupplier.get());
    }

    @Override
    public <R> R accept(ColumnMappingVisitor<R> visitor) {
        return visitor.visit(this);
    }

    public static <T> ValueMapping<T> of(SqlColumn<T> column, Supplier<T> valueSupplier) {
        return new ValueMapping<>(column, valueSupplier);
    }
}
