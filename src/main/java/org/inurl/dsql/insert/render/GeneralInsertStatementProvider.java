package org.inurl.dsql.insert.render;

import java.util.Map;

public interface GeneralInsertStatementProvider {
    Map<String, Object> getParameters();

    String getInsertStatement();
}
