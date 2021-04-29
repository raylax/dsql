package org.inurl.dsql.select.function;

import java.util.Objects;
import java.util.Optional;

import org.inurl.dsql.BindableColumn;

/**
 * Represents a function that can change the underlying type. For example, converting a binary field for a base64
 * string, or an integer to a string, etc.
 *
 * <p>Thanks to @endink for the idea.
 *
 * @author Jeff Butler
 *
 * @param <T> The type of the underlying column. For example, if a function converts a VARCHAR to an INT, then the
 *     underlying type will be a String
 * @param <R> The type of the column after the conversion. For example, if a function converts a VARCHAR to an INT, then
 *     the converted type will be Integer
 * @param <U> the specific subtype that implements the function
 */
public abstract class AbstractTypeConvertingFunction<T, R, U extends AbstractTypeConvertingFunction<T, R, U>>
        implements BindableColumn<R> {
    protected final BindableColumn<T> column;
    protected String alias;

    protected AbstractTypeConvertingFunction(BindableColumn<T> column) {
        this.column = Objects.requireNonNull(column);
    }

    @Override
    public Optional<String> alias() {
        return Optional.ofNullable(alias);
    }

    @Override
    public U as(String alias) {
        U newThing = copy();
        newThing.alias = alias;
        return newThing;
    }

    protected abstract U copy();
}
