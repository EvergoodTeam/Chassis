package evergoodteam.chassis.mixin;

import lombok.extern.log4j.Log4j2;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.gui.screen.pack.PackListWidget;
import net.minecraft.client.gui.screen.pack.PackScreen;
import net.minecraft.client.gui.screen.pack.ResourcePackOrganizer;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.stream.Stream;
import java.lang.reflect.Field;
import java.util.ArrayList;

import static evergoodteam.chassis.objects.resourcepacks.ResourcePackBase.*;
import static evergoodteam.chassis.util.Reference.*;

@Log4j2
@Mixin(PackScreen.class)
public class PackScreenMixin {

    // Fix random selection of icon when icon is missing
    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/client/gui/screen/pack/PackScreen;loadPackIcon(Lnet/minecraft/client/texture/TextureManager;Lnet/minecraft/resource/ResourcePackProfile;)Lnet/minecraft/util/Identifier;", cancellable = true)
    public void injectLoadPackIcon(TextureManager textureManager, ResourcePackProfile resourcePackProfile, CallbackInfoReturnable<Identifier> cir){

        if(NO_ICON.contains(resourcePackProfile.getName())){
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
            log.error(e);
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
                log.error(e);
            }
        }

        if (field == null) throw new NoSuchFieldException("No field matching: " + names);

        return field;
    }
}