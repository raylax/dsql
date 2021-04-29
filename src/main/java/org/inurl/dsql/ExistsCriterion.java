package org.inurl.dsql;

import java.util.Objects;

public class ExistsCriterion extends SqlCriterion {
    private final ExistsPredicate existsPredicate;

    private ExistsCriterion(Builder builder) {
        super(builder);
        this.existsPredicate = Objects.requireNonNull(builder.existsPredicate);
    }

    public  ExistsPredicate existsPredicate() {
        return existsPredicate;
    }

    @Override
    public <R> R accept(SqlCriterionVisitor<R> visitor) {
        return visitor.visit(this);
    }

    public static class Builder extends AbstractBuilder<Builder> {
        private ExistsPredicate existsPredicate;

        public Builder withExistsPredicate(ExistsPredicate existsPredicate) {
            this.existsPredicate = existsPredicate;
            return this;
        }

        public ExistsCriterion build() {
            return new ExistsCriterion(this);
        }

        @Override
        protected Builder getThis() {
            return this;
        }
    }
}
