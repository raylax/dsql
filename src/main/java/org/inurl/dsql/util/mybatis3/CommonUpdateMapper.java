package org.inurl.dsql.util.mybatis3;

import org.apache.ibatis.annotations.UpdateProvider;
import org.inurl.dsql.update.render.UpdateStatementProvider;
import org.inurl.dsql.util.SqlProviderAdapter;

/**
 * This is a general purpose MyBatis mapper for update statements.
 *
 * <p>This mapper can be injected as-is into a MyBatis configuration, or it can be extended with existing mappers.
 *
 * @author Jeff Butler
 */
public interface CommonUpdateMapper {
    /**
     * Execute an update statement.
     *
     * @param updateStatement the update statement
     * @return the number of rows affected
     */
    @UpdateProvider(type = SqlProviderAdapter.class, method = "update")
    int update(UpdateStatementProvider updateStatement);
}
