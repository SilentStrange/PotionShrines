package com.dreu.potionshrines.screen;

import com.dreu.potionshrines.network.PacketHandler;
import com.dreu.potionshrines.network.ResetCooldownPacket;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.stream.Collectors;

import static com.dreu.potionshrines.PotionShrines.getBakedIconOrDefault;
import static com.dreu.potionshrines.PotionShrines.getEffectFromString;
import static java.lang.Boolean.parseBoolean;
import static java.lang.Integer.parseInt;
import static org.lwjgl.glfw.GLFW.*;

public class ShrineScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T>{
    protected EditBox effectBox, amplifierBox, durationBox, maxCooldownBox;
    protected Button replenishButton, resetCooldownButton, itemNbtButton, blockNbtButton, saveButton;
    protected String icon;
    protected List<String> suggestions;
    protected static final int EFFECT_BOX_WIDTH = 227, NUMBER_BOX_WIDTH = 66, MAX_DISPLAYED_SUGGESTIONS = 5;
    protected static int scrollOffset = 0;
    protected boolean initialized = false, effectInvalid = false, translate = false;
    protected double iconX, iconY;
    protected static ResourceLocation backgroundTexture;

    public ShrineScreen(T containerMenu, Inventory inventory, Component component) {
        super(containerMenu, inventory, component);
    }
    @Override
    protected void containerTick() {
        effectBox.tick();
        amplifierBox.tick();
        durationBox.tick();
        maxCooldownBox.tick();
    }

    @Override
    public T getMenu() {
        return super.getMenu();
    }

