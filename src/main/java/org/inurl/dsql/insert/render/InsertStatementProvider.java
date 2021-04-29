package org.inurl.dsql.insert.render;

public interface InsertStatementProvider<T> {
    T getRecord();

    String getInsertStatement();
}
