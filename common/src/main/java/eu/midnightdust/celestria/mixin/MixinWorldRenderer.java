package eu.midnightdust.celestria.mixin;

import eu.midnightdust.celestria.render.ShootingStarRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public abstract class MixinWorldRenderer {
    @Unique private final ShootingStarRenderer celestria$shootingStarRenderer = new ShootingStarRenderer();
    @Shadow @Nullable private ClientWorld world;

    @Inject(at = @At(value = "TAIL"), method = "renderSky(Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;FLnet/minecraft/client/render/Camera;ZLjava/lang/Runnable;)V")
    public void celestria$renderShootingStars(Matrix4f matrix4f, Matrix4f projectionMatrix, float tickDelta, Camera camera, boolean thickFog, Runnable fogCallback, CallbackInfo ci) {
        MatrixStack matrices = new MatrixStack();
        matrices.multiplyPositionMatrix(matrix4f);
        celestria$shootingStarRenderer.renderShootingStars(world, matrices);
    }
}
