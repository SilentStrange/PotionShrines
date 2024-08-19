package com.dreu.potionshrines.blocks;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class ShrineRenderer implements BlockEntityRenderer<ShrineBlockEntity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("potion_shrines", "textures/block/shrinetemp.png");

    public ShrineRenderer(BlockEntityRendererProvider.Context context) {
        super();
    }

    @Override
    public void render(ShrineBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {
        poseStack.pushPose();
        poseStack.translate(0.5, Math.sin((blockEntity.getLevel().getGameTime() + partialTicks) * 0.05) * 0.1 + 2.5, 0.5);
        poseStack.mulPose(Vector3f.YP.rotationDegrees((blockEntity.getLevel().getGameTime() + partialTicks) % 360));  // Apply rotation around the Y-axis

        RenderSystem.disableCull();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, TEXTURE);
        RenderSystem.enableDepthTest();

        BufferBuilder buffer = Tesselator.getInstance().getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        buffer.vertex(poseStack.last().pose(), -0.5f, -0.5f, 0).uv(0, 1).endVertex();
        buffer.vertex(poseStack.last().pose(), 0.5f, -0.5f, 0).uv(1, 1).endVertex();
        buffer.vertex(poseStack.last().pose(), 0.5f, 0.5f, 0).uv(1, 0).endVertex();
        buffer.vertex(poseStack.last().pose(), -0.5f, 0.5f, 0).uv(0, 0).endVertex();

        poseStack.mulPose(Vector3f.YP.rotationDegrees(90));

        buffer.vertex(poseStack.last().pose(), -0.5f, -0.5f, 0).uv(0, 1).endVertex();
        buffer.vertex(poseStack.last().pose(), 0.5f, -0.5f, 0).uv(1, 1).endVertex();
        buffer.vertex(poseStack.last().pose(), 0.5f, 0.5f, 0).uv(1, 0).endVertex();
        buffer.vertex(poseStack.last().pose(), -0.5f, 0.5f, 0).uv(0, 0).endVertex();

        Tesselator.getInstance().end();
        poseStack.popPose();
    }
}