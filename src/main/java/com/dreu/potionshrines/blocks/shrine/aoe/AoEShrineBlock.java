package com.dreu.potionshrines.blocks.shrine.aoe;

import com.dreu.potionshrines.registry.PSBlockEntities;
import com.dreu.potionshrines.registry.PSBlocks;
import com.dreu.potionshrines.registry.PSTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
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
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import static com.dreu.potionshrines.PotionShrines.getEffectFromString;
import static com.dreu.potionshrines.blocks.shrine.simple.ShrineBaseBlock.HALF;

public class AoEShrineBlock extends Block implements EntityBlock {
    public static final IntegerProperty LIGHT_LEVEL = IntegerProperty.create("light_level", 0, 15);
    public AoEShrineBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(stateDefinition.any()
                .setValue(LIGHT_LEVEL, 0));
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
        BlockEntity blockEntity = new AoEShrineBlockEntity(blockPos, blockState).fromConfig();
        blockEntity.setChanged();
        return blockEntity;
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos blockPos, Player player) {
        if (player.isShiftKeyDown()){
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
        return createTickerHelper(blockEntityType, PSBlockEntities.AOE_SHRINE.get(), AoEShrineBlockEntity::tick);
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
        AoEShrineBlockEntity shrine = (AoEShrineBlockEntity) level.getBlockEntity(blockPos);
        if (player.isCreative() && !player.isShiftKeyDown() && !level.isClientSide){
            NetworkHooks.openScreen((ServerPlayer) player, shrine, blockPos);
            return InteractionResult.SUCCESS;
        } else if (shrine.canUse()) {
            if (!level.isClientSide) {
                shrine.resetCooldown();
                level.playSound(null, blockPos, SoundEvents.BEACON_DEACTIVATE, SoundSource.BLOCKS, 3F, 1F);
                if (shrine.canEffectPlayers())
                    level.getEntitiesOfClass(Player.class, new AABB(blockPos).inflate(shrine.getRadius())).stream()
                        .filter(nearPlayer -> nearPlayer.blockPosition().distSqr(blockPos) <= shrine.getRadius() * shrine.getRadius())
                        .toList().forEach(filteredPlayer ->
                            filteredPlayer.addEffect(new MobEffectInstance(
                                getEffectFromString(shrine.getEffect()),
                                shrine.getDuration() * 20,
                                shrine.getAmplifier() - 1)));
                if (shrine.canEffectMonsters())
                    level.getEntitiesOfClass(LivingEntity.class, new AABB(blockPos).inflate(shrine.getRadius())).stream()
                        .filter(nearEntity -> nearEntity.blockPosition().distSqr(blockPos) <= shrine.getRadius() * shrine.getRadius()
                                && (nearEntity instanceof Monster || nearEntity.getType().getTags().toList().contains(PSTags.Entities.MONSTERS)))
                        .toList().forEach(filteredMonster ->
                            filteredMonster.addEffect(new MobEffectInstance(
                                getEffectFromString(shrine.getEffect()),
                                shrine.getDuration(),
                                shrine.getAmplifier())));
            }
            return InteractionResult.SUCCESS;
        }
        return super.use(blockState, level, blockPos, player, interactionHand, blockHitResult);
    }

    @Override
    public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState1, boolean b) {
        level.setBlock(blockPos.below(1), PSBlocks.AOE_SHRINE_BASE.get().defaultBlockState().setValue(HALF, Half.TOP), 11);
        level.setBlock(blockPos.below(2), PSBlocks.AOE_SHRINE_BASE.get().defaultBlockState().setValue(HALF, Half.BOTTOM), 11);
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIGHT_LEVEL);
    }
}
