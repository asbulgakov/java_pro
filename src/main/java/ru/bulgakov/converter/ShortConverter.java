package ru.bulgakov.converter;

import ru.bulgakov.exception.ConversionException;

public class ShortConverter implements TypeConverter<Short> {
    @Override
    public Short convert(String value) throws ConversionException {
        try {
            return Short.parseShort(value.trim());
        } catch (NumberFormatException e) {
            throw new ConversionException("Cannot convert '" + value + "' to Short", e);
        }
    }

    @Override
    public Class<Short> getTargetType() {
        return Short.class;
    }
}
