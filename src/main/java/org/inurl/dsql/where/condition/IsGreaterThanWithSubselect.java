package org.inurl.dsql.where.condition;

import org.inurl.dsql.AbstractSubselectCondition;
import org.jetbrains.annotations.NotNull;
import org.inurl.dsql.select.SelectModel;
import org.inurl.dsql.util.Buildable;

public class IsGreaterThanWithSubselect<T> extends AbstractSubselectCondition<T> {

    protected IsGreaterThanWithSubselect(Buildable<SelectModel> selectModelBuilder) {
        super(selectModelBuilder);
    }

    @NotNull
    public static <T> IsGreaterThanWithSubselect<T> of(Buildable<SelectModel> selectModelBuilder) {
        return new IsGreaterThanWithSubselect<>(selectModelBuilder);
    }

    @Override
    public String renderCondition(String columnName, String renderedSelectStatement) {
        return columnName + " > (" + renderedSelectStatement + ")";
    }
}
