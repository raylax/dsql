package org.inurl.dsql.update.render;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DefaultUpdateStatementProvider implements UpdateStatementProvider {
    private final String updateStatement;
    private final Map<String, Object> parameters = new HashMap<>();

    private DefaultUpdateStatementProvider(Builder builder) {
        updateStatement = Objects.requireNonNull(builder.updateStatement);
        parameters.putAll(builder.parameters);
    }

    @Override
    public Map<String, Object> getParameters() {
        return parameters;
    }

    @Override
    public String getUpdateStatement() {
        return updateStatement;
    }

    public static Builder withUpdateStatement(String updateStatement) {
        return new Builder().withUpdateStatement(updateStatement);
    }

    public static class Builder {
        private String updateStatement;
        private final Map<String, Object> parameters = new HashMap<>();

        public Builder withUpdateStatement(String updateStatement) {
            this.updateStatement = updateStatement;
            return this;
        }

        public Builder withParameters(Map<String, Object> parameters) {
            this.parameters.putAll(parameters);
            return this;
        }

        public DefaultUpdateStatementProvider build() {
            return new DefaultUpdateStatementProvider(this);
        }
    }
}
