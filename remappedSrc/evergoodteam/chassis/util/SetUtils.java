package evergoodteam.chassis.util;

import java.util.Set;

public class SetUtils {


    /**
     * Get index of provided value from provided set
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
