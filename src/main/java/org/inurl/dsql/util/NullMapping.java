package org.inurl.dsql.util;

import org.inurl.dsql.SqlColumn;

public class NullMapping extends AbstractColumnMapping {
    private NullMapping(SqlColumn<?> column) {
        super(column);
    }

    public static NullMapping of(SqlColumn<?> column) {
        return new NullMapping(column);
    }

    @Override
    public <R> R accept(ColumnMappingVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
