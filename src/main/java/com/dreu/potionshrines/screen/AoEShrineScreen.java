package com.dreu.potionshrines.screen;

import com.dreu.potionshrines.network.PacketHandler;
import com.dreu.potionshrines.network.ResetCooldownPacket;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.dreu.potionshrines.PotionShrines.MODID;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;

public class AoEShrineScreen extends AbstractContainerScreen<AoEShrineMenu> {
    private EditBox effectBox;
    private EditBox amplifierBox;
    private EditBox durationBox;
    private Button resetCooldownButton;
    private EditBox maxCooldownBox;
    private EditBox radiusBox;
    private Button replenishButton;
    private Button playersButton;
    private Button monstersButton;
    private List<String> suggestions;
    private static final int effectBoxWidth = 227;
    private static final int numberBoxWidth = 66;
    private static final int editBoxHeight = 18;
    private int scrollOffset = 0;
    private static final int maxDisplayed = 5;
    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(MODID, "textures/gui/aoe_shrine_screen.png");
    public AoEShrineScreen(AoEShrineMenu aoEShrineMenu, Inventory inventory, Component component) {
        super(aoEShrineMenu, inventory, component);
        imageWidth = 296;
        imageHeight = 196;
    }
    @Override
    protected void containerTick() {
        effectBox.tick();
        amplifierBox.tick();
        durationBox.tick();
        maxCooldownBox.tick();
        radiusBox.tick();
    }
    @Override
    protected void init() {
        super.init();
        effectBox = new EditBox(font, leftPos + 8, topPos + 32, effectBoxWidth, editBoxHeight, Component.literal(""));
        effectBox.setMaxLength(100);
        effectBox.setVisible(true);
        effectBox.setTextColor(0xFFFFFF);
        effectBox.setResponder(this::onEffectChanged);
        effectBox.setValue(menu.shrineEntity.effect);

        amplifierBox = new EditBox(font, leftPos + 243, topPos + 32, 45, editBoxHeight, Component.literal(""));
        amplifierBox.setMaxLength(3);
        amplifierBox.setVisible(true);
        amplifierBox.setTextColor(0xFFFFFF);
        amplifierBox.setResponder(this::onAmplifierChanged);
        amplifierBox.setFilter((s -> s.matches("\\d*")));
        amplifierBox.setValue(String.valueOf(menu.shrineEntity.amplifier));

        durationBox = new EditBox(font, leftPos + 8, topPos + 66, numberBoxWidth, editBoxHeight, Component.literal(""));
        durationBox.setMaxLength(6);
        durationBox.setVisible(true);
        durationBox.setTextColor(0xFFFFFF);
        durationBox.setResponder(this::onDurationChanged);
        durationBox.setFilter((s -> s.matches("\\d*")));
        durationBox.setValue(String.valueOf(menu.shrineEntity.duration / 20));

        resetCooldownButton = new Button(leftPos + 81, topPos + 65, numberBoxWidth, 20, Component.literal(String.valueOf(menu.shrineEntity.getRemainingCooldown() / 20)), this::onCooldownClick);

        maxCooldownBox = new EditBox(font, leftPos + 148, topPos + 66, numberBoxWidth, editBoxHeight, Component.literal(""));
        maxCooldownBox.setMaxLength(6);
        maxCooldownBox.setVisible(true);
        maxCooldownBox.setTextColor(0xFFFFFF);
        maxCooldownBox.setResponder(this::onCooldownChanged);
        maxCooldownBox.setFilter((s -> s.matches("\\d*")));
        maxCooldownBox.setValue(String.valueOf(menu.shrineEntity.maxCooldown / 20));

        radiusBox = new EditBox(font, leftPos + 222, topPos + 66, numberBoxWidth, editBoxHeight, Component.literal(""));
        radiusBox.setMaxLength(2);
        radiusBox.setVisible(true);
        radiusBox.setTextColor(0xFFFFFF);
        radiusBox.setResponder(this::onRadiusChanged);
        radiusBox.setFilter((s -> s.matches("\\d*")));
        radiusBox.setValue(String.valueOf(menu.shrineEntity.maxCooldown / 20));

        replenishButton = new Button(leftPos + 8, topPos + 99, numberBoxWidth, 20, Component.translatable("potion_shrines." + menu.shrineEntity.replenish), this::onBooleanClick);
        playersButton = new Button(leftPos + 8, topPos + 132, numberBoxWidth, 20, Component.translatable("potion_shrines." + menu.shrineEntity.effectPlayers), this::onBooleanClick);
        monstersButton = new Button(leftPos + 8, topPos + 167, numberBoxWidth, 20, Component.translatable("potion_shrines." + menu.shrineEntity.effectMonsters), this::onBooleanClick);


        addRenderableWidget(effectBox);
        addRenderableWidget(amplifierBox);
        addRenderableWidget(durationBox);
        addRenderableWidget(resetCooldownButton);
        addRenderableWidget(maxCooldownBox);
        addRenderableWidget(radiusBox);
        addRenderableWidget(replenishButton);
        addRenderableWidget(playersButton);
        addRenderableWidget(monstersButton);
        suggestions = new ArrayList<>();
    }

