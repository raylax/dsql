package org.inurl.dsql;

import java.util.Objects;
import java.util.Optional;

import org.inurl.dsql.render.TableAliasCalculator;

public class StringConstant implements BindableColumn<String> {

    private final String alias;
    private final String value;

    private StringConstant(String value) {
        this(value, null);
    }

    private StringConstant(String value, String alias) {
        this.value = Objects.requireNonNull(value);
        this.alias = alias;
    }

    @Override
    public Optional<String> alias() {
        return Optional.ofNullable(alias);
    }

    @Override
    public String renderWithTableAlias(TableAliasCalculator tableAliasCalculator) {
        return "'" + value + "'";
    }

    @Override
    public StringConstant as(String alias) {
        return new StringConstant(value, alias);
    }

    public static StringConstant of(String value) {
        return new StringConstant(value);
    }
}
