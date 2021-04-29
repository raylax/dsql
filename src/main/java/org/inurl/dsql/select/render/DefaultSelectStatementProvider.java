package org.inurl.dsql.select.render;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DefaultSelectStatementProvider implements SelectStatementProvider {
    private final String selectStatement;
    private final Map<String, Object> parameters;

    private DefaultSelectStatementProvider(Builder builder) {
        selectStatement = Objects.requireNonNull(builder.selectStatement);
        parameters = Collections.unmodifiableMap(Objects.requireNonNull(builder.parameters));
    }

    @Override
    public Map<String, Object> getParameters() {
        return parameters;
    }

    @Override
    public String getSelectStatement() {
        return selectStatement;
    }

    public static Builder withSelectStatement(String selectStatement) {
        return new Builder().withSelectStatement(selectStatement);
    }

    public static class Builder {
        private String selectStatement;
        private final Map<String, Object> parameters = new HashMap<>();

        public Builder withSelectStatement(String selectStatement) {
            this.selectStatement = selectStatement;
            return this;
        }

        public Builder withParameters(Map<String, Object> parameters) {
            this.parameters.putAll(parameters);
            return this;
        }

        public DefaultSelectStatementProvider build() {
            return new DefaultSelectStatementProvider(this);
        }
    }
}
