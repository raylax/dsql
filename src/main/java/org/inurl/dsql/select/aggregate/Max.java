package org.inurl.dsql.select.aggregate;

import org.inurl.dsql.BindableColumn;
import org.inurl.dsql.render.TableAliasCalculator;
import org.inurl.dsql.select.function.AbstractUniTypeFunction;

public class Max<T> extends AbstractUniTypeFunction<T, Max<T>> {

    private Max(BindableColumn<T> column) {
        super(column);
    }

    @Override
    public String renderWithTableAlias(TableAliasCalculator tableAliasCalculator) {
        return "max(" + column.renderWithTableAlias(tableAliasCalculator) + ")";
    }

    @Override
    protected Max<T> copy() {
        return new Max<>(column);
    }

    public static <T> Max<T> of(BindableColumn<T> column) {
        return new Max<>(column);
    }
}
