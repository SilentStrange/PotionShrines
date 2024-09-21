package com.dreu.potionshrines.levelgen.features.placers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import static com.dreu.potionshrines.blocks.shrine.simple.SimpleShrineBaseBlock.HALF;

public class ShrineCaveFeaturePlacer extends Feature<NoneFeatureConfiguration> {
    private final Block base;
    private final Block shrine;
    public ShrineCaveFeaturePlacer(Block base, Block shrine) {
        super(NoneFeatureConfiguration.CODEC);
        this.base = base;
        this.shrine = shrine;
    }
    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        if (!context.level().getServer().getWorldData().worldGenSettings().generateStructures()) return false;
        BlockPos origin = context.origin();
        origin = new BlockPos(origin.getX(), -59, origin.getZ());
        while (!(context.level().getBlockState(origin).getMaterial().isReplaceable() && context.level().getBlockState(origin.below()).getMaterial().isSolid())){
            if (origin.getY() > context.level().getHeightmapPos(Heightmap.Types.OCEAN_FLOOR_WG, origin).getY() - 5) return false;
            origin = origin.above();
        }
        if (canPlaceBlock(context.level(), origin)
                && canPlaceBlock(context.level(), origin.above(1))
                && canPlaceBlock(context.level(), origin.above(2))
                && context.level().setBlock(origin.above(2), shrine.defaultBlockState(), 11)) {

            context.level().setBlock(origin.above(1), base.defaultBlockState().setValue(HALF, Half.TOP), 11);
            context.level().setBlock(origin, base.defaultBlockState().setValue(HALF, Half.BOTTOM), 11);
        }
        return false;
    }

    public boolean canPlaceBlock(WorldGenLevel level, BlockPos blockPos){
        return level.getBlockState(blockPos).getMaterial().isReplaceable() && level.getFluidState(blockPos).isEmpty();
    }
}
