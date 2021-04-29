package org.inurl.dsql.select.join;

import org.inurl.dsql.BasicColumn;

public class EqualTo extends JoinCondition {

    public EqualTo(BasicColumn rightColumn) {
        super(rightColumn);
    }

    @Override
    public String operator() {
        return "=";
    }
}
