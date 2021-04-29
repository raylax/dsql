package org.inurl.dsql.util.mybatis3;

import org.apache.ibatis.annotations.InsertProvider;
import org.inurl.dsql.insert.render.InsertSelectStatementProvider;
import org.inurl.dsql.insert.render.MultiRowInsertStatementProvider;
import org.inurl.dsql.insert.render.GeneralInsertStatementProvider;
import org.inurl.dsql.insert.render.InsertStatementProvider;
import org.inurl.dsql.util.SqlProviderAdapter;

/**
 * This is a general purpose mapper for executing various types of insert statement.
 *
 * @param <T> the type of record associated with this mapper
 */
public interface CommonInsertMapper<T> {
    /**
     * Execute an insert statement with input fields mapped to values in a POJO.
     *
     * @param insertStatement the insert statement
     * @return the number of rows affected
     */
    @InsertProvider(type = SqlProviderAdapter.class, method = "insert")
    int insert(InsertStatementProvider<T> insertStatement);

    /**
     * Execute an insert statement with input fields supplied directly.
     *
     * @param insertStatement the insert statement
     * @return the number of rows affected
     */
    @InsertProvider(type = SqlProviderAdapter.class, method = "generalInsert")
    int generalInsert(GeneralInsertStatementProvider insertStatement);

    /**
     * Execute an insert statement with input fields supplied by a select statement.
     *
     * @param insertSelectStatement the insert statement
     * @return the number of rows affected
     */
    @InsertProvider(type = SqlProviderAdapter.class, method = "insertSelect")
    int insertSelect(InsertSelectStatementProvider insertSelectStatement);

    /**
     * Execute an insert statement that inserts multiple rows. The row values are supplied by mapping
     * to values in a List of POJOs.
     *
     * @param insertStatement the insert statement
     * @return the number of rows affected
     */
    @InsertProvider(type = SqlProviderAdapter.class, method = "insertMultiple")
    int insertMultiple(MultiRowInsertStatementProvider<T> insertStatement);
}
