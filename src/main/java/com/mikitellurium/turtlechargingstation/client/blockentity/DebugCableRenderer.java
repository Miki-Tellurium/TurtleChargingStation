package com.mikitellurium.turtlechargingstation.client.blockentity;

import com.mikitellurium.turtlechargingstation.common.blockentity.CopperCableBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Colors;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionf;

import java.util.function.Supplier;

public class DebugCableRenderer implements BlockEntityRenderer<CopperCableBlockEntity> {

    private final TextRenderer textRenderer;
    private final Supplier<Quaternionf> cameraOrientation;

    public DebugCableRenderer(BlockEntityRendererFactory.Context context) {
        this.textRenderer = context.getTextRenderer();
        this.cameraOrientation = () -> context.getEntityRenderDispatcher().getRotation();
    }

    @Override
    public void render(CopperCableBlockEntity copperCableBlock, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrixStack.push();

        matrixStack.translate(0.5F, 0.5F, 0.5F);
        matrixStack.multiply(cameraOrientation.get());
        matrixStack.scale(0.02F, -0.02F, 0.02F);

        int opacity = (int) (MinecraftClient.getInstance().options.getTextBackgroundOpacity(0.25f) * 255) << 24;
        String text = String.valueOf(copperCableBlock.getClientNetworkId());
        textRenderer.draw(text, 0, 0, Colors.WHITE, false, matrixStack.peek().getPositionMatrix(), vertexConsumers, TextRenderer.TextLayerType.SEE_THROUGH, opacity, light);

        matrixStack.pop();
    }

}
