package org.inurl.dsql;

import java.util.Objects;
import java.util.Optional;

import org.inurl.dsql.render.TableAliasCalculator;

public class Constant<T> implements BindableColumn<T> {

    private final String alias;
    private final String value;

    private Constant(String value) {
        this(value, null);
    }

    private Constant(String value, String alias) {
        this.value = Objects.requireNonNull(value);
        this.alias = alias;
    }

    @Override
    public Optional<String> alias() {
        return Optional.ofNullable(alias);
    }

    @Override
    public String renderWithTableAlias(TableAliasCalculator tableAliasCalculator) {
        return value;
    }

    @Override
    public Constant<T> as(String alias) {
        return new Constant<>(value, alias);
    }

    public static <T> Constant<T> of(String value) {
        return new Constant<>(value);
    }
}
