package evergoodteam.chassis.client.models;

public enum BlockModelType implements ModelType{
    /** Same texture on all 6 sides */
    ALL,
    /** Uses a specific texture for the bases and one for the sides (e.g. quartz pillar) */
    COLUMN,
    /** */
    COLUMN_HORIZONTAL,
    /** Same as a column, but textures are mirrored when placed near other mirrored (e.g. deepslate) */
    COLUMN_MIRRORED;

    @Override
    public String toString(){
        return name().toLowerCase();
    }
}
