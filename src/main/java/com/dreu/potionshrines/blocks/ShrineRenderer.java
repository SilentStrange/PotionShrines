package com.dreu.potionshrines.blocks;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class ShrineRenderer implements BlockEntityRenderer<ShrineBlockEntity> {

    public ShrineRenderer(BlockEntityRendererProvider.Context context) {
        super();
    }
    @Override
    public void render(ShrineBlockEntity shrine, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {
        poseStack.pushPose();

        // Positioning the text
        poseStack.translate(0.5D, 1.2D, 0.5D);
        poseStack.scale(0.1F, 0.1F, 0.1F);

        // Get the text you want to render
        String text = String.valueOf(shrine.getRemainingCooldown()); // Assuming you have a method to get the text

        // Render the text
        Font font = Minecraft.getInstance().font;
        int width = font.width(text);
        poseStack.translate(-width / 10.0D, 0.0D, 0.0D);

        // Render the text with a shadow for better readability
        font.drawInBatch(text, 0F, 0F, 0xFFFFFF, false, poseStack.last().pose(), bufferSource, true, 0, combinedLight);

        poseStack.popPose();
    }
}