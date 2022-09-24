package evergoodteam.chassis.objects.blocks;

import evergoodteam.chassis.client.ChassisClient;
import net.minecraft.block.Block;

public interface BlockSettings {

    default Block setTransparent() {
        if(this != null) ChassisClient.addTransparentBlock((Block) this);
        return (Block) this;
    }
}
