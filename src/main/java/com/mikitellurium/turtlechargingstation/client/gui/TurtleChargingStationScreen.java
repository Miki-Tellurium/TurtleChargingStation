package com.mikitellurium.turtlechargingstation.client.gui;

import com.mikitellurium.telluriumforge.util.MouseUtils;
import com.mikitellurium.turtlechargingstation.client.gui.element.EnergyStorageElement;
import com.mikitellurium.turtlechargingstation.client.gui.element.TurtleInfoElement;
import com.mikitellurium.turtlechargingstation.util.FastId;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.Rect2i;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class TurtleChargingStationScreen extends HandledScreen<TurtleChargingStationScreenHandler> {

    private static final Identifier GUI_TEXTURE = FastId.ofMod("textures/gui/turtle_charging_station_gui.png");
    private EnergyStorageElement energyStorage;
    private TurtleInfoElement turtleInfo;
    private int tickTimer = 0;

    public TurtleChargingStationScreen(TurtleChargingStationScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        this.backgroundWidth = 208;
        this.backgroundHeight = 197;
        this.titleX = (this.backgroundWidth - this.textRenderer.getWidth(this.title)) / 2;
        this.titleY = 5;
        this.playerInventoryTitleX = 28;
        this.playerInventoryTitleY = 105;
        super.init();
        energyStorage = new EnergyStorageElement(this.getScreenHandler().getBlockEntity(), this.x + 8, this.y + 15);
        turtleInfo = new TurtleInfoElement(this.getScreenHandler().getBlockEntity(), this.x + 30, this.y + 16);
    }

    @Override
    protected void handledScreenTick() {
        super.handledScreenTick();
        if (++tickTimer >= 12) {
            this.turtleInfo.updateStringTimer();
            tickTimer = 0;
        }
    }

    @Override
    protected void drawBackground(DrawContext context, float partialTick, int mouseX, int mouseY) {
        int textureWidth = 256;
        int textureHeight = 256;
        context.drawTexture(GUI_TEXTURE, this.x, this.y, 0, 0, textureWidth, textureHeight, textureWidth, textureHeight);
        energyStorage.draw(context);
        turtleInfo.draw(context);
    }


    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(context, mouseX, mouseY, partialTick);
        super.render(context, mouseX, mouseY, partialTick);
        this.drawMouseoverTooltip(context, mouseX, mouseY);
        this.renderEnergyAreaTooltips(context, mouseX, mouseY);
    }

    private void renderEnergyAreaTooltips(DrawContext context, int mouseX, int mouseY) {
        Rect2i area = energyStorage.getArea();
        if (MouseUtils.isAboveArea(mouseX, mouseY, area.getX(), area.getY(), area.getWidth() - 2, area.getHeight() - 2)) {
            context.drawTooltip(this.textRenderer, energyStorage.getTooltip(), mouseX, mouseY);
        }
    }
}
