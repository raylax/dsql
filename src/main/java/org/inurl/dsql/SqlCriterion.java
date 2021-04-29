package org.inurl.dsql;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public abstract class SqlCriterion {

    private final String connector;
    private final List<SqlCriterion> subCriteria;

    protected SqlCriterion(AbstractBuilder<?> builder) {
        connector = builder.connector;
        subCriteria = Objects.requireNonNull(builder.subCriteria);
    }

    public Optional<String> connector() {
        return Optional.ofNullable(connector);
    }

    public <R> Stream<R> mapSubCriteria(Function<SqlCriterion, R> mapper) {
        return subCriteria.stream().map(mapper);
    }

    public abstract <R> R accept(SqlCriterionVisitor<R> visitor);

    protected abstract static class AbstractBuilder<T extends AbstractBuilder<T>> {
        private String connector;
        private final List<SqlCriterion> subCriteria = new ArrayList<>();

        public T withConnector(String connector) {
            this.connector = connector;
            return getThis();
        }

        public T withSubCriteria(List<SqlCriterion> subCriteria) {
            this.subCriteria.addAll(subCriteria);
            return getThis();
        }

        protected abstract T getThis();
    }
}
