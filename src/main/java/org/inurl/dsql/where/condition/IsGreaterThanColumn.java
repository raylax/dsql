package org.inurl.dsql.where.condition;

import org.inurl.dsql.AbstractColumnComparisonCondition;
import org.inurl.dsql.BasicColumn;

public class IsGreaterThanColumn<T> extends AbstractColumnComparisonCondition<T> {

    protected IsGreaterThanColumn(BasicColumn column) {
        super(column);
    }

    @Override
    protected String renderCondition(String leftColumn, String rightColumn) {
        return leftColumn + " > " + rightColumn;
    }

    public static <T> IsGreaterThanColumn<T> of(BasicColumn column) {
        return new IsGreaterThanColumn<>(column);
    }
}
