package com.mikitellurium.turtlechargingstation.client.gui;

import com.mikitellurium.telluriumforge.util.MouseUtils;
import com.mikitellurium.turtlechargingstation.client.gui.element.EnergyStorageElement;
import com.mikitellurium.turtlechargingstation.common.blockentity.TurtleChargingStationTileEntity;
import com.mikitellurium.turtlechargingstation.util.FastLoc;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class TurtleChargingStationGui extends GuiContainer {
    private static final ResourceLocation GUI_TEXTURE = FastLoc.ofMod("textures/gui/turtle_charging_station_gui.png");
    private final IInventory playerInv;
    private final TurtleChargingStationContainer container;
    private EnergyStorageElement energyStorage;
    //private TurtleInfoElement turtleInfo;

    public TurtleChargingStationGui(IInventory playerInv, TurtleChargingStationTileEntity tile) {
        super(new TurtleChargingStationContainer(playerInv, tile));
        this.playerInv = playerInv;
        this.container = (TurtleChargingStationContainer) inventorySlots;
    }

    @Override
    public void initGui() {
        this.xSize = 208;
        this.ySize = 197;
        super.initGui();
        energyStorage = new EnergyStorageElement(container.getTile(), this.guiLeft + 8, this.guiTop + 15);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
        this.renderEnergyAreaTooltips(mouseX, mouseY);
    }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String title = this.container.getTile().getDisplayName().getUnformattedText();
        int x = (this.xSize - this.fontRenderer.getStringWidth(title)) / 2;
        this.fontRenderer.drawString(title, x, 5, 4210752);
        this.fontRenderer.drawString(this.playerInv.getDisplayName().getUnformattedText(), 28, 105, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(GUI_TEXTURE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
        energyStorage.draw(this);
    }

    private void renderEnergyAreaTooltips(int mouseX, int mouseY) {
        Rectangle area = energyStorage.getArea();
        if (MouseUtils.isAboveArea(mouseX, mouseY, area.x, area.y, area.width - 2, area.height - 2)) {
            this.drawHoveringText(energyStorage.getTooltip().getUnformattedText(), mouseX, mouseY);
        }
    }
}
