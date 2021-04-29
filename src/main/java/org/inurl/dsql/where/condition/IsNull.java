package org.inurl.dsql.where.condition;

import java.util.function.BooleanSupplier;

import org.inurl.dsql.AbstractNoValueCondition;

public class IsNull<T> extends AbstractNoValueCondition<T> {
    private static final IsNull<?> EMPTY = new IsNull<Object>() {
        @Override
        public boolean shouldRender() {
            return false;
        }
    };

    public static <T> IsNull<T> empty() {
        @SuppressWarnings("unchecked")
        IsNull<T> t = (IsNull<T>) EMPTY;
        return t;
    }

    public IsNull() {
        super();
    }

    @Override
    public String renderCondition(String columnName) {
        return columnName + " is null";
    }

    /**
     * If renderable and the supplier returns true, returns this condition. Else returns a condition
     *     that will not render.
     *
     * @deprecated replaced by {@link IsNull#filter(BooleanSupplier)}
     * @param booleanSupplier function that specifies whether the condition should render
     * @param <S> condition type - not used except for compilation compliance
     * @return this condition if renderable and the supplier returns true, otherwise a condition
     *     that will not render.
     */
    @Deprecated
    public <S> IsNull<S> when(BooleanSupplier booleanSupplier) {
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
    public <S> IsNull<S> filter(BooleanSupplier booleanSupplier) {
        @SuppressWarnings("unchecked")
        IsNull<S> self = (IsNull<S>) this;
        return filterSupport(booleanSupplier, IsNull::empty, self);
    }
}
