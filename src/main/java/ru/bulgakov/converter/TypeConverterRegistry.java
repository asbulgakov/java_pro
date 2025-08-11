package ru.bulgakov.converter;

import ru.bulgakov.exception.ConversionException;

import java.util.HashMap;
import java.util.Map;

public class TypeConverterRegistry {
    private static final Map<Class<?>, TypeConverter<?>> converters = new HashMap<>();

    static {
        register(new StringConverter());
        register(new IntegerConverter());
        register(new LongConverter());
        register(new DoubleConverter());
        register(new FloatConverter());
        register(new BooleanConverter());
        register(new ByteConverter());
        register(new ShortConverter());
        register(new CharacterConverter());
    }


    public static <T> void register(TypeConverter<T> converter) {
        converters.put(converter.getTargetType(), converter);

        Class<T> targetType = converter.getTargetType();
        if (targetType == Integer.class) {
            converters.put(int.class, converter);
        } else if (targetType == Long.class) {
            converters.put(long.class, converter);
        } else if (targetType == Double.class) {
            converters.put(double.class, converter);
        } else if (targetType == Float.class) {
            converters.put(float.class, converter);
        } else if (targetType == Boolean.class) {
            converters.put(boolean.class, converter);
        } else if (targetType == Byte.class) {
            converters.put(byte.class, converter);
        } else if (targetType == Short.class) {
            converters.put(short.class, converter);
        } else if (targetType == Character.class) {
            converters.put(char.class, converter);
        }
    }


    @SuppressWarnings("unchecked")
    public static <T> TypeConverter<T> getConverter(Class<T> targetType) {
        return (TypeConverter<T>) converters.get(targetType);
    }

    public static <T> T convert(String value, Class<T> targetType) throws ConversionException {
        TypeConverter<T> converter = getConverter(targetType);
        if (converter == null) {
            throw new ConversionException("No converter found for type: " + targetType.getName());
        }
        return converter.convert(value);
    }

}
