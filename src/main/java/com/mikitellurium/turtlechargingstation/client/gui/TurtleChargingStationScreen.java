package com.mikitellurium.turtlechargingstation.client.gui;

import com.mikitellurium.telluriumforge.util.MouseUtils;
import com.mikitellurium.turtlechargingstation.client.gui.element.EnergyStorageElement;
import com.mikitellurium.turtlechargingstation.client.gui.element.TurtleInfoElement;
import com.mikitellurium.turtlechargingstation.util.FastLoc;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class TurtleChargingStationScreen extends AbstractContainerScreen<TurtleChargingStationMenu> {

    private static final ResourceLocation GUI_TEXTURE = FastLoc.ofMod("textures/gui/turtle_charging_station_gui.png");
    private EnergyStorageElement energyStorage;
    private TurtleInfoElement turtleInfo;
    private int tickTimer = 0;

    public TurtleChargingStationScreen(TurtleChargingStationMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    protected void init() {
        this.imageWidth = 208;
        this.imageHeight = 197;
        this.titleLabelY = 5;
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
        this.inventoryLabelX = 28;
        this.inventoryLabelY = 105;
        super.init();
        energyStorage = new EnergyStorageElement(menu.getBlockEntity(), this.leftPos + 8, this.topPos + 15);
        turtleInfo = new TurtleInfoElement(menu.getBlockEntity(), this.leftPos + 30, this.topPos + 16);
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        if (++tickTimer >= 12) {
            this.turtleInfo.updateStringTimer();
            tickTimer = 0;
        }
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        int textureWidth = 256;
        int textureHeight = 256;
        graphics.blit(GUI_TEXTURE, this.leftPos, this.topPos, 0, 0, textureWidth, textureHeight, textureWidth, textureHeight);
        energyStorage.draw(graphics);
        turtleInfo.draw(graphics);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics, mouseX, mouseY, partialTick);
        super.render(graphics, mouseX, mouseY, partialTick);
        this.renderTooltip(graphics, mouseX, mouseY);
        this.renderEnergyAreaTooltips(graphics, mouseX, mouseY);
    }

    private void renderEnergyAreaTooltips(GuiGraphics graphics, int mouseX, int mouseY) {
        Rect2i area = energyStorage.getArea();
        if (MouseUtils.isAboveArea(mouseX, mouseY, area.getX(), area.getY(), area.getWidth() - 2, area.getHeight() - 2)) {
            graphics.renderTooltip(this.font, energyStorage.getTooltip(), mouseX, mouseY);
        }
    }

}
