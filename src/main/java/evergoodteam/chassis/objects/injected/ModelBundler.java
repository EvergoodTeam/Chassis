package evergoodteam.chassis.objects.injected;

import java.util.ArrayList;
import java.util.List;

public class ModelBundler {

    private static final List<ModelBundler> BUNDLER_LIST = new ArrayList<>();

    private String namespace;
    private List<String> columns;

    public ModelBundler(String namespace, String[] columns) {
        this(namespace);
        this.columns.addAll(List.of(columns));
    }

    public ModelBundler(String namespace) {
        this.namespace = namespace;
        this.columns = new ArrayList<>();

        BUNDLER_LIST.add(this);
    }

    public String getNamespace() {
        return this.namespace;
    }

    public List<String> getColumns() {
        return this.columns;
    }

    public static List<ModelBundler> getBundlerList() {
        return BUNDLER_LIST;
    }
}
