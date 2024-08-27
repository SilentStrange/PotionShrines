package com.dreu.potionshrines.levelgen.features.placers;

import com.dreu.potionshrines.registry.PSBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import static com.dreu.potionshrines.blocks.shrine.ShrineBaseBlock.HALF;
import static net.minecraft.world.level.block.StairBlock.FACING;
import static net.minecraft.world.level.block.StairBlock.SHAPE;

public class AoEShrineSurfaceAltarFeaturePlacer extends Feature<NoneFeatureConfiguration> {
    public AoEShrineSurfaceAltarFeaturePlacer() {super(NoneFeatureConfiguration.CODEC);}
    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        for (int i = -1; i < 2; i++){
            for (int j = -1; j < 2; j++){
                if (!context.level().getBlockState(context.origin().below().north(i).east(j)).getMaterial().isSolid())
                    return false;
            }
        }
        if (canPlaceBlock(context.level(), context.origin())
                && canPlaceBlock(context.level(), context.origin().above(1))
                && canPlaceBlock(context.level(), context.origin().above(2))
                && canPlaceBlock(context.level(), context.origin().above(3))
                && context.level().setBlock(context.origin().above(3), PSBlocks.AOE_SHRINE.get().defaultBlockState(), 11)) {

            context.level().setBlock(context.origin().above(2), PSBlocks.AOE_SHRINE_BASE.get().defaultBlockState().setValue(HALF, Half.TOP), 11);
            context.level().setBlock(context.origin().above(), PSBlocks.AOE_SHRINE_BASE.get().defaultBlockState().setValue(HALF, Half.BOTTOM), 11);

            if (canPlaceBlock(context.level(), context.origin()))
                context.level().setBlock(context.origin(), Blocks.DEEPSLATE_BRICKS.defaultBlockState(), 11);

            if (canPlaceBlock(context.level(), context.origin().west().north()))
                context.level().setBlock(context.origin().west().north(),
                        Blocks.DEEPSLATE_BRICK_STAIRS.defaultBlockState()
                                .setValue(FACING, Direction.EAST)
                                .setValue(HALF, Half.BOTTOM)
                                .setValue(SHAPE, StairsShape.OUTER_RIGHT), 11);
            if (canPlaceBlock(context.level(), context.origin().north()))
                context.level().setBlock(context.origin().north(),
                        Blocks.DEEPSLATE_BRICK_STAIRS.defaultBlockState()
                                .setValue(FACING, Direction.SOUTH)
                                .setValue(HALF, Half.BOTTOM)
                                .setValue(SHAPE, StairsShape.STRAIGHT), 11);
            if (canPlaceBlock(context.level(), context.origin().east().north()))
                context.level().setBlock(context.origin().east().north(),
                        Blocks.DEEPSLATE_BRICK_STAIRS.defaultBlockState()
                                .setValue(FACING, Direction.WEST)
                                .setValue(HALF, Half.BOTTOM)
                                .setValue(SHAPE, StairsShape.OUTER_LEFT), 11);
            if (canPlaceBlock(context.level(), context.origin().west()))
                context.level().setBlock(context.origin().west(),
                        Blocks.DEEPSLATE_BRICK_STAIRS.defaultBlockState()
                                .setValue(FACING, Direction.EAST)
                                .setValue(HALF, Half.BOTTOM)
                                .setValue(SHAPE, StairsShape.STRAIGHT), 11);
            if (canPlaceBlock(context.level(), context.origin().east()))
                context.level().setBlock(context.origin().east(),
                        Blocks.DEEPSLATE_BRICK_STAIRS.defaultBlockState()
                                .setValue(FACING, Direction.WEST)
                                .setValue(HALF, Half.BOTTOM)
                                .setValue(SHAPE, StairsShape.STRAIGHT), 11);
            if (canPlaceBlock(context.level(), context.origin().west().south()))
                context.level().setBlock(context.origin().west().south(),
                        Blocks.DEEPSLATE_BRICK_STAIRS.defaultBlockState()
                                .setValue(FACING, Direction.EAST)
                                .setValue(HALF, Half.BOTTOM)
                                .setValue(SHAPE, StairsShape.OUTER_LEFT), 11);
            if (canPlaceBlock(context.level(), context.origin().south()))
                context.level().setBlock(context.origin().south(),
                        Blocks.DEEPSLATE_BRICK_STAIRS.defaultBlockState()
                                .setValue(FACING, Direction.NORTH)
                                .setValue(HALF, Half.BOTTOM)
                                .setValue(SHAPE, StairsShape.STRAIGHT), 11);
            if (canPlaceBlock(context.level(), context.origin().east().south()))
                context.level().setBlock(context.origin().east().south(),
                        Blocks.DEEPSLATE_BRICK_STAIRS.defaultBlockState()
                                .setValue(FACING, Direction.NORTH)
                                .setValue(HALF, Half.BOTTOM)
                                .setValue(SHAPE, StairsShape.OUTER_LEFT), 11);
            return true;
        }
        return false;
    }

    public boolean canPlaceBlock(WorldGenLevel level, BlockPos blockPos){
        return  blockPos.getY() < level.getMaxBuildHeight()
                && level.getBlockState(blockPos).getMaterial().isReplaceable();
    }
}
