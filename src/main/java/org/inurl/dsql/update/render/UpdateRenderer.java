package org.inurl.dsql.update.render;

import static org.inurl.dsql.util.StringUtilities.spaceBefore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.inurl.dsql.render.TableAliasCalculator;
import org.inurl.dsql.update.UpdateModel;
import org.inurl.dsql.util.FragmentAndParameters;
import org.inurl.dsql.util.StringUtilities;
import org.inurl.dsql.where.render.WhereClauseProvider;
import org.inurl.dsql.where.render.WhereRenderer;
import org.inurl.dsql.render.RenderingStrategy;
import org.inurl.dsql.where.WhereModel;

public class UpdateRenderer {
    private final UpdateModel updateModel;
    private final RenderingStrategy renderingStrategy;
    private final AtomicInteger sequence = new AtomicInteger(1);

    private UpdateRenderer(Builder builder) {
        updateModel = Objects.requireNonNull(builder.updateModel);
        renderingStrategy = Objects.requireNonNull(builder.renderingStrategy);
    }

    public UpdateStatementProvider render() {
        SetPhraseVisitor visitor = new SetPhraseVisitor(sequence, renderingStrategy);

        List<Optional<FragmentAndParameters>> fragmentsAndParameters =
                updateModel.mapColumnMappings(m -> m.accept(visitor))
                .collect(Collectors.toList());

        return updateModel.whereModel()
                .flatMap(this::renderWhereClause)
                .map(wc -> renderWithWhereClause(fragmentsAndParameters, wc))
                .orElseGet(() -> renderWithoutWhereClause(fragmentsAndParameters));
    }

    private UpdateStatementProvider renderWithWhereClause(List<Optional<FragmentAndParameters>> fragmentsAndParameters,
            WhereClauseProvider whereClause) {
        return DefaultUpdateStatementProvider
                .withUpdateStatement(calculateUpdateStatement(fragmentsAndParameters, whereClause))
                .withParameters(calculateParameters(fragmentsAndParameters))
                .withParameters(whereClause.getParameters())
                .build();
    }

    private String calculateUpdateStatement(List<Optional<FragmentAndParameters>> fragmentsAndParameters,
            WhereClauseProvider whereClause) {
        return calculateUpdateStatement(fragmentsAndParameters)
                + StringUtilities.spaceBefore(whereClause.getWhereClause());
    }

    private String calculateUpdateStatement(List<Optional<FragmentAndParameters>> fragmentsAndParameters) {
        return "update"
                + StringUtilities.spaceBefore(updateModel.table().tableNameAtRuntime())
                + StringUtilities.spaceBefore(calculateSetPhrase(fragmentsAndParameters));
    }

    private UpdateStatementProvider renderWithoutWhereClause(
            List<Optional<FragmentAndParameters>> fragmentsAndParameters) {
        return DefaultUpdateStatementProvider.withUpdateStatement(calculateUpdateStatement(fragmentsAndParameters))
                .withParameters(calculateParameters(fragmentsAndParameters))
                .build();
    }

    private String calculateSetPhrase(List<Optional<FragmentAndParameters>> fragmentsAndParameters) {
        return fragmentsAndParameters.stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(FragmentAndParameters::fragment)
                .collect(Collectors.joining(", ", "set ", ""));
    }

    private Map<String, Object> calculateParameters(List<Optional<FragmentAndParameters>> fragmentsAndParameters) {
        return fragmentsAndParameters.stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(FragmentAndParameters::parameters)
                .collect(HashMap::new, HashMap::putAll, HashMap::putAll);
    }

    private Optional<WhereClauseProvider> renderWhereClause(WhereModel whereModel) {
        return WhereRenderer.withWhereModel(whereModel)
                .withRenderingStrategy(renderingStrategy)
                .withSequence(sequence)
                .withTableAliasCalculator(TableAliasCalculator.empty())
                .build()
                .render();
    }

    public static Builder withUpdateModel(UpdateModel updateModel) {
        return new Builder().withUpdateModel(updateModel);
    }

    public static class Builder {
        private UpdateModel updateModel;
        private RenderingStrategy renderingStrategy;

        public Builder withUpdateModel(UpdateModel updateModel) {
            this.updateModel = updateModel;
            return this;
        }

        public Builder withRenderingStrategy(RenderingStrategy renderingStrategy) {
            this.renderingStrategy = renderingStrategy;
            return this;
        }

        public UpdateRenderer build() {
            return new UpdateRenderer(this);
        }
    }
}
