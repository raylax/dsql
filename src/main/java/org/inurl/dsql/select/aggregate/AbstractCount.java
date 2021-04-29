package org.inurl.dsql.select.aggregate;

import java.util.Optional;

import org.inurl.dsql.BindableColumn;

/**
 * Count functions are implemented differently than the other aggregates. This is primarily to preserve
 * backwards compatibility. Count functions are configured as BindableColumns of type Long
 * as it is assumed that the count functions always return a number.
 */
public abstract class AbstractCount implements BindableColumn<Long> {
    private final String alias;

    protected AbstractCount() {
        this(null);
    }

    protected AbstractCount(String alias) {
        this.alias = alias;
    }

    @Override
    public Optional<String> alias() {
        return Optional.ofNullable(alias);
    }
}
