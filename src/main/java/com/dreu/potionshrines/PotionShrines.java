package com.dreu.potionshrines;

import com.dreu.potionshrines.blocks.shrine.aoe.AoEShrineRenderer;
import com.dreu.potionshrines.blocks.shrine.simple.ShrineRenderer;
import com.dreu.potionshrines.config.ExampleResourcePack;
import com.dreu.potionshrines.network.PacketHandler;
import com.dreu.potionshrines.registry.*;
import com.dreu.potionshrines.screen.aoe.AoEShrineScreen;
import com.dreu.potionshrines.screen.IconSelectionScreen;
import com.dreu.potionshrines.screen.simple.SimpleShrineScreen;
import com.mojang.logging.LogUtils;
import com.mojang.math.Transformation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.SimpleModelState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

import java.util.*;

import static com.dreu.potionshrines.config.AoEShrine.AOE_SHRINES;
import static com.dreu.potionshrines.config.AoEShrine.TOTAL_WEIGHT_AOE;
import static com.dreu.potionshrines.config.SimpleShrine.SHRINES;
import static com.dreu.potionshrines.config.SimpleShrine.TOTAL_WEIGHT;

@Mod(PotionShrines.MODID)
public class PotionShrines {
    public static final String MODID = "potion_shrines";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final Random rand = new Random();
    public static final Map<String, BakedModel> BAKED_ICONS = new HashMap<>();
    public static final Set<String> SHRINE_ICONS = new HashSet<>();
    public static final int EDIT_BOX_HEIGHT = 18;
    static {
        SHRINES.forEach((shrine) -> {
            TOTAL_WEIGHT += shrine.getInt("Weight");
            SHRINE_ICONS.add(shrine.get("Icon"));
        });
        AOE_SHRINES.forEach((shrine) -> {
            TOTAL_WEIGHT_AOE += shrine.getInt("Weight");
            SHRINE_ICONS.add(shrine.get("Icon"));
        });
    }

    public PotionShrines() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        PSItems.ITEMS.register(eventBus);
        PSBlocks.BLOCKS.register(eventBus);
        PSBlockEntities.BLOCK_ENTITIES.register(eventBus);
        PSFeatures.FEATURES.register(eventBus);
        PSFeatures.Configured.CONFIGURED_FEATURES.register(eventBus);
        PSFeatures.Placed.PLACED_FEATURES.register(eventBus);
        PSMenuTypes.MENUS.register(eventBus);
        PacketHandler.register();

        eventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            MenuScreens.register(PSMenuTypes.AOE_SHRINE_MENU.get(), AoEShrineScreen::new);
            MenuScreens.register(PSMenuTypes.ICON_SELECTION_MENU.get(), IconSelectionScreen::new);
            MenuScreens.register(PSMenuTypes.SIMPLE_SHRINE_MENU.get(), SimpleShrineScreen::new);
            BlockEntityRenderers.register(PSBlockEntities.SIMPLE_SHRINE.get(), (c) -> new ShrineRenderer());
            BlockEntityRenderers.register(PSBlockEntities.AOE_SHRINE.get(), (c) -> new AoEShrineRenderer());
        }
        @SubscribeEvent
        public static void registerModels(ModelEvent.RegisterAdditional event){
            for (String icon : SHRINE_ICONS) {
                event.register(new ResourceLocation("potion_shrines", icon));
            }
            event.register(new ResourceLocation("potion_shrines", "default"));
        }
        @SubscribeEvent
        public static void bakeModels(ModelEvent.BakingCompleted event){
            for (String icon : SHRINE_ICONS) {
                ResourceLocation iconLocation = new ResourceLocation("potion_shrines", icon);
                BAKED_ICONS.put(icon, Minecraft.getInstance().getModelManager().getModelBakery().getModel(iconLocation).bake(
                        Minecraft.getInstance().getModelManager().getModelBakery(),
                        (material) -> Minecraft.getInstance().getTextureAtlas(material.atlasLocation()).apply(material.texture()),
                        new SimpleModelState(Transformation.identity()),
                        iconLocation
                ));
            }
            ResourceLocation iconLocation = new ResourceLocation("potion_shrines", "default");
            BAKED_ICONS.put("default", Minecraft.getInstance().getModelManager().getModelBakery().getModel(iconLocation).bake(
                        Minecraft.getInstance().getModelManager().getModelBakery(),
                        (material) -> Minecraft.getInstance().getTextureAtlas(material.atlasLocation()).apply(material.texture()),
                        new SimpleModelState(Transformation.identity()),
                        iconLocation
            ));
        }

        @SubscribeEvent
        public static void onTextureStitch(TextureStitchEvent.Pre event) {
            if (event.getAtlas().location().equals(TextureAtlas.LOCATION_BLOCKS)) {
                for (String icon : SHRINE_ICONS) {
                    event.addSprite(new ResourceLocation("potion_shrines", icon));
                }
                event.addSprite(new ResourceLocation("potion_shrines", "default"));
            }
        }
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        ExampleResourcePack.generate();
    }

    @Mod.EventBusSubscriber(modid = PotionShrines.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public class ForgeEvents {

    @SubscribeEvent
    public static void onBiomeLoading(Event event) {
        // Your logic to modify biomes goes here
    }
}
    public static MobEffect getEffectFromString(String effect){
        return ForgeRegistries.MOB_EFFECTS.getDelegateOrThrow(new ResourceLocation(effect)).get();
    }

    public static final List<String> romanNumerals = new ArrayList<>();
    static {
        romanNumerals.add("");
        romanNumerals.add("");
        for (int i = 2; i <= 255; i++) {
            romanNumerals.add(toRoman(i));
        }
    }

    // Method to convert an integer to Roman numeral
    public static String toRoman(int number) {
        String[] thousands = {"", "M"};
        String[] hundreds = {"", "C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM"};
        String[] tens = {"", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC"};
        String[] ones = {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX"};

        return thousands[number / 1000] +
               hundreds[(number % 1000) / 100] +
               tens[(number % 100) / 10] +
               ones[number % 10] + " ";
    }
    public static String asTime(int seconds) {
        return String.format("%d:%02d", seconds / 60, seconds % 60);
    }

}
