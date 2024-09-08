package com.dreu.potionshrines.screen;

import net.minecraft.client.gui.screens.Screen;

public interface IconScreen<T extends Screen> {
    T withIcon(String icon);
    String getIcon();
}
