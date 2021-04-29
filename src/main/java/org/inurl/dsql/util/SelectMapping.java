package org.inurl.dsql.util;

import org.inurl.dsql.SqlColumn;
import org.inurl.dsql.select.SelectModel;

public class SelectMapping extends AbstractColumnMapping {

    private final SelectModel selectModel;

    private SelectMapping(SqlColumn<?> column, Buildable<SelectModel> selectModelBuilder) {
        super(column);
        selectModel = selectModelBuilder.build();
    }

    public SelectModel selectModel() {
        return selectModel;
    }

    @Override
    public <R> R accept(ColumnMappingVisitor<R> visitor) {
        return visitor.visit(this);
    }

    public static SelectMapping of(SqlColumn<?> column, Buildable<SelectModel> selectModelBuilder) {
        return new SelectMapping(column, selectModelBuilder);
    }
}
