package com.dreu.potionshrines.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import static net.minecraftforge.versions.forge.ForgeVersion.MOD_ID;

public class AoEShrineScreen extends Screen {
    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(MOD_ID, "textures/gui/aoe_shrine_screen.png");
    private final int imageWidth = 176;
    private final int imageHeight = 166;
    private int leftPos;
    private int topPos;

    protected AoEShrineScreen(Component component) {
        super(component);
    }

    @Override
    protected void init() {
        super.init();
        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;
        // Add buttons, text fields, etc. here
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(poseStack); // Draw the dark background overlay
        RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE); // Set the background texture
        blit(poseStack, leftPos, topPos, 0, 0, imageWidth, imageHeight); // Draw the background texture
        super.render(poseStack, mouseX, mouseY, partialTicks); // Draw the rest of the screen elements
    }

    @Override
    public boolean isPauseScreen() {
        return false; // Prevent the game from pausing when the screen is open
    }

}
