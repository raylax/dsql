package examples.simple;

import org.inurl.dsql.SqlColumn;
import org.inurl.dsql.SqlTable;

import java.sql.JDBCType;

public final class AddressDynamicSqlSupport {
    public static final Address address = new Address();
    public static final SqlColumn<Integer> id = address.id;
    public static final SqlColumn<String> streetAddress = address.streetAddress;
    public static final SqlColumn<String> city = address.city;
    public static final SqlColumn<String> state = address.state;

    public static final class Address extends SqlTable {
        public final SqlColumn<Integer> id = column("address_id", JDBCType.INTEGER);
        public final SqlColumn<String> streetAddress = column("street_address", JDBCType.VARCHAR);
        public final SqlColumn<String> city = column("city", JDBCType.VARCHAR);
        public final SqlColumn<String> state = column("state", JDBCType.VARCHAR);

        public Address() {
            super("Address");
        }
    }
}
