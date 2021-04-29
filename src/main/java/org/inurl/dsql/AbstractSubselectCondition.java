package org.inurl.dsql;

import org.inurl.dsql.select.SelectModel;
import org.inurl.dsql.util.Buildable;

public abstract class AbstractSubselectCondition<T> implements VisitableCondition<T> {
    private final SelectModel selectModel;

    protected AbstractSubselectCondition(Buildable<SelectModel> selectModelBuilder) {
        this.selectModel = selectModelBuilder.build();
    }

    public SelectModel selectModel() {
        return selectModel;
    }

    @Override
    public <R> R accept(ConditionVisitor<T, R> visitor) {
        return visitor.visit(this);
    }

    public abstract String renderCondition(String columnName, String renderedSelectStatement);
}
