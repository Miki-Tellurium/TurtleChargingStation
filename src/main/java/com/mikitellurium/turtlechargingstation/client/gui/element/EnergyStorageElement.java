package com.mikitellurium.turtlechargingstation.client.gui.element;

import com.mikitellurium.telluriumforge.util.TextureSprite;
import com.mikitellurium.turtlechargingstation.common.blockentity.TurtleChargingStationBlockEntity;
import com.mikitellurium.turtlechargingstation.util.FastLoc;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class EnergyStorageElement {

    private static final ResourceLocation ENERGY_STORAGE_TEXTURE = FastLoc.modLoc("textures/gui/energy_storage.png");
    private final int textureWidth = 30;
    private final int textureHeight = 66;
    private final TurtleChargingStationBlockEntity station;
    private final TextureSprite emptyStorage;
    private final TextureSprite fullStorage;
    private final int xPos;
    private final int yPos;

    public EnergyStorageElement(TurtleChargingStationBlockEntity station, int xPos, int yPos) {
        this.station = station;
        this.emptyStorage = new TextureSprite(ENERGY_STORAGE_TEXTURE, 0, 0, 16, 66, xPos, yPos);
        this.fullStorage = new TextureSprite(ENERGY_STORAGE_TEXTURE, 16, 0, 14, 64, xPos + 1, yPos + 1);
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public void draw(GuiGraphics graphics) {
        graphics.blit(emptyStorage.texture(), xPos, yPos, emptyStorage.uOffset(), emptyStorage.vOffset(), emptyStorage.width(), emptyStorage.height(), textureWidth, textureHeight);
        if (this.station.getEnergy() > 0) {
            this.drawEnergyLevel(graphics);
        }
    }

    private void drawEnergyLevel(GuiGraphics graphics) {
        graphics.blit(fullStorage.texture(), fullStorage.xPos(), fullStorage.yPos() + this.getEnergyLevel(),fullStorage.uOffset(), this.getEnergyLevel(),
                fullStorage.width(), fullStorage.height() - this.getEnergyLevel(), textureWidth, textureHeight);
    }

    private int getEnergyLevel() {
        return fullStorage.height() - (int) Math.ceil(fullStorage.height() * (station.getEnergy() / (float) station.getMaxEnergy()));
    }

    public Component getTooltip() {
        return Component.literal(station.getEnergy() + "/" + station.getMaxEnergy() + " FE");
    }

    public Rect2i getArea() {
        return new Rect2i(xPos, yPos, emptyStorage.width(), emptyStorage.height());
    }

}
