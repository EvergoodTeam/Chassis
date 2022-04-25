package evergoodteam.chassis.objects.assets;

public class ModelJson {

    /**
     * @param namespace   your ModId
     * @param type        "handheld": used mostly for tools <br> "generated": everything else item related <br>
     *                    "block": item generated from block
     * @param textureName name of your texture .png File
     * @return
     */
    public static String createItemModelJson(String namespace, String type, String textureName) {

        if ("generated".equals(type) || "handheld".equals(type)) {

            return "{\n" +
                    "  \"parent\": \"item/" + type + "\",\n" +
                    "  \"textures\": {\n" +
                    "    \"layer0\": \"" + namespace + ":item/" + textureName + "\"\n" +
                    "  }\n" +
                    "}";
        } else if ("block".equals(type)) {

            return "{\n" +
                    "  \"parent\": \"" + namespace + ":block/" + textureName + "\"\n" +
                    "}";
        } else return "";
    }

    /**
     * When dealing with Columns, have every texture with the same prefix (eg. example_block) and with either the "_end" or
     * "_side" suffix to specify top/bottom textures and side textures <br><br> (eg. example_block_side, example_block_end)
     *
     * @param cubeType    "all": same texture on all 6 sides <br> "column": uses a specific textures for top/bottom and sides
     * @param textureName name of your .png Files
     * @return
     */
    public static String createBlockModelJson(String cubeType, String textureName) {

        if ("all".equals(cubeType)) {
            return "{\n" +
                    "  \"parent\": \"block/cube_all\",\n" +
                    "  \"textures\": {\n" +
                    "    \"all\": \"" + textureName + "\"\n" +
                    "  }\n" +
                    "}";
        } else if ("column".equals(cubeType)) {
            return "{\n" +
                    "  \"parent\": \"block/cube_column\",\n" +
                    "  \"textures\": {\n" +
                    "    \"end\": \"" + textureName + "_end\",\n" +
                    "    \"side\": \"" + textureName + "_side\"\n" +
                    "  }\n" +
                    "}";
        } else return "";
    }
}
