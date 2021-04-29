package org.inurl.dsql.select.function;

import org.inurl.dsql.render.TableAliasCalculator;
import org.inurl.dsql.BindableColumn;

public class Substring<T> extends AbstractUniTypeFunction<T, Substring<T>> {

    private final int offset;
    private final int length;

    private Substring(BindableColumn<T> column, int offset, int length) {
        super(column);
        this.offset = offset;
        this.length = length;
    }

    @Override
    public String renderWithTableAlias(TableAliasCalculator tableAliasCalculator) {
        return "substring("
                + column.renderWithTableAlias(tableAliasCalculator)
                + ", "
                + offset
                + ", "
                + length
                + ")";
    }

    @Override
    protected Substring<T> copy() {
        return new Substring<>(column, offset, length);
    }

    public static <T> Substring<T> of(BindableColumn<T> column, int offset, int length) {
        return new Substring<>(column, offset, length);
    }
}
