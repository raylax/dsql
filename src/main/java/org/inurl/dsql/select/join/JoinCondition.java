package org.inurl.dsql.select.join;

import org.inurl.dsql.BasicColumn;

public abstract class JoinCondition {
    private final BasicColumn rightColumn;

    protected JoinCondition(BasicColumn rightColumn) {
        this.rightColumn = rightColumn;
    }

    public BasicColumn rightColumn() {
        return rightColumn;
    }

    public abstract String operator();
}
