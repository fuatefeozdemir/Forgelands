package net.jolaf.forgelands.block.custom;

import com.mojang.serialization.MapCodec;
import net.jolaf.forgelands.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

public class CrusherBlock extends BaseEntityBlock {
    public static final MapCodec<CrusherBlock> CODEC = simpleCodec(CrusherBlock::new);

    // DEPOT MODELİ İÇİN BOYUT: 13 piksel yükseklik (16 üzerinden)
    // Bu, bloğun üstündeki siyah boşlukları engeller ve hitbox'ı küçültür.
    private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 11, 16);

    public CrusherBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CrusherBlockEntity(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    // Şeffaf kısımların ışıklandırmasını düzeltir
    @Override
    public float getShadeBrightness(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return 1.0F;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return true;
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!level.isClientSide && entity instanceof ItemEntity itemEntity) {
            if (level.getBlockEntity(pos) instanceof CrusherBlockEntity crusherBE) {
                ItemStackHandler inventory = crusherBE.getItemHandler();
                ItemStack stackInEntity = itemEntity.getItem();

                if (inventory.getStackInSlot(0).isEmpty() && stackInEntity.is(Items.RAW_IRON)) {
                    inventory.setStackInSlot(0, stackInEntity.split(1));
                    if (stackInEntity.isEmpty()) {
                        itemEntity.discard();
                    }
                    level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1f, 1f);
                }
            }
        }
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack heldStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide()) return ItemInteractionResult.SUCCESS;

        if (level.getBlockEntity(pos) instanceof CrusherBlockEntity crusherBE) {
            ItemStackHandler inventory = crusherBE.getItemHandler();

            if (heldStack.getItem() instanceof PickaxeItem) {
                return handleCrushing(level, pos, inventory);
            }

            return handleItemInteraction(player, hand, heldStack, inventory);
        }

        return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
    }

    private ItemInteractionResult handleCrushing(Level level, BlockPos pos, ItemStackHandler inventory) {
        ItemStack inBlock = inventory.getStackInSlot(0);

        if (inBlock.is(Items.RAW_IRON)) {
            inventory.setStackInSlot(0, new ItemStack(ModItems.IRON_DUST.get()));
            level.playSound(null, pos, SoundEvents.ANVIL_USE, SoundSource.BLOCKS, 1f, 1f);
            return ItemInteractionResult.SUCCESS;
        }

        return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
    }

    private ItemInteractionResult handleItemInteraction(Player player, InteractionHand hand, ItemStack heldStack, ItemStackHandler inventory) {
        ItemStack inBlock = inventory.getStackInSlot(0);

        if (inBlock.isEmpty() && !heldStack.isEmpty()) {
            inventory.setStackInSlot(0, heldStack.split(1));
            return ItemInteractionResult.SUCCESS;
        }

        if (!inBlock.isEmpty() && heldStack.isEmpty()) {
            ItemStack extracted = inventory.extractItem(0, 64, false);
            player.setItemInHand(hand, extracted);
            return ItemInteractionResult.SUCCESS;
        }

        return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof CrusherBlockEntity crusherBE) {
                ItemStack stack = crusherBE.getItemHandler().getStackInSlot(0);
                if (!stack.isEmpty()) {
                    Block.popResource(level, pos, stack);
                }
            }
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }
}