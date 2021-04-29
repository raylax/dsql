package org.inurl.dsql.where.condition;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import org.inurl.dsql.AbstractSingleValueCondition;

public class IsLike<T> extends AbstractSingleValueCondition<T> {
    private static final IsLike<?> EMPTY = new IsLike<Object>(null) {
        @Override
        public boolean shouldRender() {
            return false;
        }
    };

    public static <T> IsLike<T> empty() {
        @SuppressWarnings("unchecked")
        IsLike<T> t = (IsLike<T>) EMPTY;
        return t;
    }

    protected IsLike(T value) {
        super(value);
    }

    @Override
    public String renderCondition(String columnName, String placeholder) {
        return columnName + " like " + placeholder;
    }

    public static <T> IsLike<T> of(T value) {
        return new IsLike<>(value);
    }

    /**
     * If renderable and the value matches the predicate, returns this condition. Else returns a condition
     *     that will not render.
     *
     * @deprecated replaced by {@link IsLike#filter(Predicate)}
     * @param predicate predicate applied to the value, if renderable
     * @return this condition if renderable and the value matches the predicate, otherwise a condition
     *     that will not render.
     */
    @Deprecated
    public IsLike<T> when(Predicate<T> predicate) {
        return filter(predicate);
    }

    /**
     * If renderable, apply the mapping to the value and return a new condition with the new value. Else return a
     *     condition that will not render (this).
     *
     * @deprecated replaced by {@link IsLike#map(Function)}
     * @param mapper a mapping function to apply to the value, if renderable
     * @return a new condition with the result of applying the mapper to the value of this condition,
     *     if renderable, otherwise a condition that will not render.
     */
    @Deprecated
    public IsLike<T> then(UnaryOperator<T> mapper) {
        return map(mapper);
    }

    @Override
    public IsLike<T> filter(Predicate<? super T> predicate) {
        return filterSupport(predicate, IsLike::empty, this);
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
    public <R> IsLike<R> map(Function<? super T, ? extends R> mapper) {
        return mapSupport(mapper, IsLike::new, IsLike::empty);
    }
}
