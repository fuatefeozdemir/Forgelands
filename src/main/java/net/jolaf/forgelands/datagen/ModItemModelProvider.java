package net.jolaf.forgelands.datagen;

import net.jolaf.forgelands.Forgelands;
import net.jolaf.forgelands.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Forgelands.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ModItems.TUNGSTEN.get());
        basicItem(ModItems.RAW_TUNGSTEN.get());

        basicItem(ModItems.COAL_DUST.get());
        basicItem(ModItems.IRON_DUST.get());
        basicItem(ModItems.COPPER_DUST.get());
        basicItem(ModItems.GOLD_DUST.get());
        basicItem(ModItems.TUNGSTEN_DUST.get());

    }
}
