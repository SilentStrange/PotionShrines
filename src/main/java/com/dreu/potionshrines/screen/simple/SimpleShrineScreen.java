package com.dreu.potionshrines.screen.simple;

import com.dreu.potionshrines.blocks.shrine.aoe.AoEShrineRenderer;
import com.dreu.potionshrines.network.PacketHandler;
import com.dreu.potionshrines.network.ResetCooldownPacket;
import com.dreu.potionshrines.network.SaveSimpleShrinePacket;
import com.dreu.potionshrines.screen.IconScreen;
import com.dreu.potionshrines.screen.IconSelectionMenu;
import com.dreu.potionshrines.screen.IconSelectionScreen;
import com.dreu.potionshrines.screen.aoe.AoEShrineMenu;
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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.dreu.potionshrines.PotionShrines.*;
import static java.lang.Boolean.parseBoolean;
import static java.lang.Integer.parseInt;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;

public class SimpleShrineScreen extends AbstractContainerScreen<SimpleShrineMenu> implements IconScreen<SimpleShrineScreen> {
    private EditBox effectBox, amplifierBox, durationBox, maxCooldownBox;
    private Button replenishButton, resetCooldownButton, itemNbtButton, blockNbtButton, saveButton;
    private String icon;
    private List<String> suggestions;
    static final int EFFECT_BOX_WIDTH = 227;
    static final int NUMBER_BOX_WIDTH = 66;
    private static final int MAX_DISPLAYED = 5;
    private static int scrollOffset = 0;
    private boolean initialized = false, effectInvalid = false;
    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(MODID, "textures/gui/simple_shrine_screen.png");

    public SimpleShrineScreen(SimpleShrineMenu simpleShrineMenu, Inventory inventory, Component component) {
        super(simpleShrineMenu, inventory, component);
        imageWidth = 296;
        imageHeight = 196;
    }

    @Override
    protected void containerTick() {
        effectBox.tick();
        amplifierBox.tick();
        durationBox.tick();
        maxCooldownBox.tick();
    }

    @Override
    protected void init() {
        super.init();

        if (!initialized) {
            resetCooldownButton = new Button(leftPos + 81, topPos + 65, NUMBER_BOX_WIDTH, 20, Component.literal(String.valueOf(menu.shrineEntity.getRemainingCooldown() / 20)), this::onCooldownClick);
            blockNbtButton = new Button(leftPos + 47, topPos + 166, NUMBER_BOX_WIDTH, 20,
                Component.translatable("gui.potion_shrines.blockNbt"), this::onCopyBlockNbtClick);
            itemNbtButton = new Button(leftPos + 113, topPos + 166, NUMBER_BOX_WIDTH, 20,
                Component.translatable("gui.potion_shrines.itemNbt"), this::onCopyItemNbtClick);
            saveButton = new Button(leftPos + 222, topPos + 166, NUMBER_BOX_WIDTH, 20,
                Component.translatable("gui.potion_shrines.save"), this::onSaveClick);

            effectBox = new EditBox(font, leftPos + 8, topPos + 32, EFFECT_BOX_WIDTH, EDIT_BOX_HEIGHT, Component.literal(""));
            effectBox.setMaxLength(100);
            effectBox.setVisible(true);
            effectBox.setTextColor(0xFFFFFF);
            effectBox.setResponder(this::onEffectChanged);
            effectBox.setValue(menu.shrineEntity.getEffect());

            amplifierBox = new EditBox(font, leftPos + 243, topPos + 32, 45, EDIT_BOX_HEIGHT, Component.literal(""));
            amplifierBox.setMaxLength(3);
            amplifierBox.setVisible(true);
            amplifierBox.setTextColor(0xFFFFFF);
            amplifierBox.setResponder(this::onAmplifierChanged);
            amplifierBox.setFilter((s -> s.matches("\\d*")));
            amplifierBox.setValue(String.valueOf(menu.shrineEntity.getAmplifier()));

            durationBox = new EditBox(font, leftPos + 8, topPos + 66, NUMBER_BOX_WIDTH, EDIT_BOX_HEIGHT, Component.literal(""));
            durationBox.setMaxLength(6);
            durationBox.setVisible(true);
            durationBox.setTextColor(0xFFFFFF);
            durationBox.setResponder(this::onDurationChanged);
            durationBox.setFilter((s -> s.matches("\\d*")));
            durationBox.setValue(String.valueOf(menu.shrineEntity.getDuration() / 20));

            maxCooldownBox = new EditBox(font, leftPos + 148, topPos + 66, NUMBER_BOX_WIDTH, EDIT_BOX_HEIGHT, Component.literal(""));
            maxCooldownBox.setMaxLength(6);
            maxCooldownBox.setVisible(true);
            maxCooldownBox.setTextColor(0xFFFFFF);
            maxCooldownBox.setResponder(this::onCooldownChanged);
            maxCooldownBox.setFilter((s -> s.matches("\\d*")));
            maxCooldownBox.setValue(String.valueOf(menu.shrineEntity.getMaxCooldown() / 20));

            replenishButton = new Button(leftPos + 222, topPos + 66, NUMBER_BOX_WIDTH, 20,
                    Component.translatable("potion_shrines." + menu.shrineEntity.canReplenish()), this::onBooleanClick);
            suggestions = new ArrayList<>();
            icon = menu.shrineEntity.getIcon();
        } else {
            effectBox.x = leftPos + 8;
            effectBox.y = topPos + 32;
            amplifierBox.x = leftPos + 243;
            amplifierBox.y = topPos + 32;
            durationBox.x = leftPos + 8;
            durationBox.y = topPos + 66;
            saveButton.x = leftPos + 222;
            saveButton.y = topPos + 166;
            blockNbtButton.x = leftPos + 47;
            blockNbtButton.y = topPos + 166;
            itemNbtButton.x = leftPos + 113;
            itemNbtButton.y = topPos + 166;
            resetCooldownButton.x = leftPos + 81;
            resetCooldownButton.y = topPos + 65;
            maxCooldownBox.x = leftPos + 148;
            maxCooldownBox.y = topPos + 66;
            replenishButton.x = leftPos + 222;
            replenishButton.y = topPos + 66;
        }

        addRenderableWidget(effectBox);
        addRenderableWidget(amplifierBox);
        addRenderableWidget(durationBox);
        addRenderableWidget(resetCooldownButton);
        addRenderableWidget(maxCooldownBox);
        addRenderableWidget(replenishButton);
        addRenderableWidget(itemNbtButton);
        addRenderableWidget(blockNbtButton);
        addRenderableWidget(saveButton);

        addRenderableWidget(new Button(leftPos + 222, topPos + 99, NUMBER_BOX_WIDTH, 20,
                Component.translatable("gui.potion_shrines.reset"), this::onResetClick));
        addRenderableWidget(new Button(leftPos + 222, topPos + 132, NUMBER_BOX_WIDTH, 20,
                Component.translatable("gui.potion_shrines.cancel"), this::onCancelClick));

        initialized = true;
    }

