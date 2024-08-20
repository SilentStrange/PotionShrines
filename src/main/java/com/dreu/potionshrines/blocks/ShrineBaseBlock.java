package com.dreu.potionshrines.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ShrineBaseBlock extends Block {
    public static final EnumProperty HALF = BlockStateProperties.HALF;
    public static final VoxelShape BOTTOM_SHAPE =
            Shapes.join(
                Shapes.join(
                    Shapes.join(
                            Block.box(1, 0, 1, 15, 2, 15),
                            Block.box(2, 2, 2, 14, 4, 14), BooleanOp.OR),
                    Shapes.join(
                        Block.box(3, 4, 3, 13, 11, 13),
                        Block.box(4, 11, 4, 12, 27,12), BooleanOp.OR),
                    BooleanOp.OR
                ),
                Shapes.join(
                    Shapes.join(
                        Block.box(3, 27, 3, 13, 28, 13),
                        Block.box(2, 28, 2, 14, 30, 14), BooleanOp.OR),
                    Shapes.join(
                        Block.box(2, 30, 1, 14, 31, 15),
                        Block.box(1, 30, 2, 15, 31, 14), BooleanOp.OR),
                    BooleanOp.OR
                ),
                BooleanOp.OR
            );
    public static final VoxelShape TOP_SHAPE =
            Shapes.join(
                Shapes.join(
                    Shapes.join(
                        Block.box(1, -16, 1, 15, -14, 15),
                        Block.box(2, -14, 2, 14, -12, 14), BooleanOp.OR),
                    Shapes.join(
                        Block.box(3, -12, 3, 13, -5, 13),
                        Block.box(4, -5, 4, 12, 11,12), BooleanOp.OR),
                    BooleanOp.OR
                ),
                Shapes.join(
                    Shapes.join(
                        Block.box(3, 11, 3, 13, 12, 13),
                        Block.box(2, 12, 2, 14, 14, 14), BooleanOp.OR),
                    Shapes.join(
                        Block.box(2, 14, 1, 14, 15, 15),
                        Block.box(1, 14, 2, 15, 15, 14), BooleanOp.OR),
                    BooleanOp.OR
                ),
                BooleanOp.OR
            );

    public ShrineBaseBlock(Properties properties) {super(properties);}

    @Override
    public ItemStack getCloneItemStack(BlockState blockState, HitResult target, BlockGetter level, BlockPos blockPos, Player player) {
        blockPos = blockPos.above(blockState.getValue(HALF) == Half.TOP ? 1 : 2);
        return level.getBlockState(blockPos).getBlock().getCloneItemStack(level.getBlockState(blockPos), target, level, blockPos, player);
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return blockState.getValue(HALF) == Half.TOP ? RenderShape.INVISIBLE : RenderShape.MODEL;
    }
    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext context) {
        return blockState.getValue(HALF) == Half.TOP ? TOP_SHAPE : BOTTOM_SHAPE;
    }

    @Override
    public void destroy(LevelAccessor level, BlockPos blockPos, BlockState blockState) {
        blockPos = blockPos.above(blockState.getValue(HALF) == Half.TOP ? 1 : 2);
        level.removeBlock(blockPos, true);
        level.removeBlock(blockPos.below(1), true);
        level.removeBlock(blockPos.below(2), true);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HALF);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        blockPos = blockPos.above(blockState.getValue(HALF) == Half.TOP ? 1 : 2);
        return level.getBlockState(blockPos).getBlock().use(level.getBlockState(blockPos), level, blockPos, player, interactionHand, blockHitResult);
    }
}
