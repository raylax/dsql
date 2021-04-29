package org.inurl.dsql.insert.render;

import java.util.List;

public interface MultiRowInsertStatementProvider<T> {

    String getInsertStatement();

    List<T> getRecords();
}
