package org.inurl.dsql.where.condition;

import org.inurl.dsql.AbstractSubselectCondition;
import org.inurl.dsql.select.SelectModel;
import org.inurl.dsql.util.Buildable;
import org.jetbrains.annotations.NotNull;

public class IsGreaterThanOrEqualToWithSubselect<T> extends AbstractSubselectCondition<T> {

    protected IsGreaterThanOrEqualToWithSubselect(Buildable<SelectModel> selectModelBuilder) {
        super(selectModelBuilder);
    }

    @NotNull
    public static <T> IsGreaterThanOrEqualToWithSubselect<T> of(Buildable<SelectModel> selectModelBuilder) {
        return new IsGreaterThanOrEqualToWithSubselect<>(selectModelBuilder);
    }

    @Override
    public String renderCondition(String columnName, String renderedSelectStatement) {
        return columnName + " >= (" + renderedSelectStatement + ")";
    }
}
