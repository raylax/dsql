package org.inurl.dsql.select.join;

import java.util.Objects;

import org.inurl.dsql.BasicColumn;

public class JoinCriterion {

    private final String connector;
    private final BasicColumn leftColumn;
    private final JoinCondition joinCondition;

    private JoinCriterion(Builder builder) {
        connector = Objects.requireNonNull(builder.connector);
        leftColumn = Objects.requireNonNull(builder.joinColumn);
        joinCondition = Objects.requireNonNull(builder.joinCondition);
    }

    public String connector() {
        return connector;
    }

    public BasicColumn leftColumn() {
        return leftColumn;
    }

    public BasicColumn rightColumn() {
        return joinCondition.rightColumn();
    }

    public String operator() {
        return joinCondition.operator();
    }

    public static class Builder {
        private String connector;
        private BasicColumn joinColumn;
        private JoinCondition joinCondition;

        public Builder withConnector(String connector) {
            this.connector = connector;
            return this;
        }

        public Builder withJoinColumn(BasicColumn joinColumn) {
            this.joinColumn = joinColumn;
            return this;
        }

        public Builder withJoinCondition(JoinCondition joinCondition) {
            this.joinCondition = joinCondition;
            return this;
        }

        public JoinCriterion build() {
            return new JoinCriterion(this);
        }
    }
}
