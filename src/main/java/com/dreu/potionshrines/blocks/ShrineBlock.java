package com.dreu.potionshrines.blocks;

import com.dreu.potionshrines.registry.PSBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import static com.dreu.potionshrines.PotionShrines.getEffectFromString;

public class ShrineBlock extends Block implements EntityBlock {
    public ShrineBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Block.box(1, 0, 1, 15, 16, 15);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        BlockEntity blockEntity = new ShrineBlockEntity(blockPos, blockState);
        blockEntity.setChanged();
        return blockEntity;
    }

    public boolean triggerEvent(BlockState blockState, Level level, BlockPos blockPos, int i, int i1) {
        super.triggerEvent(blockState, level, blockPos, i, i1);
        return level.getBlockEntity(blockPos) != null && level.getBlockEntity(blockPos).triggerEvent(i, i1);
    }
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, PSBlockEntities.SHRINE.get(), ShrineBlockEntity::tick);
    }
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> aBlockEntityType, BlockEntityType<E> eBlockEntityType, BlockEntityTicker<? super E> blockEntityTicker) {
        return eBlockEntityType == aBlockEntityType ? (BlockEntityTicker<A>)blockEntityTicker : null;
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        ShrineBlockEntity shrine = (ShrineBlockEntity) level.getBlockEntity(blockPos);
        if (shrine.canUse()) {
            shrine.resetCooldown();
            if (!level.isClientSide) {
            player.addEffect(new MobEffectInstance(
                    getEffectFromString(shrine.getEffect()),
                    shrine.getDuration() * 20,
                    shrine.getAmplifier()));
            }
            return InteractionResult.SUCCESS;
        }
        if (level.isClientSide) player.sendSystemMessage(Component.literal("Client Side ----------------------"));
        else player.sendSystemMessage(Component.literal("Server Side ----------------------"));
        player.sendSystemMessage(Component.literal("Effect: " + shrine.getEffect()));
        player.sendSystemMessage(Component.literal("Amplifier: " + shrine.getAmplifier()));
        player.sendSystemMessage(Component.literal("Duration: " + shrine.getDuration()));
        player.sendSystemMessage(Component.literal("Cooldown: " + shrine.getMaxCooldown()));
        player.sendSystemMessage(Component.literal("Remaining: " + shrine.getRemainingCooldown()));
        return super.use(blockState, level, blockPos, player, interactionHand, blockHitResult);
    }
}
