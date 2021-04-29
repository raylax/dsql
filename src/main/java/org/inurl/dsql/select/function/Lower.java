package org.inurl.dsql.select.function;

import org.inurl.dsql.BindableColumn;
import org.inurl.dsql.render.TableAliasCalculator;

public class Lower<T> extends AbstractUniTypeFunction<T, Lower<T>> {

    private Lower(BindableColumn<T> column) {
        super(column);
    }

    @Override
    public String renderWithTableAlias(TableAliasCalculator tableAliasCalculator) {
        return "lower("
                + column.renderWithTableAlias(tableAliasCalculator)
                + ")";
    }

    @Override
    protected Lower<T> copy() {
        return new Lower<>(column);
    }

    public static <T> Lower<T> of(BindableColumn<T> column) {
        return new Lower<>(column);
    }
}
