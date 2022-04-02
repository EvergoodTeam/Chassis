package evergoodteam.chassis.util.handlers;

import java.util.Arrays;

import static evergoodteam.chassis.util.Reference.COLUMNS;
import static evergoodteam.chassis.util.Reference.MODIDS;

public class ListHandler {

    public static void addColumnType(String type){

        COLUMNS.addAll(Arrays.asList(type));
    }

    public static void addColumnType(String[] types){

        COLUMNS.addAll(Arrays.stream(types).toList());
    }

    public static void addModId(String namespace){

        MODIDS.add(namespace);
    }
}
