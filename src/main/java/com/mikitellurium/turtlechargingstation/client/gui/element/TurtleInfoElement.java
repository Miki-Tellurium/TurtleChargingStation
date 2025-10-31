package com.mikitellurium.turtlechargingstation.client.gui.element;

import com.mikitellurium.telluriumforge.util.Utils;
import com.mikitellurium.turtlechargingstation.common.blockentity.TurtleChargingStationTileEntity;
import dan200.computercraft.shared.turtle.blocks.TileTurtle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

import java.awt.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TurtleInfoElement {
    private static final int WHITE = 0xFFFFFF;
    private final TurtleChargingStationTileEntity station;
    private final Rectangle area;
    private final Map<EnumFacing, TurtleData> turtleData = Utils.make(new HashMap<>(), (map) -> {
        for (EnumFacing direction : EnumFacing.values()) {
            map.put(direction, new TurtleData());
        }
    });
    private final FontRenderer font = Minecraft.getMinecraft().fontRenderer;
    private int stringTimer = 0;

    public TurtleInfoElement(TurtleChargingStationTileEntity station, int xPos, int yPos) {
        this.area = new Rectangle(xPos, yPos, 166, 87);
        this.station = station;
    }

    public void draw(Gui parent) {
        this.turtleData.forEach((direction, data) -> data.updateData(this.station, direction));
        int xPos = area.x;
        int yPos = area.y;
        int namePos = xPos + 75;
        int fuelPos = xPos + 143;
        ITextComponent name = new TextComponentTranslation("gui.turtle_charging_station.turtle_name");
        ITextComponent fuelLevel = new TextComponentTranslation("gui.turtle_charging_station.fuel_level");
        parent.drawCenteredString(font, name.getUnformattedText(), namePos, yPos + 2, WHITE);
        parent.drawCenteredString(font, fuelLevel.getUnformattedText(), fuelPos, yPos + 2, WHITE);
        int h = yPos + 2;
        for (EnumFacing direction : EnumFacing.values()) {
            TurtleData data = this.turtleData.get(direction);
            h = h + 12;
            String directionName = this.getDirectionName(direction);
            ITextComponent turtleName = this.trimLabel(data.getLabel());
            parent.drawString(font, directionName, this.alignString(directionName, xPos - 8), h, WHITE);
            parent.drawCenteredString(font, turtleName.getUnformattedText(), namePos, h, data.turtleColor);
            parent.drawCenteredString(font, this.getFuelString(data.turtleFuel), fuelPos, h, WHITE);
        }
    }

    private String getDirectionName(EnumFacing direction) {
        String name = new TextComponentTranslation("gui.turtle_charging_station." + direction.getName()).getUnformattedText();
        String withColon = name + ":";
        int leadingSpace = 7 - withColon.length(); // 7 is fixed
        return String.join("", Collections.nCopies(Math.max(leadingSpace, 0), " ")) + withColon; // Add space to align the ':'
    }

    // Align text to the right
    private int alignString(String string, int xPos) {
        int width = font.getStringWidth(string);
        return Math.max(xPos + (40 - width), xPos);
    }

    public void updateStringTimer() {
        stringTimer++;
    }

    private ITextComponent trimLabel(ITextComponent text) {
        final int maxWidth = 80;
        String s = text.getUnformattedText();
        String finalString = s;
        if (font.getStringWidth(s) > maxWidth) {
            String sub = this.font.trimStringToWidth(s, maxWidth);
            final int maxLength = sub.length();
            int excessChars = s.length() - maxLength + 2; // +2 avoid trimming end of string
            int index = stringTimer % excessChars;
            if (index > 0) index--; // Make start slower
            finalString = s.substring(index, maxLength + index);
        }
        return new TextComponentString(finalString);
    }

    private String getFuelString(int fuelLevel) {
        return fuelLevel == -1 ? "-" : String.valueOf(fuelLevel);
    }

    public Rectangle getArea() {
        return area;
    }

    private static class TurtleData {
        private String label = "-";
        private int turtleColor = WHITE;
        private int turtleFuel = -1;

        private void updateData(TurtleChargingStationTileEntity station, EnumFacing direction) {
            Optional<TileTurtle> optional = this.getAdjacentTurtle(station, direction);
            if (optional.isPresent()) {
                TileTurtle turtle = optional.get();
                this.label = turtle.hasCustomName() ? turtle.getLabel() : String.valueOf(turtle.getComputerID());
                this.turtleColor = turtle.getColour() == -1 ? WHITE : turtle.getColour();
                this.turtleFuel = turtle.getAccess().getFuelLevel();
            } else {
                this.label = "-";
                this.turtleColor = WHITE;
                this.turtleFuel = -1;
            }
        }

        private Optional<TileTurtle> getAdjacentTurtle(TurtleChargingStationTileEntity station, EnumFacing direction) {
            TileEntity tile = station.getWorld().getTileEntity(station.getPos().offset(direction));
            return tile instanceof TileTurtle ? Optional.of((TileTurtle) tile) : Optional.empty();
        }

        private ITextComponent getLabel() {
            return new TextComponentString(label);
        }
    }
}
