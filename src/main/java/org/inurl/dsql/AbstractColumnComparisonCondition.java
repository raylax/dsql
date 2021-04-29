package org.inurl.dsql;

import org.inurl.dsql.render.TableAliasCalculator;

public abstract class AbstractColumnComparisonCondition<T> implements VisitableCondition<T> {

    protected final BasicColumn column;

    protected AbstractColumnComparisonCondition(BasicColumn column) {
        this.column = column;
    }

    @Override
    public <R> R accept(ConditionVisitor<T, R> visitor) {
        return visitor.visit(this);
    }

    public String renderCondition(String columnName, TableAliasCalculator tableAliasCalculator) {
        return renderCondition(columnName, column.renderWithTableAlias(tableAliasCalculator));
    }

    protected abstract String renderCondition(String leftColumn, String rightColumn);
}
