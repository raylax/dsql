package org.inurl.dsql.util.mybatis3;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.ToIntBiFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.function.UnaryOperator;

import org.inurl.dsql.SqlTable;
import org.inurl.dsql.delete.DeleteDSLCompleter;
import org.inurl.dsql.delete.render.DeleteStatementProvider;
import org.inurl.dsql.insert.InsertDSL;
import org.inurl.dsql.insert.MultiRowInsertDSL;
import org.inurl.dsql.insert.render.GeneralInsertStatementProvider;
import org.inurl.dsql.insert.render.MultiRowInsertStatementProvider;
import org.inurl.dsql.select.CountDSLCompleter;
import org.inurl.dsql.select.QueryExpressionDSL;
import org.inurl.dsql.select.SelectDSLCompleter;
import org.inurl.dsql.select.SelectModel;
import org.inurl.dsql.select.render.SelectStatementProvider;
import org.inurl.dsql.update.render.UpdateStatementProvider;
import org.inurl.dsql.BasicColumn;
import org.inurl.dsql.SqlBuilder;
import org.inurl.dsql.insert.GeneralInsertDSL;
import org.inurl.dsql.insert.render.InsertStatementProvider;
import org.inurl.dsql.render.RenderingStrategies;
import org.inurl.dsql.select.CountDSL;
import org.inurl.dsql.update.UpdateDSLCompleter;

/**
 * Utility functions for building MyBatis3 mappers.
 *
 * @author Jeff Butler
 *
 */
public class MyBatis3Utils {
    private MyBatis3Utils() {}

    public static long count(ToLongFunction<SelectStatementProvider> mapper, BasicColumn column, SqlTable table,
                             CountDSLCompleter completer) {
        return mapper.applyAsLong(count(column, table, completer));
    }

    public static SelectStatementProvider count(BasicColumn column, SqlTable table, CountDSLCompleter completer) {
        return countFrom(SqlBuilder.countColumn(column).from(table), completer);
    }

    public static long countDistinct(ToLongFunction<SelectStatementProvider> mapper, BasicColumn column, SqlTable table,
            CountDSLCompleter completer) {
        return mapper.applyAsLong(countDistinct(column, table, completer));
    }

    public static SelectStatementProvider countDistinct(BasicColumn column, SqlTable table,
            CountDSLCompleter completer) {
        return countFrom(SqlBuilder.countDistinctColumn(column).from(table), completer);
    }

    public static SelectStatementProvider countFrom(SqlTable table, CountDSLCompleter completer) {
        return countFrom(SqlBuilder.countFrom(table), completer);
    }

    public static long countFrom(ToLongFunction<SelectStatementProvider> mapper,
            SqlTable table, CountDSLCompleter completer) {
        return mapper.applyAsLong(countFrom(table, completer));
    }

    public static SelectStatementProvider countFrom(CountDSL<SelectModel> start, CountDSLCompleter completer) {
        return completer.apply(start)
                .build()
                .render(RenderingStrategies.MYBATIS3);
    }

    public static long countFrom(ToLongFunction<SelectStatementProvider> mapper,
            CountDSL<SelectModel> start, CountDSLCompleter completer) {
        return mapper.applyAsLong(countFrom(start, completer));
    }

    public static DeleteStatementProvider deleteFrom(SqlTable table, DeleteDSLCompleter completer) {
        return completer.apply(SqlBuilder.deleteFrom(table))
                .build()
                .render(RenderingStrategies.MYBATIS3);
    }

    public static int deleteFrom(ToIntFunction<DeleteStatementProvider> mapper,
            SqlTable table, DeleteDSLCompleter completer) {
        return mapper.applyAsInt(deleteFrom(table, completer));
    }

    public static <R> InsertStatementProvider<R> insert(R record, SqlTable table,
            UnaryOperator<InsertDSL<R>> completer) {
        return completer.apply(SqlBuilder.insert(record).into(table))
                .build()
                .render(RenderingStrategies.MYBATIS3);
    }

