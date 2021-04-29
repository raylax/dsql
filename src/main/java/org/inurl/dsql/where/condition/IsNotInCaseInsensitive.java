package org.inurl.dsql.where.condition;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.inurl.dsql.AbstractListValueCondition;
import org.inurl.dsql.Callback;
import org.inurl.dsql.util.StringUtilities;

public class IsNotInCaseInsensitive extends AbstractListValueCondition<String> {
    private static final IsNotInCaseInsensitive EMPTY = new IsNotInCaseInsensitive(Collections.emptyList());

    public static IsNotInCaseInsensitive empty() {
        return EMPTY;
    }

    protected IsNotInCaseInsensitive(Collection<String> values) {
        super(values);
    }

    protected IsNotInCaseInsensitive(Collection<String> values, Callback emptyCallback) {
        super(values, emptyCallback);
    }

    @Override
    public String renderCondition(String columnName, Stream<String> placeholders) {
        return "upper(" + columnName + ") " +
                placeholders.collect(
                        Collectors.joining(",", "not in (", ")"));
    }

    @Override
    public IsNotInCaseInsensitive withListEmptyCallback(Callback callback) {
        return new IsNotInCaseInsensitive(values, callback);
    }

    @Override
    public IsNotInCaseInsensitive filter(Predicate<? super String> predicate) {
        return filterSupport(predicate, IsNotInCaseInsensitive::new, this, IsNotInCaseInsensitive::empty);
    }

    /**
     * If renderable, apply the mapping to each value in the list return a new condition with the mapped values.
     *     Else return a condition that will not render (this).
     *
     * @param mapper a mapping function to apply to the values, if renderable
     * @return a new condition with mapped values if renderable, otherwise a condition
     *     that will not render.
     */
    public IsNotInCaseInsensitive map(UnaryOperator<String> mapper) {
        return mapSupport(mapper, IsNotInCaseInsensitive::new, IsNotInCaseInsensitive::empty);
    }

    public static IsNotInCaseInsensitive of(String... values) {
        return of(Arrays.asList(values));
    }

    public static IsNotInCaseInsensitive of(Collection<String> values) {
        return new IsNotInCaseInsensitive(values).map(StringUtilities::safelyUpperCase);
    }
}
