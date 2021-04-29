package org.inurl.dsql.util;

import org.inurl.dsql.SqlColumn;

/**
 * This class represents a mapping between a column and a string constant.  The constant should be rendered
 * surrounded by single quotes for SQL.
 *
 * @author Jeff Butler
 *
 */
public class StringConstantMapping extends AbstractColumnMapping {
    private final String constant;

    private StringConstantMapping(SqlColumn<?> column, String constant) {
        super(column);
        this.constant = constant;
    }

    public String constant() {
        return constant;
    }

    public static StringConstantMapping of(SqlColumn<?> column, String constant) {
        return new StringConstantMapping(column, constant);
    }

    @Override
    public <R> R accept(ColumnMappingVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
