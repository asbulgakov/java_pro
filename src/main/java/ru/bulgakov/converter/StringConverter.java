package ru.bulgakov.converter;

public class StringConverter implements TypeConverter<String> {
    @Override
    public String convert(String value) {
        return value;
    }

    @Override
    public Class<String> getTargetType() {
        return String.class;
    }
}
