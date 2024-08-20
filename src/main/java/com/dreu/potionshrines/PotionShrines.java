package com.dreu.potionshrines;

import com.dreu.potionshrines.blocks.ShrineBlockEntity;
import com.dreu.potionshrines.blocks.ShrineRenderer;
import com.dreu.potionshrines.registry.PSBlockEntities;
import com.dreu.potionshrines.registry.PSBlocks;
import com.dreu.potionshrines.registry.PSItems;
import com.mojang.logging.LogUtils;
import com.mojang.math.Transformation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.SimpleModelState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.dreu.potionshrines.config.PSShrineConfig.SHRINE_ICONS;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(PotionShrines.MODID)
public class PotionShrines {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "potion_shrines";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final Random rand = new Random();
    public static final Map<String, BakedModel> BAKED_ICONS = new HashMap<>();

    public PotionShrines()
    {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(this::commonSetup);
        PSBlocks.BLOCKS.register(eventBus);
        PSBlockEntities.BLOCK_ENTITIES.register(eventBus);
        PSItems.ITEMS.register(eventBus);
        MinecraftForge.EVENT_BUS.register(this);

    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            BlockEntityRenderers.register(PSBlockEntities.SHRINE.get(), ShrineRenderer::new);
        }
        @SubscribeEvent
        public static void registerModels(ModelEvent.RegisterAdditional event){
            for (String icon : SHRINE_ICONS) {
                event.register(new ResourceLocation("potion_shrines", "shrine/" + icon));
            }
        }
        @SubscribeEvent
        public static void bakeModels(ModelEvent.BakingCompleted event){
            for (String icon : SHRINE_ICONS) {
                ResourceLocation iconLocation = new ResourceLocation("potion_shrines", "shrine/" + icon);
                BAKED_ICONS.put(icon, Minecraft.getInstance().getModelManager().getModelBakery().getModel(iconLocation).bake(
                        Minecraft.getInstance().getModelManager().getModelBakery(),
                        (material) -> Minecraft.getInstance().getTextureAtlas(material.atlasLocation()).apply(material.texture()),
                        new SimpleModelState(Transformation.identity()),
                        iconLocation
                ));
            }
        }

        @SubscribeEvent
        public static void onTextureStitch(TextureStitchEvent.Pre event) {
            if (event.getAtlas().location().equals(TextureAtlas.LOCATION_BLOCKS)) {
                for (String icon : SHRINE_ICONS) {
                    event.addSprite(new ResourceLocation("potion_shrines", "shrine/" + icon));
                }
            }
        }
    }

    public static MobEffect getEffectFromString(String effect){
        return ForgeRegistries.MOB_EFFECTS.getDelegateOrThrow(new ResourceLocation(effect)).get();
    }
}
