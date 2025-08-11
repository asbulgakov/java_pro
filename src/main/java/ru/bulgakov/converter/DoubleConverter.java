package ru.bulgakov.converter;

import ru.bulgakov.exception.ConversionException;

public class DoubleConverter implements TypeConverter<Double> {
    @Override
    public Double convert(String value) throws ConversionException {
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            throw new ConversionException("Cannot convert '" + value + "' to Double", e);
        }
    }

    @Override
    public Class<Double> getTargetType() {
        return Double.class;
    }

}
