package evergoodteam.chassis;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import evergoodteam.chassis.configs.screen.ConfigScreen;
import evergoodteam.chassis.util.Reference;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ChassisModMenu implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> new ConfigScreen(parent, Reference.CHASSIS_CONFIGS);
    }
}
