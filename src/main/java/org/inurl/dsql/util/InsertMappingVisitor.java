package org.inurl.dsql.util;

public abstract class InsertMappingVisitor<R> implements ColumnMappingVisitor<R> {
    @Override
    public final <T> R visit(ValueMapping<T> mapping) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final <T> R visit(ValueOrNullMapping<T> mapping) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final <T> R visit(ValueWhenPresentMapping<T> mapping) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final R visit(SelectMapping mapping) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final R visit(ColumnToColumnMapping columnMapping) {
        throw new UnsupportedOperationException();
    }
}
