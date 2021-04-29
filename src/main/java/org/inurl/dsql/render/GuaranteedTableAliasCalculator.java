package org.inurl.dsql.render;

import java.util.Map;
import java.util.Optional;

import org.inurl.dsql.SqlTable;

/**
 * Returns the alias for a table if specified, or the table name itself.
 * This is useful for join rendering when we always want to have an alias for the table.
 *
 * @author Jeff Butler
 *
 */
public class GuaranteedTableAliasCalculator extends TableAliasCalculator {

    private GuaranteedTableAliasCalculator(Map<SqlTable, String> aliases) {
        super(aliases);
    }

    @Override
    public Optional<String> aliasForColumn(SqlTable table) {
        return super.aliasForColumn(table)
                .map(Optional::of)
                .orElseGet(() -> Optional.of(table.tableNameAtRuntime()));
    }

    public static TableAliasCalculator of(Map<SqlTable, String> aliases) {
        return new GuaranteedTableAliasCalculator(aliases);
    }
}
