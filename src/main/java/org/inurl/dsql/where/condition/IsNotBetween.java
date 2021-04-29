package org.inurl.dsql.where.condition;

import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import org.inurl.dsql.AbstractTwoValueCondition;

public class IsNotBetween<T> extends AbstractTwoValueCondition<T> {
    private static final IsNotBetween<?> EMPTY = new IsNotBetween<Object>(null, null) {
        @Override
        public boolean shouldRender() {
            return false;
        }
    };

    public static <T> IsNotBetween<T> empty() {
        @SuppressWarnings("unchecked")
        IsNotBetween<T> t = (IsNotBetween<T>) EMPTY;
        return t;
    }

    protected IsNotBetween(T value1, T value2) {
        super(value1, value2);
    }

    @Override
    public String renderCondition(String columnName, String placeholder1, String placeholder2) {
        return columnName + " not between " + placeholder1 + " and " + placeholder2;
    }

    /**
     * If renderable and the values match the predicate, returns this condition. Else returns a condition
     *     that will not render.
     *
     * @deprecated replaced by {@link IsNotBetween#filter(BiPredicate)}
     * @param predicate predicate applied to the values, if renderable
     * @return this condition if renderable and the values match the predicate, otherwise a condition
     *     that will not render.
     */
    @Deprecated
    public IsNotBetween<T> when(BiPredicate<T, T> predicate) {
        return filter(predicate);
    }

    /**
     * If renderable, apply the mappings to the values and return a new condition with the new values. Else return a
     *     condition that will not render (this).
     *
     * @deprecated replaced by {@link IsNotBetween#map(Function, Function)}
     * @param mapper1 a mapping function to apply to the first value, if renderable
     * @param mapper2 a mapping function to apply to the second value, if renderable
     * @return a new condition with the result of applying the mappers to the values of this condition,
     *     if renderable, otherwise a condition that will not render.
     */
    @Deprecated
    public IsNotBetween<T> then(UnaryOperator<T> mapper1, UnaryOperator<T> mapper2) {
        return map(mapper1, mapper2);
    }

    @Override
    public IsNotBetween<T> filter(BiPredicate<? super T, ? super T> predicate) {
        return filterSupport(predicate, IsNotBetween::empty, this);
    }

    @Override
    public IsNotBetween<T> filter(Predicate<? super T> predicate) {
        return filterSupport(predicate, IsNotBetween::empty, this);
    }

    /**
     * If renderable, apply the mappings to the values and return a new condition with the new values. Else return a
     *     condition that will not render (this).
     *
     * @param mapper1 a mapping function to apply to the first value, if renderable
     * @param mapper2 a mapping function to apply to the second value, if renderable
     * @param <R> type of the new condition
     * @return a new condition with the result of applying the mappers to the values of this condition,
     *     if renderable, otherwise a condition that will not render.
     */
    public <R> IsNotBetween<R> map(Function<? super T, ? extends R> mapper1,
            Function<? super T, ? extends R> mapper2) {
        return mapSupport(mapper1, mapper2, IsNotBetween::new, IsNotBetween::empty);
    }

    /**
     * If renderable, apply the mapping to both values and return a new condition with the new values. Else return a
     *     condition that will not render (this).
     *
     * @param mapper a mapping function to apply to both values, if renderable
     * @param <R> type of the new condition
     * @return a new condition with the result of applying the mappers to the values of this condition,
     *     if renderable, otherwise a condition that will not render.
     */
    public <R> IsNotBetween<R> map(Function<? super T, ? extends R> mapper) {
        return map(mapper, mapper);
    }

    public static <T> Builder<T> isNotBetween(T value1) {
        return new Builder<>(value1);
    }

    public static <T> WhenPresentBuilder<T> isNotBetweenWhenPresent(T value1) {
        return new WhenPresentBuilder<>(value1);
    }

    public static class Builder<T> extends AndGatherer<T, IsNotBetween<T>> {

        private Builder(T value1) {
            super(value1);
        }

        @Override
        protected IsNotBetween<T> build() {
            return new IsNotBetween<>(value1, value2);
        }
    }

    public static class WhenPresentBuilder<T> extends AndGatherer<T, IsNotBetween<T>> {

        private WhenPresentBuilder(T value1) {
            super(value1);
        }

        @Override
        protected IsNotBetween<T> build() {
            return new IsNotBetween<>(value1, value2).filter(Objects::nonNull);
        }
    }
}
