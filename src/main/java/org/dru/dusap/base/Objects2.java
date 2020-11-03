package org.dru.dusap.base;

import java.util.*;

public final class Objects2 {
    public static <T, C extends Collection<T>> C requireNonNull(final C collection, final String name) {
        Objects.requireNonNull(collection, name);
        int index = 0;
        for (final T value : collection) {
            Objects.requireNonNull(value, String.format("element in %s[%d]", name, index));
            index++;
        }
        return collection;
    }

    public static <T> T[] requireNonNull(final T[] array, final String name) {
        Objects.requireNonNull(array, name);
        for (int index = 0; index < array.length; index++) {
            Objects.requireNonNull(array[index], String.format("element in %s[%d]", name, index));
        }
        return array;
    }

    private Objects2() {
    }
}
