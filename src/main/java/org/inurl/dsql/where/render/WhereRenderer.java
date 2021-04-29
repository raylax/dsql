package org.inurl.dsql.where.render;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.inurl.dsql.render.TableAliasCalculator;
import org.inurl.dsql.util.FragmentAndParameters;
import org.inurl.dsql.util.FragmentCollector;
import org.inurl.dsql.SqlCriterion;
import org.inurl.dsql.render.RenderingStrategy;
import org.inurl.dsql.where.WhereModel;

public class WhereRenderer {
    private final WhereModel whereModel;
    private final CriterionRenderer criterionRenderer;

    private WhereRenderer(Builder builder) {
        whereModel = Objects.requireNonNull(builder.whereModel);

        criterionRenderer = new CriterionRenderer.Builder()
                .withSequence(builder.sequence)
                .withRenderingStrategy(builder.renderingStrategy)
                .withTableAliasCalculator(builder.tableAliasCalculator)
                .withParameterName(builder.parameterName)
                .build();
    }

    public Optional<WhereClauseProvider> render() {
        List<RenderedCriterion> renderedCriteria = whereModel.mapCriteria(this::render)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        if (renderedCriteria.isEmpty()) {
            return Optional.empty();
        }

        // The first is rendered without the initial connector because we don't want something like
        // where and(id = ?).  This can happen if the first condition doesn't render.
        FragmentAndParameters initialCriterion = renderedCriteria.get(0).fragmentAndParameters();

        FragmentCollector fc = renderedCriteria.stream()
                .skip(1)
                .map(RenderedCriterion::fragmentAndParametersWithConnector)
                .collect(FragmentCollector.collect(initialCriterion));

        WhereClauseProvider wcp = WhereClauseProvider.withWhereClause(calculateWhereClause(fc))
                .withParameters(fc.parameters())
                .build();
        return Optional.of(wcp);
    }

    private Optional<RenderedCriterion> render(SqlCriterion criterion) {
        return criterion.accept(criterionRenderer);
    }

    private String calculateWhereClause(FragmentCollector collector) {
        return collector.fragments()
                .collect(Collectors.joining(" ", "where ", ""));
    }

    public static Builder withWhereModel(WhereModel whereModel) {
        return new Builder().withWhereModel(whereModel);
    }

    public static class Builder {
        private WhereModel whereModel;
        private RenderingStrategy renderingStrategy;
        private TableAliasCalculator tableAliasCalculator;
        private AtomicInteger sequence;
        private String parameterName;

        public Builder withWhereModel(WhereModel whereModel) {
            this.whereModel = whereModel;
            return this;
        }

        public Builder withRenderingStrategy(RenderingStrategy renderingStrategy) {
            this.renderingStrategy = renderingStrategy;
            return this;
        }

        public Builder withTableAliasCalculator(TableAliasCalculator tableAliasCalculator) {
            this.tableAliasCalculator = tableAliasCalculator;
            return this;
        }

        public Builder withSequence(AtomicInteger sequence) {
            this.sequence = sequence;
            return this;
        }

        public Builder withParameterName(String parameterName) {
            this.parameterName = parameterName;
            return this;
        }

        public WhereRenderer build() {
            return new WhereRenderer(this);
        }
    }
}
