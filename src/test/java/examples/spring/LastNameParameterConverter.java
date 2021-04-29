package examples.spring;

import org.inurl.dsql.ParameterTypeConverter;
import org.springframework.core.convert.converter.Converter;

public class LastNameParameterConverter implements ParameterTypeConverter<LastName, String>, Converter<LastName, String> {
    @Override
    public String convert(LastName source) {
        return source == null ? null : source.getName();
    }
}
