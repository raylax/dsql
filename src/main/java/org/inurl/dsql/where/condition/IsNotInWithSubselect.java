package org.inurl.dsql.where.condition;

import org.inurl.dsql.AbstractSubselectCondition;
import org.inurl.dsql.select.SelectModel;
import org.jetbrains.annotations.NotNull;
import org.inurl.dsql.util.Buildable;

public class IsNotInWithSubselect<T> extends AbstractSubselectCondition<T> {

    protected IsNotInWithSubselect(Buildable<SelectModel> selectModelBuilder) {
        super(selectModelBuilder);
    }

    @NotNull
    public static <T> IsNotInWithSubselect<T> of(Buildable<SelectModel> selectModelBuilder) {
        return new IsNotInWithSubselect<>(selectModelBuilder);
    }

    @Override
    public String renderCondition(String columnName, String renderedSelectStatement) {
        return columnName + " not in (" + renderedSelectStatement + ")";
    }
}
