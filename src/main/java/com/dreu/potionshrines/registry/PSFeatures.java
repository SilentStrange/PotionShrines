package com.dreu.potionshrines.registry;

import com.dreu.potionshrines.PotionShrines;
import com.dreu.potionshrines.levelgen.features.placers.AoEShrineCaveFeaturePlacer;
import com.dreu.potionshrines.levelgen.features.placers.AoEShrineSurfaceAltarFeaturePlacer;
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

import static com.dreu.potionshrines.config.General.*;
import static com.dreu.potionshrines.registry.PSBlocks.*;

public class PSFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, PotionShrines.MODID);

    public static final RegistryObject<Feature<NoneFeatureConfiguration>> SIMPLE_SHRINE_SURFACE_ALTAR = FEATURES.register("simple_shrine_surface_altar", ShrineSurfaceAltarFeaturePlacer::new);
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> AOE_SHRINE_SURFACE_ALTAR = FEATURES.register("aoe_shrine_surface_altar", AoEShrineSurfaceAltarFeaturePlacer::new);
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> SHRINE_CAVE = FEATURES.register("shrine_cave", ShrineCaveFeaturePlacer::new);
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> AOE_SHRINE_CAVE = FEATURES.register("aoe_shrine_cave", AoEShrineCaveFeaturePlacer::new);
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> AURA_SHRINE_SURFACE_ALTAR = FEATURES.register("aura_shrine_surface_altar", AuraShrineSurfaceAltarFeaturePlacer::new);
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> SIMPLE_SHRINE_CAVE = FEATURES.register("simple_shrine_cave", () -> new ShrineCaveFeaturePlacer(SIMPLE_SHRINE_BASE.get(), SIMPLE_SHRINE.get()));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> AOE_SHRINE_CAVE = FEATURES.register("aoe_shrine_cave", () -> new ShrineCaveFeaturePlacer(AOE_SHRINE_BASE.get(), AOE_SHRINE.get()));

    public static final class Configured {
		public static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES = DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, PotionShrines.MODID);

        public static final RegistryObject<ConfiguredFeature<NoneFeatureConfiguration, ?>> SIMPLE_SHRINE_SURFACE_ALTAR = register("simple_shrine_surface_altar", () ->
				new ConfiguredFeature<>(PSFeatures.SIMPLE_SHRINE_SURFACE_ALTAR.get(), new NoneFeatureConfiguration()));
	    public static final RegistryObject<ConfiguredFeature<NoneFeatureConfiguration, ?>> SIMPLE_SHRINE_CAVE = register("simple_shrine_cave", () ->
				new ConfiguredFeature<>(PSFeatures.SIMPLE_SHRINE_CAVE.get(), new NoneFeatureConfiguration()));

	    public static final RegistryObject<ConfiguredFeature<NoneFeatureConfiguration, ?>> AOE_SHRINE_SURFACE_ALTAR = register("aoe_shrine_surface_altar", () ->
				new ConfiguredFeature<>(PSFeatures.AOE_SHRINE_SURFACE_ALTAR.get(), new NoneFeatureConfiguration()));
		public static final RegistryObject<ConfiguredFeature<NoneFeatureConfiguration, ?>> AOE_SHRINE_CAVE = register("aoe_shrine_cave", () ->
				new ConfiguredFeature<>(PSFeatures.AOE_SHRINE_CAVE.get(), new NoneFeatureConfiguration()));

        private static <FC extends FeatureConfiguration, F extends Feature<FC>> RegistryObject<ConfiguredFeature<FC, ?>> register(String name, Supplier<ConfiguredFeature<FC, F>> feature) {
			return CONFIGURED_FEATURES.register(name, feature);
		}
    }

    public static final class Placed {
		public static final DeferredRegister<PlacedFeature> PLACED_FEATURES = DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, PotionShrines.MODID);

        public static final RegistryObject<PlacedFeature> SIMPLE_SHRINE_SURFACE_ALTAR = register("simple_shrine_surface_altar",
				Configured.SIMPLE_SHRINE_SURFACE_ALTAR, List.of(
						RarityFilter.onAverageOnceEvery(SIMPLE_SHRINE_RARITY),
						SurfaceWaterDepthFilter.forMaxDepth(63),
						PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
						InSquarePlacement.spread(),
						BiomeFilter.biome()
                        ));

		public static final RegistryObject<PlacedFeature> SIMPLE_SHRINE_CAVE = register("simple_shrine_cave",
				Configured.SIMPLE_SHRINE_CAVE, List.of(
						RarityFilter.onAverageOnceEvery(SIMPLE_SHRINE_RARITY),
						InSquarePlacement.spread()
				));
		public static final RegistryObject<PlacedFeature> AOE_SHRINE_SURFACE_ALTAR = register("aoe_shrine_surface_altar",
				Configured.AOE_SHRINE_SURFACE_ALTAR, List.of(
						RarityFilter.onAverageOnceEvery(AOE_SHRINE_RARITY),
						SurfaceWaterDepthFilter.forMaxDepth(63),
						PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
						InSquarePlacement.spread(),
						BiomeFilter.biome()
                        ));

		public static final RegistryObject<PlacedFeature> AOE_SHRINE_CAVE = register("aoe_shrine_cave",
				Configured.AOE_SHRINE_CAVE, List.of(
						RarityFilter.onAverageOnceEvery(AOE_SHRINE_RARITY),
						InSquarePlacement.spread()
				));

		@SuppressWarnings({"all"})
		private static RegistryObject<PlacedFeature> register(String name, RegistryObject<? extends ConfiguredFeature<?, ?>> feature, List<PlacementModifier> placementModifiers) {
			return PLACED_FEATURES.register(name, () -> new PlacedFeature((Holder<ConfiguredFeature<?, ?>>) feature.getHolder().get(), ImmutableList.copyOf(placementModifiers)));
		}

    }
}
