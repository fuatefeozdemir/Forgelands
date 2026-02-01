package net.jolaf.forgelands.block.custom;

import com.mojang.serialization.MapCodec;
import net.jolaf.forgelands.recipe.CrushingRecipe;
import net.jolaf.forgelands.recipe.CrushingRecipeInput;
import net.jolaf.forgelands.recipe.ModRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.crafting.RecipeHolder;
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

import java.util.Optional;

public class CrusherBlock extends BaseEntityBlock {
    public static final MapCodec<CrusherBlock> CODEC = simpleCodec(CrusherBlock::new);
    private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 11, 16);

    public CrusherBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() { return CODEC; }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) { return new CrusherBlockEntity(pos, state); }

    @Override
    public RenderShape getRenderShape(BlockState state) { return RenderShape.MODEL; }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack heldStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide()) return ItemInteractionResult.SUCCESS;

        if (level.getBlockEntity(pos) instanceof CrusherBlockEntity crusherBE) {
            ItemStackHandler inventory = crusherBE.getItemHandler();
            ItemInteractionResult result;

            if (heldStack.getItem() instanceof PickaxeItem) {
                result = handleCrushing(level, pos, inventory);
            } else {
                result = handleItemInteraction(player, hand, heldStack, inventory);
            }

            crusherBE.setChanged();
            level.sendBlockUpdated(pos, state, state, 3);
            return result;
        }
        return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
    }

    private ItemInteractionResult handleCrushing(Level level, BlockPos pos, ItemStackHandler inventory) {
        ItemStack inBlock = inventory.getStackInSlot(0);

        System.out.println("[CRUSHER DEBUG] Bloğun içindeki eşya: " + inBlock.getItem().toString());

        if (inBlock.isEmpty()) {
            System.out.println("[CRUSHER DEBUG] İşlem iptal: Slot boş.");
            return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
        }

        // --- KRİTİK DEBUG: Bellekteki tarifleri listele ---
        System.out.println("[CRUSHER DEBUG] Kayıtlı tarifler taranıyor...");
        level.getRecipeManager().getRecipes().forEach(r -> {
            if (r.id().toString().contains("forgelands")) {
                System.out.println("[CRUSHER DEBUG] Bellekte Bulunan Mod Tarifi: " + r.id());
            }
        });

        CrushingRecipeInput recipeInput = new CrushingRecipeInput(inBlock);
        System.out.println("[CRUSHER DEBUG] Aranan Tarif Tipi: " + ModRecipes.CRUSHING_TYPE.getId());

        Optional<RecipeHolder<CrushingRecipe>> recipe = level.getRecipeManager()
                .getRecipeFor(ModRecipes.CRUSHING_TYPE.get(), recipeInput, level);

        if (recipe.isPresent()) {
            System.out.println("[CRUSHER DEBUG] Tarif BAŞARIYLA BULUNDU: " + recipe.get().id());

            if (level instanceof ServerLevel serverLevel) {
                ItemStack resultStack = recipe.get().value().assemble(recipeInput, level.registryAccess());
                System.out.println("[CRUSHER DEBUG] Üretilen Eşya: " + resultStack.toString());

                inventory.setStackInSlot(0, resultStack);

                serverLevel.sendParticles(ParticleTypes.SMOKE, pos.getX() + 0.5, pos.getY() + 0.8, pos.getZ() + 0.5, 15, 0.2, 0.1, 0.2, 0.05);
                level.playSound(null, pos, SoundEvents.ANVIL_USE, SoundSource.BLOCKS, 1f, 1f);
            }
            return ItemInteractionResult.SUCCESS;
        }

        System.out.println("[CRUSHER DEBUG] HATA: Uygun bir JSON tarifi bulunamadı!");
        return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
    }

    private ItemInteractionResult handleItemInteraction(Player player, InteractionHand hand, ItemStack heldStack, ItemStackHandler inventory) {
        ItemStack inBlock = inventory.getStackInSlot(0);

        if (inBlock.isEmpty() && !heldStack.isEmpty()) {
            inventory.setStackInSlot(0, heldStack.split(1));
            return ItemInteractionResult.SUCCESS;
        }

        if (!inBlock.isEmpty()) {
            ItemStack extracted = inventory.extractItem(0, 64, false);
            if (heldStack.isEmpty()) {
                player.setItemInHand(hand, extracted);
            } else {
                if (!player.getInventory().add(extracted)) player.drop(extracted, false);
                inventory.setStackInSlot(0, heldStack.split(1));
            }
            return ItemInteractionResult.SUCCESS;
        }
        return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
    }
}