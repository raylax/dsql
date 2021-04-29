package org.inurl.dsql.insert;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.inurl.dsql.SqlTable;
import org.jetbrains.annotations.NotNull;
import org.inurl.dsql.SqlColumn;
import org.inurl.dsql.select.SelectModel;
import org.inurl.dsql.util.Buildable;

public class InsertSelectDSL implements Buildable<InsertSelectModel> {

    private final SqlTable table;
    private final InsertColumnListModel columnList;
    private final SelectModel selectModel;

    private InsertSelectDSL(SqlTable table, InsertColumnListModel columnList, SelectModel selectModel) {
        this.table = Objects.requireNonNull(table);
        this.selectModel = Objects.requireNonNull(selectModel);
        this.columnList = columnList;
    }

    private InsertSelectDSL(SqlTable table, SelectModel selectModel) {
        this.table = Objects.requireNonNull(table);
        this.selectModel = Objects.requireNonNull(selectModel);
        this.columnList = null;
    }

    @NotNull
    @Override
    public InsertSelectModel build() {
        return InsertSelectModel.withTable(table)
                .withColumnList(columnList)
                .withSelectModel(selectModel)
                .build();
    }

    public static InsertColumnGatherer insertInto(SqlTable table) {
        return new InsertColumnGatherer(table);
    }

    public static class InsertColumnGatherer {
        private final SqlTable table;

        private InsertColumnGatherer(SqlTable table) {
            this.table = table;
        }

        public SelectGatherer withColumnList(SqlColumn<?>...columns) {
            return withColumnList(Arrays.asList(columns));
        }

        public SelectGatherer withColumnList(List<SqlColumn<?>> columns) {
            return new SelectGatherer(table, columns);
        }

        public InsertSelectDSL withSelectStatement(Buildable<SelectModel> selectModelBuilder) {
            return new InsertSelectDSL(table, selectModelBuilder.build());
        }
    }

    public static class SelectGatherer {
        private final SqlTable table;
        private final InsertColumnListModel columnList;

        private SelectGatherer(SqlTable table, List<SqlColumn<?>> columns) {
            this.table = table;
            columnList = InsertColumnListModel.of(columns);
        }

        public InsertSelectDSL withSelectStatement(Buildable<SelectModel> selectModelBuilder) {
            return new InsertSelectDSL(table, columnList, selectModelBuilder.build());
        }
    }
}
