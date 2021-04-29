package org.inurl.dsql.select.aggregate;

import java.util.Objects;

import org.inurl.dsql.BasicColumn;
import org.inurl.dsql.render.TableAliasCalculator;

public class CountDistinct extends AbstractCount {

    private final BasicColumn column;

    private CountDistinct(BasicColumn column) {
        this.column = Objects.requireNonNull(column);
    }

    private CountDistinct(BasicColumn column, String alias) {
        super(alias);
        this.column = Objects.requireNonNull(column);
    }

    @Override
    public String renderWithTableAlias(TableAliasCalculator tableAliasCalculator) {
        return "count(distinct " + column.renderWithTableAlias(tableAliasCalculator) + ")";
    }

    @Override
    public CountDistinct as(String alias) {
        return new CountDistinct(column, alias);
    }

    public static CountDistinct of(BasicColumn column) {
        return new CountDistinct(column);
    }
}
