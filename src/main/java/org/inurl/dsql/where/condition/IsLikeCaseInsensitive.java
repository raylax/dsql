package org.inurl.dsql.where.condition;

import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import org.inurl.dsql.AbstractSingleValueCondition;
import org.inurl.dsql.util.StringUtilities;

public class IsLikeCaseInsensitive extends AbstractSingleValueCondition<String> {
    private static final IsLikeCaseInsensitive EMPTY = new IsLikeCaseInsensitive(null) {
        @Override
        public boolean shouldRender() {
            return false;
        }
    };

    public static IsLikeCaseInsensitive empty() {
        return EMPTY;
    }

    protected IsLikeCaseInsensitive(String value) {
        super(value);
    }

    @Override
    public String renderCondition(String columnName, String placeholder) {
        return "upper(" + columnName + ") like " + placeholder;
    }

    @Override
    public String value() {
        return StringUtilities.safelyUpperCase(super.value());
    }

    public static IsLikeCaseInsensitive of(String value) {
        return new IsLikeCaseInsensitive(value);
    }

    /**
     * If renderable and the value matches the predicate, returns this condition. Else returns a condition
     *     that will not render.
     *
     * @deprecated replaced by {@link IsLikeCaseInsensitive#filter(Predicate)}
     * @param predicate predicate applied to the value, if renderable
     * @return this condition if renderable and the value matches the predicate, otherwise a condition
     *     that will not render.
     */
    @Deprecated
    public IsLikeCaseInsensitive when(Predicate<String> predicate) {
        return filter(predicate);
    }

    /**
     * If renderable, apply the mapping to the value and return a new condition with the new value. Else return a
     *     condition that will not render (this).
     *
     * @deprecated replaced by {@link IsLikeCaseInsensitive#map(UnaryOperator)}
     * @param mapper a mapping function to apply to the value, if renderable
     * @return a new condition with the result of applying the mapper to the value of this condition,
     *     if renderable, otherwise a condition that will not render.
     */
    @Deprecated
    public IsLikeCaseInsensitive then(UnaryOperator<String> mapper) {
        return map(mapper);
    }

    @Override
    public IsLikeCaseInsensitive filter(Predicate<? super String> predicate) {
        return filterSupport(predicate, IsLikeCaseInsensitive::empty, this);
    }

    /**
     * If renderable, apply the mapping to the value and return a new condition with the new value. Else return a
     *     condition that will not render (this).
     *
     * @param mapper a mapping function to apply to the value, if renderable
     * @return a new condition with the result of applying the mapper to the value of this condition,
     *     if renderable, otherwise a condition that will not render.
     */
    public IsLikeCaseInsensitive map(UnaryOperator<String> mapper) {
        return mapSupport(mapper, IsLikeCaseInsensitive::new, IsLikeCaseInsensitive::empty);
    }
}
