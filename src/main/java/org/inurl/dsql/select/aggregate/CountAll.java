package org.inurl.dsql.select.aggregate;

import org.inurl.dsql.render.TableAliasCalculator;

public class CountAll extends AbstractCount {

    public CountAll() {
        super();
    }

    private CountAll(String alias) {
        super(alias);
    }

    @Override
    public String renderWithTableAlias(TableAliasCalculator tableAliasCalculator) {
        return "count(*)";
    }

    @Override
    public CountAll as(String alias) {
        return new CountAll(alias);
    }
}
