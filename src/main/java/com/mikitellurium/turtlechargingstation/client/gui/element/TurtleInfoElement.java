package com.mikitellurium.turtlechargingstation.client.gui.element;

import com.mikitellurium.turtlechargingstation.common.blockentity.TurtleChargingStationBlockEntity;
import dan200.computercraft.shared.turtle.blocks.TurtleBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.Rect2i;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Direction;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TurtleInfoElement {

    private static final int WHITE = ColorHelper.Argb.getArgb(255, 255, 255, 255);
    private final TurtleChargingStationBlockEntity station;
    private final Rect2i area;
    private final Map<Direction, TurtleData> turtleData = Util.make(new HashMap<>(), (map) -> {
        for (Direction direction : Direction.values()) {
            map.put(direction, new TurtleData());
        }
    });
    private final TextRenderer font = MinecraftClient.getInstance().textRenderer;
    private int stringTimer = 0;

    public TurtleInfoElement(TurtleChargingStationBlockEntity station, int xPos, int yPos) {
        this.area = new Rect2i(xPos, yPos, 166, 87);
        this.station = station;
    }

    public void draw(DrawContext context) {
        this.turtleData.forEach((direction, data) -> data.updateData(this.station, direction));
        int xPos = area.getX();
        int yPos = area.getY();
        int namePos = xPos + 75;
        int fuelPos = xPos + 143;
        Text name = Text.translatable("gui.turtlechargingstation.turtle_charging_station.turtle_name");
        Text fuelLevel = Text.translatable("gui.turtlechargingstation.turtle_charging_station.fuel_level");
        context.drawCenteredTextWithShadow(font, name, namePos, yPos + 2, WHITE);
        context.drawCenteredTextWithShadow(font, fuelLevel, fuelPos, yPos + 2, WHITE);
        int h = yPos + 2;
        for (Direction direction : Direction.values()) {
            TurtleData data = this.turtleData.get(direction);
            h = h + 12;
            String directionName = this.getDirectionName(direction);
            Text turtleName = this.trimLabel(data.getLabel());
            context.drawTextWithShadow(font, directionName, this.alignString(directionName, xPos - 8), h, WHITE);
            context.drawCenteredTextWithShadow(font, turtleName, namePos, h, data.turtleColor);
            context.drawCenteredTextWithShadow(font, this.getFuelString(data.turtleFuel), fuelPos, h, WHITE);
        }
    }

    private String getDirectionName(Direction direction) {
        String name = Text.translatable("gui.turtlechargingstation.turtle_charging_station." + direction.getName()).getString();
        String withColon = name + ":";
        int leadingSpace = 7 - withColon.length(); // 7 is fixed
        return " ".repeat(Math.max(leadingSpace, 0)) + withColon; // Add space to align the ':'
    }

    // Align text to the right
    private int alignString(String string, int xPos) {
        int width = font.getWidth(string);
        return Math.max(xPos + (40 - width), xPos);
    }

    public void updateStringTimer() {
        stringTimer++;
    }

    private Text trimLabel(Text component) {
        final int maxWidth = 80;
        String s = component.getString();
        String finalString = s;
        if (font.getWidth(s) > maxWidth) {
            String sub = this.font.trimToWidth(s, maxWidth);
            final int maxLength = sub.length();
            int excessChars = s.length() - maxLength + 2; // +2 avoid trimming end of string
            int index = stringTimer % excessChars;
            if (index > 0) index--; // Make start slower
            finalString = s.substring(index, maxLength + index);
        }
        return Text.literal(finalString);
    }

    private String getFuelString(int fuelLevel) {
        return fuelLevel == -1 ? "-" : String.valueOf(fuelLevel);
    }

    public Rect2i getArea() {
        return area;
    }

    private static class TurtleData {

        private String label = "-";
        private int turtleColor = WHITE;
        private int turtleFuel = -1;

        private void updateData(TurtleChargingStationBlockEntity station, Direction direction) {
            Optional<TurtleBlockEntity> optional = this.getAdjacentTurtle(station, direction);
            if (optional.isPresent()) {
                TurtleBlockEntity turtle = optional.get();
                this.label = turtle.hasCustomName() ? turtle.getLabel() : String.valueOf(turtle.getComputerID());
                this.turtleColor = turtle.getColour() == -1 ? WHITE : turtle.getColour();
                this.turtleFuel = turtle.getAccess().getFuelLevel();
            } else {
                this.label = "-";
                this.turtleColor = WHITE;
                this.turtleFuel = -1;
            }
        }

        private Optional<TurtleBlockEntity> getAdjacentTurtle(TurtleChargingStationBlockEntity station, Direction direction) {
            BlockEntity blockEntity = station.getWorld().getBlockEntity(station.getPos().offset(direction));
            return blockEntity instanceof TurtleBlockEntity turtle ? Optional.of(turtle) : Optional.empty();
        }

        private Text getLabel() {
            return Text.literal(label);
        }

    }

}
