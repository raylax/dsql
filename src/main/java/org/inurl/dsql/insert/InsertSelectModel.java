package org.inurl.dsql.insert;

import java.util.Objects;
import java.util.Optional;

import org.inurl.dsql.SqlTable;
import org.inurl.dsql.insert.render.InsertSelectStatementProvider;
import org.inurl.dsql.select.SelectModel;
import org.jetbrains.annotations.NotNull;
import org.inurl.dsql.insert.render.InsertSelectRenderer;
import org.inurl.dsql.render.RenderingStrategy;

public class InsertSelectModel {
    private final SqlTable table;
    private final InsertColumnListModel columnList;
    private final SelectModel selectModel;

    private InsertSelectModel(Builder builder) {
        table = Objects.requireNonNull(builder.table);
        columnList = builder.columnList;
        selectModel = Objects.requireNonNull(builder.selectModel);
    }

    public SqlTable table() {
        return table;
    }

    public SelectModel selectModel() {
        return selectModel;
    }

    public Optional<InsertColumnListModel> columnList() {
        return Optional.ofNullable(columnList);
    }

    @NotNull
    public InsertSelectStatementProvider render(RenderingStrategy renderingStrategy) {
        return InsertSelectRenderer.withInsertSelectModel(this)
                .withRenderingStrategy(renderingStrategy)
                .build()
                .render();
    }

    public static Builder withTable(SqlTable table) {
        return new Builder().withTable(table);
    }

    public static class Builder {
        private SqlTable table;
        private InsertColumnListModel columnList;
        private SelectModel selectModel;

        public Builder withTable(SqlTable table) {
            this.table = table;
            return this;
        }

        public Builder withColumnList(InsertColumnListModel columnList) {
            this.columnList = columnList;
            return this;
        }

        public Builder withSelectModel(SelectModel selectModel) {
            this.selectModel = selectModel;
            return this;
        }

        public InsertSelectModel build() {
            return new InsertSelectModel(this);
        }
    }
}
