package org.inurl.dsql.where.condition;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import org.inurl.dsql.AbstractSingleValueCondition;

public class IsGreaterThan<T> extends AbstractSingleValueCondition<T> {
    private static final IsGreaterThan<?> EMPTY = new IsGreaterThan<Object>(null) {
        @Override
        public boolean shouldRender() {
            return false;
        }
    };

    public static <T> IsGreaterThan<T> empty() {
        @SuppressWarnings("unchecked")
        IsGreaterThan<T> t = (IsGreaterThan<T>) EMPTY;
        return t;
    }

    protected IsGreaterThan(T value) {
        super(value);
    }

    @Override
    public String renderCondition(String columnName, String placeholder) {
        return columnName + " > " + placeholder;
    }

    public static <T> IsGreaterThan<T> of(T value) {
        return new IsGreaterThan<>(value);
    }

    /**
     * If renderable and the value matches the predicate, returns this condition. Else returns a condition
     *     that will not render.
     *
     * @deprecated replaced by {@link IsGreaterThan#filter(Predicate)}
     * @param predicate predicate applied to the value, if renderable
     * @return this condition if renderable and the value matches the predicate, otherwise a condition
     *     that will not render.
     */
    @Deprecated
    public IsGreaterThan<T> when(Predicate<T> predicate) {
        return filter(predicate);
    }

    /**
     * If renderable, apply the mapping to the value and return a new condition with the new value. Else return a
     *     condition that will not render (this).
     *
     * @deprecated replaced by {@link IsGreaterThan#map(Function)}
     * @param mapper a mapping function to apply to the value, if renderable
     * @return a new condition with the result of applying the mapper to the value of this condition,
     *     if renderable, otherwise a condition that will not render.
     */
    @Deprecated
    public IsGreaterThan<T> then(UnaryOperator<T> mapper) {
        return map(mapper);
    }

    @Override
    public IsGreaterThan<T> filter(Predicate<? super T> predicate) {
        return filterSupport(predicate, IsGreaterThan::empty, this);
    }

    /**
     * If renderable, apply the mapping to the value and return a new condition with the new value. Else return a
     *     condition that will not render (this).
     *
     * @param mapper a mapping function to apply to the value, if renderable
     * @param <R> type of the new condition
     * @return a new condition with the result of applying the mapper to the value of this condition,
     *     if renderable, otherwise a condition that will not render.
     */
    public <R> IsGreaterThan<R> map(Function<? super T, ? extends R> mapper) {
        return mapSupport(mapper, IsGreaterThan::new, IsGreaterThan::empty);
    }
}
