package org.inurl.dsql.where.condition;

import org.inurl.dsql.AbstractColumnComparisonCondition;
import org.inurl.dsql.BasicColumn;

public class IsLessThanColumn<T> extends AbstractColumnComparisonCondition<T> {

    protected IsLessThanColumn(BasicColumn column) {
        super(column);
    }

    @Override
    protected String renderCondition(String leftColumn, String rightColumn) {
        return leftColumn + " < " + rightColumn;
    }

    public static <T> IsLessThanColumn<T> of(BasicColumn column) {
        return new IsLessThanColumn<>(column);
    }
}
