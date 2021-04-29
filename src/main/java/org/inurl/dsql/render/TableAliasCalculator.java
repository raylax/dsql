package org.inurl.dsql.render;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.inurl.dsql.SqlTable;

public class TableAliasCalculator {

    private final Map<SqlTable, String> aliases;

    protected TableAliasCalculator(Map<SqlTable, String> aliases) {
        this.aliases = Objects.requireNonNull(aliases);
    }

    public Optional<String> aliasForColumn(SqlTable table) {
        return Optional.ofNullable(aliases.get(table));
    }

    public Optional<String> aliasForTable(SqlTable table) {
        return Optional.ofNullable(aliases.get(table));
    }

    public static TableAliasCalculator of(SqlTable table, String alias) {
        Map<SqlTable, String> tableAliases = new HashMap<>();
        tableAliases.put(table, alias);
        return of(tableAliases);
    }

    public static TableAliasCalculator of(Map<SqlTable, String> aliases) {
        return new TableAliasCalculator(aliases);
    }

    public static TableAliasCalculator empty() {
        return of(Collections.emptyMap());
    }
}
