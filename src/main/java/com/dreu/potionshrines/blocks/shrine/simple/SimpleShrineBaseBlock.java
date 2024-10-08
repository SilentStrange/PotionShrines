package com.dreu.potionshrines.blocks.shrine.simple;

import com.dreu.potionshrines.config.General;
import com.dreu.potionshrines.registry.PSBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import static com.dreu.potionshrines.config.General.OBTAINABLE;

public class SimpleShrineBaseBlock extends Block {
    public static final EnumProperty<Half> HALF = BlockStateProperties.HALF;
    public static final VoxelShape BOTTOM_SHAPE =
            Shapes.join(
                Shapes.join(
                    Shapes.join(
                            Block.box(1, 0, 1, 15, 2, 15),
                            Block.box(2, 2, 2, 14, 4, 14), BooleanOp.OR),
                    Shapes.join(
                        Block.box(3, 4, 3, 13, 11, 13),
                        Block.box(4, 11, 4, 12, 28,12), BooleanOp.OR),
                    BooleanOp.OR
                ),
                Shapes.join(
                    Shapes.join(
                        Block.box(3, 28, 3, 13, 29, 13),
                        Block.box(2, 29, 2, 14, 31, 14), BooleanOp.OR),
                    Shapes.join(
                        Block.box(2, 31, 1, 14, 32, 15),
                        Block.box(1, 31, 2, 15, 32, 14), BooleanOp.OR),
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
                        Block.box(4, -5, 4, 12, 12,12), BooleanOp.OR),
                    BooleanOp.OR
                ),
                Shapes.join(
                    Shapes.join(
                        Block.box(3, 12, 3, 13, 13, 13),
                        Block.box(2, 13, 2, 14, 15, 14), BooleanOp.OR),
                    Shapes.join(
                        Block.box(2, 15, 1, 14, 16, 15),
                        Block.box(1, 15, 2, 15, 16, 14), BooleanOp.OR),
                    BooleanOp.OR
                ),
                BooleanOp.OR
            );

    public static final VoxelShape COLLISION_SHAPE = Block.box(3, 0, 3, 13, 16, 13);

    public SimpleShrineBaseBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(stateDefinition.any()
                .setValue(HALF, Half.BOTTOM));
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState blockState) {return PushReaction.BLOCK;}
    @Override
    public VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext context) {return COLLISION_SHAPE;}

    @Override
    public ItemStack getCloneItemStack(BlockState blockState, HitResult target, BlockGetter level, BlockPos blockPos, Player player) {
        return level.getBlockState(blockPos.above()).getBlock().getCloneItemStack(level.getBlockState(blockPos.above()), target, level, blockPos.above(), player);
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
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState1, boolean b) {
        blockPos = blockPos.above(blockState.getValue(HALF) == Half.TOP ? 1 : 2);
        if (level.getBlockState(blockPos).is(PSBlocks.SIMPLE_SHRINE.get())) {
            level.destroyBlock(blockPos.below(2), true);
            level.removeBlock(blockPos.below(1), true);
            level.removeBlock(blockPos, true);
        }
    }
    @SuppressWarnings("all")
    @Override
    public boolean onDestroyedByPlayer(BlockState blockState, Level level, BlockPos blockPos, Player player, boolean willHarvest, FluidState fluid) {
        BlockPos shrinePos = blockPos.above(blockState.getValue(HALF) == Half.BOTTOM ? 2 : 1);
        if (level.getBlockEntity(shrinePos) != null && !level.isClientSide) {
            if (OBTAINABLE && !player.isCreative()) {
                ItemStack drop = new ItemStack(this);
                level.getBlockEntity(shrinePos).saveToItem(drop);
                popResource(level, blockPos, drop);
            }
            level.removeBlock(shrinePos.below(2), true);
            level.removeBlock(shrinePos.below(1), true);
            level.removeBlock(shrinePos, true);
            return true;
        }
        return super.onDestroyedByPlayer(blockState, level, blockPos, player, !player.isCreative(), fluid);
    }

    @Override
    public boolean canEntityDestroy(BlockState blockState, BlockGetter level, BlockPos blockPos, Entity entity) {
        return !General.SHRINE_INDESTRUCTIBLE && super.canEntityDestroy(blockState, level, blockPos, entity);
    }

    @Override
    public float getExplosionResistance(BlockState state, BlockGetter level, BlockPos pos, Explosion explosion) {
        return General.SHRINE_INDESTRUCTIBLE ? 3600000 : 1200;
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        blockPos = blockPos.above(blockState.getValue(HALF) == Half.TOP ? 1 : 2);
        return level.getBlockState(blockPos).getBlock().use(level.getBlockState(blockPos), level, blockPos, player, interactionHand, blockHitResult);
    }

    @Override
    public boolean isPathfindable(BlockState p_60475_, BlockGetter p_60476_, BlockPos p_60477_, PathComputationType p_60478_) {
        return false;
    }

    @Override
    public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState1, boolean b) {
        level.setBlock(blockPos.above(), blockState.getValue(HALF) == Half.BOTTOM
                ? PSBlocks.SIMPLE_SHRINE_BASE.get().defaultBlockState().setValue(HALF, Half.TOP)
                : PSBlocks.SIMPLE_SHRINE.get().defaultBlockState(), 11);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HALF);
    }
}
