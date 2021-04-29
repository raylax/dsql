package org.inurl.dsql.select.aggregate;

import org.inurl.dsql.BindableColumn;
import org.inurl.dsql.render.TableAliasCalculator;
import org.inurl.dsql.select.function.AbstractUniTypeFunction;

public class Avg<T> extends AbstractUniTypeFunction<T, Avg<T>> {

    private Avg(BindableColumn<T> column) {
        super(column);
    }

    @Override
    public String renderWithTableAlias(TableAliasCalculator tableAliasCalculator) {
        return "avg(" + column.renderWithTableAlias(tableAliasCalculator) + ")";
    }

    @Override
    protected Avg<T> copy() {
        return new Avg<>(column);
    }

    public static <T> Avg<T> of(BindableColumn<T> column) {
        return new Avg<>(column);
    }
}
