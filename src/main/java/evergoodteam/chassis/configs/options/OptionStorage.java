package evergoodteam.chassis.configs.options;

import evergoodteam.chassis.configs.ConfigBase;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static evergoodteam.chassis.util.Reference.CMI;

public class OptionStorage {

    private static final Logger LOGGER = LoggerFactory.getLogger(CMI + "/C/Storage");
    private final List<CategoryOption> categories = new ArrayList<>();
    private final CategoryOption generic;
    private final List<String> keys = new ArrayList<>();
    private final OptionList<AbstractOption<?>> properties = new OptionList<>(this);
    private final OptionList<BooleanOption> booleanOptions = new OptionList<>(this);
    private final OptionList<IntegerSliderOption> integerOptions = new OptionList<>(this);
    private final OptionList<DoubleSliderOption> doubleOptions = new OptionList<>(this);
    private final OptionList<StringSetOption> stringSetOptions = new OptionList<>(this);

    public OptionStorage(ConfigBase config) {
        this.generic = new CategoryOption(config, "generic", "");
        this.categories.add(generic);
    }

    public CategoryOption getGenericCategory() {
        return this.generic;
    }

    public List<CategoryOption> getCategories() {
        return categories;
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

    public @Nullable AbstractOption<?> getOption(String name){
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
        return this.properties.optionList;
    }

    public List<BooleanOption> getBooleanOptions() {
        return booleanOptions.optionList;
    }

    public List<IntegerSliderOption> getIntegerOption() {
        return integerOptions.optionList;
    }

    public List<DoubleSliderOption> getDoubleOptions() {
        return doubleOptions.optionList;
    }

    public List<StringSetOption> getStringSetOptions() {
        return stringSetOptions.optionList;
    }

    private static class OptionList<T extends AbstractOption<?>> {

        private OptionStorage storage;
        private List<T> optionList = new ArrayList<>();

        public OptionList(OptionStorage storage) {
            this.storage = storage;
        }

        public void store(T option) {
            if(!storage.keys.contains(option.getName())){
                storage.keys.add(option.getName());
                storage.properties.optionList.add(option);
                optionList.add(option);
            }
            else LOGGER.error("The option \"{}\" will be ignored because an option with such name already exists", option.getName());
        }

        public @Nullable T get(String name) {
            return optionList.stream().filter(option -> option.getName().equals(name)).findFirst().orElse(null);
        }
    }
}
