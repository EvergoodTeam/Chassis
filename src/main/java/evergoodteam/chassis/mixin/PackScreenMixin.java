package evergoodteam.chassis.mixin;

import evergoodteam.chassis.objects.resourcepacks.ResourcePackBase;
import net.minecraft.client.gui.screen.pack.PackListWidget;
import net.minecraft.client.gui.screen.pack.PackScreen;
import net.minecraft.client.gui.screen.pack.ResourcePackOrganizer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static evergoodteam.chassis.util.Reference.CMI;
import static org.slf4j.LoggerFactory.getLogger;

@Mixin(PackScreen.class)
public class PackScreenMixin {

    private static final Logger LOG = getLogger(CMI + "/Screen");

    /**
     * Fixex random selection of icon when icon is missing
     */
    @Inject(at = @At("HEAD"), method = "loadPackIcon(Lnet/minecraft/client/texture/TextureManager;Lnet/minecraft/resource/ResourcePackProfile;)Lnet/minecraft/util/Identifier;", cancellable = true)
    public void injectLoadPackIcon(TextureManager textureManager, ResourcePackProfile resourcePackProfile, CallbackInfoReturnable<Identifier> cir) {

        if (ResourcePackBase.getDefaultIcons().contains(resourcePackProfile.getDisplayName().getString())) {
            //LOG.info("Attempting to set unknown pack icon before error is thrown / random texture");
            cir.setReturnValue(new Identifier("textures/misc/unknown_pack.png"));
        }
    }

    /**
     * Hides ResourcePack from GUI
     */
    @Inject(at = @At("TAIL"), method = "updatePackList(Lnet/minecraft/client/gui/screen/pack/PackListWidget;Ljava/util/stream/Stream;)V")
    public void injectUpdatePackList(PackListWidget widget, Stream<ResourcePackOrganizer.Pack> packs, CallbackInfo info) {

        List<PackListWidget.ResourcePackEntry> toRemove = new ArrayList<>();

        for (PackListWidget.ResourcePackEntry resourcePackEntry : widget.children()) {
            ResourcePackOrganizer.Pack packFromEntry = this.getPackFromEntry(resourcePackEntry);

            if (packFromEntry != null) {
                String name = packFromEntry.getDisplayName().getString();

                if (ResourcePackBase.getHiddenBooleans().containsKey(name)) {
                    if (ResourcePackBase.getHiddenBooleans().get(name).getValue()) toRemove.add(resourcePackEntry);
                }
            }
        }

        for (PackListWidget.ResourcePackEntry resourcePackEntry : toRemove) {
            widget.children().remove(resourcePackEntry);
        }

    }

    private ResourcePackOrganizer.Pack getPackFromEntry(PackListWidget.ResourcePackEntry entry) {
        try {
            Field field = findField("pack", "field_19129");
            field.setAccessible(true);

            return (ResourcePackOrganizer.Pack) field.get(entry);
        } catch (Exception e) {
            LOG.error("Error on getting pack", e);
        }

        return null;
    }

    private Field findField(String... names) {

        Field field = null;

        for (String string : names) {
            try {
                field = PackListWidget.ResourcePackEntry.class.getDeclaredField(string);
                field.setAccessible(true);
                break;
            } catch (Exception ignored) {
            }
        }

        return field;
    }
}
