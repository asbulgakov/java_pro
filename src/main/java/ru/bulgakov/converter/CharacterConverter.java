package ru.bulgakov.converter;

import ru.bulgakov.exception.ConversionException;

public class CharacterConverter implements TypeConverter<Character> {
    @Override
    public Character convert(String value) throws ConversionException {
        if (value == null || value.isEmpty()) {
            return '\0';
        }
        if (value.length() > 1) {
            throw new ConversionException("Cannot convert '" + value + "' to Character. String must be exactly 1 character long");
        }
        return value.charAt(0);
    }

    @Override
    public Class<Character> getTargetType() {
        return Character.class;
    }
}
