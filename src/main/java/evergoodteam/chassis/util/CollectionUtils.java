package evergoodteam.chassis.util;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class CollectionUtils {

    /**
     * Gets the index of the specified value from the provided set
     */
    public static <T> int getIndex(Set<T> set, T value) {
        int result = 0;
        for (T entry : set) {
            if (entry.equals(value)) return result;
            result++;
        }
        return -1;
    }

    public static <K, V> Map<K, V> matchMapKeys(Map<K, V> left, Map<K, V> toMatch){
        return left.keySet().stream().filter(toMatch::containsKey)
                .collect(Collectors.toMap(key -> key, left::get, (a, b) -> b));
    }
}
