package com.dreu.potionshrines.blocks;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import static com.dreu.potionshrines.PotionShrines.BAKED_ICONS;

public class ShrineRenderer implements BlockEntityRenderer<ShrineBlockEntity> {
    public ShrineRenderer(BlockEntityRendererProvider.Context context){}
    @Override
    public void render(ShrineBlockEntity shrineEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {
        float cooldown = shrineEntity.getRemainingCooldown();
        if (cooldown == 0){
            poseStack.pushPose();
            poseStack.translate(0.5, Math.sin((shrineEntity.getLevel().getGameTime() + partialTicks) * 0.05) * 0.1, 0.5);
            poseStack.mulPose(Vector3f.YP.rotationDegrees((shrineEntity.getLevel().getGameTime() + partialTicks) % 360));  // Apply rotation around the Y-axis

            RenderSystem.disableCull();
            RenderSystem.setShader(GameRenderer::getRendertypeItemEntityTranslucentCullShader);
            RenderSystem.enableDepthTest();

            poseStack.scale(0.88889f, 0.88889f, 0.88889f);
            poseStack.translate(-0.5, 0, -0.5);

            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
            itemRenderer.renderModelLists(getBakedIconOrDefault(shrineEntity.getIcon()), ItemStack.EMPTY, 0xF000F0, combinedOverlay, poseStack, bufferSource.getBuffer(RenderType.cutout()));

            poseStack.popPose();
        } else if (cooldown < 40){
            //Animation on replenish
            poseStack.pushPose();
            poseStack.translate(0.5, -cooldown * 0.01, 0.5);
            float normalizedCooldown = (cooldown) / 40.0f;
            poseStack.scale(1 - normalizedCooldown * normalizedCooldown, 1 - normalizedCooldown * normalizedCooldown, 1 - normalizedCooldown * normalizedCooldown);
            poseStack.mulPose(Vector3f.YP.rotationDegrees((shrineEntity.getLevel().getGameTime() + partialTicks) * (1 - normalizedCooldown * normalizedCooldown) % 360));  // Apply rotation around the Y-axis

            RenderSystem.disableCull();
            RenderSystem.setShader(GameRenderer::getRendertypeItemEntityTranslucentCullShader);
            RenderSystem.enableDepthTest();

            poseStack.scale(0.88889f, 0.88889f, 0.88889f);
            poseStack.translate(-0.5, 0, -0.5);

            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
            itemRenderer.renderModelLists(getBakedIconOrDefault(shrineEntity.getIcon()), ItemStack.EMPTY, 0xF000F0, combinedOverlay, poseStack, bufferSource.getBuffer(RenderType.cutout()));

            poseStack.popPose();
        } else if (cooldown > shrineEntity.getMaxCooldown() - 20){
            //Animation on use
            cooldown = shrineEntity.getMaxCooldown() - cooldown;
            poseStack.pushPose();
            poseStack.translate(0.5, cooldown * 0.04 + 0.1, 0.5);
            float normalizedCooldown = (cooldown - 1) / 19.0f;
            poseStack.scale(1 - normalizedCooldown * normalizedCooldown , 1 - normalizedCooldown * normalizedCooldown, 1 - normalizedCooldown * normalizedCooldown);
            poseStack.mulPose(Vector3f.YP.rotationDegrees((shrineEntity.getLevel().getGameTime() + partialTicks) * (1 + normalizedCooldown * normalizedCooldown * 8.0f) % 360));  // Apply rotation around the Y-axis

            RenderSystem.disableCull();
            RenderSystem.setShader(GameRenderer::getRendertypeItemEntityTranslucentCullShader);
            RenderSystem.enableDepthTest();

            poseStack.scale(0.88889f, 0.88889f, 0.88889f);
            poseStack.translate(-0.5, 0, -0.5);

            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
            itemRenderer.renderModelLists(getBakedIconOrDefault(shrineEntity.getIcon()), ItemStack.EMPTY, 0xF000F0, combinedOverlay, poseStack, bufferSource.getBuffer(RenderType.cutout()));

            poseStack.popPose();
        }
        RenderSystem.disableCull();
        RenderSystem.enableDepthTest();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, new ResourceLocation("potion_shrines", "textures/shrine/recharging.png"));
        poseStack.pushPose();

        poseStack.translate(0.5, -1.375, 0.5);
        BufferBuilder buffer = Tesselator.getInstance().getBuilder();

        float uvY = 1 - (float) shrineEntity.getRemainingCooldown() / shrineEntity.getMaxCooldown();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        for (int i = 0; i < 4; i++) {
            buffer.vertex(poseStack.last().pose(), -0.125f, 0, 0.1876f).uv(0, 1).endVertex();
            buffer.vertex(poseStack.last().pose(), 0.125f, 0, 0.1876f).uv(1, 1).endVertex();
            buffer.vertex(poseStack.last().pose(), 0.125f, 1.0625f * uvY, 0.1876f).uv(1, 1 - uvY).endVertex();
            buffer.vertex(poseStack.last().pose(), -0.125f, 1.0625f * uvY, 0.1876f).uv(0, 1 - uvY).endVertex();
            poseStack.mulPose(Vector3f.YP.rotationDegrees(90));
        }
        Tesselator.getInstance().end();

        poseStack.popPose();
    }
    private BakedModel getBakedIconOrDefault(String key) {
        return BAKED_ICONS.get(key) == null ? BAKED_ICONS.get("default") : BAKED_ICONS.get(key);
    }
}