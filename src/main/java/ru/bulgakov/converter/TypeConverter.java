package ru.bulgakov.converter;

import ru.bulgakov.exception.ConversionException;

public interface TypeConverter<T> {
    T convert(String value) throws ConversionException;
    Class<T> getTargetType();
}
