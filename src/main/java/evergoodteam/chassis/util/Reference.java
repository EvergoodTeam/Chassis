package evergoodteam.chassis.util;

import evergoodteam.chassis.config.ConfigBase;
import evergoodteam.chassis.common.resourcepack.ResourcePackBase;

public class Reference {

    public static final String MODID = "chassis";
    public static final String CMI = StringUtils.capitalize(MODID);

    public static final ConfigBase CHASSIS_CONFIGS = new ConfigBase(MODID);
    /*public static final ResourcePackBase CHASSIS_RESOURCES = new ResourcePackBase(CHASSIS_CONFIGS, MODID)
            .setIcon("https://evergoodteam.github.io/utils/icons/chassisIcon.png")
            .setColor("53a074");*/
}
