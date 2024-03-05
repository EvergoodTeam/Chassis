package evergoodteam.chassis.config.option;

import evergoodteam.chassis.config.ConfigBase;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static evergoodteam.chassis.util.Reference.CMI;

public class OptionStorage {

    private static final Logger LOGGER = LoggerFactory.getLogger(CMI + "/C/Storage");
    private final List<CategoryOption> categories = new ArrayList<>();
    private final CategoryOption configLock;
    private final CategoryOption resourceLock;
    private final CategoryOption generic;
    private final List<String> keys = new ArrayList<>();
    private final OptionList<AbstractOption<?>> properties = new OptionList<>(this);
    private final OptionList<BooleanOption> booleanOptions = new OptionList<>(this);
    private final OptionList<IntegerSliderOption> integerOptions = new OptionList<>(this);
    private final OptionList<DoubleSliderOption> doubleOptions = new OptionList<>(this);
    private final OptionList<StringSetOption> stringSetOptions = new OptionList<>(this);

    public OptionStorage(ConfigBase config) {
        this.generic = new CategoryOption(config, "generic", "").getBuilder().hideComment(true).build();
        this.configLock = new CategoryOption(config, "configLock", "Default options bundled with every config.");
        this.resourceLock = new CategoryOption(config, "resourceLock", "Default options bundled with every config.");
        this.categories.add(configLock);
        this.categories.add(resourceLock);
        this.categories.add(generic);
    }

    @ApiStatus.Internal
    public CategoryOption getConfigLockCat() {
        return this.configLock;
    }

    @ApiStatus.Internal
    public CategoryOption getResourceLockCat() {
        return this.resourceLock;
    }

    public CategoryOption getGenericCategory() {
        return this.generic;
    }

    public List<CategoryOption> getCategories() {
        return categories;
    }

    public List<CategoryOption> getUserCategories() {
        List<CategoryOption> result = new ArrayList<>(categories);
        result.remove(configLock);
        result.remove(resourceLock);
        return result;
    }

    public void storeBooleanOption(BooleanOption option) {
        booleanOptions.store(option);
    }

    public void storeIntegerOption(IntegerSliderOption option) {
        integerOptions.store(option);
    }

    public void storeDoubleOption(DoubleSliderOption option) {
        doubleOptions.store(option);
    }

    public void storeStringSetOption(StringSetOption option) {
        stringSetOptions.store(option);
    }

    public @Nullable AbstractOption<?> getOption(String name) {
        return getOptions().stream().filter(option -> option.getName().equals(name)).findFirst().orElse(null);
    }

    public BooleanOption getBooleanOption(String name) {
        return booleanOptions.get(name);
    }

    public IntegerSliderOption getIntegerOption(String name) {
        return integerOptions.get(name);
    }

    public DoubleSliderOption getDoubleOption(String name) {
        return doubleOptions.get(name);
    }

    public StringSetOption getStringSetOption(String name) {
        return stringSetOptions.get(name);
    }

    public List<AbstractOption<?>> getOptions() {
        return properties.list;
    }

    public List<BooleanOption> getBooleanOptions() {
        return booleanOptions.list;
    }

    public List<IntegerSliderOption> getIntegerOption() {
        return integerOptions.list;
    }

    public List<DoubleSliderOption> getDoubleOptions() {
        return doubleOptions.list;
    }

    public List<StringSetOption> getStringSetOptions() {
        return stringSetOptions.list;
    }

    private static class OptionList<T extends AbstractOption<?>> {

        private OptionStorage storage;
        private List<T> list = new ArrayList<>();

        public OptionList(OptionStorage storage) {
            this.storage = storage;
        }

        public void store(T option) {
            if (!storage.keys.contains(option.getName())) {
                storage.keys.add(option.getName());
                storage.properties.list.add(option);
                list.add(option);
            } else
                LOGGER.error("The option \"{}\" will be ignored because an option with such name already exists", option.getName());
        }

        public @Nullable T get(String name) {
            return list.stream().filter(option -> option.getName().equals(name)).findFirst().orElse(null);
        }
    }
}
