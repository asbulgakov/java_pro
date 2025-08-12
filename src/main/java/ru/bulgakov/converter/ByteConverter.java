package ru.bulgakov.converter;

import ru.bulgakov.exception.ConversionException;

public class ByteConverter implements TypeConverter<Byte> {
    @Override
    public Byte convert(String value) throws ConversionException {
        try {
            return Byte.parseByte(value.trim());
        } catch (NumberFormatException e) {
            throw new ConversionException("Cannot convert '" + value + "' to Byte", e);
        }
    }

    @Override
    public Class<Byte> getTargetType() {
        return Byte.class;
    }
}

