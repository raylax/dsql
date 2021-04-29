package org.inurl.dsql.select.join;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

import org.inurl.dsql.TableExpression;

public class JoinSpecification {

    private final TableExpression table;
    private final List<JoinCriterion> joinCriteria;
    private final JoinType joinType;

    private JoinSpecification(Builder builder) {
        table = Objects.requireNonNull(builder.table);
        joinCriteria = Objects.requireNonNull(builder.joinCriteria);
        joinType = Objects.requireNonNull(builder.joinType);
    }

    public TableExpression table() {
        return table;
    }

    public <R> Stream<R> mapJoinCriteria(Function<JoinCriterion, R> mapper) {
        return joinCriteria.stream().map(mapper);
    }

    public JoinType joinType() {
        return joinType;
    }

    public static Builder withJoinTable(TableExpression table) {
        return new Builder().withJoinTable(table);
    }

    public static class Builder {
        private TableExpression table;
        private final List<JoinCriterion> joinCriteria = new ArrayList<>();
        private JoinType joinType;

        public Builder withJoinTable(TableExpression table) {
            this.table = table;
            return this;
        }

        public Builder withJoinCriterion(JoinCriterion joinCriterion) {
            this.joinCriteria.add(joinCriterion);
            return this;
        }

        public Builder withJoinCriteria(List<JoinCriterion> joinCriteria) {
            this.joinCriteria.addAll(joinCriteria);
            return this;
        }

        public Builder withJoinType(JoinType joinType) {
            this.joinType = joinType;
            return this;
        }

        public JoinSpecification build() {
            return new JoinSpecification(this);
        }
    }
}
