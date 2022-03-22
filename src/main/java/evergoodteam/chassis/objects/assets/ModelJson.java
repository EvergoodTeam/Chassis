package evergoodteam.chassis.objects.assets;

public class ModelJson {

    public static String makeItemModelJson(String modId, String type, String textureName) {

        // Type: item, block
        // Specific: handheld, generated
        // "handheld" is used mostly for tools, "generated" is used for everything else

        if ("generated".equals(type) || "handheld".equals(type)) {

            return "{\n" +
                    "  \"parent\": \"item/" + type + "\",\n" +
                    "  \"textures\": {\n" +
                    "    \"layer0\": \"" + modId + ":item/" + textureName + "\"\n" +
                    "  }\n" +
                    "}";
        }

        else if ("block".equals(type)) {

            return "{\n" +
                    "  \"parent\": \""+ modId + ":block/"+ textureName +"\"\n" +
                    "}";
        }


        else return "";
    }

    public static String makeBlockModelJson(String cubeType, String textureName){

        if("all".equals(cubeType)){
            return "{\n" +
                    "  \"parent\": \"block/cube_all\",\n" +
                    "  \"textures\": {\n" +
                    "    \"all\": \"" + textureName + "\"\n" +
                    "  }\n" +
                    "}";
        }

        else if("column".equals(cubeType)){
            return "{\n" +
                    "  \"parent\": \"block/cube_column\",\n" +
                    "  \"textures\": {\n" +
                    "    \"end\": \"" + textureName + "_end\",\n" +
                    "    \"side\": \"" + textureName + "_side\"\n" +
                    "  }\n" +
                    "}";
        }

        else return "";

    }
}
