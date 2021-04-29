package org.inurl.dsql.where.condition;

import org.inurl.dsql.AbstractColumnComparisonCondition;
import org.inurl.dsql.BasicColumn;

public class IsEqualToColumn<T> extends AbstractColumnComparisonCondition<T> {

    protected IsEqualToColumn(BasicColumn column) {
        super(column);
    }

    @Override
    protected String renderCondition(String leftColumn, String rightColumn) {
        return leftColumn + " = " + rightColumn;
    }

    public static <T> IsEqualToColumn<T> of(BasicColumn column) {
        return new IsEqualToColumn<>(column);
    }
}