    private void onCopyBlockNbtClick(Button button) {
        CompoundTag tag = new CompoundTag();
        tag.putString("effect", effectBox.getValue());
        tag.putInt("amplifier", parseInt(amplifierBox.getValue()));
        tag.putInt("duration", parseInt(durationBox.getValue()) * 20);
        tag.putInt("max_cooldown", parseInt(maxCooldownBox.getValue()) * 20);
        tag.putInt("remaining_cooldown", parseInt(resetCooldownButton.getMessage().getString()) * 20);
        tag.putBoolean("replenish", parseBoolean(replenishButton.getMessage().getString()));
        tag.putString("icon", icon);

        this.minecraft.keyboardHandler.setClipboard(tag.getAsString());
    }

    private void onCopyItemNbtClick(Button button){
        CompoundTag tag = new CompoundTag();
        tag.putString("effect", effectBox.getValue());
        tag.putInt("amplifier", parseInt(amplifierBox.getValue()));
        tag.putInt("duration", parseInt(durationBox.getValue()) * 20);
        tag.putInt("max_cooldown", parseInt(maxCooldownBox.getValue()) * 20);
        tag.putInt("remaining_cooldown", parseInt(resetCooldownButton.getMessage().getString()) * 20);
        tag.putBoolean("replenish", parseBoolean(replenishButton.getMessage().getString()));
        tag.putString("icon", icon);
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.put("BlockEntityTag", tag);

        this.minecraft.keyboardHandler.setClipboard(compoundTag.getAsString());
    }
    private void onIconClick() {
        Minecraft.getInstance().setScreen(new IconSelectionScreen(
                new IconSelectionMenu(this.menu.containerId),
                this.minecraft.player.getInventory(),
                Component.literal("Icon Selection")).withReturnScreen(this));
    }

    private void onResetClick(Button button) {
        effectBox.setValue(menu.shrineEntity.getEffect());
        amplifierBox.setValue(String.valueOf(menu.shrineEntity.getAmplifier()));
        durationBox.setValue(String.valueOf(menu.shrineEntity.getDuration() / 20));
        maxCooldownBox.setValue(String.valueOf(menu.shrineEntity.getMaxCooldown() / 20));
        icon = menu.shrineEntity.getIcon();
    }

    private void onCancelClick(Button button) {
        onClose();
    }

