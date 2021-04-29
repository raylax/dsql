package org.inurl.dsql.util.spring;

import org.inurl.dsql.delete.render.DeleteStatementProvider;
import org.inurl.dsql.insert.GeneralInsertModel;
import org.inurl.dsql.insert.InsertModel;
import org.inurl.dsql.insert.render.GeneralInsertStatementProvider;
import org.inurl.dsql.insert.render.MultiRowInsertStatementProvider;
import org.inurl.dsql.select.SelectModel;
import org.inurl.dsql.select.render.SelectStatementProvider;
import org.inurl.dsql.update.UpdateModel;
import org.inurl.dsql.update.render.UpdateStatementProvider;
import org.inurl.dsql.delete.DeleteModel;
import org.inurl.dsql.insert.BatchInsertModel;
import org.inurl.dsql.insert.MultiRowInsertModel;
import org.inurl.dsql.insert.render.BatchInsert;
import org.inurl.dsql.insert.render.InsertStatementProvider;
import org.inurl.dsql.render.RenderingStrategies;
import org.inurl.dsql.util.Buildable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.support.KeyHolder;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class JdbcTemplateAdapter {
    private final NamedParameterJdbcTemplate template;

    public JdbcTemplateAdapter(NamedParameterJdbcTemplate template) {
        this.template = Objects.requireNonNull(template);
    }

    public long count(Buildable<SelectModel> countStatement) {
        return count(countStatement.build().render(RenderingStrategies.SPRING_NAMED_PARAMETER));
    }

    public long count(SelectStatementProvider countStatement) {
        final Long result = template.queryForObject(countStatement.getSelectStatement(), countStatement.getParameters(),
                Long.class);
        return Optional.ofNullable(result).orElseThrow(NullPointerException::new);
    }

    public int delete(Buildable<DeleteModel> deleteStatement) {
        return delete(deleteStatement.build().render(RenderingStrategies.SPRING_NAMED_PARAMETER));
    }

    public int delete(DeleteStatementProvider deleteStatement) {
        return template.update(deleteStatement.getDeleteStatement(), deleteStatement.getParameters());
    }

    public int generalInsert(Buildable<GeneralInsertModel> insertStatement) {
        return generalInsert(insertStatement.build().render(RenderingStrategies.SPRING_NAMED_PARAMETER));
    }

    public int generalInsert(GeneralInsertStatementProvider insertStatement) {
        return template.update(insertStatement.getInsertStatement(), insertStatement.getParameters());
    }

    public int generalInsert(Buildable<GeneralInsertModel> insertStatement, KeyHolder keyHolder) {
        return generalInsert(insertStatement.build().render(RenderingStrategies.SPRING_NAMED_PARAMETER), keyHolder);
    }

    public int generalInsert(GeneralInsertStatementProvider insertStatement, KeyHolder keyHolder) {
        return template.update(insertStatement.getInsertStatement(),
                new MapSqlParameterSource(insertStatement.getParameters()), keyHolder);
    }

    public <T> int insert(Buildable<InsertModel<T>> insertStatement) {
        return insert(insertStatement.build().render(RenderingStrategies.SPRING_NAMED_PARAMETER));
    }

    public <T> int insert(InsertStatementProvider<T> insertStatement) {
        return template.update(insertStatement.getInsertStatement(),
                new BeanPropertySqlParameterSource(insertStatement.getRecord()));
    }

    public <T> int insert(Buildable<InsertModel<T>> insertStatement, KeyHolder keyHolder) {
        return insert(insertStatement.build().render(RenderingStrategies.SPRING_NAMED_PARAMETER), keyHolder);
    }

    public <T> int insert(InsertStatementProvider<T> insertStatement, KeyHolder keyHolder) {
        return template.update(insertStatement.getInsertStatement(),
                new BeanPropertySqlParameterSource(insertStatement.getRecord()), keyHolder);
    }

    public <T> int[] insertBatch(Buildable<BatchInsertModel<T>> insertStatement) {
        return insertBatch(insertStatement.build().render(RenderingStrategies.SPRING_NAMED_PARAMETER));
    }

    public <T> int[] insertBatch(BatchInsert<T> insertStatement) {
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(insertStatement.getRecords());
        return template.batchUpdate(insertStatement.getInsertStatementSQL(), batch);
    }

    public <T> int insertMultiple(Buildable<MultiRowInsertModel<T>> insertStatement) {
        return insertMultiple(insertStatement.build().render(RenderingStrategies.SPRING_NAMED_PARAMETER));
    }

    public <T> int insertMultiple(MultiRowInsertStatementProvider<T> insertStatement) {
        return template.update(insertStatement.getInsertStatement(),
                new BeanPropertySqlParameterSource(insertStatement));
    }

    public <T> int insertMultiple(Buildable<MultiRowInsertModel<T>> insertStatement, KeyHolder keyHolder) {
        return insertMultiple(insertStatement.build().render(RenderingStrategies.SPRING_NAMED_PARAMETER), keyHolder);
    }

    public <T> int insertMultiple(MultiRowInsertStatementProvider<T> insertStatement, KeyHolder keyHolder) {
        return template.update(insertStatement.getInsertStatement(),
                new BeanPropertySqlParameterSource(insertStatement), keyHolder);
    }

    public <T> List<T> selectList(Buildable<SelectModel> selectStatement, RowMapper<T> rowMapper) {
        return selectList(selectStatement.build().render(RenderingStrategies.SPRING_NAMED_PARAMETER), rowMapper);
    }

    public <T> List<T> selectList(SelectStatementProvider selectStatement, RowMapper<T> rowMapper) {
        return template.query(selectStatement.getSelectStatement(), selectStatement.getParameters(), rowMapper);
    }

    public <T> Optional<T> selectOne(Buildable<SelectModel> selectStatement, RowMapper<T> rowMapper) {
        return selectOne(selectStatement.build().render(RenderingStrategies.SPRING_NAMED_PARAMETER), rowMapper);
    }

    public <T> Optional<T> selectOne(SelectStatementProvider selectStatement, RowMapper<T> rowMapper) {
        T result;
        try {
            result = template.queryForObject(selectStatement.getSelectStatement(), selectStatement.getParameters(),
                            rowMapper);
        } catch (EmptyResultDataAccessException e) {
            result = null;
        }

        return Optional.ofNullable(result);
    }

    public int update(Buildable<UpdateModel> updateStatement) {
        return update(updateStatement.build().render(RenderingStrategies.SPRING_NAMED_PARAMETER));
    }

    public int update(UpdateStatementProvider updateStatement) {
        return template.update(updateStatement.getUpdateStatement(), updateStatement.getParameters());
    }
}
