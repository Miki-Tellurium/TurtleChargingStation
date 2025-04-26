package com.mikitellurium.turtlechargingstation.gui.element;

import com.mikitellurium.turtlechargingstation.blockentity.TurtleChargingStationBlockEntity;
import com.mikitellurium.turtlechargingstation.util.FastLoc;
import com.mikitellurium.turtlechargingstation.util.SimpleSprite;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class EnergyStorageElement {

    private static final ResourceLocation ENERGY_STORAGE_TEXTURE = FastLoc.modLoc("textures/gui/energy_storage.png");
    private final SimpleSprite sprite = new SimpleSprite(ENERGY_STORAGE_TEXTURE, 34, 84);
    private final TurtleChargingStationBlockEntity station;
    private final Rect2i area;

    public EnergyStorageElement(TurtleChargingStationBlockEntity station, int xPos, int yPos, int width, int height) {
        this.station = station;
        area = new Rect2i(xPos, yPos, width, height);
    }

    public void draw(GuiGraphics graphics) {
        RenderSystem.setShaderTexture(0, sprite.texture());
        graphics.blit(sprite.texture(), area.getX(), area.getY(), 0, 0, area.getWidth(), area.getHeight(),
                sprite.texWidth(), sprite.texHeight());
        drawEnergyLevel(graphics);
    }

    private void drawEnergyLevel(GuiGraphics graphics) {
        graphics.blit(sprite.texture(), area.getX(), area.getY() + getEnergyLevel(),18, getEnergyLevel(),
                area.getWidth(), area.getHeight() - getEnergyLevel(), sprite.texWidth(), sprite.texHeight());
    }
    // Get the pixel in the texture to start drawing at relative to the amount of stored energy
    private int getEnergyLevel() {
        return sprite.texHeight() - (int)Math.floor((area.getHeight() * (station.getEnergy() / (float)station.getMaxEnergy())));
    }
    // Energy tooltip
    public List<Component> getTooltips() {
        return List.of(Component.literal(station.getEnergy() + "/" + station.getMaxEnergy() + " FE"));
    }

    public Rect2i getArea() {
        return area;
    }

}
