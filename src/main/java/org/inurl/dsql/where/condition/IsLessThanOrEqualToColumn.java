package org.inurl.dsql.where.condition;

import org.inurl.dsql.AbstractColumnComparisonCondition;
import org.inurl.dsql.BasicColumn;

public class IsLessThanOrEqualToColumn<T> extends AbstractColumnComparisonCondition<T> {

    protected IsLessThanOrEqualToColumn(BasicColumn column) {
        super(column);
    }

    @Override
    protected String renderCondition(String leftColumn, String rightColumn) {
        return leftColumn + " <= " + rightColumn;
    }

    public static <T> IsLessThanOrEqualToColumn<T> of(BasicColumn column) {
        return new IsLessThanOrEqualToColumn<>(column);
    }
}
