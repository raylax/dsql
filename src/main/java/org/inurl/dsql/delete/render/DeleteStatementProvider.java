package org.inurl.dsql.delete.render;

import java.util.Map;

public interface DeleteStatementProvider {
    Map<String, Object> getParameters();

    String getDeleteStatement();
}
