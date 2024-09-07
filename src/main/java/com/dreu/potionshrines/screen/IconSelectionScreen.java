package com.dreu.potionshrines.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.Nullable;

import static com.dreu.potionshrines.PotionShrines.MODID;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;

public class IconSelectionScreen extends AbstractContainerScreen<IconSelectionMenu> implements MenuProvider {
    private IconScreen<?> returnScreen;
    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(MODID, "textures/gui/shrine_icon_screen.png");

    public IconSelectionScreen(IconSelectionMenu shrineIconMenu, Inventory inventory, Component component) {
        super(shrineIconMenu, inventory, component);
        imageWidth = 248;
        imageHeight = 166;
    }

    public IconSelectionScreen withReturnScreen(IconScreen<?> returnScreen){
        this.returnScreen = returnScreen;
        return this;
    }


    @Override
    protected void renderBg(PoseStack poseStack, float partialTick, int mouseX, int mouseY) {
        renderBackground(poseStack);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);

        blit(poseStack, leftPos, topPos, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);
    }

    @Override
    protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
        this.font.draw(poseStack, this.title, (float)this.titleLabelX, (float)this.titleLabelY, 4210752);
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("testing shit");
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW_KEY_ESCAPE){
            Minecraft.getInstance().setScreen(returnScreen.withIcon("regeneration"));
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new IconSelectionMenu(id);
    }
}
