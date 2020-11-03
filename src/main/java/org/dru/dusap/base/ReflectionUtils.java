package org.dru.dusap.base;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class ReflectionUtils {
    public static List<Annotation> getAnnotationsAnnotatedWith(final AnnotatedElement element,
                                                               final Class<? extends Annotation> annotationType) {
        Objects.requireNonNull(element, "element");
        return Stream.of(element.getAnnotations())
                .filter((annotation) -> annotation.annotationType().isAnnotationPresent(annotationType))
                .collect(Collectors.toList());
    }

    public static <T> List<Class<? super T>> getHierarchy(final Class<T> type) {
        Objects.requireNonNull(type, "type");
        final List<Class<? super T>> result = new ArrayList<>();
        Class<? super T> current = type;
        while (current != null) {
            result.add(current);
            current = current.getSuperclass();
        }
        Collections.reverse(result);
        return result;
    }

    public static List<Field> getDeclaredFields(final Class<?> type, final Predicate<? super Field> filter) {
        Objects.requireNonNull(filter, "filter");
        return getHierarchy(type).stream()
                .flatMap((current) -> Stream.of(current.getDeclaredFields()))
                .filter(filter)
                .collect(Collectors.toList());
    }

    public static List<Method> getDeclaredMethods(final Class<?> type, final Predicate<? super Method> filter) {
        Objects.requireNonNull(filter, "filter");
        return getHierarchy(type).stream()
                .flatMap((current) -> Stream.of(current.getDeclaredMethods()))
                .filter(filter)
                .collect(Collectors.toList());
    }

    public static <T> T newInstance(final Constructor<T> constructor, final Object... initargs) {
        Objects.requireNonNull(constructor, "constructor");
        constructor.setAccessible(true);
        try {
            return constructor.newInstance(initargs);
        } catch (final InstantiationException | IllegalAccessException | InvocationTargetException exc) {
            throw new RuntimeException("failed to create new instance: " + constructor.toGenericString(), exc);
        }
    }

    public static void setFieldValue(final Object object, final Field field, final Object value) {
        Objects.requireNonNull(object, "object");
        Objects.requireNonNull(field, "field");
        field.setAccessible(true);
        try {
            field.set(object, value);
        } catch (final IllegalAccessException exc) {
            throw new RuntimeException("failed to set field: " + field.toGenericString(), exc);
        }
    }

    public static Object invokeMethod(final Object object, final Method method, final Object... args) {
        Objects.requireNonNull(object, "object");
        Objects.requireNonNull(method, "method");
        method.setAccessible(true);
        try {
            return method.invoke(object, args);
        } catch (final IllegalAccessException | InvocationTargetException exc) {
            throw new RuntimeException("failed to invoke method: " + method.toGenericString(), exc);
        }
    }

    private ReflectionUtils() {
    }
}
