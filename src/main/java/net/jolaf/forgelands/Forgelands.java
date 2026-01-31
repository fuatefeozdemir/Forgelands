package net.jolaf.forgelands;

import com.mojang.logging.LogUtils;
import net.jolaf.forgelands.block.ModBlockEntities;
import net.jolaf.forgelands.block.ModBlocks;
import net.jolaf.forgelands.block.custom.CrusherBlockEntityRenderer;
import net.jolaf.forgelands.item.ModCreativeModeTabs;
import net.jolaf.forgelands.item.ModItems;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Forgelands.MOD_ID)
public class Forgelands {
    public static final String MOD_ID = "forgelands";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Forgelands() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Kayıtlar
        ModCreativeModeTabs.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addCreative);

        MinecraftForge.EVENT_BUS.register(this);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) { }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        // Tungsten ve Crusher eşyalarını sekmeye ekle
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.accept(ModItems.TUNGSTEN);
            event.accept(ModItems.RAW_TUNGSTEN);
            event.accept(ModBlocks.TUNGSTEN_ORE);
            event.accept(ModBlocks.TUNGSTEN_BLOCK);
            event.accept(ModBlocks.CRUSHER_BLOCK);
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) { }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) { }

        // Renderer'ı sisteme tanıtan kritik metod
        @SubscribeEvent
        public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(ModBlockEntities.CRUSHER_BE.get(), CrusherBlockEntityRenderer::new);
        }
    }
}