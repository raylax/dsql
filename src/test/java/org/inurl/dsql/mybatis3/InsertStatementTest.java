package org.inurl.dsql.mybatis3;

import org.inurl.dsql.SqlBuilder;
import org.inurl.dsql.SqlColumn;
import org.inurl.dsql.SqlTable;
import org.inurl.dsql.insert.render.InsertStatementProvider;
import org.inurl.dsql.render.RenderingStrategies;
import org.junit.jupiter.api.Test;

import java.sql.JDBCType;

import static org.assertj.core.api.Assertions.assertThat;

class InsertStatementTest {
    private static final SqlTable foo = SqlTable.of("foo");
    private static final SqlColumn<Integer> id = foo.column("id", JDBCType.INTEGER);
    private static final SqlColumn<String> firstName = foo.column("first_name", JDBCType.VARCHAR);
    private static final SqlColumn<String> lastName = foo.column("last_name", JDBCType.VARCHAR);
    private static final SqlColumn<String> occupation = foo.column("occupation", JDBCType.VARCHAR);

    @Test
    void testFullInsertStatementBuilder() {
        TestRecord record = new TestRecord();
        record.setLastName("jones");
        record.setOccupation("dino driver");

        InsertStatementProvider<?> insertStatement = SqlBuilder.insert(record)
                .into(foo)
                .map(id).toProperty("id")
                .map(firstName).toProperty("firstName")
                .map(lastName).toProperty("lastName")
                .map(occupation).toProperty("occupation")
                .build()
                .render(RenderingStrategies.MYBATIS3);

        String expected = "insert into foo (id, first_name, last_name, occupation) "
                + "values (#{record.id,jdbcType=INTEGER}, "
                + "#{record.firstName,jdbcType=VARCHAR}, " + "#{record.lastName,jdbcType=VARCHAR}, "
                + "#{record.occupation,jdbcType=VARCHAR})";
        assertThat(insertStatement.getInsertStatement()).isEqualTo(expected);
    }

    @Test
    void testSelectiveInsertStatementBuilder() {
        TestRecord record = new TestRecord();
        record.setLastName("jones");
        record.setOccupation("dino driver");

        InsertStatementProvider<?> insertStatement = SqlBuilder.insert(record)
                .into(foo)
                .map(id).toPropertyWhenPresent("id", record::getId)
                .map(firstName).toPropertyWhenPresent("firstName", record::getFirstName)
                .map(lastName).toPropertyWhenPresent("lastName", record::getLastName)
                .map(occupation).toPropertyWhenPresent("occupation", record::getOccupation)
                .build()
                .render(RenderingStrategies.MYBATIS3);

        String expected = "insert into foo (last_name, occupation) "
                + "values (#{record.lastName,jdbcType=VARCHAR}, "
                + "#{record.occupation,jdbcType=VARCHAR})";
        assertThat(insertStatement.getInsertStatement()).isEqualTo(expected);
    }

    static class TestRecord {
        private Integer id;
        private String firstName;
        private String lastName;
        private String occupation;

        Integer getId() {
            return id;
        }

        void setId(Integer id) {
            this.id = id;
        }

        String getFirstName() {
            return firstName;
        }

        void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        String getLastName() {
            return lastName;
        }

        void setLastName(String lastName) {
            this.lastName = lastName;
        }

        String getOccupation() {
            return occupation;
        }

        void setOccupation(String occupation) {
            this.occupation = occupation;
        }
    }
}
