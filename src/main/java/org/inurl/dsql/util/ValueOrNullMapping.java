package org.inurl.dsql.util;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

import org.inurl.dsql.SqlColumn;

public class ValueOrNullMapping<T> extends AbstractColumnMapping {

    private final Supplier<T> valueSupplier;
    // keep a reference to the column so we don't lose the type
    private final SqlColumn<T> localColumn;

    private ValueOrNullMapping(SqlColumn<T> column, Supplier<T> valueSupplier) {
        super(column);
        this.valueSupplier = Objects.requireNonNull(valueSupplier);
        localColumn = Objects.requireNonNull(column);
    }

    public Optional<Object> value() {
        return Optional.ofNullable(localColumn.convertParameterType(valueSupplier.get()));
    }

    @Override
    public <R> R accept(ColumnMappingVisitor<R> visitor) {
        return visitor.visit(this);
    }

    public static <T> ValueOrNullMapping<T> of(SqlColumn<T> column, Supplier<T> valueSupplier) {
        return new ValueOrNullMapping<>(column, valueSupplier);
    }
}
