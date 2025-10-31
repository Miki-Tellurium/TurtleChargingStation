package com.mikitellurium.turtlechargingstation.client.gui.element;

import com.mikitellurium.telluriumforge.gui.TextureSprite;
import com.mikitellurium.turtlechargingstation.common.blockentity.TurtleChargingStationTileEntity;
import com.mikitellurium.turtlechargingstation.util.FastLoc;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import java.awt.*;

public class EnergyStorageElement {
    private static final ResourceLocation ENERGY_STORAGE_TEXTURE = FastLoc.ofMod("textures/gui/energy_storage.png");
    private final TurtleChargingStationTileEntity tile;
    private final int textureWidth = 30;
    private final int textureHeight = 66;
    private final TextureSprite emptyStorage;
    private final TextureSprite fullStorage;
    private final int xPos;
    private final int yPos;

    public EnergyStorageElement(TurtleChargingStationTileEntity tile, int xPos, int yPos) {
        this.tile = tile;
        this.emptyStorage = new TextureSprite(ENERGY_STORAGE_TEXTURE, 0, 0, 16, 66, xPos, yPos);
        this.fullStorage = new TextureSprite(ENERGY_STORAGE_TEXTURE, 16, 0, 14, 64, xPos + 1, yPos + 1);
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public void draw(GuiScreen parent) {
        parent.mc.getTextureManager().bindTexture(emptyStorage.texture());
        Gui.drawModalRectWithCustomSizedTexture(xPos, yPos, emptyStorage.uOffset(), emptyStorage.vOffset(), emptyStorage.width(), emptyStorage.height(), textureWidth, textureHeight);
        if (this.tile.getEnergy() > 0) {
            this.drawEnergyLevel(parent);
        }
    }

    private void drawEnergyLevel(GuiScreen parent) {
        parent.mc.getTextureManager().bindTexture(fullStorage.texture());
        Gui.drawModalRectWithCustomSizedTexture(fullStorage.xPos(), fullStorage.yPos() + this.getEnergyLevel(),fullStorage.uOffset(), this.getEnergyLevel(),
                fullStorage.width(), fullStorage.height() - this.getEnergyLevel(), textureWidth, textureHeight);
    }

    private int getEnergyLevel() {
        return fullStorage.height() - (int) Math.ceil(fullStorage.height() * (tile.getEnergy() / (float) tile.getMaxEnergy()));
    }

    public ITextComponent getTooltip() {
        return new TextComponentString(tile.getEnergy() + "/" + tile.getMaxEnergy() + " FE");
    }

    public Rectangle getArea() {
        return new Rectangle(xPos, yPos, emptyStorage.width(), emptyStorage.height());
    }
}
