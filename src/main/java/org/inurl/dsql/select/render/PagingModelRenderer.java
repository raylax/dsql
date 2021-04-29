package org.inurl.dsql.select.render;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import org.inurl.dsql.select.PagingModel;
import org.inurl.dsql.util.FragmentAndParameters;
import org.inurl.dsql.render.RenderingStrategy;

public class PagingModelRenderer {
    private final RenderingStrategy renderingStrategy;
    private final PagingModel pagingModel;
    private final AtomicInteger sequence;

    private PagingModelRenderer(Builder builder) {
        renderingStrategy = Objects.requireNonNull(builder.renderingStrategy);
        pagingModel = Objects.requireNonNull(builder.pagingModel);
        sequence = Objects.requireNonNull(builder.sequence);
    }

    public Optional<FragmentAndParameters> render() {
        return pagingModel.limit().map(this::limitAndOffsetRender)
                .orElseGet(this::fetchFirstRender);
    }

    private Optional<FragmentAndParameters> limitAndOffsetRender(Long limit) {
        return new LimitAndOffsetPagingModelRenderer(renderingStrategy, limit,
                pagingModel, sequence).render();
    }

    private Optional<FragmentAndParameters> fetchFirstRender() {
        return new FetchFirstPagingModelRenderer(renderingStrategy, pagingModel, sequence).render();
    }

    public static class Builder {
        private RenderingStrategy renderingStrategy;
        private PagingModel pagingModel;
        private AtomicInteger sequence;

        public Builder withRenderingStrategy(RenderingStrategy renderingStrategy) {
            this.renderingStrategy = renderingStrategy;
            return this;
        }

        public Builder withPagingModel(PagingModel pagingModel) {
            this.pagingModel = pagingModel;
            return this;
        }

        public Builder withSequence(AtomicInteger sequence) {
            this.sequence = sequence;
            return this;
        }

        public PagingModelRenderer build() {
            return new PagingModelRenderer(this);
        }
    }
}
