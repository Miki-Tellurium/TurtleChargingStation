package com.mikitellurium.turtlecharginstation.gui.element;

import com.mikitellurium.telluriumforge.util.SimpleSprite;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraftforge.energy.EnergyStorage;

import java.util.List;

public class EnergyStorageElement {

    private final SimpleSprite sprite;
    private final EnergyStorage energyStorage;
    private final Rect2i area;

    public EnergyStorageElement(EnergyStorage energyStorage, SimpleSprite texture, int xPos, int yPos, int width, int height) {
        this.sprite = texture;
        this.energyStorage = energyStorage;
        area = new Rect2i(xPos, yPos, width, height);
    }

    public void draw(GuiGraphics graphics) {
        RenderSystem.setShaderTexture(0, sprite.texture());
        graphics.blit(sprite.texture(), area.getX(), area.getY(), 0, 0, area.getWidth(), area.getHeight(),
                sprite.width(), sprite.height());
        drawEnergyLevel(graphics);
    }

    private void drawEnergyLevel(GuiGraphics graphics) {
        graphics.blit(sprite.texture(), area.getX(), area.getY() + getEnergyLevel(),18, getEnergyLevel(),
                area.getWidth(), area.getHeight() - getEnergyLevel(), sprite.width(), sprite.height());
    }
    // Get the pixel in the texture to start drawing at relative to the amount of stored energy
    private int getEnergyLevel() {
        return sprite.height() - (int)Math.floor((area.getHeight()*(energyStorage.getEnergyStored()/(float)energyStorage.getMaxEnergyStored())));
    }
    // Energy tooltip
    public List<Component> getTooltips() {
        return List.of(Component.literal(energyStorage.getEnergyStored()+"/"+energyStorage.getMaxEnergyStored()+" FE"));
    }

    public Rect2i getArea() {
        return area;
    }

}
