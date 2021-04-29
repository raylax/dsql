package org.inurl.dsql.select;

import java.util.Objects;
import java.util.Optional;

import org.inurl.dsql.TableExpression;
import org.inurl.dsql.TableExpressionVisitor;

public class SubQuery implements TableExpression {
    private final SelectModel selectModel;
    private final String alias;

    private SubQuery(Builder builder) {
        selectModel = Objects.requireNonNull(builder.selectModel);
        alias = builder.alias;
    }

    public SelectModel selectModel() {
        return selectModel;
    }

    public Optional<String> alias() {
        return Optional.ofNullable(alias);
    }

    @Override
    public boolean isSubQuery() {
        return true;
    }

    @Override
    public <R> R accept(TableExpressionVisitor<R> visitor) {
        return visitor.visit(this);
    }

    public static class Builder {
        private SelectModel selectModel;
        private String alias;

        public Builder withSelectModel(SelectModel selectModel) {
            this.selectModel = selectModel;
            return this;
        }

        public Builder withAlias(String alias) {
            this.alias = alias;
            return this;
        }

        public SubQuery build() {
            return new SubQuery(this);
        }
    }
}
