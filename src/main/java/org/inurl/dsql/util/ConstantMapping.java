package org.inurl.dsql.util;

import org.inurl.dsql.SqlColumn;

/**
 * This class represents a mapping between a column and a constant.  The constant should be rendered
 * exactly as specified here.
 *
 * @author Jeff Butler
 *
 */
public class ConstantMapping extends AbstractColumnMapping {
    private final String constant;

    private ConstantMapping(SqlColumn<?> column, String constant) {
        super(column);
        this.constant = constant;
    }

    public String constant() {
        return constant;
    }

    public static ConstantMapping of(SqlColumn<?> column, String constant) {
        return new ConstantMapping(column, constant);
    }

    @Override
    public <R> R accept(ColumnMappingVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
