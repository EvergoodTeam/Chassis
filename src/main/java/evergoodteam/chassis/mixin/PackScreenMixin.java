package evergoodteam.chassis.mixin;

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

import static evergoodteam.chassis.objects.resourcepacks.ResourcePackBase.HIDDEN;
import static evergoodteam.chassis.objects.resourcepacks.ResourcePackBase.NO_ICON;
import static evergoodteam.chassis.util.Reference.getLogger;

@Mixin(PackScreen.class)
public class PackScreenMixin {

    private static final Logger LOGGER = getLogger("Screen");

    // Fix random selection of icon when icon is missing
    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/client/gui/screen/pack/PackScreen;loadPackIcon(Lnet/minecraft/client/texture/TextureManager;Lnet/minecraft/resource/ResourcePackProfile;)Lnet/minecraft/util/Identifier;", cancellable = true)
    public void injectLoadPackIcon(TextureManager textureManager, ResourcePackProfile resourcePackProfile, CallbackInfoReturnable<Identifier> cir) {

        if (NO_ICON.contains(resourcePackProfile.getName())) {
            //log.info("Attempting to set unknown pack icon before error is thrown / random texture issue");
            cir.setReturnValue(new Identifier("textures/misc/unknown_pack.png"));
        }
    }

    // Hide ResourcePack from GUI
    @Inject(at = @At("TAIL"), method = "Lnet/minecraft/client/gui/screen/pack/PackScreen;updatePackList(Lnet/minecraft/client/gui/screen/pack/PackListWidget;Ljava/util/stream/Stream;)V")
    private void injectUpdatePackList(PackListWidget widget, Stream<ResourcePackOrganizer.Pack> packs, CallbackInfo info) {

        List<PackListWidget.ResourcePackEntry> toRemove = new ArrayList<>();

        for (PackListWidget.ResourcePackEntry resourcePackEntry : widget.children()) {

            ResourcePackOrganizer.Pack packFromEntry = this.getPackFromEntry(resourcePackEntry);

            if (packFromEntry != null) {
                String name = packFromEntry.getDisplayName().getString();
                if (HIDDEN.contains(name)) toRemove.add(resourcePackEntry);
            }
        }

        for (PackListWidget.ResourcePackEntry resourcePackEntry : toRemove) {

            widget.children().remove(resourcePackEntry);
        }

    }

    private ResourcePackOrganizer.Pack getPackFromEntry(PackListWidget.ResourcePackEntry entry) {

        try {
            Field field = findField(PackListWidget.ResourcePackEntry.class, "pack", "field_19129");
            field.setAccessible(true);

            return (ResourcePackOrganizer.Pack) field.get(entry);
        } catch (Exception e) {
            LOGGER.error("Error on getting pack", e);
        }

        return null;
    }

    private Field findField(Class<?> aClass, String... names) throws NoSuchFieldException {

        Field field = null;

        for (String string : names) {
            try {
                field = aClass.getDeclaredField(string);
                field.setAccessible(true);
                break;
            } catch (Exception e) {
                LOGGER.error("Error on finding field", e);
            }
        }

        if (field == null) throw new NoSuchFieldException("No field matching: " + names);

        return field;
    }
}
