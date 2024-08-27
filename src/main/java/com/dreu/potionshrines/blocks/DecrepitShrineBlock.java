package com.dreu.potionshrines.blocks;

import com.dreu.potionshrines.config.General;
import net.minecraft.core.BlockPos;
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
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.HitResult;

import static com.dreu.potionshrines.config.General.OBTAINABLE;
import static com.dreu.potionshrines.config.General.SHRINE_INDESTRUCTIBLE;

public class DecrepitShrineBlock extends Block {
    public static final EnumProperty<Half> HALF = BlockStateProperties.HALF;
    public static final BooleanProperty PLAYER_PLACED = BooleanProperty.create("player_placed");

    public DecrepitShrineBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(stateDefinition.any()
                .setValue(HALF, Half.BOTTOM)
                .setValue(PLAYER_PLACED, true)
        );
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState blockState) {
        return PushReaction.BLOCK;
    }

    @Override
    public ItemStack getCloneItemStack(BlockState blockState, HitResult target, BlockGetter level, BlockPos blockPos, Player player) {
        return level.getBlockState(blockPos.above()).getBlock().getCloneItemStack(level.getBlockState(blockPos.above()), target, level, blockPos.above(), player);
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return blockState.getValue(HALF) == Half.TOP ? RenderShape.INVISIBLE : RenderShape.MODEL;
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState1, boolean b) {
        level.removeBlock(blockPos.above(blockState.getValue(HALF) == Half.BOTTOM ? 1 : -1), true);
    }
    @Override
    public boolean onDestroyedByPlayer(BlockState blockState, Level level, BlockPos blockPos, Player player, boolean willHarvest, FluidState fluid) {
        if (!level.isClientSide) {
            if (!player.isCreative() && (OBTAINABLE || blockState.getValue(PLAYER_PLACED))) {
                popResource(level, blockPos, new ItemStack(this));
            }
            return level.removeBlock(blockPos.above(blockState.getValue(HALF) == Half.BOTTOM ? 1 : -1), true);
        }
        return super.onDestroyedByPlayer(blockState, level, blockPos, player, !player.isCreative(), fluid);
    }

    @Override
    public float getDestroyProgress(BlockState blockState, Player player, BlockGetter blockGetter, BlockPos blockPos) {
        return blockState.getValue(PLAYER_PLACED) || !SHRINE_INDESTRUCTIBLE ? super.getDestroyProgress(blockState, player, blockGetter, blockPos) : 0f;
    }

    @Override
    public boolean canEntityDestroy(BlockState blockState, BlockGetter level, BlockPos blockPos, Entity entity) {
        return (!General.SHRINE_INDESTRUCTIBLE || blockState.getValue(PLAYER_PLACED)) && super.canEntityDestroy(blockState, level, blockPos, entity);
    }

    @Override
    public float getExplosionResistance(BlockState blockState, BlockGetter level, BlockPos pos, Explosion explosion) {
        return General.SHRINE_INDESTRUCTIBLE && !blockState.getValue(PLAYER_PLACED) ? 3600000 : 1200;
    }
    @Override
    public boolean isPathfindable(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, PathComputationType pathComputationType) {
        return false;
    }
    @Override
    public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState1, boolean b) {
        if (blockState.getValue(HALF) == Half.BOTTOM){
            level.setBlock(blockPos.above(), this.defaultBlockState().setValue(HALF, Half.TOP), 11);
        } else {
            level.setBlock(blockPos.below(), this.defaultBlockState().setValue(HALF, Half.BOTTOM), 11);
        }
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HALF, PLAYER_PLACED);
    }
}
