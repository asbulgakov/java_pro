package ru.bulgakov.converter;

import ru.bulgakov.exception.ConversionException;

public class IntegerConverter implements TypeConverter<Integer> {
    @Override
    public Integer convert(String value) throws ConversionException {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            throw new ConversionException("Cannot convert '" + value + "' to Integer", e);
        }
    }

    @Override
    public Class<Integer> getTargetType() {
        return Integer.class;
    }
}
