package com.mikitellurium.turtlecharginstation.gui.element;

import com.mikitellurium.telluriumforge.util.SimpleSprite;
import com.mikitellurium.turtlecharginstation.blockentity.TurtleChargingStationBlockEntity;
import com.mikitellurium.turtlecharginstation.util.FastLoc;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.energy.EnergyStorage;

import java.util.List;

public class EnergyStorageElement {

    private static final ResourceLocation ENERGY_STORAGE_TEXTURE = FastLoc.modLoc("textures/gui/energy_storage.png");
    private final SimpleSprite sprite = new SimpleSprite(ENERGY_STORAGE_TEXTURE, 34, 84);
    private final TurtleChargingStationBlockEntity station;
    private final Rect2i area;

    public EnergyStorageElement(TurtleChargingStationBlockEntity station, int xPos, int yPos, int width, int height) {
        this.station = station;
        this.area = new Rect2i(xPos, yPos, width, height);
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
        return sprite.height() - (int)Math.floor((area.getHeight() * (station.getEnergy() / (float)station.getMaxEnergy())));
    }
    // Energy tooltip
    public Component getTooltip() {
        return Component.literal(station.getEnergy() + "/" + station.getMaxEnergy() + " FE");
    }

    public Rect2i getArea() {
        return area;
    }

}
