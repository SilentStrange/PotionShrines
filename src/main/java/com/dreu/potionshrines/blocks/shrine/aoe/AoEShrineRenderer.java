package com.dreu.potionshrines.blocks.shrine.aoe;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

import static com.dreu.potionshrines.PotionShrines.getBakedIconOrDefault;

public class AoEShrineRenderer implements BlockEntityRenderer<AoEShrineBlockEntity>{
    public AoEShrineRenderer(){}
    @Override
    public void render(AoEShrineBlockEntity aoeshrineEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {
        if (!Objects.equals(aoeshrineEntity.getEffect(), "null")) {
            float cooldown = aoeshrineEntity.getRemainingCooldown();
            float uvY = aoeshrineEntity.canReplenish() ? 1 - (float) aoeshrineEntity.getRemainingCooldown() / aoeshrineEntity.getMaxCooldown() : 0;

            if (cooldown == 0) {
                uvY = 1;
                poseStack.pushPose();
                poseStack.translate(0.5, Math.sin((aoeshrineEntity.getLevel().getGameTime() + partialTicks) * 0.05) * 0.1, 0.5);
                poseStack.mulPose(Vector3f.YP.rotationDegrees((aoeshrineEntity.getLevel().getGameTime() + partialTicks) % 360));  // Apply rotation around the Y-axis

                RenderSystem.setShader(GameRenderer::getRendertypeItemEntityTranslucentCullShader);
                RenderSystem.enableDepthTest();

                poseStack.scale(0.88889f, 0.88889f, 0.88889f);
                poseStack.translate(-0.5, 0.2, -0.5);

                ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
                itemRenderer.renderModelLists(getBakedIconOrDefault(aoeshrineEntity.getIcon()), ItemStack.EMPTY, 0xF000F0, combinedOverlay, poseStack, bufferSource.getBuffer(RenderType.cutout()));

                poseStack.popPose();
            } else if (cooldown < 40) {
                //Animation on replenish
                poseStack.pushPose();
                poseStack.translate(0.5, -cooldown * 0.01, 0.5);
                poseStack.translate(0, Math.sin((aoeshrineEntity.getLevel().getGameTime() + partialTicks) * 0.05) * 0.1, 0);
                float normalizedCooldown = cooldown / 39.0f;
                poseStack.scale(1 - normalizedCooldown * normalizedCooldown, 1 - normalizedCooldown * normalizedCooldown, 1 - normalizedCooldown * normalizedCooldown);
                poseStack.mulPose(Vector3f.YP.rotationDegrees(((aoeshrineEntity.getLevel().getGameTime() + partialTicks) - 3600 * normalizedCooldown * normalizedCooldown) % 360));  // Apply rotation around the Y-axis

                RenderSystem.setShader(GameRenderer::getRendertypeItemEntityTranslucentCullShader);
                RenderSystem.enableDepthTest();

                poseStack.scale(0.88889f, 0.88889f, 0.88889f);
                poseStack.translate(-0.5, 0, -0.5);

                ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
                itemRenderer.renderModelLists(getBakedIconOrDefault(aoeshrineEntity.getIcon()), ItemStack.EMPTY, 0xF000F0, combinedOverlay, poseStack, bufferSource.getBuffer(RenderType.cutout()));

                poseStack.popPose();
            } else if (cooldown > aoeshrineEntity.getMaxCooldown() - 20) {
                //Animation on use
                cooldown = aoeshrineEntity.getMaxCooldown() - cooldown;
                poseStack.pushPose();
                poseStack.translate(0.5, cooldown * 0.04 + 0.1, 0.5);
                poseStack.translate(0, Math.sin((aoeshrineEntity.getLevel().getGameTime() + partialTicks) * 0.05) * 0.1, 0);
                float normalizedCooldown = cooldown / 19.0f;
                poseStack.scale(1 - normalizedCooldown * normalizedCooldown, 1 - normalizedCooldown * normalizedCooldown, 1 - normalizedCooldown * normalizedCooldown);
                poseStack.mulPose(Vector3f.YP.rotationDegrees(((aoeshrineEntity.getLevel().getGameTime() + partialTicks) - 1800 * normalizedCooldown * normalizedCooldown) % 360));  // Apply rotation around the Y-axis

                RenderSystem.setShader(GameRenderer::getRendertypeItemEntityTranslucentCullShader);
                RenderSystem.enableDepthTest();

                poseStack.scale(0.88889f, 0.88889f, 0.88889f);
                poseStack.translate(-0.5, 0, -0.5);

                ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
                itemRenderer.renderModelLists(getBakedIconOrDefault(aoeshrineEntity.getIcon()), ItemStack.EMPTY, 0xF000F0, combinedOverlay, poseStack, bufferSource.getBuffer(RenderType.cutout()));

                poseStack.popPose();
                uvY = 1 - normalizedCooldown;
            }
            RenderSystem.enableDepthTest();
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, new ResourceLocation("potion_shrines", "textures/block/aoe_recharging.png"));
            poseStack.pushPose();

            poseStack.translate(0.5, -1.4375, 0.5);
            BufferBuilder buffer = Tesselator.getInstance().getBuilder();

            buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
            for (int i = 0; i < 4; i++) {
                buffer.vertex(poseStack.last().pose(), -0.125f, 0.0f, 0.2501f).uv(0, 1).endVertex();
                buffer.vertex(poseStack.last().pose(), 0.125f, 0.0f, 0.2501f).uv(1, 1).endVertex();
                buffer.vertex(poseStack.last().pose(), 0.125f, uvY, 0.2501f).uv(1, 1 - uvY).endVertex();
                buffer.vertex(poseStack.last().pose(), -0.125f, uvY, 0.2501f).uv(0, 1 - uvY).endVertex();
                poseStack.mulPose(Vector3f.YP.rotationDegrees(90));
            }
            Tesselator.getInstance().end();

            poseStack.popPose();
        }
    }
}