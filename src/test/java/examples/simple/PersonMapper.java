package examples.simple;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.type.JdbcType;
import org.inurl.dsql.BasicColumn;
import org.inurl.dsql.delete.DeleteDSLCompleter;
import org.inurl.dsql.insert.GeneralInsertDSL;
import org.inurl.dsql.select.CountDSLCompleter;
import org.inurl.dsql.select.SelectDSLCompleter;
import org.inurl.dsql.select.render.SelectStatementProvider;
import org.inurl.dsql.update.UpdateDSL;
import org.inurl.dsql.update.UpdateDSLCompleter;
import org.inurl.dsql.update.UpdateModel;
import org.inurl.dsql.util.SqlProviderAdapter;
import org.inurl.dsql.util.mybatis3.CommonCountMapper;
import org.inurl.dsql.util.mybatis3.CommonDeleteMapper;
import org.inurl.dsql.util.mybatis3.CommonInsertMapper;
import org.inurl.dsql.util.mybatis3.CommonUpdateMapper;
import org.inurl.dsql.util.mybatis3.MyBatis3Utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;

import static examples.simple.PersonDynamicSqlSupport.addressId;
import static examples.simple.PersonDynamicSqlSupport.birthDate;
import static examples.simple.PersonDynamicSqlSupport.employed;
import static examples.simple.PersonDynamicSqlSupport.firstName;
import static examples.simple.PersonDynamicSqlSupport.id;
import static examples.simple.PersonDynamicSqlSupport.lastName;
import static examples.simple.PersonDynamicSqlSupport.occupation;
import static examples.simple.PersonDynamicSqlSupport.person;
import static org.inurl.dsql.SqlBuilder.isEqualTo;

/**
 *
 * Note: this is the canonical mapper with the new style methods
 * and represents the desired output for MyBatis Generator
 *
 */
