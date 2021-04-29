package org.inurl.dsql.where.condition;

import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import org.inurl.dsql.AbstractSingleValueCondition;
import org.inurl.dsql.util.StringUtilities;

public class IsNotLikeCaseInsensitive extends AbstractSingleValueCondition<String> {
    private static final IsNotLikeCaseInsensitive EMPTY = new IsNotLikeCaseInsensitive(null) {
        @Override
        public boolean shouldRender() {
            return false;
        }
    };

    public static IsNotLikeCaseInsensitive empty() {
        return EMPTY;
    }

    protected IsNotLikeCaseInsensitive(String value) {
        super(value);
    }

    @Override
    public String renderCondition(String columnName, String placeholder) {
        return "upper(" + columnName + ") not like " + placeholder;
    }

    @Override
    public String value() {
        return StringUtilities.safelyUpperCase(super.value());
    }

    public static IsNotLikeCaseInsensitive of(String value) {
        return new IsNotLikeCaseInsensitive(value);
    }

    /**
     * If renderable and the value matches the predicate, returns this condition. Else returns a condition
     *     that will not render.
     *
     * @deprecated replaced by {@link IsNotLikeCaseInsensitive#filter(Predicate)}
     * @param predicate predicate applied to the value, if renderable
     * @return this condition if renderable and the value matches the predicate, otherwise a condition
     *     that will not render.
     */
    @Deprecated
    public IsNotLikeCaseInsensitive when(Predicate<String> predicate) {
        return filter(predicate);
    }

    /**
     * If renderable, apply the mapping to the value and return a new condition with the new value. Else return a
     *     condition that will not render (this).
     *
     * @deprecated replaced by {@link IsNotLikeCaseInsensitive#map(UnaryOperator)}
     * @param mapper a mapping function to apply to the value, if renderable
     * @return a new condition with the result of applying the mapper to the value of this condition,
     *     if renderable, otherwise a condition that will not render.
     */
    @Deprecated
    public IsNotLikeCaseInsensitive then(UnaryOperator<String> mapper) {
        return map(mapper);
    }

    @Override
    public IsNotLikeCaseInsensitive filter(Predicate<? super String> predicate) {
        return filterSupport(predicate, IsNotLikeCaseInsensitive::empty, this);
    }

    /**
     * If renderable, apply the mapping to the value and return a new condition with the new value. Else return a
     *     condition that will not render (this).
     *
     * @param mapper a mapping function to apply to the value, if renderable
     * @return a new condition with the result of applying the mapper to the value of this condition,
     *     if renderable, otherwise a condition that will not render.
     */
    public IsNotLikeCaseInsensitive map(UnaryOperator<String> mapper) {
        return mapSupport(mapper, IsNotLikeCaseInsensitive::new, IsNotLikeCaseInsensitive::empty);
    }
}
