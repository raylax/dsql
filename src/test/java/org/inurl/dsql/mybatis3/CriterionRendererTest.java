package org.inurl.dsql.mybatis3;

import org.assertj.core.api.Assertions;
import org.inurl.dsql.ColumnAndConditionCriterion;
import org.inurl.dsql.SqlBuilder;
import org.inurl.dsql.SqlColumn;
import org.inurl.dsql.SqlTable;
import org.inurl.dsql.render.RenderingStrategies;
import org.inurl.dsql.render.TableAliasCalculator;
import org.inurl.dsql.util.FragmentAndParameters;
import org.inurl.dsql.where.condition.IsEqualTo;
import org.inurl.dsql.where.render.CriterionRenderer;
import org.junit.jupiter.api.Test;

import java.sql.JDBCType;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

class CriterionRendererTest {
    @Test
    void testAliasWithIgnore() {
        SqlTable table = SqlTable.of("foo");
        SqlColumn<Integer> column = table.column("id", JDBCType.INTEGER);

        IsEqualTo<Integer> condition = SqlBuilder.isEqualTo(() -> 3);
        ColumnAndConditionCriterion<Integer> criterion = ColumnAndConditionCriterion.withColumn(column)
                .withCondition(condition)
                .build();
        AtomicInteger sequence = new AtomicInteger(1);

        CriterionRenderer renderer = new CriterionRenderer.Builder()
                .withSequence(sequence)
                .withRenderingStrategy(RenderingStrategies.MYBATIS3)
                .withTableAliasCalculator(TableAliasCalculator.empty())
                .build();

        Assertions.assertThat(criterion.accept(renderer)).hasValueSatisfying(rc -> {
            FragmentAndParameters fp = rc.fragmentAndParametersWithConnector();
            assertThat(fp.fragment()).isEqualTo("id = #{parameters.p1,jdbcType=INTEGER}");
            assertThat(fp.parameters()).hasSize(1);
        });
    }

    @Test
    void testAliasWithoutIgnore() {
        SqlTable table = SqlTable.of("foo");
        SqlColumn<Integer> column = table.column("id", JDBCType.INTEGER);
        IsEqualTo<Integer> condition = SqlBuilder.isEqualTo(() -> 3);
        ColumnAndConditionCriterion<Integer> criterion = ColumnAndConditionCriterion.withColumn(column)
                .withCondition(condition)
                .build();
        AtomicInteger sequence = new AtomicInteger(1);
        Map<SqlTable, String> tableAliases = new HashMap<>();
        tableAliases.put(table, "a");

        CriterionRenderer renderer = new CriterionRenderer.Builder()
                .withSequence(sequence)
                .withRenderingStrategy(RenderingStrategies.MYBATIS3)
                .withTableAliasCalculator(TableAliasCalculator.of(tableAliases))
                .build();

        Assertions.assertThat(criterion.accept(renderer)).hasValueSatisfying(rc -> {
            FragmentAndParameters fp = rc.fragmentAndParametersWithConnector();
            assertThat(fp.fragment()).isEqualTo("a.id = #{parameters.p1,jdbcType=INTEGER}");
            assertThat(fp.parameters()).hasSize(1);
        });
    }

    @Test
    void testTypeHandler() {
        SqlTable table = SqlTable.of("foo");
        SqlColumn<Date> column = new SqlColumn.Builder<Date>()
                .withName("id")
                .withTable(table)
                .withJdbcType(JDBCType.DATE)
                .withTypeHandler("foo.Bar")
                .build();
        IsEqualTo<Date> condition = SqlBuilder.isEqualTo(new Date());
        ColumnAndConditionCriterion<Date> criterion = ColumnAndConditionCriterion.withColumn(column)
                .withCondition(condition)
                .build();
        AtomicInteger sequence = new AtomicInteger(1);

        CriterionRenderer renderer = new CriterionRenderer.Builder()
                .withSequence(sequence)
                .withRenderingStrategy(RenderingStrategies.MYBATIS3)
                .withTableAliasCalculator(TableAliasCalculator.empty())
                .build();

        Assertions.assertThat(criterion.accept(renderer)).hasValueSatisfying(rc -> {
            FragmentAndParameters fp = rc.fragmentAndParametersWithConnector();
            assertThat(fp.fragment()).isEqualTo("id = #{parameters.p1,jdbcType=DATE,typeHandler=foo.Bar}");
            assertThat(fp.parameters()).hasSize(1);
        });
    }

    @Test
    void testTypeHandlerAndAlias() {
        SqlTable table = SqlTable.of("foo");
        SqlColumn<Integer> column = table.column("id", JDBCType.INTEGER, "foo.Bar");
        IsEqualTo<Integer> condition = SqlBuilder.isEqualTo(() -> 3);
        ColumnAndConditionCriterion<Integer> criterion = ColumnAndConditionCriterion.withColumn(column)
                .withCondition(condition)
                .build();
        AtomicInteger sequence = new AtomicInteger(1);
        Map<SqlTable, String> tableAliases = new HashMap<>();
        tableAliases.put(table, "a");

        CriterionRenderer renderer = new CriterionRenderer.Builder()
                .withSequence(sequence)
                .withRenderingStrategy(RenderingStrategies.MYBATIS3)
                .withTableAliasCalculator(TableAliasCalculator.of(tableAliases))
                .build();

        Assertions.assertThat(criterion.accept(renderer)).hasValueSatisfying(rc -> {
            FragmentAndParameters fp = rc.fragmentAndParametersWithConnector();
            assertThat(fp.fragment()).isEqualTo("a.id = #{parameters.p1,jdbcType=INTEGER,typeHandler=foo.Bar}");
            assertThat(fp.parameters()).hasSize(1);
        });
    }
}
