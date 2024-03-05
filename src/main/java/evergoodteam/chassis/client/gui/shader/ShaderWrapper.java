package evergoodteam.chassis.client.gui.shader;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.impl.client.rendering.FabricShaderProgram;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.resource.ResourceFactory;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class ShaderWrapper {

    private static final List<Pair<
            Function<ResourceFactory, ShaderProgram>,
            Consumer<ShaderProgram>
            >> REGISTERED = new ArrayList<>();
    public ShaderProgram shaderProgram;

    public ShaderWrapper(Identifier name, VertexFormat vertexFormat) {
        REGISTERED.add(new Pair<>(resourceFactory -> {
            try {
                // Handles custom namespaces
                return new FabricShaderProgram(resourceFactory, name, vertexFormat);
            } catch (IOException e) {
                throw new RuntimeException("Failed to initialize shader program \"%s\"".formatted(name), e);
            }
        }, program -> this.shaderProgram = program));
    }

    public void use() {
        RenderSystem.setShader(() -> this.shaderProgram);
    }

    public static void forEachProgram(Consumer<Pair<Function<ResourceFactory, ShaderProgram>, Consumer<ShaderProgram>>> loader) {
        REGISTERED.forEach(loader);
    }
}
