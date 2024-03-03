package evergoodteam.chassis.client.gui.text;

import java.util.Optional;

public interface GradientTextRendererGetter {

    default Optional<GradientTextRenderer> chassisCreateGradientTextRenderer(){
        return Optional.empty();
    }
}
