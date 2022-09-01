package evergoodteam.chassis.client.models;

public enum ItemModelType implements ModelType{
    /** Standard item */
    GENERATED,
    /** Tool item */
    HANDHELD,
    /** Block's item form */
    BLOCK;

    @Override
    public String toString(){
        return name().toLowerCase();
    }
}