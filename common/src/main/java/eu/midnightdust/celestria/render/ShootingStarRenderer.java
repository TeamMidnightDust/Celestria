package eu.midnightdust.celestria.render;

import com.mojang.blaze3d.systems.RenderSystem;
import eu.midnightdust.celestria.CelestriaClient;
import eu.midnightdust.celestria.ShootingStar;
import eu.midnightdust.celestria.config.CelestriaConfig;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix4f;

import static eu.midnightdust.celestria.Celestria.id;
import static java.lang.Math.pow;

public class ShootingStarRenderer {
    public void renderShootingStars(ClientWorld world, MatrixStack matrices) {
        if (world != null && CelestriaConfig.enableShootingStars && !CelestriaClient.shootingStars.isEmpty()) {
            world.getProfiler().swap("shooting_stars");
            RenderSystem.defaultBlendFunc();
            RenderSystem.enableDepthTest();
            RenderSystem.enableBlend();
            RenderSystem.setShader(GameRenderer::getPositionTexColorProgram);
            CelestriaClient.shootingStars.forEach(star -> renderShootingStar(world, matrices, star));
            RenderSystem.disableBlend();
            RenderSystem.disableDepthTest();
        }
    }
    @SuppressWarnings("SuspiciousNameCombination")
    private void renderShootingStar(ClientWorld world, MatrixStack matrices, ShootingStar star) {
        if (world != null && CelestriaConfig.enableShootingStars && !CelestriaClient.shootingStars.isEmpty()) {
            world.getProfiler().swap("shooting_stars");
            float alpha = (float) Math.clamp((star.progress - pow(1f / star.progress, 4)) / CelestriaConfig.shootingStarPathLength, 0, 1);
            matrices.push();
            matrices.scale(CelestriaConfig.shootingStarDistance,CelestriaConfig.shootingStarDistance,CelestriaConfig.shootingStarDistance);
            int direction = isEven(star.type) ? -1 : 1;
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(star.y));
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(star.rotation * direction));
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(star.x+(star.progress*CelestriaConfig.shootingStarSpeed*0.05f)));
            matrices.translate(star.progress * CelestriaConfig.shootingStarSpeed * direction, 0, 0);
            Matrix4f matrix4f = matrices.peek().getPositionMatrix();
            RenderSystem.setShaderTexture(0, id("textures/environment/shooting_star"+(star.type+1)+".png"));
            BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
            float height = star.size / 100f * 20.0F;
            float width = star.size / 100f * 100.0F;
            bufferBuilder.vertex(matrix4f, -height, -width, height).texture(0.0F, 0.0F).color(1, 1, 1, alpha);
            bufferBuilder.vertex(matrix4f, height, -width, height).texture(1.0F, 0.0F).color(1, 1, 1, alpha);
            bufferBuilder.vertex(matrix4f, height, -width, -height).texture(1.0F, 1.0F).color(1, 1, 1, alpha);
            bufferBuilder.vertex(matrix4f, -height, -width, -height).texture(0.0F, 1.0F).color(1, 1, 1, alpha);
            BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
            matrices.pop();
        }
    }
    public static boolean isEven(int i) {
        return (i | 1) > i;
    }
}
