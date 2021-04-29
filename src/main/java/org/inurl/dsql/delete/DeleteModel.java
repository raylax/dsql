package org.inurl.dsql.delete;

import java.util.Objects;
import java.util.Optional;

import org.inurl.dsql.SqlTable;
import org.inurl.dsql.delete.render.DeleteRenderer;
import org.inurl.dsql.delete.render.DeleteStatementProvider;
import org.jetbrains.annotations.NotNull;
import org.inurl.dsql.render.RenderingStrategy;
import org.inurl.dsql.where.WhereModel;

public class DeleteModel {
    private final SqlTable table;
    private final WhereModel whereModel;

    private DeleteModel(Builder builder) {
        table = Objects.requireNonNull(builder.table);
        whereModel = builder.whereModel;
    }

    public SqlTable table() {
        return table;
    }

    public Optional<WhereModel> whereModel() {
        return Optional.ofNullable(whereModel);
    }

    @NotNull
    public DeleteStatementProvider render(RenderingStrategy renderingStrategy) {
        return DeleteRenderer.withDeleteModel(this)
                .withRenderingStrategy(renderingStrategy)
                .build()
                .render();
    }

    public static Builder withTable(SqlTable table) {
        return new Builder().withTable(table);
    }

    public static class Builder {
        private SqlTable table;
        private WhereModel whereModel;

        public Builder withTable(SqlTable table) {
            this.table = table;
            return this;
        }

        public Builder withWhereModel(WhereModel whereModel) {
            this.whereModel = whereModel;
            return this;
        }

        public DeleteModel build() {
            return new DeleteModel(this);
        }
    }
}
