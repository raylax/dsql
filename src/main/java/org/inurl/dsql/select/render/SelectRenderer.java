package org.inurl.dsql.select.render;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.inurl.dsql.SortSpecification;
import org.inurl.dsql.select.PagingModel;
import org.inurl.dsql.select.QueryExpressionModel;
import org.inurl.dsql.select.SelectModel;
import org.inurl.dsql.util.CustomCollectors;
import org.inurl.dsql.util.FragmentAndParameters;
import org.inurl.dsql.util.FragmentCollector;
import org.inurl.dsql.render.RenderingStrategy;
import org.inurl.dsql.select.OrderByModel;

public class SelectRenderer {
    private final SelectModel selectModel;
    private final RenderingStrategy renderingStrategy;
    private final AtomicInteger sequence;

    private SelectRenderer(Builder builder) {
        selectModel = Objects.requireNonNull(builder.selectModel);
        renderingStrategy = Objects.requireNonNull(builder.renderingStrategy);
        sequence = builder.sequence().orElseGet(() -> new AtomicInteger(1));
    }

    public SelectStatementProvider render() {
        FragmentCollector fragmentCollector = selectModel
                .mapQueryExpressions(this::renderQueryExpression)
                .collect(FragmentCollector.collect());
        renderOrderBy(fragmentCollector);
        renderPagingModel(fragmentCollector);

        String selectStatement = fragmentCollector.fragments().collect(Collectors.joining(" "));

        return DefaultSelectStatementProvider.withSelectStatement(selectStatement)
                .withParameters(fragmentCollector.parameters())
                .build();
    }

    private FragmentAndParameters renderQueryExpression(QueryExpressionModel queryExpressionModel) {
        return QueryExpressionRenderer.withQueryExpression(queryExpressionModel)
                .withRenderingStrategy(renderingStrategy)
                .withSequence(sequence)
                .build()
                .render();
    }

    private void renderOrderBy(FragmentCollector fragmentCollector) {
        selectModel.orderByModel().ifPresent(om -> renderOrderBy(fragmentCollector, om));
    }

    private void renderOrderBy(FragmentCollector fragmentCollector, OrderByModel orderByModel) {
        String phrase = orderByModel.mapColumns(this::calculateOrderByPhrase)
                .collect(CustomCollectors.joining(", ", "order by ", ""));
        fragmentCollector.add(FragmentAndParameters.withFragment(phrase).build());
    }

    private String calculateOrderByPhrase(SortSpecification column) {
        String phrase = column.orderByName();
        if (column.isDescending()) {
            phrase = phrase + " DESC";
        }
        return phrase;
    }

    private void renderPagingModel(FragmentCollector fragmentCollector) {
        selectModel.pagingModel().flatMap(this::renderPagingModel)
            .ifPresent(fragmentCollector::add);
    }

    private Optional<FragmentAndParameters> renderPagingModel(PagingModel pagingModel) {
        return new PagingModelRenderer.Builder()
                .withPagingModel(pagingModel)
                .withRenderingStrategy(renderingStrategy)
                .withSequence(sequence)
                .build()
                .render();
    }

    public static Builder withSelectModel(SelectModel selectModel) {
        return new Builder().withSelectModel(selectModel);
    }

    public static class Builder {
        private SelectModel selectModel;
        private RenderingStrategy renderingStrategy;
        private AtomicInteger sequence;

        public Builder withSelectModel(SelectModel selectModel) {
            this.selectModel = selectModel;
            return this;
        }

        public Builder withRenderingStrategy(RenderingStrategy renderingStrategy) {
            this.renderingStrategy = renderingStrategy;
            return this;
        }

        public Builder withSequence(AtomicInteger sequence) {
            this.sequence = sequence;
            return this;
        }

        private Optional<AtomicInteger> sequence() {
            return Optional.ofNullable(sequence);
        }

        public SelectRenderer build() {
            return new SelectRenderer(this);
        }
    }
}
