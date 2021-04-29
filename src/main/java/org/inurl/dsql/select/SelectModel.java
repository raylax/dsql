package org.inurl.dsql.select;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import org.inurl.dsql.select.render.SelectStatementProvider;
import org.jetbrains.annotations.NotNull;
import org.inurl.dsql.render.RenderingStrategy;
import org.inurl.dsql.select.render.SelectRenderer;

public class SelectModel {
    private final List<QueryExpressionModel> queryExpressions;
    private final OrderByModel orderByModel;
    private final PagingModel pagingModel;

    private SelectModel(Builder builder) {
        queryExpressions = Objects.requireNonNull(builder.queryExpressions);
        orderByModel = builder.orderByModel;
        pagingModel = builder.pagingModel;
    }

    public <R> Stream<R> mapQueryExpressions(Function<QueryExpressionModel, R> mapper) {
        return queryExpressions.stream().map(mapper);
    }

    public Optional<OrderByModel> orderByModel() {
        return Optional.ofNullable(orderByModel);
    }

    public Optional<PagingModel> pagingModel() {
        return Optional.ofNullable(pagingModel);
    }

    @NotNull
    public SelectStatementProvider render(RenderingStrategy renderingStrategy) {
        return SelectRenderer.withSelectModel(this)
                .withRenderingStrategy(renderingStrategy)
                .build()
                .render();
    }

    public static Builder withQueryExpressions(List<QueryExpressionModel> queryExpressions) {
        return new Builder().withQueryExpressions(queryExpressions);
    }

    public static class Builder {
        private final List<QueryExpressionModel> queryExpressions = new ArrayList<>();
        private OrderByModel orderByModel;
        private PagingModel pagingModel;

        public Builder withQueryExpression(QueryExpressionModel queryExpression) {
            this.queryExpressions.add(queryExpression);
            return this;
        }

        public Builder withQueryExpressions(List<QueryExpressionModel> queryExpressions) {
            this.queryExpressions.addAll(queryExpressions);
            return this;
        }

        public Builder withOrderByModel(OrderByModel orderByModel) {
            this.orderByModel = orderByModel;
            return this;
        }

        public Builder withPagingModel(PagingModel pagingModel) {
            this.pagingModel = pagingModel;
            return this;
        }

        public SelectModel build() {
            return new SelectModel(this);
        }
    }
}
