package com.dreu.potionshrines.blocks.shrine;

import com.dreu.potionshrines.blocks.DecrepitShrineBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import static com.dreu.potionshrines.blocks.shrine.ShrineBaseBlock.BOTTOM_SHAPE;
import static com.dreu.potionshrines.blocks.shrine.ShrineBaseBlock.TOP_SHAPE;

public class ShrineDecrepitBlock extends DecrepitShrineBlock {
    public ShrineDecrepitBlock(Properties properties) {
        super(properties);
    }
    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return blockState.getValue(HALF) == Half.BOTTOM ? BOTTOM_SHAPE : TOP_SHAPE;
    }
}
