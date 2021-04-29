package org.inurl.dsql.where.condition;

import org.inurl.dsql.AbstractSubselectCondition;
import org.inurl.dsql.select.SelectModel;
import org.jetbrains.annotations.NotNull;
import org.inurl.dsql.util.Buildable;

public class IsInWithSubselect<T> extends AbstractSubselectCondition<T> {

    protected IsInWithSubselect(Buildable<SelectModel> selectModelBuilder) {
        super(selectModelBuilder);
    }

    @NotNull
    public static <T> IsInWithSubselect<T> of(Buildable<SelectModel> selectModelBuilder) {
        return new IsInWithSubselect<>(selectModelBuilder);
    }

    @Override
    public String renderCondition(String columnName, String renderedSelectStatement) {
        return columnName + " in (" + renderedSelectStatement + ")";
    }
}
