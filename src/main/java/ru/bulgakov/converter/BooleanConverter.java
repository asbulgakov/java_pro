package ru.bulgakov.converter;

import ru.bulgakov.exception.ConversionException;

public class BooleanConverter implements TypeConverter<Boolean> {
    @Override
    public Boolean convert(String value) throws ConversionException {
        String trimmed = value.trim().toLowerCase();
        if ("true".equals(trimmed) || "false".equals(trimmed)) {
            return Boolean.parseBoolean(trimmed);
        }
        throw new ConversionException("Cannot convert '" + value + "' to Boolean. Expected 'true' or 'false'");
    }

    @Override
    public Class<Boolean> getTargetType() {
        return Boolean.class;
    }
}
