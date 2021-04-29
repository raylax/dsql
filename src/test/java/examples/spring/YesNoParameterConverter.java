package examples.spring;

import org.inurl.dsql.ParameterTypeConverter;

public class YesNoParameterConverter implements ParameterTypeConverter<Boolean, String> {

    @Override
    public String convert(Boolean source) {
        return source == null ? null : source ? "Yes" : "No";
    }
}
