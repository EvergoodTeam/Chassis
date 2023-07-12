package evergoodteam.chassis.util.handlers;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.HashSet;
import java.util.Set;

public class RenderEventHandler {

    private static final RenderEventHandler INSTANCE = new RenderEventHandler();
    private final Set<Renderer> overlayRenderers = new HashSet<>();

    public static RenderEventHandler getInstance() {
        return INSTANCE;
    }

    public void registerOverlayRenderer(Renderer renderer) {
        this.overlayRenderers.add(renderer);
    }

    public void onPostRender(MinecraftClient client, DrawContext context, float partialTicks){
        for(Renderer renderer : overlayRenderers){
            client.getProfiler().push(renderer.getSupplier());
            renderer.onRenderGameOverlayPost(context);
            client.getProfiler().pop();
        }
    }
}
