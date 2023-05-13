package evergoodteam.chassis.datagen;

public interface ProviderSizeGetter {

    default int runningProvidersSize() {
        return -1;
    }

    default int providersSize() {
        return -1;
    }
}
