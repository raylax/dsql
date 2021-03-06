package org.inurl.dsql.where.condition;

import static org.inurl.dsql.util.StringUtilities.spaceAfter;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.inurl.dsql.AbstractListValueCondition;
import org.inurl.dsql.Callback;

public class IsIn<T> extends AbstractListValueCondition<T> {
    private static final IsIn<?> EMPTY = new IsIn<>(Collections.emptyList());

    public static <T> IsIn<T> empty() {
        @SuppressWarnings("unchecked")
        IsIn<T> t = (IsIn<T>) EMPTY;
        return t;
    }

    protected IsIn(Collection<T> values) {
        super(values);
    }

    protected IsIn(Collection<T> values, Callback emptyCallback) {
        super(values, emptyCallback);
    }

    @Override
    public String renderCondition(String columnName, Stream<String> placeholders) {
        return spaceAfter(columnName)
                + placeholders.collect(Collectors.joining(",", "in (", ")"));
    }

    @Override
    public IsIn<T> withListEmptyCallback(Callback callback) {
        return new IsIn<>(values, callback);
    }

    /**
     * This method allows you to modify the condition's values before they are placed into the parameter map.
     * For example, you could filter nulls, or trim strings, etc. This process will run before final rendering of SQL.
     * If you filter values out of the stream, then final condition will not reference those values. If you filter all
     * values out of the stream, then the condition will not render.
     *
     * @deprecated replaced by {@link IsIn#map(Function)} and {@link IsIn#filter(Predicate)}
     * @param valueStreamTransformer a UnaryOperator that will transform the value stream before
     *     the values are placed in the parameter map
     * @return new condition with the specified transformer
     */
    @Deprecated
    public IsIn<T> then(UnaryOperator<Stream<T>> valueStreamTransformer) {
        List<T> mapped = valueStreamTransformer.apply(values.stream())
                .collect(Collectors.toList());
        return new IsIn<>(mapped, emptyCallback);
    }

    @Override
    public IsIn<T> filter(Predicate<? super T> predicate) {
        return filterSupport(predicate, IsIn::new, this, IsIn::empty);
    }

    /**
     * If renderable, apply the mapping to each value in the list return a new condition with the mapped values.
     *     Else return a condition that will not render (this).
     *
     * @param mapper a mapping function to apply to the values, if renderable
     * @param <R> type of the new condition
     * @return a new condition with mapped values if renderable, otherwise a condition
     *     that will not render.
     */
    public <R> IsIn<R> map(Function<? super T, ? extends R> mapper) {
        BiFunction<Collection<R>, Callback, IsIn<R>> constructor = IsIn::new;
        return mapSupport(mapper, constructor, IsIn::empty);
    }

    @SafeVarargs
    public static <T> IsIn<T> of(T... values) {
        return of(Arrays.asList(values));
    }

    public static <T> IsIn<T> of(Collection<T> values) {
        return new IsIn<>(values);
    }
}
