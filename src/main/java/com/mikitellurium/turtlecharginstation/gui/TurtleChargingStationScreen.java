package com.mikitellurium.turtlecharginstation.gui;

import com.mikitellurium.telluriumforge.util.MouseUtils;
import com.mikitellurium.turtlecharginstation.gui.element.EnergyStorageElement;
import com.mikitellurium.turtlecharginstation.gui.element.TurtleInfoElement;
import com.mikitellurium.turtlecharginstation.util.FastLoc;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.Optional;
import java.util.function.Supplier;

public class TurtleChargingStationScreen extends AbstractContainerScreen<TurtleChargingStationMenu> {

    private static final ResourceLocation GUI_TEXTURE = FastLoc.modLoc("textures/gui/turtle_charging_station_gui.png");
    private EnergyStorageElement energyStorage;
    private TurtleInfoElement turtleInfo;
    private final Supplier<Integer> xPos = () -> (this.width - this.imageWidth) / 2 - 60;
    private final Supplier<Integer> yPos = () -> (this.height - this.imageHeight) / 2;

    public TurtleChargingStationScreen(TurtleChargingStationMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelY = 5;
        energyStorage = new EnergyStorageElement(menu.getBlockEntity(), xPos.get() + 8, yPos.get() + 18,
                16, 84);
        turtleInfo = new TurtleInfoElement(menu.getBlockEntity(), xPos.get() + 30, yPos.get() + 16);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        int textureWidth = 256;
        int textureHeight = 256;
        graphics.blit(GUI_TEXTURE, xPos.get(), yPos.get(), 0, 0, textureWidth, textureHeight, textureWidth, textureHeight);
        energyStorage.draw(graphics);
        turtleInfo.draw(graphics);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTick);
        renderTooltip(graphics, mouseX, mouseY);
    }

    // This also remove player inventory title from gui
    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);
        renderEnergyAreaTooltips(graphics, mouseX, mouseY);
    }

    private void renderEnergyAreaTooltips(GuiGraphics graphics, int mouseX, int mouseY) {
        Rect2i area = energyStorage.getArea();
        if(MouseUtils.isAboveArea(mouseX, mouseY, area.getX(), area.getY(), area.getWidth() - 2, area.getHeight() - 2)) {
            graphics.renderTooltip(this.font, energyStorage.getTooltips(), Optional.empty(),
                    mouseX - area.getX() - 55, mouseY - area.getY() + 15);
        }
    }

}
