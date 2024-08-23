package com.dreu.potionshrines.registry;

import com.dreu.potionshrines.PotionShrines;
import com.dreu.potionshrines.config.PSGeneralConfig;
import com.dreu.potionshrines.levelgen.features.placers.ShrineCaveFeaturePlacer;
import com.dreu.potionshrines.levelgen.features.placers.ShrineSurfaceAltarFeaturePlacer;
import com.google.common.collect.ImmutableList;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.function.Supplier;

public class PSFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, PotionShrines.MODID);

    public static final RegistryObject<Feature<NoneFeatureConfiguration>> SHRINE_SURFACE_ALTAR = FEATURES.register("shrine_surface_altar",
			() -> new ShrineSurfaceAltarFeaturePlacer(NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> SHRINE_CAVE = FEATURES.register("shrine_cave",
			() -> new ShrineCaveFeaturePlacer(NoneFeatureConfiguration.CODEC));

    public static final class Configured {
        public static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES = DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, PotionShrines.MODID);

       public static final RegistryObject<ConfiguredFeature<NoneFeatureConfiguration, ?>> SHRINE_SURFACE_ALTAR = register("shrine_surface_altar", () ->
				new ConfiguredFeature<>(PSFeatures.SHRINE_SURFACE_ALTAR.get(), new NoneFeatureConfiguration()));
		public static final RegistryObject<ConfiguredFeature<NoneFeatureConfiguration, ?>> SHRINE_CAVE = register("shrine_cave", () ->
				new ConfiguredFeature<>(PSFeatures.SHRINE_CAVE.get(), new NoneFeatureConfiguration()));

        private static <FC extends FeatureConfiguration, F extends Feature<FC>> RegistryObject<ConfiguredFeature<FC, ?>> register(String name, Supplier<ConfiguredFeature<FC, F>> feature) {
			return CONFIGURED_FEATURES.register(name, feature);
		}
    }

    public static final class Placed {
        public static final DeferredRegister<PlacedFeature> PLACED_FEATURES = DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, PotionShrines.MODID);

        public static final RegistryObject<PlacedFeature> SHRINE_SURFACE_ALTAR = register("shrine_surface_altar",
				Configured.SHRINE_SURFACE_ALTAR, List.of(
						RarityFilter.onAverageOnceEvery(PSGeneralConfig.SHRINE_RARITY),
						SurfaceWaterDepthFilter.forMaxDepth(63),
						PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
						InSquarePlacement.spread(),
						BiomeFilter.biome()
                        ));

		public static final RegistryObject<PlacedFeature> SHRINE_CAVE = register("shrine_cave",
				Configured.SHRINE_CAVE, List.of(
						RarityFilter.onAverageOnceEvery(1),
						InSquarePlacement.spread()
				));

        		@SuppressWarnings("unchecked")
		private static RegistryObject<PlacedFeature> register(String name, RegistryObject<? extends ConfiguredFeature<?, ?>> feature, List<PlacementModifier> placementModifiers) {
			return PLACED_FEATURES.register(name, () -> new PlacedFeature((Holder<ConfiguredFeature<?, ?>>) feature.getHolder().get(), ImmutableList.copyOf(placementModifiers)));
		}

    }
}
