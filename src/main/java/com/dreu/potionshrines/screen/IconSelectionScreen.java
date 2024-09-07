package com.dreu.potionshrines.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.dreu.potionshrines.PotionShrines.*;
import static com.dreu.potionshrines.screen.AoEShrineScreen.NUMBER_BOX_WIDTH;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;

public class IconSelectionScreen extends AbstractContainerScreen<IconSelectionMenu> implements MenuProvider {
    private IconScreen<?> returnScreen;
    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(MODID, "textures/gui/shrine_icon_screen.png");
    private static final int MAX_DISPLAYED = 7;
    private static final int ICON_WIDTH = 34;
    private static final int ICON_BOX_WIDTH = 238;
    private int scrollOffset = 0;
    private boolean initialized = false;
    private EditBox iconBox;
    private ArrayList<String> suggestions;

    public IconSelectionScreen(IconSelectionMenu shrineIconMenu, Inventory inventory, Component component) {
        super(shrineIconMenu, inventory, component);
        imageWidth = 254;
        imageHeight = 114;
    }

    @Override
    protected void init() {
        super.init();
        if (!initialized) {
            iconBox = new EditBox(font, leftPos + 8, topPos + 20, ICON_BOX_WIDTH, EDIT_BOX_HEIGHT, Component.literal(""));
            iconBox.setValue(returnScreen.getIcon());
            iconBox.setResponder(this::onIconChanged);
            suggestions = new ArrayList<>(List.of(returnScreen.getIcon()));
            initialized = true;
        } else {
            iconBox.x = leftPos + 8;
            iconBox.y = topPos + 20;
        }
        addRenderableWidget(new Button(leftPos + 8, topPos + 86, NUMBER_BOX_WIDTH, 20,
                Component.translatable("gui.potion_shrines.reset"), this::onResetClick));
        addRenderableWidget(new Button(leftPos + 93, topPos + 86, NUMBER_BOX_WIDTH, 20,
                Component.translatable("gui.potion_shrines.cancel"), this::onCancelClick));
        addRenderableWidget(new Button(leftPos + imageWidth - 74, topPos + 86, NUMBER_BOX_WIDTH, 20,
                Component.translatable("gui.potion_shrines.done"), this::onDoneClick));
        addRenderableWidget(iconBox);
    }

    private void onIconChanged(String iconSearch) {
        suggestions.clear();
        for (String icon : BAKED_ICONS.keySet()){
            if (icon.toLowerCase().contains(iconSearch.toLowerCase()))
                suggestions.add(icon);

            if (icon.toLowerCase().matches(iconSearch.toLowerCase())) {
                suggestions = new ArrayList<>(List.of(icon));
                break;
            }
        }
        scrollOffset = 0;
    }

    private void onDoneClick(Button button) {
        //Todo: give a warning if invalid
        Minecraft.getInstance().setScreen(returnScreen.withIcon(BAKED_ICONS.get(iconBox.getValue()) == null ? returnScreen.getIcon() : iconBox.getValue()));
    }

    private void onCancelClick(Button button) {
        Minecraft.getInstance().setScreen((Screen) returnScreen);
    }

    private void onResetClick(Button button) {
        iconBox.setValue(returnScreen.getIcon());
    }

    public IconSelectionScreen withReturnScreen(IconScreen<?> returnScreen){
        this.returnScreen = returnScreen;
        return this;
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        super.render(poseStack, mouseX, mouseY, partialTicks);
        renderSuggestedIcons(poseStack, mouseX, mouseY, partialTicks);
    }

    private void renderSuggestedIcons(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        if (suggestions.isEmpty()) return;
        if (mouseY > topPos + 45 && mouseY < topPos + 80){
            if (mouseX > leftPos + 7 && mouseX < leftPos + 246 - ICON_WIDTH * (Math.max(0, MAX_DISPLAYED - suggestions.size()))){
                int xOffset = (mouseX - leftPos - 8) / ICON_WIDTH;

                hLine(poseStack, leftPos + 8 + ICON_WIDTH * xOffset, leftPos + 41 + ICON_WIDTH * xOffset, topPos + 46, 0xFF80ff80);
                hLine(poseStack, leftPos + 8 + ICON_WIDTH * xOffset, leftPos + 41 + ICON_WIDTH * xOffset, topPos + 79, 0xFF80ff80);
                vLine(poseStack, leftPos + 8 + ICON_WIDTH * xOffset, topPos + 46, topPos + 79, 0xFF80ff80);
                vLine(poseStack, leftPos + 41 + ICON_WIDTH * xOffset, topPos + 46, topPos + 79, 0xFF80ff80);
                renderTooltip(poseStack, Component.literal(suggestions.get(xOffset + scrollOffset)), mouseX, mouseY);
            }
        }
        for (int offset = 0; offset < Math.min(MAX_DISPLAYED, suggestions.size()); offset++) {
            RenderSystem.setShader(GameRenderer::getRendertypeItemEntityTranslucentCullShader);
            RenderSystem.disableCull();

            // Push matrix and apply transformations
            poseStack.pushPose();

            poseStack.translate(leftPos + 12 + ICON_WIDTH * offset, topPos + 63, 0); // Positioning in the center
            poseStack.scale(25.0F, 25.0F, 25.0F);
            poseStack.translate(0.5F, 0.25f, 0.5F);
            poseStack.mulPose(Vector3f.ZP.rotationDegrees(180));
            poseStack.mulPose(Vector3f.YP.rotationDegrees((Minecraft.getInstance().level.getGameTime() + partialTicks) % 3600L));
            poseStack.translate(-0.5F, -0.25f, -0.5F);

            MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
            Minecraft.getInstance().getItemRenderer().renderModelLists(BAKED_ICONS.get(suggestions.get(offset + scrollOffset)), ItemStack.EMPTY, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, poseStack, bufferSource.getBuffer(RenderType.cutout()));
            bufferSource.endBatch();

            RenderSystem.enableCull();
            poseStack.popPose();
        }
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
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (suggestions.size() > MAX_DISPLAYED) {
            scrollOffset = Math.max(0, Math.min(scrollOffset - (int) delta, suggestions.size() - MAX_DISPLAYED));
        }
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW_KEY_ESCAPE){
            Minecraft.getInstance().setScreen((Screen) returnScreen);
            return true;
        }
        if (iconBox.canConsumeInput())
            return iconBox.keyPressed(keyCode, scanCode, modifiers);
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!suggestions.isEmpty()){
            if (mouseY > topPos + 45 && mouseY < topPos + 80) {
                if (mouseX > leftPos + 7 && mouseX < leftPos + 246 - ICON_WIDTH * (Math.max(0, MAX_DISPLAYED - suggestions.size()))) {
                    int index = ((int) mouseX - leftPos - 8) / ICON_WIDTH + scrollOffset;
                    iconBox.setValue(suggestions.get(index));
                }
            }
        }
        if (iconBox.isFocused() && button == 1){
            if (mouseY >= topPos + 20 && mouseY <= topPos + 20 + EDIT_BOX_HEIGHT && mouseX >= leftPos + 8 && mouseX <= leftPos + 8 + ICON_BOX_WIDTH ){
                iconBox.setValue("");
                return iconBox.mouseClicked(mouseX, mouseY, button);
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new IconSelectionMenu(id);
    }

    @Override
    protected void containerTick() {
        iconBox.tick();
    }
}
