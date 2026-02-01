package net.jolaf.forgelands.datagen;

import net.jolaf.forgelands.Forgelands;
import net.jolaf.forgelands.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends ItemTagsProvider {
    public ModItemTagProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> completableFuture,
                              CompletableFuture<TagLookup<Block>> lookupCompletableFuture, @Nullable ExistingFileHelper existingFileHelper) {
        super(packOutput, completableFuture, lookupCompletableFuture, Forgelands.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        // --- STANDART MADEN ETİKETLERİ ---

        // Demir, Altın ve Bakır'ı global "c:ingots/..." etiketlerine ekliyoruz
        this.tag(commonTag("ingots/iron")).add(Items.IRON_INGOT);
        this.tag(commonTag("ingots/gold")).add(Items.GOLD_INGOT);
        this.tag(commonTag("ingots/copper")).add(Items.COPPER_INGOT);

        // --- ÖZEL MADEN ETİKETLERİ (TUNGSTEN) ---

        // Tungsten Ingot'u hem kendi etiketine hem de genel külçe etiketine ekliyoruz
        this.tag(commonTag("ingots/tungsten")).add(ModItems.TUNGSTEN_INGOT.get());
        this.tag(commonTag("ingots")).add(ModItems.TUNGSTEN_INGOT.get());

        // --- KÖMÜR ETİKETLERİ ---

        // Kömürü "c:coals" etiketi altına alıyoruz (JSON'da bunu kullanabilirsin)
        this.tag(commonTag("coals")).add(Items.COAL).add(Items.CHARCOAL);
    }

    // Yardımcı Metot: "c" (Common) namespace'i kullanarak etiket oluşturur.
    // Bu sayede JSON dosyasında "tag": "c:ingots/iron" yazdığında Minecraft bu kodu tanır.
    private TagKey<Item> commonTag(String name) {
        return ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", name));
    }
}