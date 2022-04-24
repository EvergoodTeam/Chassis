package evergoodteam.chassis.mixin;

import lombok.extern.log4j.Log4j2;
import net.minecraft.client.main.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static evergoodteam.chassis.util.Reference.*;

@Log4j2
@Mixin(Main.class)
public class MainMixin {

    @Inject(method = "main", at = @At("HEAD"), remap = false)
    private static void injectMain(CallbackInfo ci) {

        init();
    }
}