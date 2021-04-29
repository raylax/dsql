package examples.simple;

import org.inurl.dsql.SqlColumn;
import org.inurl.dsql.SqlTable;

import java.sql.JDBCType;
import java.util.Date;

public final class PersonDynamicSqlSupport {
    public static final Person person = new Person();
    public static final SqlColumn<Integer> id = person.id;
    public static final SqlColumn<String> firstName = person.firstName;
    public static final SqlColumn<LastName> lastName = person.lastName;
    public static final SqlColumn<Date> birthDate = person.birthDate;
    public static final SqlColumn<Boolean> employed = person.employed;
    public static final SqlColumn<String> occupation = person.occupation;
    public static final SqlColumn<Integer> addressId = person.addressId;

    public static final class Person extends SqlTable {
        public final SqlColumn<Integer> id = column("id", JDBCType.INTEGER);
        public final SqlColumn<String> firstName = column("first_name", JDBCType.VARCHAR);
        public final SqlColumn<LastName> lastName = column("last_name", JDBCType.VARCHAR, "examples.simple.LastNameTypeHandler");
        public final SqlColumn<Date> birthDate = column("birth_date", JDBCType.DATE);
        public final SqlColumn<Boolean> employed = column("employed", JDBCType.VARCHAR, "examples.simple.YesNoTypeHandler");
        public final SqlColumn<String> occupation = column("occupation", JDBCType.VARCHAR);
        public final SqlColumn<Integer> addressId = column("address_id", JDBCType.INTEGER);

        public Person() {
            super("Person");
        }
    }
}
