package evergoodteam.chassis.objects;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface EntryBase {

    default <T> T addToList(@NotNull List<T> list) {
        list.add((T) this);
        return (T) this;
    }
}
