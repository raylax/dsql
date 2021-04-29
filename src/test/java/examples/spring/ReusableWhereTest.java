/*
 *    Copyright 2016-2021 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package examples.spring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.inurl.dsql.delete.DeleteModel;
import org.inurl.dsql.render.RenderingStrategies;
import org.inurl.dsql.select.SelectModel;
import org.inurl.dsql.select.render.SelectStatementProvider;
import org.inurl.dsql.update.UpdateModel;
import org.inurl.dsql.util.Buildable;
import org.inurl.dsql.util.spring.JdbcTemplateAdapter;
import org.inurl.dsql.where.WhereApplier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.util.List;

import static examples.spring.PersonDynamicSqlSupport.addressId;
import static examples.spring.PersonDynamicSqlSupport.birthDate;
import static examples.spring.PersonDynamicSqlSupport.id;
import static examples.spring.PersonDynamicSqlSupport.occupation;
import static examples.spring.PersonDynamicSqlSupport.person;
import static org.assertj.core.api.Assertions.assertThat;
import static org.inurl.dsql.SqlBuilder.countFrom;
import static org.inurl.dsql.SqlBuilder.deleteFrom;
import static org.inurl.dsql.SqlBuilder.isEqualTo;
import static org.inurl.dsql.SqlBuilder.isLessThan;
import static org.inurl.dsql.SqlBuilder.isNotNull;
import static org.inurl.dsql.SqlBuilder.isNull;
import static org.inurl.dsql.SqlBuilder.select;
import static org.inurl.dsql.SqlBuilder.update;

class ReusableWhereTest {

    private JdbcTemplateAdapter template;

    @BeforeEach
    void setup() {
        EmbeddedDatabase db = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.HSQL)
                .generateUniqueName(true)
                .addScript("classpath:/examples/simple/CreateSimpleDB.sql")
                .build();
        template = new JdbcTemplateAdapter(new NamedParameterJdbcTemplate(db));
    }

    @Test
    void testCount() {
        Buildable<SelectModel> countStatement = countFrom(person)
                .applyWhere(commonWhere);

        long rows = template.count(countStatement);
        assertThat(rows).isEqualTo(3);
    }

    @Test
    void testDelete() {
        Buildable<DeleteModel> deleteStatement = deleteFrom(person)
                .applyWhere(commonWhere);

        long rows = template.delete(deleteStatement);

        assertThat(rows).isEqualTo(3);
    }

    @Test
    void testSelect() {
        Buildable<SelectModel> selectStatement = select(person.allColumns())
                .from(person)
                .applyWhere(commonWhere);

        List<PersonRecord> rows = template.selectList(selectStatement, PersonTemplateTest.personRowMapper);

        assertThat(rows).hasSize(3);
    }

    @Test
    void testUpdate() {
        Buildable<UpdateModel> updateStatement = update(person)
                .set(occupation).equalToStringConstant("worker")
                .applyWhere(commonWhere);

        int rows = template.update(updateStatement);

        assertThat(rows).isEqualTo(3);
    }

    @Test
    void testComposition() {
        WhereApplier whereApplier = commonWhere.andThen(wa -> wa.and(birthDate, isNotNull()));
        whereApplier = whereApplier.andThen(wa -> wa.or(addressId, isLessThan(3)));

        SelectStatementProvider selectStatement = select(person.allColumns())
                .from(person)
                .applyWhere(whereApplier)
                .build()
                .render(RenderingStrategies.SPRING_NAMED_PARAMETER);

        assertThat(selectStatement.getSelectStatement()).isEqualTo(
                "select * from Person " +
                    "where id = :p1 or occupation is null and birth_date is not null or address_id < :p2");

    }

    private final WhereApplier commonWhere =  d -> d.where(id, isEqualTo(1)).or(occupation, isNull());
}
