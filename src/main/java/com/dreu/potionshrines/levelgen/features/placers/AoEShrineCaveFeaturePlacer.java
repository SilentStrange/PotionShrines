package com.dreu.potionshrines.levelgen.features.placers;

import com.dreu.potionshrines.registry.PSBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import static com.dreu.potionshrines.blocks.shrine.simple.ShrineBaseBlock.HALF;

public class AoEShrineCaveFeaturePlacer extends Feature<NoneFeatureConfiguration> {
    public AoEShrineCaveFeaturePlacer() {super(NoneFeatureConfiguration.CODEC);}
    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        BlockPos origin = context.origin();
        origin = new BlockPos(origin.getX(), -59, origin.getZ());
        while (!(context.level().getBlockState(origin).getMaterial().isReplaceable() && context.level().getBlockState(origin.below()).getMaterial().isSolid())){
            if (origin.getY() > context.level().getHeightmapPos(Heightmap.Types.OCEAN_FLOOR_WG, origin).getY() - 5) return false;
            origin = origin.above();
        }
        if (canPlaceBlock(context.level(), origin)
                && canPlaceBlock(context.level(), origin.above(1))
                && canPlaceBlock(context.level(), origin.above(2))
                && context.level().setBlock(origin.above(2), PSBlocks.AOE_SHRINE.get().defaultBlockState(), 11)) {

            context.level().setBlock(origin.above(1), PSBlocks.AOE_SHRINE_BASE.get().defaultBlockState().setValue(HALF, Half.TOP), 11);
            context.level().setBlock(origin, PSBlocks.AOE_SHRINE_BASE.get().defaultBlockState().setValue(HALF, Half.BOTTOM), 11);
        }
        return false;
    }

    public boolean canPlaceBlock(WorldGenLevel level, BlockPos blockPos){
        return level.getBlockState(blockPos).getMaterial().isReplaceable();
    }
}
