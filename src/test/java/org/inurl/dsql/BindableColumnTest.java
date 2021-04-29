package org.inurl.dsql;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BindableColumnTest {

    @Test
    void testDefaultFunctions() {
        StringConstant constant = StringConstant.of("Fred");

        assertThat(constant.jdbcType()).isEmpty();
        assertThat(constant.typeHandler()).isEmpty();
    }
}
