package evergoodteam.chassis.configs;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import static evergoodteam.chassis.util.Reference.*;

@Environment(EnvType.CLIENT)
public class ChassisModMenu implements ModMenuApi {

    /*
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return screen -> new Screen(Text.of("")) {
            @Override
            protected void init() {

                if(ConfigBase.CONFIGURATIONS.get(MODID) != null){
                    ConfigBase.CONFIGURATIONS.get(MODID).openConfigFile();
                }
                this.client.setScreen(screen);
            }
        };
    }*/

    /* Soon™, will be used to create a proper config screen inside of the game
    @Override
    public Map<String, ConfigScreenFactory<?>> getProvidedConfigScreenFactories() {
        return ImmutableMap.of("minecraft", parent -> new OptionsScreen(parent, MinecraftClient.getInstance().options));
    }
    */
}
