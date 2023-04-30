package evergoodteam.chassis.client.models;

import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static evergoodteam.chassis.util.Reference.CMI;

/**
 * @deprecated use {@link evergoodteam.chassis.objects.resourcepacks.ResourcePackBase ResourcePackBase} instead
 */
@Deprecated
public class ModelBundler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CMI + "/MODELS");
    private static final List<ModelBundler> BUNDLER_LIST = new ArrayList<>();

    private final String namespace;
    private final Map<String, BlockModelType> blocks;
    private final Map<String, ItemModelType> items;
    private final Set<String> columns;

    /**
     * Bundle that holds models for entries
     *
     * @param namespace your modId
     */
    public ModelBundler(String namespace) {
        this.namespace = namespace;
        this.items = new HashMap<>();
        this.blocks = new HashMap<>();
        this.columns = new HashSet<>();
        BUNDLER_LIST.add(this);
    }

    public ModelBundler addColumnMirrored(Identifier block) {
        blocks.put(block.toString(), BlockModelType.COLUMN);
        blocks.put(block + "_mirrored", BlockModelType.COLUMN_MIRRORED);
        return this;
    }

    public ModelBundler addColumn(Identifier block) {
        blocks.put(block.toString(), BlockModelType.COLUMN);
        blocks.put(block + "_horizontal", BlockModelType.COLUMN_HORIZONTAL);
        return this;
    }

    public ModelBundler addBlock(Identifier block, BlockModelType type) {
        blocks.put(block.toString(), type);
        return this;
    }

    public ModelBundler addItem(Identifier item, ItemModelType type) {
        items.put(item.toString(), type);
        return this;
    }

    /**
     * @deprecated use {@link #addBlock(Identifier, BlockModelType)} instead
     */
    @Deprecated
    public ModelBundler addColumn(String[] columns) {
        for (String string : columns) addColumn(string);
        return this;
    }

    /**
     * @deprecated use {@link #addBlock(Identifier, BlockModelType)} instead
     */
    @Deprecated
    public ModelBundler addColumn(String column) {
        columns.add(column);
        return this;
    }

    public String getNamespace() {
        return namespace;
    }

    public Map<String, BlockModelType> getBlocks() {
        return blocks;
    }

    public Map<String, ItemModelType> getItems() {
        return items;
    }

    /**
     * @deprecated use {@link #getBlockType(String)} instead
     */
    @Deprecated
    public Set<String> getColumns() {
        return columns;
    }

    public @Nullable ModelType getBlockType(String identifier) {
        return blocks.get(identifier);
    }

    public @Nullable ModelType getItemType(String identifier) {
        return items.get(identifier);
    }

    public boolean hasNamespace(String namespace) {
        return getNamespace().equals(namespace);
    }

    public boolean hasEntry(String identifier){
        return hasBlock(identifier) || hasItem(identifier);
    }

    public boolean hasBlock(String identifier){
        return blocks.containsKey(identifier);
    }

    public boolean hasItem(String identifier){
        return items.containsKey(identifier);
    }

    /**
     * @return whether the bundler contains models or not
     */
    public boolean hasEntries() {
        return !blocks.isEmpty() || !items.isEmpty();
    }

    public static List<ModelBundler> getBundlerList() {
        return BUNDLER_LIST;
    }
}
