package com.mikitellurium.turtlechargingstation.util;

import net.minecraft.client.renderer.Rect2i;

public class MouseUtil {

    public static boolean isAboveArea(double mouseX, double mouseY, Rect2i area) {
        return isAboveArea(mouseX, mouseY, area.getX(), area.getY(), area.getWidth(), area.getHeight());
    }

    public static boolean isAboveArea(double mouseX, double mouseY, int xPos, int yPos, int width, int height) {
        return (mouseX >= xPos && mouseX <= xPos + width) && (mouseY >= yPos && mouseY <= yPos + height);
    }

}
