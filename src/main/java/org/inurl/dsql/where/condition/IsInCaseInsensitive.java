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

public class IsInCaseInsensitive extends AbstractListValueCondition<String> {
    private static final IsInCaseInsensitive EMPTY = new IsInCaseInsensitive(Collections.emptyList());

    public static IsInCaseInsensitive empty() {
        return EMPTY;
    }

    protected  IsInCaseInsensitive(Collection<String> values) {
        super(values);
    }

    protected  IsInCaseInsensitive(Collection<String> values, Callback emptyCallback) {
        super(values, emptyCallback);
    }

    @Override
    public String renderCondition(String columnName, Stream<String> placeholders) {
        return "upper(" + columnName + ") " +
                placeholders.collect(Collectors.joining(",", "in (", ")"));
    }

    @Override
    public IsInCaseInsensitive withListEmptyCallback(Callback callback) {
        return new IsInCaseInsensitive(values, callback);
    }

    @Override
    public IsInCaseInsensitive filter(Predicate<? super String> predicate) {
        return filterSupport(predicate, IsInCaseInsensitive::new, this, IsInCaseInsensitive::empty);
    }

    /**
     * If renderable, apply the mapping to each value in the list return a new condition with the mapped values.
     *     Else return a condition that will not render (this).
     *
     * @param mapper a mapping function to apply to the values, if renderable
     * @return a new condition with mapped values if renderable, otherwise a condition
     *     that will not render.
     */
    public IsInCaseInsensitive map(UnaryOperator<String> mapper) {
        return mapSupport(mapper, IsInCaseInsensitive::new, IsInCaseInsensitive::empty);
    }

    public static IsInCaseInsensitive of(String... values) {
        return of(Arrays.asList(values));
    }

    public static IsInCaseInsensitive of(Collection<String> values) {
        return new IsInCaseInsensitive(values).map(StringUtilities::safelyUpperCase);
    }
}