    @Override
    protected void init() {
        super.init();
        if (!initialized) initialize();
        else alignWidgets();
        addWidgets();
        initialized = true;
    }
    protected void addWidgets(){}
    protected void alignWidgets(){}
    protected void initialize(){}
    protected void updateNbtValidity(){
        for (Widget widget : renderables){
            if (widget instanceof EditBox box){
                if (box.getValue().isEmpty()) {
                    itemNbtButton.active = false;
                    blockNbtButton.active = false;
                    return;
                }
            }
        }
        itemNbtButton.active = !effectInvalid;
        blockNbtButton.active = !effectInvalid;
    }
    protected void onCancelClick(Button button) {
        onClose();
    }
    protected void onCooldownClick(Button button) {
        PacketHandler.CHANNEL.sendToServer(new ResetCooldownPacket());
    }
    protected void onAmplifierChanged(String newAmplifier) {
        if (!newAmplifier.isEmpty() && parseInt(newAmplifier) > 255) {
            amplifierBox.setValue("255");
        }
        updateNbtValidity();
    }
    protected void onDurationChanged(String newDuration) {
        updateNbtValidity();
    }
    protected void onIconClick(){}
    protected void onBooleanClick(Button button) {
        button.setMessage(Component.translatable("potion_shrines." + !parseBoolean(button.getMessage().getString())));
    }
    protected void onCooldownChanged(String newCooldown) {
        updateNbtValidity();
    }
    protected void onEffectChanged(String effectInput) {
        if (effectInput.isEmpty()) {
            effectInvalid = true;
            suggestions.clear();
        } else {
            suggestions = ForgeRegistries.MOB_EFFECTS.getEntries().stream()
                    .map(resourceKey -> resourceKey.getKey().location().toString())
                    .filter(name -> translate ? Component.translatable(getEffectFromString(name).getDescriptionId()).getString().toLowerCase().contains(effectInput.toLowerCase()) : name.toLowerCase().contains(effectInput.toLowerCase()))
                    .collect(Collectors.toList());
            effectInvalid = suggestions.isEmpty() && getEffectFromString(effectInput) == null;
        }
        saveButton.active = !effectInvalid;
        updateNbtValidity();
        scrollOffset = 0;
    }
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (suggestions.size() > MAX_DISPLAYED_SUGGESTIONS) {
            scrollOffset = Math.max(0, Math.min(scrollOffset - (int) delta, suggestions.size() - MAX_DISPLAYED_SUGGESTIONS));
        }
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        super.render(poseStack, mouseX, mouseY, partialTicks);
        if (effectInvalid){
            poseStack.translate(0, 0, 1);
            hLine(poseStack, leftPos + 7, leftPos + 235, topPos + 31, 0xFFFF0000);
            hLine(poseStack, leftPos + 7, leftPos + 235, topPos + 50, 0xFFFF0000);
            vLine(poseStack, leftPos + 7, topPos + 31, topPos + 50, 0xFFFF0000);
            vLine(poseStack, leftPos + 235, topPos + 31, topPos + 50, 0xFFFF0000);
        }
        if (resetCooldownButton.isMouseOver(mouseX, mouseY))
            renderTooltip(poseStack, Component.translatable("gui.potion_shrines.reset_cooldown"), mouseX, mouseY);
        renderIcon(poseStack, partialTicks);
        if (effectBox.isFocused() && !suggestions.isEmpty() && !(suggestions.size() == 1 && suggestions.get(0).equals(effectBox.getValue())))
            renderSuggestionsDropdown(poseStack, mouseX, mouseY);
    }
    protected void renderSuggestionsDropdown(PoseStack poseStack, int mouseX, int mouseY) {
        poseStack.translate(0, 0, 55);
        int x = leftPos + 8;
        int y = topPos + 50;

        // Render background box for the dropdown
        fill(poseStack, x, y, x + EFFECT_BOX_WIDTH, y + Math.min(suggestions.size(), MAX_DISPLAYED_SUGGESTIONS) * 14, 0xFF000000); // Black background

        // Render each suggestion
        for (int i = 0; i < Math.min(MAX_DISPLAYED_SUGGESTIONS, suggestions.size()); i++) {
            int index = i + scrollOffset;
            String suggestion = translate ? Component.translatable(getEffectFromString(suggestions.get(index)).getDescriptionId()).getString() : suggestions.get(index);
            int yOffset = y + i * 14;

            drawString(poseStack, font, suggestion, x + 4, yOffset + 3, 0x808080);

            if (mouseX >= x && mouseX <= x + EFFECT_BOX_WIDTH && mouseY >= yOffset && mouseY < yOffset + 14) {
                fill(poseStack, x, yOffset, x + EFFECT_BOX_WIDTH, yOffset + 14, 0x40FFFFFF);
            }
        }
    }
        @Override
    protected void renderBg(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
        renderBackground(poseStack);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, backgroundTexture);

        blit(poseStack, leftPos, topPos, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);
    }
    protected void renderIcon(PoseStack poseStack, float partialTicks) {
        RenderSystem.disableCull();

        poseStack.pushPose();
        poseStack.translate(leftPos + iconX, topPos + iconY, 2);
        poseStack.scale(45.0F, 45.0F, 45.0F);
        poseStack.translate(0.5F, 0.5F, 0.5F);
        poseStack.mulPose(Vector3f.ZP.rotationDegrees(180));
        poseStack.mulPose(Vector3f.YP.rotationDegrees((Minecraft.getInstance().level.getGameTime() + partialTicks) % 3600L));
        poseStack.translate(-0.5F, -0.5F, -0.5F);

        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        Minecraft.getInstance().getItemRenderer().renderModelLists(getBakedIconOrDefault(icon), ItemStack.EMPTY, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, poseStack, bufferSource.getBuffer(RenderType.cutout()));
        bufferSource.endBatch();

        RenderSystem.enableCull();
        poseStack.popPose();
    }
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (suggestions.isEmpty()) {
            for (Widget widget : renderables) {
                if (widget instanceof EditBox editBox) {
                    if (editBox.isMouseOver(mouseX, mouseY)) {
                        unfocusExcept(editBox);
                        if (button == 1) {
                            editBox.setValue("");
                        }
                        return super.mouseClicked(mouseX, mouseY, 0);
                    }
                }
            }
            if (isMouseOverIcon((int) mouseX, (int) mouseY)) {
                onIconClick();
                return true;
            }
        }
        if (mouseX >= leftPos + 8 && mouseX <= leftPos + 8 + EFFECT_BOX_WIDTH) {
            int suggestionCount = Math.min(MAX_DISPLAYED_SUGGESTIONS, suggestions.size());
            int suggestionYBottom = topPos + 50 + suggestionCount * 14;
            if (effectBox.isMouseOver(mouseX, mouseY) && button == 1) {
                suggestions.clear();
                effectBox.setValue("");
                return super.mouseClicked(mouseX, mouseY, 0);
            } else if (mouseY >= topPos + 50 && mouseY <= suggestionYBottom && !suggestions.isEmpty()) {
                int index = (int) (mouseY - topPos - 50) / 14 + scrollOffset;
                effectBox.setValue(suggestions.get(index));
                suggestions.clear();
                return false;
            } else {
                if (getEffectFromString(effectBox.getValue()) == null) {
                    if (suggestions.isEmpty()) {
                        effectInvalid = true;
                    } else {
                        effectBox.setValue(suggestions.get(0));
                    }
                }
                suggestions.clear();
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW_KEY_TAB) {
            translate = !translate;
            onEffectChanged(effectBox.getValue());
        }
        if (effectBox.canConsumeInput() && keyCode != GLFW_KEY_ESCAPE) {
            if (keyCode == GLFW_KEY_ENTER) {
                effectBox.setValue(suggestions.isEmpty() ? effectBox.getValue() : suggestions.get(0));
                suggestions.clear();
            }
            return effectBox.keyPressed(keyCode, scanCode, modifiers);
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        // Pass the key release event to the EditBox
        if (effectBox.keyReleased(keyCode, scanCode, modifiers)) {
            return false;
        }
        return super.keyReleased(keyCode, scanCode, modifiers);
    }
    protected boolean isMouseOverIcon(int mouseX, int mouseY){return false;}
    public void unfocusExcept(EditBox keep) {
        renderables.stream().filter(widget -> widget instanceof EditBox).forEach(editBox -> {
            if (editBox != keep) {
                ((EditBox) editBox).setFocus(false);
            }
        });
    }
}
