package org.inurl.dsql.select.render;

import java.util.Map;

public interface SelectStatementProvider {
    Map<String, Object> getParameters();

    String getSelectStatement();
}
