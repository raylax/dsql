package org.inurl.dsql.select.aggregate;

import org.inurl.dsql.render.TableAliasCalculator;
import org.inurl.dsql.BindableColumn;
import org.inurl.dsql.select.function.AbstractUniTypeFunction;

public class Sum<T> extends AbstractUniTypeFunction<T, Sum<T>> {

    private Sum(BindableColumn<T> column) {
        super(column);
    }

    @Override
    public String renderWithTableAlias(TableAliasCalculator tableAliasCalculator) {
        return "sum(" + column.renderWithTableAlias(tableAliasCalculator) + ")";
    }

    @Override
    protected Sum<T> copy() {
        return new Sum<>(column);
    }

    public static <T> Sum<T> of(BindableColumn<T> column) {
        return new Sum<>(column);
    }
}
