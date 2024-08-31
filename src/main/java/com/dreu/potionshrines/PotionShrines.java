package com.dreu.potionshrines;

import com.dreu.potionshrines.blocks.aoe.AoEShrineRenderer;
import com.dreu.potionshrines.blocks.shrine.ShrineRenderer;
import com.dreu.potionshrines.registry.*;
import com.dreu.potionshrines.screen.AoEShrineScreen;
import com.mojang.logging.LogUtils;
import com.mojang.math.Transformation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.SimpleModelState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.List;

import static com.dreu.potionshrines.config.AoEShrine.AOE_SHRINES;
import static com.dreu.potionshrines.config.AoEShrine.TOTAL_WEIGHT_AOE;
import static com.dreu.potionshrines.config.Shrine.SHRINES;
import static com.dreu.potionshrines.config.Shrine.TOTAL_WEIGHT;

@Mod(PotionShrines.MODID)
public class PotionShrines {
    public static final String MODID = "potion_shrines";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final Random rand = new Random();
    public static final Map<String, BakedModel> BAKED_ICONS = new HashMap<>();
    public static final Set<String> SHRINE_ICONS = new HashSet<>();
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

        eventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            MenuScreens.register(PSMenuTypes.AOE_SHRINE_MENU.get(), AoEShrineScreen::new);
            BlockEntityRenderers.register(PSBlockEntities.SHRINE.get(), (c) -> new ShrineRenderer());
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
        if (new File("resourcepacks/Potion Shrine Icons").mkdirs()) {
            try {
                FileWriter packMcMeta = new FileWriter("resourcepacks/Potion Shrine Icons/pack.mcmeta");
                packMcMeta.write("""
                        {
                          "pack": {
                            "pack_format": 9,
                            "description": "Example resource pack for custom Potion Shrine icons"
                          }
                        }""");
                packMcMeta.close();
            } catch (Exception ignored) {}
            try {
                FileWriter readMe = new FileWriter("resourcepacks/Potion Shrine Icons/README.txt");
                readMe.write("""
                        This pack is structured as follows:
                        [Potion Shrine Icons]
                           -[assets]
                              -[potion_shrines]
                                 -[textures]
                                 -[models]
                           - pack.mcmeta

                        Follow these steps to add a custom icon that can then be used* for shrines in the config:
                        Step 1: In the "models" folder, create a json file with the same name as the icon png (i.e. - "example.png" and "example.json") [MUST BE SAME NAME]
                            the json file should look like this:
                                {
                                   "parent":  "item/generated",
                                   "textures": {
                                      "layer0": "potion_shrines:example"
                                   }
                                }
                                
                        Step 2: In the "textures" folder put the desired png for the custom icon (nothing more than 64x64 is recommended)

                        It should look like this when you're done:
                        [Potion Shrine Icons]
                           -[assets]
                              -[potion_shrines]
                                 -[models]
                                    - example.json
                                 -[textures]
                                    - example.png
                           - pack.mcmeta

                        *In order for these to be usable the game MUST be restarted.
                        """);
                readMe.close();
            } catch (Exception ignored) {}
            if (new File("resourcepacks/Potion Shrine Icons/assets/potion_shrines/models").mkdirs()) {
                try {
                    FileWriter exampleModelJson = new FileWriter("resourcepacks/Potion Shrine Icons/assets/potion_shrines/models/example.json");
                    exampleModelJson.write("""
                            {
                              "parent":  "item/generated",
                              "textures": {
                                "layer0": "potion_shrines:example"
                              }
                            }""");
                    exampleModelJson.close();
                } catch (Exception ignored) {
                }
            }
            if (new File("resourcepacks/Potion Shrine Icons/assets/potion_shrines/textures").mkdirs()){
                BufferedImage image = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = image.createGraphics();

                // Fill the entire image with transparent pixels
                g2d.setColor(new Color(0, 0, 0, 0));
                g2d.fillRect(0, 0, 16, 16);

                g2d.setColor(new Color(255, 233, 39, 255));
                g2d.fillRect(8, 2, 1, 1);
                g2d.fillRect(9, 2, 1, 1);
                g2d.fillRect(8, 4, 1, 1);
                g2d.fillRect(9, 4, 1, 1);
                g2d.fillRect(8, 5, 1, 1);
                g2d.fillRect(9, 5, 1, 1);
                g2d.fillRect(8, 7, 1, 1);
                g2d.fillRect(9, 7, 1, 1);
                g2d.fillRect(2, 8, 1, 1);
                g2d.fillRect(3, 8, 1, 1);
                g2d.fillRect(4, 8, 1, 1);
                g2d.fillRect(5, 8, 1, 1);
                g2d.fillRect(7, 8, 1, 1);
                g2d.fillRect(8, 8, 1, 1);
                g2d.fillRect(9, 8, 1, 1);
                g2d.fillRect(10, 8, 1, 1);
                g2d.fillRect(12, 8, 1, 1);
                g2d.fillRect(13, 8, 1, 1);
                g2d.fillRect(14, 8, 1, 1);
                g2d.fillRect(15, 8, 1, 1);
                g2d.fillRect(2, 9, 1, 1);
                g2d.fillRect(3, 9, 1, 1);
                g2d.fillRect(4, 9, 1, 1);
                g2d.fillRect(5, 9, 1, 1);
                g2d.fillRect(7, 9, 1, 1);
                g2d.fillRect(8, 9, 1, 1);
                g2d.fillRect(9, 9, 1, 1);
                g2d.fillRect(10, 9, 1, 1);
                g2d.fillRect(12, 9, 1, 1);
                g2d.fillRect(13, 9, 1, 1);
                g2d.fillRect(14, 9, 1, 1);
                g2d.fillRect(15, 9, 1, 1);
                g2d.fillRect(8, 10, 1, 1);
                g2d.fillRect(9, 10, 1, 1);
                g2d.fillRect(8, 12, 1, 1);
                g2d.fillRect(9, 12, 1, 1);
                g2d.fillRect(8, 13, 1, 1);
                g2d.fillRect(9, 13, 1, 1);
                g2d.fillRect(8, 15, 1, 1);
                g2d.fillRect(9, 15, 1, 1);

                g2d.setColor(new Color(201, 202, 169, 255));
                g2d.fillRect(10, 6, 1, 1);
                g2d.fillRect(14, 6, 1, 1);
                g2d.fillRect(3, 7, 1, 1);
                g2d.fillRect(6, 7, 1, 1);
                g2d.fillRect(6, 3, 1, 1);
                g2d.fillRect(7, 3, 1, 1);
                g2d.fillRect(8, 3, 1, 1);
                g2d.fillRect(9, 3, 1, 1);
                g2d.fillRect(10, 3, 1, 1);
                g2d.fillRect(11, 3, 1, 1);
                g2d.fillRect(5, 4, 1, 1);
                g2d.fillRect(12, 4, 1, 1);
                g2d.fillRect(4, 5, 1, 1);
                g2d.fillRect(13, 5, 1, 1);
                g2d.fillRect(3, 6, 1, 1);
                g2d.fillRect(7, 6, 1, 1);
                g2d.fillRect(11, 7, 1, 1);
                g2d.fillRect(14, 7, 1, 1);
                g2d.fillRect(6, 8, 1, 1);
                g2d.fillRect(11, 8, 1, 1);
                g2d.fillRect(6, 9, 1, 1);
                g2d.fillRect(11, 9, 1, 1);
                g2d.fillRect(3, 10, 1, 1);
                g2d.fillRect(6, 10, 1, 1);
                g2d.fillRect(11, 10, 1, 1);
                g2d.fillRect(14, 10, 1, 1);
                g2d.fillRect(3, 11, 1, 1);
                g2d.fillRect(7, 11, 1, 1);
                g2d.fillRect(10, 11, 1, 1);
                g2d.fillRect(14, 11, 1, 1);
                g2d.fillRect(4, 12, 1, 1);
                g2d.fillRect(13, 12, 1, 1);
                g2d.fillRect(5, 13, 1, 1);
                g2d.fillRect(12, 13, 1, 1);
                g2d.fillRect(6, 14, 1, 1);
                g2d.fillRect(7, 14, 1, 1);
                g2d.fillRect(8, 14, 1, 1);
                g2d.fillRect(9, 14, 1, 1);
                g2d.fillRect(10, 14, 1, 1);
                g2d.fillRect(11, 14, 1, 1);

                g2d.setColor(new Color(235, 213, 19, 255));
                g2d.fillRect(8, 6, 1, 1);
                g2d.fillRect(9, 6, 1, 1);
                g2d.fillRect(8, 11, 1, 1);
                g2d.fillRect(9, 11, 1, 1);

                g2d.dispose();

                try {
                    File outputFile = new File("resourcepacks/Potion Shrine Icons/assets/potion_shrines/textures/example.png");
                    ImageIO.write(image, "png", outputFile);
                } catch (IOException e) {
                    System.err.println("Error saving image: " + e.getMessage());
                }
            }
        }
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
