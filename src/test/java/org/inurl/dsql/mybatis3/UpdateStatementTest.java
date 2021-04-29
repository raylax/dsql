package org.inurl.dsql.mybatis3;

import org.inurl.dsql.SqlBuilder;
import org.inurl.dsql.SqlColumn;
import org.inurl.dsql.SqlTable;
import org.inurl.dsql.render.RenderingStrategies;
import org.inurl.dsql.update.render.UpdateStatementProvider;
import org.junit.jupiter.api.Test;

import java.sql.JDBCType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.inurl.dsql.SqlBuilder.isEqualTo;

class UpdateStatementTest {
    private static final SqlTable foo = SqlTable.of("foo");
    private static final SqlColumn<Integer> id = foo.column("id", JDBCType.INTEGER);
    private static final SqlColumn<String> firstName = foo.column("firstName", JDBCType.VARCHAR);
    private static final SqlColumn<String> lastName = foo.column("lastName", JDBCType.VARCHAR);
    private static final SqlColumn<String> occupation = foo.column("occupation", JDBCType.VARCHAR);

    @Test
    void testUpdateParameter() {
        UpdateStatementProvider updateStatement = SqlBuilder.update(foo)
                .set(firstName).equalTo("fred")
                .set(lastName).equalTo("jones")
                .set(occupation).equalToNull()
                .where(id, SqlBuilder.isEqualTo(3))
                .build()
                .render(RenderingStrategies.MYBATIS3);

        String expected = "update foo set firstName = #{parameters.p1,jdbcType=VARCHAR}, "
                + "lastName = #{parameters.p2,jdbcType=VARCHAR}, "
                + "occupation = null "
                + "where id = #{parameters.p3,jdbcType=INTEGER}";

        assertThat(updateStatement.getUpdateStatement()).isEqualTo(expected);
        assertThat(updateStatement.getParameters()).hasSize(3);
        assertThat(updateStatement.getParameters()).containsEntry("p1", "fred");
        assertThat(updateStatement.getParameters()).containsEntry("p2", "jones");
        assertThat(updateStatement.getParameters()).containsEntry("p3", 3);
    }

    @Test
    void testUpdateParameterStartWithNull() {
        UpdateStatementProvider updateStatement = SqlBuilder.update(foo)
                .set(occupation).equalToNull()
                .set(firstName).equalTo("fred")
                .set(lastName).equalTo("jones")
                .where(id, SqlBuilder.isEqualTo(3))
                .and(firstName, SqlBuilder.isEqualTo("barney"))
                .build()
                .render(RenderingStrategies.MYBATIS3);

        String expectedSetClause = "update foo set occupation = null, "
                + "firstName = #{parameters.p1,jdbcType=VARCHAR}, "
                + "lastName = #{parameters.p2,jdbcType=VARCHAR} "
                + "where id = #{parameters.p3,jdbcType=INTEGER} "
                + "and firstName = #{parameters.p4,jdbcType=VARCHAR}";

        assertThat(updateStatement.getUpdateStatement()).isEqualTo(expectedSetClause);
        assertThat(updateStatement.getParameters()).hasSize(4);
        assertThat(updateStatement.getParameters()).containsEntry("p1", "fred");
        assertThat(updateStatement.getParameters()).containsEntry("p2", "jones");
        assertThat(updateStatement.getParameters()).containsEntry("p3", 3);
        assertThat(updateStatement.getParameters()).containsEntry("p4", "barney");
    }
}
