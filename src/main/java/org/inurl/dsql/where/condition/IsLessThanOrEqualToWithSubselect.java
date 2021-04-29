package org.inurl.dsql.where.condition;

import org.inurl.dsql.AbstractSubselectCondition;
import org.jetbrains.annotations.NotNull;
import org.inurl.dsql.select.SelectModel;
import org.inurl.dsql.util.Buildable;

public class IsLessThanOrEqualToWithSubselect<T> extends AbstractSubselectCondition<T> {

    protected IsLessThanOrEqualToWithSubselect(Buildable<SelectModel> selectModelBuilder) {
        super(selectModelBuilder);
    }

    @NotNull
    public static <T> IsLessThanOrEqualToWithSubselect<T> of(Buildable<SelectModel> selectModelBuilder) {
        return new IsLessThanOrEqualToWithSubselect<>(selectModelBuilder);
    }

    @Override
    public String renderCondition(String columnName, String renderedSelectStatement) {
        return columnName + " <= (" + renderedSelectStatement + ")";
    }
}
