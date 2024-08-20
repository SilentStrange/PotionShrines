package com.dreu.potionshrines.blocks;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemStack;

import static com.dreu.potionshrines.PotionShrines.BAKED_ICONS;

public class ShrineRenderer implements BlockEntityRenderer<ShrineBlockEntity> {
    public ShrineRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(ShrineBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {
        poseStack.pushPose();
        poseStack.translate(0.5, Math.sin((blockEntity.getLevel().getGameTime() + partialTicks) * 0.05) * 0.1, 0.5);
        poseStack.mulPose(Vector3f.YP.rotationDegrees((blockEntity.getLevel().getGameTime() + partialTicks) % 360));  // Apply rotation around the Y-axis

        RenderSystem.disableCull();
        RenderSystem.setShader(GameRenderer::getRendertypeItemEntityTranslucentCullShader);
        RenderSystem.enableDepthTest();

        poseStack.translate(-0.5, 0, -0.5);

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        itemRenderer.renderModelLists(BAKED_ICONS.get(blockEntity.getIcon()), ItemStack.EMPTY, 0xF000F0, combinedOverlay, poseStack, bufferSource.getBuffer(RenderType.cutout()));

        poseStack.popPose();
    }
}