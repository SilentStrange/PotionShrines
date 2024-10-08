package com.dreu.potionshrines.blocks.shrine.simple;

import com.dreu.potionshrines.blocks.shrine.DecrepitShrineBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import static com.dreu.potionshrines.blocks.shrine.simple.SimpleShrineBaseBlock.BOTTOM_SHAPE;
import static com.dreu.potionshrines.blocks.shrine.simple.SimpleShrineBaseBlock.TOP_SHAPE;

public class SimpleShrineDecrepitBlock extends DecrepitShrineBlock {
    public SimpleShrineDecrepitBlock(Properties properties) {
        super(properties);
    }
    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return blockState.getValue(HALF) == Half.BOTTOM ? BOTTOM_SHAPE : TOP_SHAPE;
    }
}
