package org.inurl.dsql.insert.render;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class FieldAndValueAndParameters {
    private final String fieldName;
    private final String valuePhrase;
    private final Map<String, Object> parameters;

    private FieldAndValueAndParameters(Builder builder) {
        fieldName = Objects.requireNonNull(builder.fieldName);
        valuePhrase = Objects.requireNonNull(builder.valuePhrase);
        parameters = builder.parameters;
    }

    public String fieldName() {
        return fieldName;
    }

    public String valuePhrase() {
        return valuePhrase;
    }

    public Map<String, Object> parameters() {
        return parameters;
    }

    public static Builder withFieldName(String fieldName) {
        return new Builder().withFieldName(fieldName);
    }

    public static class Builder {
        private String fieldName;
        private String valuePhrase;
        private final Map<String, Object> parameters = new HashMap<>();

        public Builder withFieldName(String fieldName) {
            this.fieldName = fieldName;
            return this;
        }

        public Builder withValuePhrase(String valuePhrase) {
            this.valuePhrase = valuePhrase;
            return this;
        }

        public Builder withParameter(String key, Object value) {
            parameters.put(key, value);
            return this;
        }

        public FieldAndValueAndParameters build() {
            return new FieldAndValueAndParameters(this);
        }

        public Optional<FieldAndValueAndParameters> buildOptional() {
            return Optional.of(build());
        }
    }
}
