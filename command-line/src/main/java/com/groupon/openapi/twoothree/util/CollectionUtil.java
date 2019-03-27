package com.groupon.openapi.twoothree.util;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

public class CollectionUtil {

    private CollectionUtil() {
        // util class no constructor
    }

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static <T> Collection<T> emptyIfNull(@Nullable Collection<T> list) {
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableCollection(list);
    }

    public static <T> List<T> emptyIfNull(@Nullable List<T> list) {
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(list);
    }

    public static <K, V> Map<K, V> emptyIfNull(@Nullable Map<K, V> map) {
        if (map == null || map.isEmpty()) {
            return Collections.emptyMap();
        }
        return Collections.unmodifiableMap(map);
    }

    public static boolean isNotEmpty(List<?> list) {
        return !isEmpty(list);
    }
}
