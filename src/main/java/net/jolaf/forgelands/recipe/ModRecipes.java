package net.jolaf.forgelands.recipe;

import net.jolaf.forgelands.Forgelands;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Forgelands.MOD_ID);
    public static final DeferredRegister<RecipeType<?>> TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, Forgelands.MOD_ID);

    public static final RegistryObject<RecipeType<CrushingRecipe>> CRUSHING_TYPE =
            TYPES.register("crushing", () -> new RecipeType<CrushingRecipe>() {
                @Override
                public String toString() {
                    return Forgelands.MOD_ID + ":crushing";
                }
            });

    public static final RegistryObject<RecipeSerializer<CrushingRecipe>> CRUSHING_SERIALIZER =
            SERIALIZERS.register("crushing", CrushingRecipe.Serializer::new);

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
        TYPES.register(eventBus);
    }
}