package org.inurl.dsql.where.render;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class WhereClauseProvider {
    private final String whereClause;
    private final Map<String, Object> parameters;

    private WhereClauseProvider(Builder builder) {
        whereClause = Objects.requireNonNull(builder.whereClause);
        parameters = Objects.requireNonNull(builder.parameters);
    }

    public Map<String, Object> getParameters() {
        return Collections.unmodifiableMap(parameters);
    }

    public String getWhereClause() {
        return whereClause;
    }

    public static Builder withWhereClause(String whereClause) {
        return new Builder().withWhereClause(whereClause);
    }

    public static class Builder {
        private String whereClause;
        private final Map<String, Object> parameters = new HashMap<>();

        public Builder withWhereClause(String whereClause) {
            this.whereClause = whereClause;
            return this;
        }

        public Builder withParameters(Map<String, Object> parameters) {
            this.parameters.putAll(parameters);
            return this;
        }

        public WhereClauseProvider build() {
            return new WhereClauseProvider(this);
        }
    }
}
