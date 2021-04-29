package org.inurl.dsql.where.condition;

import java.util.function.BooleanSupplier;

import org.inurl.dsql.AbstractNoValueCondition;

public class IsNotNull<T> extends AbstractNoValueCondition<T> {
    private static final IsNotNull<?> EMPTY = new IsNotNull<Object>() {
        @Override
        public boolean shouldRender() {
            return false;
        }
    };

    public static <T> IsNotNull<T> empty() {
        @SuppressWarnings("unchecked")
        IsNotNull<T> t = (IsNotNull<T>) EMPTY;
        return t;
    }

    public IsNotNull() {
        super();
    }

    @Override
    public String renderCondition(String columnName) {
        return columnName + " is not null";
    }

    /**
     * If renderable and the supplier returns true, returns this condition. Else returns a condition
     *     that will not render.
     *
     * @deprecated replaced by {@link IsNotNull#filter(BooleanSupplier)}
     * @param booleanSupplier function that specifies whether the condition should render
     * @param <S> condition type - not used except for compilation compliance
     * @return this condition if renderable and the supplier returns true, otherwise a condition
     *     that will not render.
     */
    @Deprecated
    public <S> IsNotNull<S> when(BooleanSupplier booleanSupplier) {
        return filter(booleanSupplier);
    }

    /**
     * If renderable and the supplier returns true, returns this condition. Else returns a condition
     *     that will not render.
     *
     * @param booleanSupplier function that specifies whether the condition should render
     * @param <S> condition type - not used except for compilation compliance
     * @return this condition if renderable and the supplier returns true, otherwise a condition
     *     that will not render.
     */
    public <S> IsNotNull<S> filter(BooleanSupplier booleanSupplier) {
        @SuppressWarnings("unchecked")
        IsNotNull<S> self = (IsNotNull<S>) this;
        return filterSupport(booleanSupplier, IsNotNull::empty, self);
    }
}
