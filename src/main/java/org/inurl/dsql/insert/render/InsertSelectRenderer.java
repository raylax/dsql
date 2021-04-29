package org.inurl.dsql.insert.render;

import static org.inurl.dsql.util.StringUtilities.spaceBefore;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.inurl.dsql.SqlColumn;
import org.inurl.dsql.insert.InsertColumnListModel;
import org.inurl.dsql.insert.InsertSelectModel;
import org.inurl.dsql.render.RenderingStrategy;
import org.inurl.dsql.select.render.SelectStatementProvider;
import org.inurl.dsql.util.StringUtilities;

public class InsertSelectRenderer {

    private final InsertSelectModel model;
    private final RenderingStrategy renderingStrategy;

    private InsertSelectRenderer(Builder builder) {
        model = Objects.requireNonNull(builder.model);
        renderingStrategy = Objects.requireNonNull(builder.renderingStrategy);
    }

    public InsertSelectStatementProvider render() {
        SelectStatementProvider selectStatement = model.selectModel().render(renderingStrategy);

        return DefaultGeneralInsertStatementProvider.withInsertStatement(calculateInsertStatement(selectStatement))
                .withParameters(selectStatement.getParameters())
                .build();
    }

    private String calculateInsertStatement(SelectStatementProvider selectStatement) {
        return "insert into"
                + StringUtilities.spaceBefore(model.table().tableNameAtRuntime())
                + StringUtilities.spaceBefore(calculateColumnsPhrase())
                + StringUtilities.spaceBefore(selectStatement.getSelectStatement());
    }

    private Optional<String> calculateColumnsPhrase() {
        return model.columnList()
                .map(this::calculateColumnsPhrase);
    }

    private String calculateColumnsPhrase(InsertColumnListModel columnList) {
        return columnList.mapColumns(SqlColumn::name)
                .collect(Collectors.joining(", ", "(", ")"));
    }

    public static Builder withInsertSelectModel(InsertSelectModel model) {
        return new Builder().withInsertSelectModel(model);
    }

    public static class Builder {
        private InsertSelectModel model;
        private RenderingStrategy renderingStrategy;

        public Builder withInsertSelectModel(InsertSelectModel model) {
            this.model = model;
            return this;
        }

        public Builder withRenderingStrategy(RenderingStrategy renderingStrategy) {
            this.renderingStrategy = renderingStrategy;
            return this;
        }

        public InsertSelectRenderer build() {
            return new InsertSelectRenderer(this);
        }
    }
}
