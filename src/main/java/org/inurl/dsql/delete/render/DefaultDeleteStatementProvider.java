package org.inurl.dsql.delete.render;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DefaultDeleteStatementProvider implements DeleteStatementProvider {
    private final String deleteStatement;
    private final Map<String, Object> parameters;

    private DefaultDeleteStatementProvider(Builder builder) {
        deleteStatement = Objects.requireNonNull(builder.deleteStatement);
        parameters = Objects.requireNonNull(builder.parameters);
    }

    @Override
    public Map<String, Object> getParameters() {
        return parameters;
    }

    @Override
    public String getDeleteStatement() {
        return deleteStatement;
    }

    public static Builder withDeleteStatement(String deleteStatement) {
        return new Builder().withDeleteStatement(deleteStatement);
    }

    public static class Builder {
        private String deleteStatement;
        private final Map<String, Object> parameters = new HashMap<>();

        public Builder withDeleteStatement(String deleteStatement) {
            this.deleteStatement = deleteStatement;
            return this;
        }

        public Builder withParameters(Map<String, Object> parameters) {
            this.parameters.putAll(parameters);
            return this;
        }

        public DefaultDeleteStatementProvider build() {
            return new DefaultDeleteStatementProvider(this);
        }
    }
}
