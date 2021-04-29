package org.inurl.dsql.select;

import java.util.Optional;

public class PagingModel {

    private final Long limit;
    private final Long offset;
    private final Long fetchFirstRows;

    private PagingModel(Builder builder) {
        super();
        limit = builder.limit;
        offset = builder.offset;
        fetchFirstRows = builder.fetchFirstRows;
    }

    public Optional<Long> limit() {
        return Optional.ofNullable(limit);
    }

    public Optional<Long> offset() {
        return Optional.ofNullable(offset);
    }

    public Optional<Long> fetchFirstRows() {
        return Optional.ofNullable(fetchFirstRows);
    }

    public static class Builder {
        private Long limit;
        private Long offset;
        private Long fetchFirstRows;

        public Builder withLimit(Long limit) {
            this.limit = limit;
            return this;
        }

        public Builder withOffset(Long offset) {
            this.offset = offset;
            return this;
        }

        public Builder withFetchFirstRows(Long fetchFirstRows) {
            this.fetchFirstRows = fetchFirstRows;
            return this;
        }

        public PagingModel build() {
            return new PagingModel(this);
        }
    }
}
