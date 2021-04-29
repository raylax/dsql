package org.inurl.dsql.where.condition;

import org.inurl.dsql.AbstractSubselectCondition;
import org.jetbrains.annotations.NotNull;
import org.inurl.dsql.select.SelectModel;
import org.inurl.dsql.util.Buildable;

public class IsLessThanWithSubselect<T> extends AbstractSubselectCondition<T> {

    protected IsLessThanWithSubselect(Buildable<SelectModel> selectModelBuilder) {
        super(selectModelBuilder);
    }

    @NotNull
    public static <T> IsLessThanWithSubselect<T> of(Buildable<SelectModel> selectModelBuilder) {
        return new IsLessThanWithSubselect<>(selectModelBuilder);
    }

    @Override
    public String renderCondition(String columnName, String renderedSelectStatement) {
        return columnName + " < (" + renderedSelectStatement + ")";
    }
}
