package net.jolaf.forgelands.item;

import net.jolaf.forgelands.Forgelands;
import net.jolaf.forgelands.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Forgelands.MOD_ID);

    public static final RegistryObject<CreativeModeTab> ALEXANDRITE_ITEMS_TAB = CREATIVE_MODE_TABS.register("forgelands_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.TUNGSTEN.get()))
                    .title(Component.translatable("creativetab.forgelands.Forgelands"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.TUNGSTEN.get());
                        output.accept(ModItems.RAW_TUNGSTEN.get());

                        output.accept(ModItems.COAL_DUST.get());
                        output.accept(ModItems.IRON_DUST.get());
                        output.accept(ModItems.COPPER_DUST.get());
                        output.accept(ModItems.GOLD_DUST.get());
                        output.accept(ModItems.TUNGSTEN_DUST.get());

                        output.accept(ModBlocks.TUNGSTEN_ORE.get());
                        output.accept(ModBlocks.TUNGSTEN_BLOCK.get());
                        output.accept(ModBlocks.CRUSHER_BLOCK.get());

                    }).build());


    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}