    private void onBooleanClick(Button button) {
        suggestions.clear();
        button.setMessage(Component.translatable("potion_shrines." + !Boolean.parseBoolean(button.getMessage().getString())));
    }

    private void onRadiusChanged(String newRadius) {
        suggestions.clear();
        if (!newRadius.isEmpty() && Integer.parseInt(newRadius) > 64)
            radiusBox.setValue("64");
    }

    private void onCooldownChanged(String newCooldown) {
        suggestions.clear();
    }

    private void onCooldownClick(Button button) {
        PacketHandler.CHANNEL.sendToServer(new ResetCooldownPacket());
    }
    private void onAmplifierChanged(String newAmplifier) {
        suggestions.clear();
        if (!newAmplifier.isEmpty() && Integer.parseInt(newAmplifier) > 255)
            amplifierBox.setValue("255");
    }

    private void onDurationChanged(String newDuration) {
        suggestions.clear();
    }

    private void onEffectChanged(String newEffect) {
        if (newEffect.isEmpty()) {
            suggestions.clear();
        } else {
            suggestions = ForgeRegistries.MOB_EFFECTS.getEntries().stream()
                .map(resourceKey -> resourceKey.getKey().location().toString())
                .filter(name -> name.toLowerCase().contains(newEffect.toLowerCase()))
                .collect(Collectors.toList());
        }
        scrollOffset = 0;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (suggestions.size() > maxDisplayed) {
            scrollOffset = Math.max(0, Math.min(scrollOffset - (int) delta, suggestions.size() - maxDisplayed));
        }
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(poseStack);
        resetCooldownButton.setMessage(Component.literal(String.valueOf(menu.shrineEntity.getRemainingCooldown() / 20)));
        RenderSystem.enableDepthTest();
        super.render(poseStack, mouseX, mouseY, partialTicks);
        if (effectBox.isFocused() && !suggestions.isEmpty() && !(suggestions.size() == 1 && suggestions.get(0).equals(effectBox.getValue()))) {
            poseStack.translate(0, 0, 1);
            renderSuggestionsDropdown(poseStack, mouseX, mouseY);
        }
        RenderSystem.disableDepthTest();
    }
    @Override
    protected void renderBg(PoseStack poseStack, float v, int i, int i1) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);

        blit(poseStack, leftPos, topPos, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);
    }
        @Override
    protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
        font.draw(poseStack, title, 47 - font.width(title.getVisualOrderText()) * 0.5f, titleLabelY, 4210752);
        font.draw(poseStack, Component.translatable("gui.potion_shrines.effect"), titleLabelX, 22, 4210752);
        font.draw(poseStack, Component.translatable("gui.potion_shrines.amplifier"), 243, 22, 4210752);
        font.draw(poseStack, Component.translatable("gui.potion_shrines.duration"), titleLabelX, 56, 4210752);
        font.draw(poseStack, Component.translatable("tooltip.potion_shrines.cooldown"), 82, 56, 4210752);
        font.draw(poseStack, Component.translatable("tooltip.potion_shrines.radius"), 222, 56, 4210752);
        font.draw(poseStack, Component.translatable("tooltip.potion_shrines.players"), 8, 90, 4210752);
        font.draw(poseStack, Component.translatable("tooltip.potion_shrines.monsters"), 8, 123, 4210752);
        font.draw(poseStack, Component.translatable("tooltip.potion_shrines.replenish"), 8, 158, 4210752);
    }
    private void renderSuggestionsDropdown(PoseStack poseStack, int mouseX, int mouseY) {
        int x = leftPos + 8;
        int y = topPos + 50;

        // Render background box for the dropdown
        fill(poseStack, x, y, x + effectBoxWidth, y + Math.min(suggestions.size(), maxDisplayed) * 14, 0xFF000000); // Black background

        // Render each suggestion
        for (int i = 0; i < Math.min(maxDisplayed, suggestions.size()); i++) {
            int index = i + scrollOffset;
            String suggestion = suggestions.get(index);
            int yOffset = y + i * 14;

            drawString(poseStack, font, suggestion, x + 4, yOffset + 3, 0xFFFFFF);

            if (mouseX >= x && mouseX <= x + effectBoxWidth && mouseY >= yOffset && mouseY < yOffset + 14) {
                fill(poseStack, x, yOffset, x + effectBoxWidth, yOffset + 14, 0x80FFFFFF);
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int x = leftPos + 8;
        if (suggestions.isEmpty()){
            if (effectBox.isMouseOver(mouseX, mouseY)){
                unfocusExcept(effectBox);
            } else if (amplifierBox.isMouseOver(mouseX, mouseY)){
                unfocusExcept(amplifierBox);
                if (amplifierBox.isFocused())
                    amplifierBox.setValue("");
            } else if (durationBox.isMouseOver(mouseX, mouseY)){
                unfocusExcept(durationBox);
                if (durationBox.isFocused())
                    durationBox.setValue("");
            } else if (maxCooldownBox.isMouseOver(mouseX, mouseY)){
                unfocusExcept(maxCooldownBox);
                if (maxCooldownBox.isFocused())
                    maxCooldownBox.setValue("");
            } else if (radiusBox.isMouseOver(mouseX, mouseY)){
                unfocusExcept(radiusBox);
                if (radiusBox.isFocused())
                    radiusBox.setValue("");
            }
        }
        if (effectBox.isFocused()) {
            if (mouseX >= x && mouseX <= x + effectBoxWidth) {
                int suggestionCount = Math.min(maxDisplayed, suggestions.size());
                int suggestionYBottom = topPos + 50 + suggestionCount * 14;
                if (effectBox.isMouseOver(mouseX, mouseY) && button == 1) {
                    suggestions.clear();
                    effectBox.setValue("");
                } else if (mouseY >= topPos + 50 && mouseY <= suggestionYBottom && !suggestions.isEmpty()) {
                    int index = (int) (mouseY - topPos - 50) / 14 + scrollOffset;
                    effectBox.setValue(suggestions.get(index));
                    suggestions.clear();
                    return effectBox.mouseClicked(mouseX, mouseY, button);
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
    public void unfocusExcept(EditBox keep){
        renderables.stream().filter(widget -> widget instanceof EditBox).forEach(editBox -> {
            if (editBox != keep)
                ((EditBox) editBox).setFocus(false);
        });
    }


    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
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
            return true;
        }
        return super.keyReleased(keyCode, scanCode, modifiers);
    }
    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (effectBox.charTyped(codePoint, modifiers)) {
            return true;
        }
        return super.charTyped(codePoint, modifiers);
    }
}
