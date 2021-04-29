package org.inurl.dsql.where.condition;

import org.inurl.dsql.AbstractColumnComparisonCondition;
import org.inurl.dsql.BasicColumn;

public class IsGreaterThanOrEqualToColumn<T> extends AbstractColumnComparisonCondition<T> {

    protected IsGreaterThanOrEqualToColumn(BasicColumn column) {
        super(column);
    }

    @Override
    protected String renderCondition(String leftColumn, String rightColumn) {
        return leftColumn + " >= " + rightColumn;
    }

    public static <T> IsGreaterThanOrEqualToColumn<T> of(BasicColumn column) {
        return new IsGreaterThanOrEqualToColumn<>(column);
    }
}
