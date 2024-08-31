package com.dreu.potionshrines.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
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
public class AoEShrineScreen extends AbstractContainerScreen<AoEShrineMenu> {
    private EditBox effectBox;
    private List<String> suggestions;
    private static final int editBoxWidth = 280;
    private static final int editBoxHeight = 14;
    private int scrollOffset = 0;
    private static final int maxDisplayed = 5;
    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(MODID, "textures/gui/aoe_shrine_screen.png");
    public AoEShrineScreen(AoEShrineMenu aoEShrineMenu, Inventory inventory, Component component) {
        super(aoEShrineMenu, inventory, component);
        imageWidth = 296;
        imageHeight = 165;
        leftPos = (width - imageWidth) / 2;
        topPos = (height - imageHeight) / 2;
    }

    @Override
    protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
        font.draw(poseStack, title, (float)titleLabelX, (float)titleLabelY, 4210752);
        font.draw(poseStack, Component.translatable("gui.potion_shrines.effect").append(Component.literal(":")), titleLabelX, 18, 4210752);
    }
    @Override
    protected void containerTick() {
        effectBox.tick();
    }
    @Override
    protected void init() {
        super.init();
        this.effectBox = new EditBox(this.font, this.leftPos + 8, this.topPos + 28, editBoxWidth, editBoxHeight, Component.literal(""));
        this.effectBox.setMaxLength(100);
        this.effectBox.setVisible(true);
        this.effectBox.setTextColor(0xFFFFFF);
        this.effectBox.setResponder(this::onTextChanged);

        this.addRenderableWidget(this.effectBox);
        this.suggestions = new ArrayList<>();
    }

    private void onTextChanged(String newText) {
        if (newText.isEmpty()) {
            this.suggestions.clear();
        } else {
            this.suggestions = ForgeRegistries.MOB_EFFECTS.getEntries().stream()
                .map(resourceKey -> resourceKey.getKey().location().toString())
                .filter(name -> name.toLowerCase().contains(newText.toLowerCase()))
                .collect(Collectors.toList());
        }
        this.scrollOffset = 0;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (this.suggestions.size() > maxDisplayed) {
            this.scrollOffset = Math.max(0, Math.min(this.scrollOffset - (int) delta, this.suggestions.size() - maxDisplayed));
        }
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(poseStack);

        super.render(poseStack, mouseX, mouseY, partialTicks);

        if (!this.suggestions.isEmpty()) {
            this.renderSuggestionsDropdown(poseStack, mouseX, mouseY);
        }
    }
    private void renderSuggestionsDropdown(PoseStack poseStack, int mouseX, int mouseY) {
        int x = this.leftPos + 8;
        int y = this.topPos + 42;

        // Render background box for the dropdown
        fill(poseStack, x, y, x + editBoxWidth, y + maxDisplayed * editBoxHeight, 0xFF000000); // Black background

        // Render each suggestion
        for (int i = 0; i < Math.min(maxDisplayed, this.suggestions.size()); i++) {
            int index = i + scrollOffset;
            String suggestion = this.suggestions.get(index);
            int yOffset = y + i * editBoxHeight;

            drawString(poseStack, this.font, suggestion, x + 4, yOffset + 3, 0xFFFFFF);

            // Highlight suggestion on hover
            if (mouseX >= x && mouseX <= x + editBoxWidth && mouseY >= yOffset && mouseY < yOffset + editBoxHeight) {
                fill(poseStack, x, yOffset, x + editBoxWidth, yOffset + editBoxHeight, 0x80FFFFFF);
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int x = leftPos + 8;
        if (mouseX >= x && mouseX <= x + editBoxWidth) {
            int suggestionCount = Math.min(maxDisplayed, suggestions.size());
            int suggestionYBottom = topPos + 42 + suggestionCount * editBoxHeight + editBoxHeight;
            if (mouseY >= topPos + 28 && mouseY < topPos + 42 && button == 1) {
                suggestions.clear();
                effectBox.setValue("");
            } else if (mouseY >= topPos + 42 && mouseY <= suggestionYBottom && !suggestions.isEmpty()){
                int index = (int) (mouseY - topPos - 42) / editBoxHeight + scrollOffset;
                effectBox.setValue(suggestions.get(index));
                suggestions.clear();
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }


    @Override
    protected void renderBg(PoseStack poseStack, float v, int i, int i1) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);

        blit(poseStack, leftPos, topPos, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);
    }
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.effectBox.keyPressed(keyCode, scanCode, modifiers) || this.effectBox.canConsumeInput()) {
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        // Pass the key release event to the EditBox
        if (this.effectBox.keyReleased(keyCode, scanCode, modifiers)) {
            return true;
        }
        return super.keyReleased(keyCode, scanCode, modifiers);
    }
    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (this.effectBox.charTyped(codePoint, modifiers)) {
            return true;
        }
        return super.charTyped(codePoint, modifiers);
    }
}
