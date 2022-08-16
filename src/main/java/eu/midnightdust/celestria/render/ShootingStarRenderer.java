package eu.midnightdust.celestria.render;

import com.mojang.blaze3d.systems.RenderSystem;
import eu.midnightdust.celestria.Celestria;
import eu.midnightdust.celestria.CelestriaClient;
import eu.midnightdust.celestria.config.CelestriaConfig;
import eu.midnightdust.lib.util.MidnightMathUtil;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3f;

public class ShootingStarRenderer {
    public void renderShootingStar(ClientWorld world, MatrixStack matrices) {
        if (world != null && CelestriaConfig.enableShootingStars && CelestriaClient.shootingStarProgress > 0) {
            world.getProfiler().swap("shooting_star");
            matrices.push();
            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(MidnightMathUtil.isEven(CelestriaClient.shootingStarType) ? CelestriaClient.shootingStarY + CelestriaClient.shootingStarProgress : CelestriaClient.shootingStarY - CelestriaClient.shootingStarProgress));
            matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(CelestriaClient.shootingStarX));
            Matrix4f matrix4f = matrices.peek().getPositionMatrix();
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferBuilder = tessellator.getBuffer();
            RenderSystem.enableTexture();
            RenderSystem.defaultBlendFunc();
            RenderSystem.enableDepthTest();
            RenderSystem.enableBlend();
            float alpha = (float) (Math.log(CelestriaClient.shootingStarProgress) / 5f);
            RenderSystem.setShaderColor(1,1,1,alpha);
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, new Identifier(Celestria.MOD_ID, "textures/environment/shooting_star"+(CelestriaClient.shootingStarType+1)+".png"));
            bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
            bufferBuilder.vertex(matrix4f, -20.0F, -100.0F, 20.0F).texture(0.0F, 0.0F).next();
            bufferBuilder.vertex(matrix4f, 20.0F, -100.0F, 20.0F).texture(1.0F, 0.0F).next();
            bufferBuilder.vertex(matrix4f, 20.0F, -100.0F, -20.0F).texture(1.0F, 1.0F).next();
            bufferBuilder.vertex(matrix4f, -20.0F, -100.0F, -20.0F).texture(0.0F, 1.0F).next();
            BufferRenderer.drawWithShader(bufferBuilder.end());
            RenderSystem.disableTexture();
            matrices.pop();
        }
    }
}
