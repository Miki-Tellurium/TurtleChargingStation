package com.mikitellurium.turtlechargingstation.client.gui;

import com.mikitellurium.turtlechargingstation.common.blockentity.TurtleChargingStationTileEntity;
import com.mikitellurium.turtlechargingstation.util.FastLoc;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class TurtleChargingStationGui extends GuiContainer {
    private static final ResourceLocation GUI_TEXTURE = FastLoc.ofMod("textures/gui/turtle_charging_station_gui.png");

    public TurtleChargingStationGui(IInventory playerInv, TurtleChargingStationTileEntity tile) {
        super(new TurtleChargingStationContainer(playerInv, tile));
    }

    @Override
    public void initGui() {
        this.xSize = 208;
        this.ySize = 197;
        super.initGui();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(GUI_TEXTURE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
    }
}
