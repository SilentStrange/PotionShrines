package com.dreu.potionshrines.blocks;

import com.dreu.potionshrines.registry.PSBlockEntities;
import com.dreu.potionshrines.registry.PSBlocks;
import com.dreu.potionshrines.registry.PSItems;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.dreu.potionshrines.PotionShrines.getEffectFromString;
import static com.dreu.potionshrines.config.PSGeneralConfig.OBTAINABLE;

public class ShrineBlock extends Block implements EntityBlock {
    public ShrineBlock(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        return new ItemStack(Blocks.STONE);
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
        return super.use(blockState, level, blockPos, player, interactionHand, blockHitResult);
    }

    @Override
    public List<ItemStack> getDrops(BlockState blockState, LootContext.Builder context) {
        if (OBTAINABLE) {
            ItemStack drop = new ItemStack(PSItems.SHRINE.get());
            context.getOptionalParameter(LootContextParams.BLOCK_ENTITY).saveToItem(drop);
            List<ItemStack> dropList = super.getDrops(blockState, context);
            dropList.add(drop);
            return dropList;
        }
        return super.getDrops(blockState, context);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        CompoundTag itemNbt = context.getItemInHand().getTag();
        if (itemNbt != null) {
            Level level = context.getLevel();
            ShrineBlockEntity shrine = new ShrineBlockEntity(context.getClickedPos(), PSBlocks.SHRINE.get().defaultBlockState());
            shrine.setEffect(itemNbt.getString("effect"));
            shrine.setDuration(itemNbt.getInt("duration"));
            shrine.setMaxCooldown(itemNbt.getInt("max_cooldown"));
            shrine.setRemainingCooldown(itemNbt.getInt("remaining_cooldown"));
            shrine.setAmplifier(itemNbt.getInt("amplifier"));
            level.setBlockEntity(shrine);
        }
        return super.getStateForPlacement(context);
    }
}
