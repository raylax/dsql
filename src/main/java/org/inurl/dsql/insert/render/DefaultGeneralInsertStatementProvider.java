package org.inurl.dsql.insert.render;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DefaultGeneralInsertStatementProvider
        implements GeneralInsertStatementProvider, InsertSelectStatementProvider {
    private final String insertStatement;
    private final Map<String, Object> parameters = new HashMap<>();

    private DefaultGeneralInsertStatementProvider(Builder builder) {
        insertStatement = Objects.requireNonNull(builder.insertStatement);
        parameters.putAll(builder.parameters);
    }

    @Override
    public Map<String, Object> getParameters() {
        return parameters;
    }

    @Override
    public String getInsertStatement() {
        return insertStatement;
    }

    public static Builder withInsertStatement(String insertStatement) {
        return new Builder().withInsertStatement(insertStatement);
    }

    public static class Builder {
        private String insertStatement;
        private final Map<String, Object> parameters = new HashMap<>();

        public Builder withInsertStatement(String insertStatement) {
            this.insertStatement = insertStatement;
            return this;
        }

        public Builder withParameters(Map<String, Object> parameters) {
            this.parameters.putAll(parameters);
            return this;
        }

        public DefaultGeneralInsertStatementProvider build() {
            return new DefaultGeneralInsertStatementProvider(this);
        }
    }
}
