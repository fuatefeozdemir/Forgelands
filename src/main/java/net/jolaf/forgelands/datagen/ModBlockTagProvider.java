package net.jolaf.forgelands.datagen;

import net.jolaf.forgelands.Forgelands;
import net.jolaf.forgelands.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Forgelands.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.TUNGSTEN_ORE.get())
                .add(ModBlocks.TUNGSTEN_BLOCK.get())
                .add(ModBlocks.CRUSHER_BLOCK.get());

        tag(BlockTags.NEEDS_IRON_TOOL)
                .add(ModBlocks.TUNGSTEN_ORE.get())
                .add(ModBlocks.TUNGSTEN_BLOCK.get())
                .add(ModBlocks.CRUSHER_BLOCK.get());
    }
}
