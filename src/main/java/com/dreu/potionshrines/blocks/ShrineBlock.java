package com.dreu.potionshrines.blocks;

import com.dreu.potionshrines.registry.PSBlockEntities;
import com.dreu.potionshrines.registry.PSBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import static com.dreu.potionshrines.PotionShrines.getEffectFromString;
import static com.dreu.potionshrines.blocks.ShrineBaseBlock.HALF;

public class ShrineBlock extends Block implements EntityBlock {
    public static final IntegerProperty LIGHT_LEVEL = IntegerProperty.create("light_level", 0, 15);
    public ShrineBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(stateDefinition.any()
                .setValue(LIGHT_LEVEL, 15));
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Shapes.empty();
    }

    @Override
    public RenderShape getRenderShape(BlockState p_60550_) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public boolean canEntityDestroy(BlockState state, BlockGetter level, BlockPos pos, Entity entity) {
        return false;
    }
    @Override
    public float getExplosionResistance(BlockState state, BlockGetter level, BlockPos pos, Explosion explosion) {
        return 3600000;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        BlockEntity blockEntity = new ShrineBlockEntity(blockPos, blockState);
        blockEntity.setChanged();
        return blockEntity;
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos blockPos, Player player) {
        if (player.isCrouching()){
            ItemStack itemStack = new ItemStack(this);
            level.getBlockEntity(blockPos).saveToItem(itemStack);
            return itemStack;
        }
        return super.getCloneItemStack(state, target, level, blockPos, player);
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
    public PushReaction getPistonPushReaction(BlockState blockState) {
        return PushReaction.BLOCK;
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
    public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState1, boolean b) {
        level.setBlock(blockPos.below(1), PSBlocks.SHRINE_BASE.get().defaultBlockState().setValue(HALF, Half.TOP), 11);
        level.setBlock(blockPos.below(2), PSBlocks.SHRINE_BASE.get().defaultBlockState().setValue(HALF, Half.BOTTOM), 11);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIGHT_LEVEL);
    }
}
