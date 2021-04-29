package org.inurl.dsql.where.render;

import java.util.Objects;

import org.inurl.dsql.util.FragmentAndParameters;

public class RenderedCriterion {
    private final String connector;
    private final FragmentAndParameters fragmentAndParameters;

    private RenderedCriterion(Builder builder) {
        connector = builder.connector;
        fragmentAndParameters = Objects.requireNonNull(builder.fragmentAndParameters);
    }

    public FragmentAndParameters fragmentAndParameters() {
        return fragmentAndParameters;
    }

    public FragmentAndParameters fragmentAndParametersWithConnector() {
        if (connector == null) {
            return fragmentAndParameters;
        } else {
            return prependFragment(fragmentAndParameters, connector);
        }
    }

    private FragmentAndParameters prependFragment(FragmentAndParameters fragmentAndParameters, String connector) {
        return FragmentAndParameters.withFragment(connector + " " + fragmentAndParameters.fragment())
                .withParameters(fragmentAndParameters.parameters())
                .build();
    }

    public static class Builder {
        private String connector;
        private FragmentAndParameters fragmentAndParameters;

        public Builder withConnector(String connector) {
            this.connector = connector;
            return this;
        }

        public Builder withFragmentAndParameters(FragmentAndParameters fragmentAndParameters) {
            this.fragmentAndParameters = fragmentAndParameters;
            return this;
        }

        public RenderedCriterion build() {
            return new RenderedCriterion(this);
        }
    }
}