    public static <R> int insert(ToIntFunction<InsertStatementProvider<R>> mapper, R record,
            SqlTable table, UnaryOperator<InsertDSL<R>> completer) {
        return mapper.applyAsInt(insert(record, table, completer));
    }

    public static GeneralInsertStatementProvider generalInsert(SqlTable table,
                                                               UnaryOperator<GeneralInsertDSL> completer) {
        return completer.apply(GeneralInsertDSL.insertInto(table))
                .build()
                .render(RenderingStrategies.MYBATIS3);
    }

    public static int generalInsert(ToIntFunction<GeneralInsertStatementProvider> mapper,
            SqlTable table, UnaryOperator<GeneralInsertDSL> completer) {
        return mapper.applyAsInt(generalInsert(table, completer));
    }

    public static <R> MultiRowInsertStatementProvider<R> insertMultiple(Collection<R> records, SqlTable table,
                                                                        UnaryOperator<MultiRowInsertDSL<R>> completer) {
        return completer.apply(SqlBuilder.insertMultiple(records).into(table))
                .build()
                .render(RenderingStrategies.MYBATIS3);
    }

    public static <R> int insertMultiple(ToIntFunction<MultiRowInsertStatementProvider<R>> mapper,
            Collection<R> records, SqlTable table, UnaryOperator<MultiRowInsertDSL<R>> completer) {
        return mapper.applyAsInt(insertMultiple(records, table, completer));
    }

    public static <R> int insertMultipleWithGeneratedKeys(ToIntBiFunction<String, List<R>> mapper,
            Collection<R> records, SqlTable table, UnaryOperator<MultiRowInsertDSL<R>> completer) {
        MultiRowInsertStatementProvider<R> provider = insertMultiple(records, table, completer);
        return mapper.applyAsInt(provider.getInsertStatement(), provider.getRecords());
    }

    public static SelectStatementProvider select(BasicColumn[] selectList, SqlTable table,
            SelectDSLCompleter completer) {
        return select(SqlBuilder.select(selectList).from(table), completer);
    }

    public static SelectStatementProvider select(QueryExpressionDSL<SelectModel> start,
                                                 SelectDSLCompleter completer) {
        return completer.apply(start)
                .build()
                .render(RenderingStrategies.MYBATIS3);
    }

    public static SelectStatementProvider selectDistinct(BasicColumn[] selectList, SqlTable table,
            SelectDSLCompleter completer) {
        return select(SqlBuilder.selectDistinct(selectList).from(table), completer);
    }

    public static <R> List<R> selectDistinct(Function<SelectStatementProvider, List<R>> mapper,
            BasicColumn[] selectList, SqlTable table, SelectDSLCompleter completer) {
        return mapper.apply(selectDistinct(selectList, table, completer));
    }

    public static <R> List<R> selectList(Function<SelectStatementProvider, List<R>> mapper,
            BasicColumn[] selectList, SqlTable table, SelectDSLCompleter completer) {
        return mapper.apply(select(selectList, table, completer));
    }

    public static <R> List<R> selectList(Function<SelectStatementProvider, List<R>> mapper,
            QueryExpressionDSL<SelectModel> start, SelectDSLCompleter completer) {
        return mapper.apply(select(start, completer));
    }

    public static <R> R selectOne(Function<SelectStatementProvider, R> mapper,
            BasicColumn[] selectList, SqlTable table, SelectDSLCompleter completer) {
        return mapper.apply(select(selectList, table, completer));
    }

    public static <R> R selectOne(Function<SelectStatementProvider, R> mapper,
            QueryExpressionDSL<SelectModel> start, SelectDSLCompleter completer) {
        return mapper.apply(select(start, completer));
    }

    public static UpdateStatementProvider update(SqlTable table, UpdateDSLCompleter completer) {
        return completer.apply(SqlBuilder.update(table))
                .build()
                .render(RenderingStrategies.MYBATIS3);
    }

    public static int update(ToIntFunction<UpdateStatementProvider> mapper,
            SqlTable table, UpdateDSLCompleter completer) {
        return mapper.applyAsInt(update(table, completer));
    }
}
