package org.inurl.dsql.insert.render;

import java.util.Map;

public interface InsertSelectStatementProvider {
    Map<String, Object> getParameters();

    String getInsertStatement();
}
