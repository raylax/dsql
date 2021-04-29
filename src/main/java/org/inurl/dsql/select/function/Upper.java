package org.inurl.dsql.select.function;

import org.inurl.dsql.render.TableAliasCalculator;
import org.inurl.dsql.BindableColumn;

public class Upper<T> extends AbstractUniTypeFunction<T, Upper<T>> {

    private Upper(BindableColumn<T> column) {
        super(column);
    }

    @Override
    public String renderWithTableAlias(TableAliasCalculator tableAliasCalculator) {
        return "upper("
                + column.renderWithTableAlias(tableAliasCalculator)
                + ")";
    }

    @Override
    protected Upper<T> copy() {
        return new Upper<>(column);
    }

    public static <T> Upper<T> of(BindableColumn<T> column) {
        return new Upper<>(column);
    }
}
