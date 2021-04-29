package org.inurl.dsql.select.aggregate;

import org.inurl.dsql.render.TableAliasCalculator;
import org.inurl.dsql.BindableColumn;
import org.inurl.dsql.select.function.AbstractUniTypeFunction;

public class Min<T> extends AbstractUniTypeFunction<T, Min<T>> {

    private Min(BindableColumn<T> column) {
        super(column);
    }

    @Override
    public String renderWithTableAlias(TableAliasCalculator tableAliasCalculator) {
        return "min(" + column.renderWithTableAlias(tableAliasCalculator) + ")";
    }

    @Override
    protected Min<T> copy() {
        return new Min<>(column);
    }

    public static <T> Min<T> of(BindableColumn<T> column) {
        return new Min<>(column);
    }
}
