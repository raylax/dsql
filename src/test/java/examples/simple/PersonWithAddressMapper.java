package examples.simple;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.type.JdbcType;
import org.inurl.dsql.BasicColumn;
import org.inurl.dsql.SqlBuilder;
import org.inurl.dsql.select.CountDSL;
import org.inurl.dsql.select.CountDSLCompleter;
import org.inurl.dsql.select.QueryExpressionDSL;
import org.inurl.dsql.select.SelectDSLCompleter;
import org.inurl.dsql.select.SelectModel;
import org.inurl.dsql.select.render.SelectStatementProvider;
import org.inurl.dsql.util.SqlProviderAdapter;
import org.inurl.dsql.util.mybatis3.MyBatis3Utils;

import java.util.List;
import java.util.Optional;

import static examples.simple.AddressDynamicSqlSupport.address;
import static examples.simple.PersonDynamicSqlSupport.birthDate;
import static examples.simple.PersonDynamicSqlSupport.employed;
import static examples.simple.PersonDynamicSqlSupport.firstName;
import static examples.simple.PersonDynamicSqlSupport.id;
import static examples.simple.PersonDynamicSqlSupport.lastName;
import static examples.simple.PersonDynamicSqlSupport.occupation;
import static examples.simple.PersonDynamicSqlSupport.person;
import static org.inurl.dsql.SqlBuilder.countFrom;
import static org.inurl.dsql.SqlBuilder.equalTo;
import static org.inurl.dsql.SqlBuilder.isEqualTo;
import static org.inurl.dsql.SqlBuilder.on;

/**
 *
 * This is a mapper that shows coding a join
 *
 */
@Mapper
public interface PersonWithAddressMapper {

    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="PersonWithAddressResult", value= {
            @Result(column="A_ID", property="id", jdbcType=JdbcType.INTEGER, id=true),
            @Result(column="first_name", property="firstName", jdbcType=JdbcType.VARCHAR),
            @Result(column="last_name", property="lastName", jdbcType=JdbcType.VARCHAR, typeHandler=LastNameTypeHandler.class),
            @Result(column="birth_date", property="birthDate", jdbcType=JdbcType.DATE),
            @Result(column="employed", property="employed", jdbcType=JdbcType.VARCHAR, typeHandler=YesNoTypeHandler.class),
            @Result(column="occupation", property="occupation", jdbcType=JdbcType.VARCHAR),
            @Result(column="address_id", property="address.id", jdbcType=JdbcType.INTEGER),
            @Result(column="street_address", property="address.streetAddress", jdbcType=JdbcType.VARCHAR),
            @Result(column="city", property="address.city", jdbcType=JdbcType.VARCHAR),
            @Result(column="state", property="address.state", jdbcType=JdbcType.CHAR)
    })
    List<PersonWithAddress> selectMany(SelectStatementProvider selectStatement);

    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("PersonWithAddressResult")
    Optional<PersonWithAddress> selectOne(SelectStatementProvider selectStatement);

    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    long count(SelectStatementProvider selectStatement);

    BasicColumn[] selectList =
            BasicColumn.columnList(id.as("A_ID"), firstName, lastName, birthDate, employed, occupation, address.id,
                    address.streetAddress, address.city, address.state);

    default Optional<PersonWithAddress> selectOne(SelectDSLCompleter completer) {
        QueryExpressionDSL<SelectModel> start = SqlBuilder.select(selectList).from(person)
                .join(address, on(person.addressId, equalTo(address.id)));
        return MyBatis3Utils.selectOne(this::selectOne, start, completer);
    }

    default List<PersonWithAddress> select(SelectDSLCompleter completer) {
        QueryExpressionDSL<SelectModel> start = SqlBuilder.select(selectList).from(person)
                .join(address, on(person.addressId, equalTo(address.id)));
        return MyBatis3Utils.selectList(this::selectMany, start, completer);
    }

    default Optional<PersonWithAddress> selectByPrimaryKey(Integer id_) {
        return selectOne(c ->
            c.where(id, isEqualTo(id_))
        );
    }

    default long count(CountDSLCompleter completer) {
        CountDSL<SelectModel> start = countFrom(person)
                .join(address, on(person.addressId, equalTo(address.id)));
        return MyBatis3Utils.countFrom(this::count, start, completer);
    }
}
