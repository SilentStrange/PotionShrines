package com.dreu.potionshrines.screen.simple;

import com.dreu.potionshrines.network.PacketHandler;
import com.dreu.potionshrines.network.SaveSimpleShrinePacket;
import com.dreu.potionshrines.screen.IconScreen;
import com.dreu.potionshrines.screen.IconSelectionMenu;
import com.dreu.potionshrines.screen.IconSelectionScreen;
import com.dreu.potionshrines.screen.ShrineScreen;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;

import static com.dreu.potionshrines.PotionShrines.*;
import static java.lang.Boolean.parseBoolean;
import static java.lang.Integer.parseInt;

public class SimpleShrineScreen extends ShrineScreen<SimpleShrineMenu> implements IconScreen<SimpleShrineScreen> {
    public SimpleShrineScreen(SimpleShrineMenu simpleShrineMenu, Inventory inventory, Component component) {
        super(simpleShrineMenu, inventory, component);
        imageWidth = 296;
        imageHeight = 196;
        iconX = 88.5;
        iconY = 103;
        backgroundTexture = new ResourceLocation(MODID, "textures/gui/simple_shrine_screen.png");
    }

    @Override
    protected void initialize() {
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
    }

    @Override
    protected void addWidgets() {
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
    }

    @Override
    protected void alignWidgets() {
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
    protected void onCopyBlockNbtClick(Button button) {
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
    protected void onCopyItemNbtClick(Button button){
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
    @Override
    protected void onIconClick() {
        Minecraft.getInstance().setScreen(new IconSelectionScreen(
                new IconSelectionMenu(this.menu.containerId),
                this.minecraft.player.getInventory(),
                Component.literal("Icon Selection")).withReturnScreen(this));
    }
    protected void onResetClick(Button button) {
        effectBox.setValue(menu.shrineEntity.getEffect());
        amplifierBox.setValue(String.valueOf(menu.shrineEntity.getAmplifier()));
        durationBox.setValue(String.valueOf(menu.shrineEntity.getDuration() / 20));
        maxCooldownBox.setValue(String.valueOf(menu.shrineEntity.getMaxCooldown() / 20));
        icon = menu.shrineEntity.getIcon();
        replenishButton.setMessage(Component.translatable("potion_shrines." + menu.shrineEntity.canReplenish()));
    }
    protected void onSaveClick(Button button) {
        if (getEffectFromString(effectBox.getValue()) == null) {
            if (suggestions.isEmpty()) {
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

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.enableDepthTest();
        super.render(poseStack, mouseX, mouseY, partialTicks);
        if (suggestions.isEmpty()) {
            resetCooldownButton.setMessage(Component.literal(String.valueOf(menu.shrineEntity.getRemainingCooldown() / 20)));
            resetCooldownButton.active = !(menu.shrineEntity.getRemainingCooldown() == 0);
            if (isMouseOverIcon(mouseX, mouseY)) {
                poseStack.translate(0, 0, 1);
                hLine(poseStack, leftPos + 85, leftPos + 136, topPos + 100, 0xFF80ff80);
                hLine(poseStack, leftPos + 85, leftPos + 136, topPos + 151, 0xFF80ff80);
                vLine(poseStack, leftPos + 85, topPos + 100, topPos + 151, 0xFF80ff80);
                vLine(poseStack, leftPos + 136, topPos + 100, topPos + 151, 0xFF80ff80);
            }
        }
        RenderSystem.disableDepthTest();
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
    @Override
    protected boolean isMouseOverIcon(int mouseX, int mouseY){
        return mouseX > leftPos + 84 && mouseX < leftPos + 137 && mouseY > topPos + 99 && mouseY < topPos + 153;
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
