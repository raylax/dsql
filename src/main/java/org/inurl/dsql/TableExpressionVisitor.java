package org.inurl.dsql;

import org.inurl.dsql.select.SubQuery;

public interface TableExpressionVisitor<R> {
    R visit(SqlTable table);

    R visit(SubQuery subQuery);
}
