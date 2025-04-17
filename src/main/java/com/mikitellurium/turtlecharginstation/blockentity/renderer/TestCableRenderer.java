package com.mikitellurium.turtlecharginstation.blockentity.renderer;

import com.mikitellurium.turtlecharginstation.blockentity.CopperCableBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

import java.util.function.Supplier;

public class TestCableRenderer implements BlockEntityRenderer<CopperCableBlockEntity> {

    private final Font font;
    private final Supplier<Quaternionf> cameraOrientation;

    public TestCableRenderer(BlockEntityRendererProvider.Context context) {
        this.font = context.getFont();
        this.cameraOrientation = () -> context.getEntityRenderer().cameraOrientation();
    }

    @Override
    public void render(CopperCableBlockEntity copperCableBlock, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        poseStack.pushPose();

        poseStack.translate(0.5F, 0.5F, 0.5F);
        Quaternionf quaternionf = cameraOrientation.get();
        poseStack.mulPose(quaternionf);
        poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
        poseStack.scale(0.02F, 0.02F, 0.02F);

        float backgroundOpacity = Minecraft.getInstance().options.getBackgroundOpacity(0.25F);
        int backgroundColor = (int) (backgroundOpacity * 255.0F) << 24;
        String text = String.valueOf(copperCableBlock.getClientNetworkId());
        font.drawInBatch(text, 0, 0, -1, false, poseStack.last().pose(), bufferSource, Font.DisplayMode.SEE_THROUGH, backgroundColor, packedLight);

        poseStack.popPose();
    }

    @Override
    public boolean shouldRender(CopperCableBlockEntity pBlockEntity, Vec3 pCameraPos) {
        return BlockEntityRenderer.super.shouldRender(pBlockEntity, pCameraPos);
    }

}
