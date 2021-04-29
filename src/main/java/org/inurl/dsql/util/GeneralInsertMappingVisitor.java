package org.inurl.dsql.util;

public abstract class GeneralInsertMappingVisitor<R> implements ColumnMappingVisitor<R> {
    @Override
    public final R visit(SelectMapping mapping) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final R visit(PropertyMapping mapping) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final R visit(PropertyWhenPresentMapping mapping) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final R visit(ColumnToColumnMapping columnMapping) {
        throw new UnsupportedOperationException();
    }
}
