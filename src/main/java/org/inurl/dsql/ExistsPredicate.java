package org.inurl.dsql;

import java.util.Objects;

import org.inurl.dsql.select.SelectModel;
import org.jetbrains.annotations.NotNull;
import org.inurl.dsql.util.Buildable;

public class ExistsPredicate {
    private final Buildable<SelectModel> selectModelBuilder;
    private final String operator;

    private ExistsPredicate(String operator, Buildable<SelectModel> selectModelBuilder) {
        this.selectModelBuilder = Objects.requireNonNull(selectModelBuilder);
        this.operator = Objects.requireNonNull(operator);
    }

    public String operator() {
        return operator;
    }

    public Buildable<SelectModel> selectModelBuilder() {
        return selectModelBuilder;
    }

    @NotNull
    public static ExistsPredicate exists(Buildable<SelectModel> selectModelBuilder) {
        return new ExistsPredicate("exists", selectModelBuilder);
    }

    @NotNull
    public static ExistsPredicate notExists(Buildable<SelectModel> selectModelBuilder) {
        return new ExistsPredicate("not exists", selectModelBuilder);
    }
}
