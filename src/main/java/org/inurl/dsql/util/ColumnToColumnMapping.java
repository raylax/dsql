package org.inurl.dsql.util;

import org.inurl.dsql.BasicColumn;
import org.inurl.dsql.SqlColumn;

public class ColumnToColumnMapping extends AbstractColumnMapping {

    private final BasicColumn rightColumn;

    private ColumnToColumnMapping(SqlColumn<?> column, BasicColumn rightColumn) {
        super(column);
        this.rightColumn = rightColumn;
    }

    public BasicColumn rightColumn() {
        return rightColumn;
    }

    @Override
    public <R> R accept(ColumnMappingVisitor<R> visitor) {
        return visitor.visit(this);
    }

    public static ColumnToColumnMapping of(SqlColumn<?> column, BasicColumn rightColumn) {
        return new ColumnToColumnMapping(column, rightColumn);
    }
}
