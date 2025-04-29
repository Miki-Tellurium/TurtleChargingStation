package com.mikitellurium.turtlechargingstation.client.blockentity;

import com.mikitellurium.turtlechargingstation.common.blockentity.CopperCableBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.util.CommonColors;
import org.joml.Quaternionf;

public class DebugCableRenderer implements BlockEntityRenderer<CopperCableBlockEntity> {

    private final Font font;
    private final EntityRenderDispatcher entityRenderDispatcher;

    public DebugCableRenderer(BlockEntityRendererProvider.Context context) {
        this.font = context.getFont();
        this.entityRenderDispatcher = context.getEntityRenderer();
    }

    @Override
    public void render(CopperCableBlockEntity copperCableBlock, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        poseStack.pushPose();

        poseStack.translate(0.5F, 0.5F, 0.5F);
        poseStack.mulPose(entityRenderDispatcher.cameraOrientation());
        poseStack.scale(0.02F, -0.02F, 0.02F);

        int opacity = (int) (Minecraft.getInstance().options.getBackgroundOpacity(0.25f) * 255) << 24;
        String text = String.valueOf(copperCableBlock.getClientNetworkId());
        font.drawInBatch(text, 0, 0, CommonColors.WHITE, false, poseStack.last().pose(), bufferSource, Font.DisplayMode.SEE_THROUGH, opacity, packedLight);

        poseStack.popPose();
    }

}
