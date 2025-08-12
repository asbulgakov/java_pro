package ru.bulgakov.converter;

import ru.bulgakov.exception.ConversionException;

public class LongConverter implements TypeConverter<Long> {
    @Override
    public Long convert(String value) throws ConversionException {
        try {
            return Long.parseLong(value.trim());
        } catch (NumberFormatException e) {
            throw new ConversionException("Cannot convert '" + value + "' to Long", e);
        }
    }

    @Override
    public Class<Long> getTargetType() {
        return Long.class;
    }

}
