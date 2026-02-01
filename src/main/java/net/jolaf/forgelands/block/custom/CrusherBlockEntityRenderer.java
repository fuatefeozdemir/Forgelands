package net.jolaf.forgelands.block.custom;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemStackHandler;

public class CrusherBlockEntityRenderer implements BlockEntityRenderer<CrusherBlockEntity> {

    public CrusherBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(CrusherBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack,
                       MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {

        ItemStackHandler itemHandler = pBlockEntity.getItemHandler();
        ItemStack stack = itemHandler.getStackInSlot(0);

        if (!stack.isEmpty()) {
            Level level = pBlockEntity.getLevel();
            int lightAbove = LevelRenderer.getLightColor(level, pBlockEntity.getBlockPos().above());

            pPoseStack.pushPose();
            pPoseStack.translate(0.5D, 0.72D, 0.5D); // 11px yüksekliğe göre
            pPoseStack.mulPose(Axis.XP.rotationDegrees(90f));
            pPoseStack.scale(0.55f, 0.55f, 0.55f);

            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
            itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, lightAbove, pPackedOverlay, pPoseStack, pBufferSource, level, 0);

            pPoseStack.popPose();
        }
    }
}