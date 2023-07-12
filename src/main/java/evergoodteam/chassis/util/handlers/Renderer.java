package evergoodteam.chassis.util.handlers;

import net.minecraft.client.gui.DrawContext;

import java.util.function.Supplier;

public interface Renderer {

    default void onRenderGameOverlayPost(DrawContext drawContext) {}

    default Supplier<String> getSupplier() {
        return () -> this.getClass().getName();
    }
}