    private void onSaveClick(Button button) {
        if (getEffectFromString(effectBox.getValue()) == null) {
            if (suggestions.isEmpty()){
                effectInvalid = true;
                return;
            }
            effectBox.setValue(suggestions.get(0));
        }
        menu.shrineEntity.setEffect(effectBox.getValue());
        if (!amplifierBox.getValue().isEmpty())
            menu.shrineEntity.setAmplifier(parseInt(amplifierBox.getValue()));
        if (!durationBox.getValue().isEmpty())
            menu.shrineEntity.setDuration(parseInt(durationBox.getValue()) * 20);
        if (!maxCooldownBox.getValue().isEmpty())
            menu.shrineEntity.setMaxCooldown(parseInt(maxCooldownBox.getValue()) * 20);
        menu.shrineEntity.setCanReplenish(parseBoolean(replenishButton.getMessage().getString()));
        menu.shrineEntity.setIcon(icon);
        PacketHandler.CHANNEL.sendToServer(new SaveSimpleShrinePacket(
                menu.shrineEntity.getEffect(),
                menu.shrineEntity.getAmplifier(),
                menu.shrineEntity.getDuration(),
                menu.shrineEntity.getMaxCooldown(),
                menu.shrineEntity.canReplenish(),
                icon
        ));
        onClose();
    }


    private void onBooleanClick(Button button) {
        suggestions.clear();
        button.setMessage(Component.translatable("potion_shrines." + !parseBoolean(button.getMessage().getString())));
    }

    private void onCooldownChanged(String newCooldown) {
        suggestions.clear();
        updateNbtValidity();
    }

    private void onCooldownClick(Button button) {
        PacketHandler.CHANNEL.sendToServer(new ResetCooldownPacket());
    }

    private void onAmplifierChanged(String newAmplifier) {
        suggestions.clear();
        if (!newAmplifier.isEmpty() && parseInt(newAmplifier) > 255) {
            amplifierBox.setValue("255");
        }
        updateNbtValidity();
    }

    private void onDurationChanged(String newDuration) {
        suggestions.clear();
        updateNbtValidity();
    }

    private void onEffectChanged(String effectInput) {
        if (effectInput.isEmpty()) {
            effectInvalid = true;
            suggestions.clear();
        } else {
            suggestions = ForgeRegistries.MOB_EFFECTS.getEntries().stream()
                    .map(resourceKey -> resourceKey.getKey().location().toString())
                    .filter(name -> name.toLowerCase().contains(effectInput.toLowerCase()))
                    .collect(Collectors.toList());
            effectInvalid = suggestions.isEmpty() && getEffectFromString(effectInput) == null;
        }
        scrollOffset = 0;
        saveButton.active = !effectInvalid;
        updateNbtValidity();
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (suggestions.size() > MAX_DISPLAYED) {
            scrollOffset = Math.max(0, Math.min(scrollOffset - (int) delta, suggestions.size() - MAX_DISPLAYED));
        }
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        resetCooldownButton.setMessage(Component.literal(String.valueOf(menu.shrineEntity.getRemainingCooldown() / 20)));
        resetCooldownButton.active = !(menu.shrineEntity.getRemainingCooldown() == 0);
        RenderSystem.enableDepthTest();
        super.render(poseStack, mouseX, mouseY, partialTicks);
        if (effectInvalid){
            poseStack.translate(0, 0, 1);
            hLine(poseStack, leftPos + 7, leftPos + 235, topPos + 31, 0xFFFF0000);
            hLine(poseStack, leftPos + 7, leftPos + 235, topPos + 50, 0xFFFF0000);
            vLine(poseStack, leftPos + 7, topPos + 31, topPos + 50, 0xFFFF0000);
            vLine(poseStack, leftPos + 235, topPos + 31, topPos + 50, 0xFFFF0000);
        }
        if (suggestions.isEmpty() && mouseX > leftPos + 84 && mouseX < leftPos + 137 && mouseY > topPos + 99 && mouseY < topPos + 153) {
            poseStack.translate(0, 0, 1);
            hLine(poseStack, leftPos + 85, leftPos + 136, topPos + 100, 0xFF80ff80);
            hLine(poseStack, leftPos + 85, leftPos + 136, topPos + 151, 0xFF80ff80);
            vLine(poseStack, leftPos + 85, topPos + 100, topPos + 151, 0xFF80ff80);
            vLine(poseStack, leftPos + 136, topPos + 100, topPos + 151, 0xFF80ff80);
        }
        if (resetCooldownButton.isMouseOver((double) mouseX, (double) mouseY))
            renderTooltip(poseStack, Component.translatable("gui.potion_shrines.reset_cooldown"), mouseX, mouseY);
        renderIcon(poseStack, mouseX, mouseY, partialTicks);
        if (effectBox.isFocused() && !suggestions.isEmpty() && !(suggestions.size() == 1 && suggestions.get(0).equals(effectBox.getValue())))
            renderSuggestionsDropdown(poseStack, mouseX, mouseY);
        RenderSystem.disableDepthTest();
    }

