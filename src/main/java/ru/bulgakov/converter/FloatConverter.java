package ru.bulgakov.converter;

import ru.bulgakov.exception.ConversionException;

public class FloatConverter implements TypeConverter<Float> {
    @Override
    public Float convert(String value) throws ConversionException {
        try {
            return Float.parseFloat(value.trim());
        } catch (NumberFormatException e) {
            throw new ConversionException("Cannot convert '" + value + "' to Float", e);
        }
    }

    @Override
    public Class<Float> getTargetType() {
        return Float.class;
    }

}
