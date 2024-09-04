package com.dreu.potionshrines.blocks.shrine.aoe;

import com.dreu.potionshrines.blocks.shrine.DecrepitShrineBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class AoEDecrepitBlock extends DecrepitShrineBlock {
    public AoEDecrepitBlock(Properties properties) {
        super(properties);
    }
    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return blockState.getValue(HALF) == Half.BOTTOM ? AoEShrineBaseBlock.BOTTOM_SHAPE : AoEShrineBaseBlock.TOP_SHAPE;
    }
}
