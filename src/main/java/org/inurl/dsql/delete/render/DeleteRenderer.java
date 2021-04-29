package org.inurl.dsql.delete.render;

import static org.inurl.dsql.util.StringUtilities.spaceBefore;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import org.inurl.dsql.render.TableAliasCalculator;
import org.inurl.dsql.where.render.WhereClauseProvider;
import org.inurl.dsql.delete.DeleteModel;
import org.inurl.dsql.render.RenderingStrategy;
import org.inurl.dsql.where.WhereModel;
import org.inurl.dsql.where.render.WhereRenderer;

public class DeleteRenderer {
    private final DeleteModel deleteModel;
    private final RenderingStrategy renderingStrategy;

    private DeleteRenderer(Builder builder) {
        deleteModel = Objects.requireNonNull(builder.deleteModel);
        renderingStrategy = Objects.requireNonNull(builder.renderingStrategy);
    }

    public DeleteStatementProvider render() {
        return deleteModel.whereModel()
                .flatMap(this::renderWhereClause)
                .map(this::renderWithWhereClause)
                .orElseGet(this::renderWithoutWhereClause);
    }

    private DeleteStatementProvider renderWithWhereClause(WhereClauseProvider whereClauseProvider) {
        return DefaultDeleteStatementProvider.withDeleteStatement(calculateDeleteStatement(whereClauseProvider))
                .withParameters(whereClauseProvider.getParameters())
                .build();
    }

    private String calculateDeleteStatement(WhereClauseProvider whereClause) {
        return calculateDeleteStatement()
                + spaceBefore(whereClause.getWhereClause());
    }

    private String calculateDeleteStatement() {
        return "delete from"
                + spaceBefore(deleteModel.table().tableNameAtRuntime());
    }

    private DeleteStatementProvider renderWithoutWhereClause() {
        return DefaultDeleteStatementProvider.withDeleteStatement(calculateDeleteStatement())
                .build();
    }

    private Optional<WhereClauseProvider> renderWhereClause(WhereModel whereModel) {
        return WhereRenderer.withWhereModel(whereModel)
                .withRenderingStrategy(renderingStrategy)
                .withSequence(new AtomicInteger(1))
                .withTableAliasCalculator(TableAliasCalculator.empty())
                .build()
                .render();
    }

    public static Builder withDeleteModel(DeleteModel deleteModel) {
        return new Builder().withDeleteModel(deleteModel);
    }

    public static class Builder {
        private DeleteModel deleteModel;
        private RenderingStrategy renderingStrategy;

        public Builder withDeleteModel(DeleteModel deleteModel) {
            this.deleteModel = deleteModel;
            return this;
        }

        public Builder withRenderingStrategy(RenderingStrategy renderingStrategy) {
            this.renderingStrategy = renderingStrategy;
            return this;
        }

        public DeleteRenderer build() {
            return new DeleteRenderer(this);
        }
    }
}
