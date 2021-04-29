package org.inurl.dsql.where.condition;

import org.inurl.dsql.AbstractColumnComparisonCondition;
import org.inurl.dsql.BasicColumn;

public class IsNotEqualToColumn<T> extends AbstractColumnComparisonCondition<T> {

    protected IsNotEqualToColumn(BasicColumn column) {
        super(column);
    }

    @Override
    protected String renderCondition(String leftColumn, String rightColumn) {
        return leftColumn + " <> " + rightColumn;
    }

    public static <T> IsNotEqualToColumn<T> of(BasicColumn column) {
        return new IsNotEqualToColumn<>(column);
    }
}