    private void renderIcon(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {

        // Set up rotation
        RenderSystem.setShader(GameRenderer::getRendertypeItemEntityTranslucentCullShader);
        RenderSystem.disableCull();

        // Push matrix and apply transformations
        poseStack.pushPose();

        poseStack.translate(leftPos + 88.5, topPos + 103, 2); // Positioning in the center
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
    protected void renderBg(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
        renderBackground(poseStack);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);

        blit(poseStack, leftPos, topPos, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);
    }

    @Override
    protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
        font.draw(poseStack, title, 47 - font.width(title.getVisualOrderText()) * 0.5f, titleLabelY, 4210752);
        font.draw(poseStack, Component.translatable("gui.potion_shrines.effect").append(Component.translatable("gui.potion_shrines.tab_hint")), titleLabelX, 22, 4210752);
        font.draw(poseStack, Component.translatable("gui.potion_shrines.amplifier"), 243, 22, 4210752);
        font.draw(poseStack, Component.translatable("gui.potion_shrines.duration"), titleLabelX, 56, 4210752);
        font.draw(poseStack, Component.translatable("tooltip.potion_shrines.cooldown"), 82, 56, 4210752);
        font.draw(poseStack, Component.translatable("tooltip.potion_shrines.max"), 148, 56, 4210752); //
        font.draw(poseStack, Component.translatable("tooltip.potion_shrines.replenish"), 222, 56, 4210752);
        font.draw(poseStack, Component.translatable("tooltip.potion_shrines.icon"), 85, 90, 4210752);
    }

    private void renderSuggestionsDropdown(PoseStack poseStack, int mouseX, int mouseY) {
        poseStack.translate(0, 0, 1);
        int x = leftPos + 8;
        int y = topPos + 50;

        // Render background box for the dropdown
        fill(poseStack, x, y, x + EFFECT_BOX_WIDTH, y + Math.min(suggestions.size(), MAX_DISPLAYED) * 14, 0xFF000000); // Black background

        // Render each suggestion
        for (int i = 0; i < Math.min(MAX_DISPLAYED, suggestions.size()); i++) {
            int index = i + scrollOffset;
            String suggestion = suggestions.get(index);
            int yOffset = y + i * 14;

            drawString(poseStack, font, suggestion, x + 4, yOffset + 3, 0xFFFFFF);

            if (mouseX >= x && mouseX <= x + EFFECT_BOX_WIDTH && mouseY >= yOffset && mouseY < yOffset + 14) {
                fill(poseStack, x, yOffset, x + EFFECT_BOX_WIDTH, yOffset + 14, 0x80FFFFFF);
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (suggestions.isEmpty()) {
            if (mouseX > leftPos + 84 && mouseX < leftPos + 137 && mouseY > topPos + 99 && mouseY < topPos + 153) {
                onIconClick();
                return true;
            }
            if (effectBox.isMouseOver(mouseX, mouseY)) {
                unfocusExcept(effectBox);
            } else if (amplifierBox.isMouseOver(mouseX, mouseY)) {
                unfocusExcept(amplifierBox);
                if (button == 1) {
                    amplifierBox.setValue("");
                }
                return super.mouseClicked(mouseX, mouseY, 0);
            } else if (durationBox.isMouseOver(mouseX, mouseY)) {
                unfocusExcept(durationBox);
                if (button == 1) {
                    durationBox.setValue("");
                }
                return super.mouseClicked(mouseX, mouseY, 0);
            } else if (maxCooldownBox.isMouseOver(mouseX, mouseY)) {
                unfocusExcept(maxCooldownBox);
                if (button == 1) {
                    maxCooldownBox.setValue("");
                }
                return super.mouseClicked(mouseX, mouseY, 0);
            }
        }
        if (mouseX >= leftPos + 8 && mouseX <= leftPos + 8 + EFFECT_BOX_WIDTH) {
            int suggestionCount = Math.min(MAX_DISPLAYED, suggestions.size());
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
    public void updateNbtValidity(){
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

    public void unfocusExcept(EditBox keep) {
        renderables.stream().filter(widget -> widget instanceof EditBox).forEach(editBox -> {
            if (editBox != keep) {
                ((EditBox) editBox).setFocus(false);
            }
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
            return false;
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

    @Override
    public SimpleShrineScreen withIcon(String icon) {
        this.icon = icon;
        return this;
    }

    @Override
    public String getIcon() {
        return icon;
    }
}
