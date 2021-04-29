package org.inurl.dsql;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

public abstract class AbstractNoValueCondition<T> implements VisitableCondition<T> {

    @Override
    public <R> R accept(ConditionVisitor<T, R> visitor) {
        return visitor.visit(this);
    }

    protected <S extends  AbstractNoValueCondition<?>> S filterSupport(BooleanSupplier booleanSupplier,
            Supplier<S> emptySupplier, S self) {
        if (shouldRender()) {
            return booleanSupplier.getAsBoolean() ? self : emptySupplier.get();
        } else {
            return self;
        }
    }

    public abstract String renderCondition(String columnName);
}
