package evergoodteam.chassis.client.gui;

import net.minecraft.entity.Entity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class RenderUtils {

    /**
     * Get the entity the provided entity is looking at.
     *
     * @author maruohon, taken from <a href="https://github.com/maruohon/tweakeroo/blob/pre-rewrite/fabric/1.20.x/src/main/java/fi/dy/masa/tweakeroo/util/RayTraceUtils.java">RayTraceUtils.java</a>
     * @see <a href="https://github.com/maruohon/tweakeroo">Tweakeroo</a>
     */
    @NotNull
    public static HitResult getRayTraceFromEntity(World worldIn, Entity entityIn) {
        double reach = 5.0d;
        return getRayTraceFromEntity(worldIn, entityIn, reach);
    }

    /**
     * Get the entity the provided entity is looking at.
     *
     * @author maruohon, taken from <a href="https://github.com/maruohon/tweakeroo/blob/pre-rewrite/fabric/1.20.x/src/main/java/fi/dy/masa/tweakeroo/util/RayTraceUtils.java">RayTraceUtils.java</a>,
     * with minor changes done to variable names and the for loop.
     * @see <a href="https://github.com/maruohon/tweakeroo">Tweakeroo</a>
     */
    @NotNull
    public static HitResult getRayTraceFromEntity(World worldIn, Entity entityIn, double range) {
        //Vec3d eyesVec = new Vec3d(entityIn.getX(), entityIn.getY() + entityIn.getStandingEyeHeight(), entityIn.getZ());
        Vec3d eyesVec = entityIn.getCameraPosVec(1.0f);
        Vec3d rangedLookRot = entityIn.getRotationVec(1.0f).multiply(range);
        Vec3d lookVec = eyesVec.add(rangedLookRot);

        RaycastContext context = new RaycastContext(eyesVec, lookVec, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, entityIn);
        HitResult result = worldIn.raycast(context);

        if (result == null) {
            result = BlockHitResult.createMissed(Vec3d.ZERO, Direction.UP, BlockPos.ORIGIN);
        }

        Box boundingBox = entityIn.getBoundingBox().expand(rangedLookRot.x, rangedLookRot.y, rangedLookRot.z).expand(1.0, 1.0, 1.0);

        // If the hit is a block (e.g. the block behind the entity), use the distance between the eyes and the block
        double distanceFromHit = result.getType() == HitResult.Type.BLOCK ? eyesVec.distanceTo(result.getPos()) : Double.MAX_VALUE;

        Optional<Vec3d> entityTrace = Optional.empty();
        Entity targetEntity = null;
        List<Entity> otherEntities = worldIn.getOtherEntities(entityIn, boundingBox);

        // Check distance between eyes and every other entity inside the bounding box
        for (Entity other : otherEntities) {
            boundingBox = other.getBoundingBox();
            Optional<Vec3d> traceTemp = boundingBox.raycast(lookVec, eyesVec);

            if (traceTemp.isPresent()) {
                double distance = eyesVec.distanceTo(traceTemp.get());

                if (distance <= distanceFromHit) {
                    targetEntity = other;
                    entityTrace = traceTemp;
                    break;
                }
            }
        }

        if (targetEntity != null) {
            result = new EntityHitResult(targetEntity, entityTrace.get());
        }

        return result;
    }
}
