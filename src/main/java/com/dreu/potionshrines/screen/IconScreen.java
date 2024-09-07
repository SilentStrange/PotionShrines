package com.dreu.potionshrines.screen;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

public interface IconScreen<T extends AbstractContainerScreen<?>> {
    T withIcon(String icon);
    String getIcon();
}
