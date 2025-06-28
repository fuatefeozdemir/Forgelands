package net.jolaf.forgelands.datagen;

import net.jolaf.forgelands.Forgelands;
import net.jolaf.forgelands.block.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Forgelands.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        // Tungsten Ore ve Tungsten Block ile ilgili modelleri kaydet
        blockWithItem(ModBlocks.TUNGSTEN_ORE);
        blockWithItem(ModBlocks.TUNGSTEN_BLOCK);

        // CrusherBlock ile ilgili modeli kaydet
        crusherBlock(ModBlocks.CRUSHER_BLOCK);
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }

    private void crusherBlock(RegistryObject<Block> blockRegistryObject) {
            simpleBlockWithItem(blockRegistryObject.get(), models().withExistingParent("crusher_block", "block/cube_all")
                    .texture("all", "forgelands:block/crusher_block"));
        }
    }

