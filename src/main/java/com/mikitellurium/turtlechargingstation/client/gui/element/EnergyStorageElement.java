package com.mikitellurium.turtlechargingstation.client.gui.element;

import com.mikitellurium.telluriumforge.util.TextureSprite;
import com.mikitellurium.turtlechargingstation.common.blockentity.TurtleChargingStationBlockEntity;
import com.mikitellurium.turtlechargingstation.util.FastId;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.Rect2i;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class EnergyStorageElement {

    private static final Identifier ENERGY_STORAGE_TEXTURE = FastId.ofMod("textures/gui/energy_storage.png");
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

    public void draw(DrawContext context) {
        context.drawTexture(emptyStorage.texture(), xPos, yPos, emptyStorage.uOffset(), emptyStorage.vOffset(), emptyStorage.width(), emptyStorage.height(), textureWidth, textureHeight);
        if (this.station.getEnergy() > 0) {
            this.drawEnergyLevel(context);
        }
    }

    private void drawEnergyLevel(DrawContext context) {
        context.drawTexture(fullStorage.texture(), fullStorage.xPos(), fullStorage.yPos() + this.getEnergyLevel(),fullStorage.uOffset(), this.getEnergyLevel(),
                fullStorage.width(), fullStorage.height() - this.getEnergyLevel(), textureWidth, textureHeight);
    }

    private int getEnergyLevel() {
        return fullStorage.height() - (int) Math.ceil(fullStorage.height() * (station.getEnergy() / (float) station.getEnergyCapacity()));
    }

    public Text getTooltip() {
        return Text.literal(station.getEnergy() + "/" + station.getEnergyCapacity());
    }

    public Rect2i getArea() {
        return new Rect2i(xPos, yPos, emptyStorage.width(), emptyStorage.height());
    }

}