@Mapper
public interface PersonMapper extends CommonCountMapper, CommonDeleteMapper, CommonInsertMapper<PersonRecord>, CommonUpdateMapper {

    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="PersonResult", value= {
            @Result(column="A_ID", property="id", jdbcType=JdbcType.INTEGER, id=true),
            @Result(column="first_name", property="firstName", jdbcType=JdbcType.VARCHAR),
            @Result(column="last_name", property="lastName", jdbcType=JdbcType.VARCHAR, typeHandler=LastNameTypeHandler.class),
            @Result(column="birth_date", property="birthDate", jdbcType=JdbcType.DATE),
            @Result(column="employed", property="employed", jdbcType=JdbcType.VARCHAR, typeHandler=YesNoTypeHandler.class),
            @Result(column="occupation", property="occupation", jdbcType=JdbcType.VARCHAR),
            @Result(column="address_id", property="addressId", jdbcType=JdbcType.INTEGER)
    })
    List<PersonRecord> selectMany(SelectStatementProvider selectStatement);

    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("PersonResult")
    Optional<PersonRecord> selectOne(SelectStatementProvider selectStatement);

    BasicColumn[] selectList =
            BasicColumn.columnList(id.as("A_ID"), firstName, lastName, birthDate, employed, occupation, addressId);

    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, person, completer);
    }

    default long count(BasicColumn column, CountDSLCompleter completer) {
        return MyBatis3Utils.count(this::count, column, person, completer);
    }

    default long countDistinct(BasicColumn column, CountDSLCompleter completer) {
        return MyBatis3Utils.countDistinct(this::count, column, person, completer);
    }

    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, person, completer);
    }

    default int deleteByPrimaryKey(Integer id_) {
        return delete(c ->
            c.where(id, isEqualTo(id_))
        );
    }

    default int generalInsert(UnaryOperator<GeneralInsertDSL> completer) {
        return MyBatis3Utils.generalInsert(this::generalInsert, person, completer);
    }

    default int insert(PersonRecord record) {
        return MyBatis3Utils.insert(this::insert, record, person, c ->
            c.map(id).toProperty("id")
            .map(firstName).toProperty("firstName")
            .map(lastName).toProperty("lastName")
            .map(birthDate).toProperty("birthDate")
            .map(employed).toProperty("employed")
            .map(occupation).toProperty("occupation")
            .map(addressId).toProperty("addressId")
        );
    }

    default int insertMultiple(PersonRecord...records) {
        return insertMultiple(Arrays.asList(records));
    }

    default int insertMultiple(Collection<PersonRecord> records) {
        return MyBatis3Utils.insertMultiple(this::insertMultiple, records, person, c ->
            c.map(id).toProperty("id")
            .map(firstName).toProperty("firstName")
            .map(lastName).toProperty("lastName")
            .map(birthDate).toProperty("birthDate")
            .map(employed).toProperty("employed")
            .map(occupation).toProperty("occupation")
            .map(addressId).toProperty("addressId")
        );
    }

    default int insertSelective(PersonRecord record) {
        return MyBatis3Utils.insert(this::insert, record, person, c ->
            c.map(id).toPropertyWhenPresent("id", record::getId)
            .map(firstName).toPropertyWhenPresent("firstName", record::getFirstName)
            .map(lastName).toPropertyWhenPresent("lastName", record::getLastName)
            .map(birthDate).toPropertyWhenPresent("birthDate", record::getBirthDate)
            .map(employed).toPropertyWhenPresent("employed", record::getEmployed)
            .map(occupation).toPropertyWhenPresent("occupation", record::getOccupation)
            .map(addressId).toPropertyWhenPresent("addressId", record::getAddressId)
        );
    }

    default Optional<PersonRecord> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, person, completer);
    }

    default List<PersonRecord> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, person, completer);
    }

    default List<PersonRecord> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, person, completer);
    }

    default Optional<PersonRecord> selectByPrimaryKey(Integer id_) {
        return selectOne(c ->
            c.where(id, isEqualTo(id_))
        );
    }

    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, person, completer);
    }

    static UpdateDSL<UpdateModel> updateAllColumns(PersonRecord record,
            UpdateDSL<UpdateModel> dsl) {
        return dsl.set(id).equalTo(record::getId)
                .set(firstName).equalTo(record::getFirstName)
                .set(lastName).equalTo(record::getLastName)
                .set(birthDate).equalTo(record::getBirthDate)
                .set(employed).equalTo(record::getEmployed)
                .set(occupation).equalTo(record::getOccupation)
                .set(addressId).equalTo(record::getAddressId);
    }

    static UpdateDSL<UpdateModel> updateSelectiveColumns(PersonRecord record,
            UpdateDSL<UpdateModel> dsl) {
        return dsl.set(id).equalToWhenPresent(record::getId)
                .set(firstName).equalToWhenPresent(record::getFirstName)
                .set(lastName).equalToWhenPresent(record::getLastName)
                .set(birthDate).equalToWhenPresent(record::getBirthDate)
                .set(employed).equalToWhenPresent(record::getEmployed)
                .set(occupation).equalToWhenPresent(record::getOccupation)
                .set(addressId).equalToWhenPresent(record::getAddressId);
    }

    default int updateByPrimaryKey(PersonRecord record) {
        return update(c ->
            c.set(firstName).equalTo(record::getFirstName)
            .set(lastName).equalTo(record::getLastName)
            .set(birthDate).equalTo(record::getBirthDate)
            .set(employed).equalTo(record::getEmployed)
            .set(occupation).equalTo(record::getOccupation)
            .set(addressId).equalTo(record::getAddressId)
            .where(id, isEqualTo(record::getId))
        );
    }

    default int updateByPrimaryKeySelective(PersonRecord record) {
        return update(c ->
            c.set(firstName).equalToWhenPresent(record::getFirstName)
            .set(lastName).equalToWhenPresent(record::getLastName)
            .set(birthDate).equalToWhenPresent(record::getBirthDate)
            .set(employed).equalToWhenPresent(record::getEmployed)
            .set(occupation).equalToWhenPresent(record::getOccupation)
            .set(addressId).equalToWhenPresent(record::getAddressId)
            .where(id, isEqualTo(record::getId))
        );
    }
}
