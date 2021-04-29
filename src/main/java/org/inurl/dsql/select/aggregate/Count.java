package org.inurl.dsql.select.aggregate;

import java.util.Objects;

import org.inurl.dsql.BasicColumn;
import org.inurl.dsql.render.TableAliasCalculator;

public class Count extends AbstractCount {

    private final BasicColumn column;

    private Count(BasicColumn column) {
        this.column = Objects.requireNonNull(column);
    }

    private Count(BasicColumn column, String alias) {
        super(alias);
        this.column = Objects.requireNonNull(column);
    }

    @Override
    public String renderWithTableAlias(TableAliasCalculator tableAliasCalculator) {
        return "count(" + column.renderWithTableAlias(tableAliasCalculator) + ")";
    }

    @Override
    public Count as(String alias) {
        return new Count(column, alias);
    }

    public static Count of(BasicColumn column) {
        return new Count(column);
    }
}
