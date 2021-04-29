package org.inurl.dsql.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class FragmentAndParameters {

    private final String fragment;
    private final Map<String, Object> parameters;

    private FragmentAndParameters(Builder builder) {
        fragment = Objects.requireNonNull(builder.fragment);
        parameters = Objects.requireNonNull(builder.parameters);
    }

    public String fragment() {
        return fragment;
    }

    public Map<String, Object> parameters() {
        return parameters;
    }

    public FragmentAndParameters add(String newFragment) {
        return withFragment(fragment + newFragment)
                .withParameters(parameters)
                .build();
    }

    public FragmentAndParameters add(String newFragment, Map<String, Object> newParameters) {
        return withFragment(fragment + newFragment)
                .withParameters(parameters)
                .withParameters(newParameters)
                .build();
    }

    public static Builder withFragment(String fragment) {
        return new Builder().withFragment(fragment);
    }

    public static class Builder {
        private String fragment;
        private final Map<String, Object> parameters = new HashMap<>();

        public Builder withFragment(String fragment) {
            this.fragment = fragment;
            return this;
        }

        public Builder withParameter(String key, Object value) {
            parameters.put(key, value);
            return this;
        }

        public Builder withParameters(Map<String, Object> parameters) {
            this.parameters.putAll(parameters);
            return this;
        }

        public FragmentAndParameters build() {
            return new FragmentAndParameters(this);
        }

        public Optional<FragmentAndParameters> buildOptional() {
            return Optional.of(build());
        }
    }
}
