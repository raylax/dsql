package org.inurl.dsql.util.mybatis3;

import org.apache.ibatis.annotations.DeleteProvider;
import org.inurl.dsql.delete.render.DeleteStatementProvider;
import org.inurl.dsql.util.SqlProviderAdapter;

/**
 * This is a general purpose MyBatis mapper for delete statements.
 *
 * <p>This mapper can be injected as-is into a MyBatis configuration, or it can be extended with existing mappers.
 *
 * @author Jeff Butler
 */
public interface CommonDeleteMapper {
    /**
     * Execute a delete statement.
     *
     * @param deleteStatement the delete statement
     * @return the number of rows affected
     */
    @DeleteProvider(type = SqlProviderAdapter.class, method = "delete")
    int delete(DeleteStatementProvider deleteStatement);
}
