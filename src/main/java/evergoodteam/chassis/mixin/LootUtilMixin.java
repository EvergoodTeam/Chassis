package evergoodteam.chassis.mixin;

import lombok.extern.log4j.Log4j2;
import net.fabricmc.fabric.api.loot.v2.LootTableSource;
import net.fabricmc.fabric.impl.loot.LootUtil;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static evergoodteam.chassis.util.Reference.LOOT;

@Log4j2
@Mixin(LootUtil.class)
public class LootUtilMixin {

    /**
     * From Fabric API 0.53.0 onwards a detailed and long error log is made
     * if a LootTable is added through Mixin
     *
     * @see net.fabricmc.fabric.impl.loot.LootUtil#LOGGER
     */

    // TODO: [I] Spammy error when using mixin to add lootTables
    @Inject(method = "determineSource", at = @At("HEAD"), cancellable = true)
    private static void interceptSource(Identifier lootTableId, ResourceManager resourceManager, CallbackInfoReturnable<LootTableSource> cir) {

        for (String namespace : LOOT.keySet()) {

            //log.info(namespace);
            if (namespace.equals(lootTableId.getNamespace())) {
                //log.info("Intercepted");
                cir.setReturnValue(LootTableSource.DATA_PACK);
            }
        }
    }
}
