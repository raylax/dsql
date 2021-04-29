package org.inurl.dsql.util;

import org.inurl.dsql.delete.render.DeleteStatementProvider;
import org.inurl.dsql.insert.render.InsertSelectStatementProvider;
import org.inurl.dsql.insert.render.MultiRowInsertStatementProvider;
import org.inurl.dsql.select.render.SelectStatementProvider;
import org.inurl.dsql.update.render.UpdateStatementProvider;
import org.inurl.dsql.insert.render.GeneralInsertStatementProvider;
import org.inurl.dsql.insert.render.InsertStatementProvider;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Adapter for use with MyBatis SQL provider annotations.
 *
 * @author Jeff Butler
 *
 */
public class SqlProviderAdapter {

    public String delete(DeleteStatementProvider deleteStatement) {
        return deleteStatement.getDeleteStatement();
    }

    public String generalInsert(GeneralInsertStatementProvider insertStatement) {
        return insertStatement.getInsertStatement();
    }

    public String insert(InsertStatementProvider<?> insertStatement) {
        return insertStatement.getInsertStatement();
    }

    public String insertMultiple(MultiRowInsertStatementProvider<?> insertStatement) {
        return insertStatement.getInsertStatement();
    }

    /**
     * This adapter method is intended for use with MyBatis' @InsertProvider annotation when there are generated
     * values expected from executing the insert statement.
     *
     * @param parameterMap The parameter map is automatically created by MyBatis when there are multiple
     *     parameters in the insert method.
     * @return the SQL statement contained in the parameter map. This is assumed to be the one
     *     and only map entry of type String.
     */
    public String insertMultipleWithGeneratedKeys(Map<String, Object> parameterMap) {
        List<String> entries = parameterMap.entrySet().stream()
                .filter(e -> e.getKey().startsWith("param"))
                .filter(e -> String.class.isAssignableFrom(e.getValue().getClass()))
                .map(e -> (String) e.getValue())
                .collect(Collectors.toList());

        if (entries.size() == 1) {
            return entries.get(0);
        } else {
            throw new IllegalArgumentException("The parameters for insertMultipleWithGeneratedKeys" +
                    " must contain exactly one parameter of type String");
        }
    }

    public String insertSelect(InsertSelectStatementProvider insertStatement) {
        return insertStatement.getInsertStatement();
    }

    public String select(SelectStatementProvider selectStatement) {
        return selectStatement.getSelectStatement();
    }

    public String update(UpdateStatementProvider updateStatement) {
        return updateStatement.getUpdateStatement();
    }
}
