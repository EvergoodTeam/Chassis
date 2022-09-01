package evergoodteam.chassis.util;

import java.util.Set;

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
}
