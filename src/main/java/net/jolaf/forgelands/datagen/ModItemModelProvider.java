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
        basicItem(ModItems.ORICHALCUM.get());
        basicItem(ModItems.RAW_ORICHALCUM.get());
    }
}
