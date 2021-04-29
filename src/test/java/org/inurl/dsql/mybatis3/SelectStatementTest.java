package org.inurl.dsql.mybatis3;

import org.inurl.dsql.SqlBuilder;
import org.inurl.dsql.SqlColumn;
import org.inurl.dsql.SqlTable;
import org.inurl.dsql.render.RenderingStrategies;
import org.inurl.dsql.select.render.SelectStatementProvider;
import org.junit.jupiter.api.Test;

import java.sql.JDBCType;
import java.util.Date;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.inurl.dsql.SqlBuilder.and;
import static org.inurl.dsql.SqlBuilder.isEqualTo;
import static org.inurl.dsql.SqlBuilder.isLessThan;
import static org.inurl.dsql.SqlBuilder.or;
import static org.inurl.dsql.SqlBuilder.select;

class SelectStatementTest {
    static final SqlTable table = SqlTable.of("foo");
    static final SqlColumn<Date> column1 = table.column("column1", JDBCType.DATE);
    static final SqlColumn<Integer> column2 = table.column("column2", JDBCType.INTEGER);

    @Test
    void testSimpleCriteriaWithoutAlias() {
        Date d = new Date();

        SelectStatementProvider selectStatement = SqlBuilder.select(column1, column2)
                .from(table, "a")
                .where(column1, SqlBuilder.isEqualTo(d))
                .or(column2, SqlBuilder.isEqualTo(4))
                .and(column2, SqlBuilder.isLessThan(3))
                .build()
                .render(RenderingStrategies.MYBATIS3);

        assertThat(selectStatement.getSelectStatement()).isEqualTo(
                "select a.column1, a.column2 from foo a where a.column1 = #{parameters.p1,jdbcType=DATE} or a.column2 = #{parameters.p2,jdbcType=INTEGER} and a.column2 < #{parameters.p3,jdbcType=INTEGER}");

        Map<String, Object> parameters = selectStatement.getParameters();

        assertAll(
                () -> assertThat(parameters).containsEntry("p1", d),
                () -> assertThat(parameters).containsEntry("p2", 4),
                () -> assertThat(parameters).containsEntry("p3", 3)
        );
    }

    @Test
    void testComplexCriteriaWithoutAlias() {
        Date d = new Date();

        SelectStatementProvider selectStatement = SqlBuilder.select(column1, column2)
                .from(table, "a")
                .where(column1, SqlBuilder.isEqualTo(d))
                .or(column2, SqlBuilder.isEqualTo(4))
                .and(column2, SqlBuilder.isLessThan(3))
                .or(column2, SqlBuilder.isEqualTo(4), SqlBuilder.and(column2, SqlBuilder.isEqualTo(6)))
                .and(column2, SqlBuilder.isLessThan(3), SqlBuilder.or(column1, SqlBuilder.isEqualTo(d)))
                .build()
                .render(RenderingStrategies.MYBATIS3);


        String expected = "select a.column1, a.column2 "
                + "from foo a "
                + "where a.column1 = #{parameters.p1,jdbcType=DATE}"
                + " or a.column2 = #{parameters.p2,jdbcType=INTEGER}"
                + " and a.column2 < #{parameters.p3,jdbcType=INTEGER}"
                + " or (a.column2 = #{parameters.p4,jdbcType=INTEGER} and a.column2 = #{parameters.p5,jdbcType=INTEGER})"
                + " and (a.column2 < #{parameters.p6,jdbcType=INTEGER} or a.column1 = #{parameters.p7,jdbcType=DATE})";

        Map<String, Object> parameters = selectStatement.getParameters();

        assertAll(
                () -> assertThat(selectStatement.getSelectStatement()).isEqualTo(expected),
                () -> assertThat(parameters).containsEntry("p1", d),
                () -> assertThat(parameters).containsEntry("p2", 4),
                () -> assertThat(parameters).containsEntry("p3", 3),
                () -> assertThat(parameters).containsEntry("p4", 4),
                () -> assertThat(parameters).containsEntry("p5", 6),
                () -> assertThat(parameters).containsEntry("p6", 3),
                () -> assertThat(parameters).containsEntry("p7", d)
        );
    }

    @Test
    void testSimpleCriteriaWithAlias() {
        Date d = new Date();

        SelectStatementProvider selectStatement = SqlBuilder.select(column1, column2)
                .from(table, "a")
                .where(column1, SqlBuilder.isEqualTo(d))
                .or(column2, SqlBuilder.isEqualTo(4))
                .and(column2, SqlBuilder.isLessThan(3))
                .build()
                .render(RenderingStrategies.MYBATIS3);

        Map<String, Object> parameters = selectStatement.getParameters();

        assertAll(
                () -> assertThat(selectStatement.getSelectStatement()).isEqualTo(
                    "select a.column1, a.column2 from foo a where a.column1 = #{parameters.p1,jdbcType=DATE} or a.column2 = #{parameters.p2,jdbcType=INTEGER} and a.column2 < #{parameters.p3,jdbcType=INTEGER}"),
                () -> assertThat(parameters).containsEntry("p1", d),
                () -> assertThat(parameters).containsEntry("p2", 4),
                () -> assertThat(parameters).containsEntry("p3", 3)
        );
    }

    @Test
    void testComplexCriteriaWithAlias() {
        Date d = new Date();

        SelectStatementProvider selectStatement = SqlBuilder.select(column1, column2)
                .from(table, "a")
                .where(column1, SqlBuilder.isEqualTo(d))
                .or(column2, SqlBuilder.isEqualTo(4))
                .and(column2, SqlBuilder.isLessThan(3))
                .or(column2, SqlBuilder.isEqualTo(4), SqlBuilder.and(column2, SqlBuilder.isEqualTo(6), SqlBuilder.or(column2, SqlBuilder.isEqualTo(7))))
                .and(column2, SqlBuilder.isLessThan(3), SqlBuilder.or(column1, SqlBuilder.isEqualTo(d), SqlBuilder.and(column2, SqlBuilder.isEqualTo(88))))
                .build()
                .render(RenderingStrategies.MYBATIS3);


        String expected = "select a.column1, a.column2 "
                + "from foo a "
                + "where a.column1 = #{parameters.p1,jdbcType=DATE}"
                + " or a.column2 = #{parameters.p2,jdbcType=INTEGER}"
                + " and a.column2 < #{parameters.p3,jdbcType=INTEGER}"
                + " or (a.column2 = #{parameters.p4,jdbcType=INTEGER} and (a.column2 = #{parameters.p5,jdbcType=INTEGER} or a.column2 = #{parameters.p6,jdbcType=INTEGER}))"
                + " and (a.column2 < #{parameters.p7,jdbcType=INTEGER} or (a.column1 = #{parameters.p8,jdbcType=DATE} and a.column2 = #{parameters.p9,jdbcType=INTEGER}))";

        Map<String, Object> parameters = selectStatement.getParameters();

        assertAll(
            () -> assertThat(selectStatement.getSelectStatement()).isEqualTo(expected),
            () -> assertThat(parameters).containsEntry("p1", d),
            () -> assertThat(parameters).containsEntry("p2", 4),
            () -> assertThat(parameters).containsEntry("p3", 3),
            () -> assertThat(parameters).containsEntry("p4", 4),
            () -> assertThat(parameters).containsEntry("p5", 6),
            () -> assertThat(parameters).containsEntry("p6", 7),
            () -> assertThat(parameters).containsEntry("p7", 3),
            () -> assertThat(parameters).containsEntry("p8", d),
            () -> assertThat(parameters).containsEntry("p9", 88)
        );
    }
}
