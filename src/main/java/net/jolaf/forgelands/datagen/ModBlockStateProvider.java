package net.jolaf.forgelands.datagen;

import net.jolaf.forgelands.Forgelands;
import net.jolaf.forgelands.block.ModBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Forgelands.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(ModBlocks.TUNGSTEN_ORE);
        blockWithItem(ModBlocks.TUNGSTEN_BLOCK);

        crusherBlock(ModBlocks.CRUSHER_BLOCK);
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }

    private void crusherBlock(RegistryObject<Block> blockRegistryObject) {
        BlockModelBuilder crusherModel = models().withExistingParent("crusher_block", "block/block")
                .texture("particle", modLoc("block/crusherblock_side"))
                .texture("down", modLoc("block/crusherblock_side"))
                .texture("up", modLoc("block/crusherblock_top"))
                .texture("side", modLoc("block/crusherblock_side"));

        crusherModel.element()
                .from(0, 0, 0)
                .to(16, 11, 16)
                .shade(false)
                .face(Direction.DOWN).texture("#down").cullface(Direction.DOWN).end()
                .face(Direction.UP).texture("#up").end()
                .face(Direction.NORTH).texture("#side").end()
                .face(Direction.SOUTH).texture("#side").end()
                .face(Direction.EAST).texture("#side").end()
                .face(Direction.WEST).texture("#side").end()
                .end();

        simpleBlockWithItem(blockRegistryObject.get(), crusherModel);
    }
}