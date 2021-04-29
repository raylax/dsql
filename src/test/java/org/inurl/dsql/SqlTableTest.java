package org.inurl.dsql;

import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author raylax
 */
class SqlTableTest {
    private static final String NAME_PROPERTY = "nameProperty";

    @Test
    void testFullName() {
        SqlTable table = new SqlTable("my_table");
        assertThat(table.tableNameAtRuntime()).isEqualTo("my_table");
    }

    @Test
    void testFullNameSupplier() {

        System.setProperty(NAME_PROPERTY, "my_table");
        SqlTable table = new SqlTable(SqlTableTest::namePropertyReader);
        assertThat(table.tableNameAtRuntime()).isEqualTo("my_table");
        System.clearProperty(NAME_PROPERTY);
    }

    @Test
    void testSchemaSupplierEmpty() {
        SqlTable table = new SqlTable(Optional::empty, "my_table");
        assertThat(table.tableNameAtRuntime()).isEqualTo("my_table");
    }

    @Test
    void testSchemaSupplierWithValue() {
        SqlTable table = new SqlTable(() -> Optional.of("my_schema"), "my_table");
        assertThat(table.tableNameAtRuntime()).isEqualTo("my_schema.my_table");
    }

    @Test
    void testSingletonSchemaSupplier() {
        SqlTable table = new SqlTable(MySchemaSupplier.instance(), "my_table");
        assertThat(table.tableNameAtRuntime()).isEqualTo("first_schema.my_table");
    }

    @Test
    void testThatSchemaSupplierDoesDelay() {
        MySchemaSupplier schemaSupplier = new MySchemaSupplier();
        SqlTable table = new SqlTable(schemaSupplier, "my_table");
        assertThat(table.tableNameAtRuntime()).isEqualTo("first_schema.my_table");

        schemaSupplier.setFirst(false);
        assertThat(table.tableNameAtRuntime()).isEqualTo("second_schema.my_table");
    }

    @Test
    void testCatalogAndSchemaSupplierEmpty() {
        SqlTable table = new SqlTable(Optional::empty, Optional::empty, "my_table");
        assertThat(table.tableNameAtRuntime()).isEqualTo("my_table");
    }

    @Test
    void testCatalogSupplierWithValue() {
        SqlTable table = new SqlTable(() -> Optional.of("my_catalog"), Optional::empty, "my_table");
        assertThat(table.tableNameAtRuntime()).isEqualTo("my_catalog..my_table");
    }

    @Test
    void testThatCatalogSupplierDoesDelay() {
        MyCatalogSupplier catalogSupplier = new MyCatalogSupplier();
        SqlTable table = new SqlTable(catalogSupplier, Optional::empty, "my_table");
        assertThat(table.tableNameAtRuntime()).isEqualTo("first_catalog..my_table");

        catalogSupplier.setFirst(false);
        assertThat(table.tableNameAtRuntime()).isEqualTo("second_catalog..my_table");
    }

    @Test
    void testThatCatalogSupplierAndSchemaSupplierBothDelay() {
        MyCatalogSupplier catalogSupplier = new MyCatalogSupplier();
        MySchemaSupplier schemaSupplier = new MySchemaSupplier();
        SqlTable table = new SqlTable(catalogSupplier, schemaSupplier, "my_table");
        assertThat(table.tableNameAtRuntime()).isEqualTo("first_catalog.first_schema.my_table");

        catalogSupplier.setFirst(false);
        assertThat(table.tableNameAtRuntime()).isEqualTo("second_catalog.first_schema.my_table");

        catalogSupplier.setFirst(true);
        schemaSupplier.setFirst(false);
        assertThat(table.tableNameAtRuntime()).isEqualTo("first_catalog.second_schema.my_table");

        catalogSupplier.setFirst(false);
        assertThat(table.tableNameAtRuntime()).isEqualTo("second_catalog.second_schema.my_table");

        catalogSupplier.setEmpty(true);
        assertThat(table.tableNameAtRuntime()).isEqualTo("second_schema.my_table");

        schemaSupplier.setEmpty(true);
        assertThat(table.tableNameAtRuntime()).isEqualTo("my_table");

        catalogSupplier.setEmpty(false);
        assertThat(table.tableNameAtRuntime()).isEqualTo("second_catalog..my_table");
    }

    private static String namePropertyReader() {
        return System.getProperty(NAME_PROPERTY);
    }

    static class MySchemaSupplier implements Supplier<Optional<String>> {
        private static MySchemaSupplier instance = new MySchemaSupplier();

        static MySchemaSupplier instance() {
            return instance;
        }

        private boolean first = true;
        private boolean empty;

        void setFirst(boolean first) {
            this.first = first;
        }

        void setEmpty(boolean empty) {
            this.empty = empty;
        }

        @Override
        public Optional<String> get() {
            if (empty) {
                return Optional.empty();
            }

            if (first) {
                return Optional.of("first_schema");
            } else {
                return Optional.of("second_schema");
            }
        }
    }

    static class MyCatalogSupplier implements Supplier<Optional<String>> {
        private boolean first = true;
        private boolean empty;

        void setFirst(boolean first) {
            this.first = first;
        }

        void setEmpty(boolean empty) {
            this.empty = empty;
        }

        @Override
        public Optional<String> get() {
            if (empty) {
                return Optional.empty();
            }

            if (first) {
                return Optional.of("first_catalog");
            } else {
                return Optional.of("second_catalog");
            }
        }
    }
}