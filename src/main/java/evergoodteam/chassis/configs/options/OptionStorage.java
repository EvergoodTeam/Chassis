package evergoodteam.chassis.configs.options;

import evergoodteam.chassis.configs.ConfigBase;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class OptionStorage {

    private OptionList<BooleanOption> booleanProperties;
    private OptionList<IntegerSliderOption> integerProperties;
    private OptionList<DoubleSliderOption> doubleProperties;
    private OptionList<StringSetOption> stringSetProperties;

    public OptionStorage(ConfigBase config) {
        this.booleanProperties = new OptionList<>();
        this.integerProperties = new OptionList<>();
        this.doubleProperties = new OptionList<>();
        this.stringSetProperties = new OptionList<>();
    }

    public void storeBoolean(BooleanOption booleanOption) {
        booleanProperties.store(booleanOption);
    }

    public void storeInteger(IntegerSliderOption integerSliderOption) {
        integerProperties.store(integerSliderOption);
    }

    public void storeDouble(DoubleSliderOption doubleSliderOption) {
        doubleProperties.store(doubleSliderOption);
    }

    public void storeStringSet(StringSetOption stringSetOption) {
        stringSetProperties.store(stringSetOption);
    }

    public BooleanOption getBoolean(String name) {
        return booleanProperties.get(name);
    }

    public IntegerSliderOption getInteger(String name) {
        return integerProperties.get(name);
    }

    public DoubleSliderOption getDouble(String name) {
        return doubleProperties.get(name);
    }

    public StringSetOption getStringSet(String name) {
        return stringSetProperties.get(name);
    }

    public List<OptionBase<?>> getOptions() {
        List<OptionBase<?>> result = new ArrayList<>();
        result.addAll(getBooleans());
        result.addAll(getIntegers());
        result.addAll(getDoubles());
        result.addAll(getStringSets());

        return result;
    }

    public List<BooleanOption> getBooleans() {
        return booleanProperties.optionList;
    }

    public List<IntegerSliderOption> getIntegers() {
        return integerProperties.optionList;
    }

    public List<DoubleSliderOption> getDoubles() {
        return doubleProperties.optionList;
    }

    public List<StringSetOption> getStringSets() {
        return stringSetProperties.optionList;
    }

    private static class OptionList<T extends OptionBase<?>> {

        public List<T> optionList;

        public OptionList() {
            this.optionList = new ArrayList<>();
        }

        public void store(T option) {
            optionList.add(option);
        }

        public @Nullable T get(String name) {
            return optionList.stream().filter(option -> option.getName().equals(name)).findFirst().orElse(null);
        }
    }
}
