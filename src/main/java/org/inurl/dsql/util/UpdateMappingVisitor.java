package org.inurl.dsql.util;

public abstract class UpdateMappingVisitor<R> implements ColumnMappingVisitor<R> {
    @Override
    public final R visit(PropertyMapping mapping) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final R visit(PropertyWhenPresentMapping mapping) {
        throw new UnsupportedOperationException();
    }
}
