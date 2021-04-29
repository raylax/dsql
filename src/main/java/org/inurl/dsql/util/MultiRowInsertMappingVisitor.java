package org.inurl.dsql.util;

public abstract class MultiRowInsertMappingVisitor<R> extends InsertMappingVisitor<R> {
    @Override
    public final R visit(PropertyWhenPresentMapping mapping) {
        throw new UnsupportedOperationException();
    }
}
