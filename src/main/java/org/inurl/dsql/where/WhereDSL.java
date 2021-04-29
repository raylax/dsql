package org.inurl.dsql.where;

public class WhereDSL extends AbstractWhereDSL<WhereDSL> {

    private WhereDSL() {}

    @Override
    protected WhereDSL getThis() {
        return this;
    }

    public static WhereDSL where() {
        return new WhereDSL();
    }

    public WhereModel build() {
        return internalBuild();
    }
}
