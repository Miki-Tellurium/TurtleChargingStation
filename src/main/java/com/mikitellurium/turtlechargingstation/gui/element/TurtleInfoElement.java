package com.mikitellurium.turtlechargingstation.gui.element;

import com.mikitellurium.turtlechargingstation.blockentity.TurtleChargingStationBlockEntity;
import dan200.computercraft.shared.turtle.blocks.TurtleBlockEntity;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TurtleInfoElement {

    private final TurtleChargingStationBlockEntity station;
    private final Rect2i area;
    private final Map<Direction, TurtleData> turtleData = Util.make(new HashMap<>(), (map) -> {
        for(Direction direction : Direction.values()) {
            map.put(direction, new TurtleData());
        }
    });
    private final Font font = Minecraft.getInstance().font;
    private static final int white = FastColor.ARGB32.color(255, 255, 255, 255);

    public TurtleInfoElement(TurtleChargingStationBlockEntity station, int xPos, int yPos) {
        this.area = new Rect2i(xPos, yPos, 166, 87);
        this.station = station;
    }

    public void draw(GuiGraphics graphics) {
        this.turtleData.forEach((direction, data) -> data.updateData(this.station, direction));
        int xPos = area.getX();
        int yPos = area.getY();
        Component name = Component.translatable("gui.turtlechargingstation.turtle_charging_station.turtle_name");
        Component fuelLevel = Component.translatable("gui.turtlechargingstation.turtle_charging_station.fuel_level");
        graphics.drawCenteredString(font, name, xPos + 95, yPos + 2, white);
        graphics.drawCenteredString(font, fuelLevel, xPos + 180, yPos + 2, white);
        int h = yPos + 2;
        for (Direction direction : Direction.values()) {
            TurtleData data = this.turtleData.get(direction);
            h = h + 12;
            String directionName = this.getDirectionName(direction);
            Component turtleName = data.getFormattedTurtleName();
            graphics.drawString(font, directionName, this.alignString(directionName, xPos - 8), h, white);
            graphics.drawCenteredString(font, turtleName, xPos + 95, h, white);
            graphics.drawCenteredString(font, this.getFuelString(data.getTurtleFuel()), xPos + 180, h, white);
        }
    }

    private String getDirectionName(Direction direction) {
        String name = Component.translatable("gui.turtlechargingstation.turtle_charging_station." + direction.getName()).getString();
        String withColon = name + ":";
        int leadingSpace = 7 - withColon.length();
        return " ".repeat(Math.max(leadingSpace, 0)) + withColon;
    }

    private String getFuelString(int fuelLevel) {
        return fuelLevel == -1 ? "-" : String.valueOf(fuelLevel);
    }

    // Align text to the right
    private int alignString(String string, int xPos) {
        int width = font.width(string);
        return Math.max(xPos + (40 - width), xPos);
    }

    public Rect2i getArea() {
        return area;
    }

    private static class TurtleData {

        private String turtleName = "-";
        private int turtleColor = white;
        private int turtleFuel = -1;

        private void updateData(TurtleChargingStationBlockEntity station, Direction direction) {
            Optional<TurtleBlockEntity> optional = this.getAdjacentTurtle(station, direction);
            if (optional.isPresent()) {
                TurtleBlockEntity turtle = optional.get();
                this.turtleName = this.getTurtleName(turtle);
                this.turtleColor = this.getTurtleColor(turtle);
                this.turtleFuel = this.getTurtleFuel(turtle);
            } else {
                this.turtleName = "-";
                this.turtleColor = white;
                this.turtleFuel = -1;
            }
        }

        private String getTurtleName(TurtleBlockEntity turtle) {
            return turtle.hasCustomName() ? turtle.getLabel() : String.valueOf(turtle.getComputerID());
        }

        private int getTurtleColor(TurtleBlockEntity turtle) {
            return turtle.getColour() == -1 ? white : turtle.getColour();
        }

        private int getTurtleFuel(TurtleBlockEntity turtle) {
            return turtle.getAccess().getFuelLevel();
        }

        private Optional<TurtleBlockEntity> getAdjacentTurtle(TurtleChargingStationBlockEntity station, Direction direction) {
            BlockEntity blockEntity = station.getLevel().getBlockEntity(station.getBlockPos().relative(direction));
            return blockEntity instanceof TurtleBlockEntity turtle ? Optional.of(turtle) : Optional.empty();
        }

        public String getTurtleName() {
            return turtleName;
        }

        public int getTurtleColor() {
            return turtleColor;
        }

        public int getTurtleFuel() {
            return turtleFuel;
        }

        public Component getFormattedTurtleName() {
            return Component.literal(this.turtleName).withColor(this.turtleColor);
        }
    }

}
