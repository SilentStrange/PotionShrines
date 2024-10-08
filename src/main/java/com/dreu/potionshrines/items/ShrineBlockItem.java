package com.dreu.potionshrines.items;

import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.dreu.potionshrines.PotionShrines.*;
import static com.dreu.potionshrines.blocks.shrine.simple.SimpleShrineBaseBlock.HALF;

public class ShrineBlockItem extends BlockItem {
    private final Block baseBlock;

    public ShrineBlockItem(Block block, Block baseBlock, Properties properties) {
        super(block, properties);
        this.baseBlock = baseBlock;
    }

    @Override
    protected boolean placeBlock(BlockPlaceContext context, BlockState blockState) {
        return context.getLevel().getEntities(null, new AABB(context.getClickedPos().getX(), context.getClickedPos().getY(), context.getClickedPos().getZ(), context.getClickedPos().getX() + 1, context.getClickedPos().getY() + 2, context.getClickedPos().getZ() + 1))
                .stream().filter(entity -> entity instanceof LivingEntity).toList().isEmpty()
                && context.getLevel().getBlockState(context.getClickedPos()).getMaterial().isReplaceable()
                && context.getLevel().getBlockState(context.getClickedPos().above(1)).getMaterial().isReplaceable()
                && context.getLevel().getBlockState(context.getClickedPos().above(2)).getMaterial().isReplaceable()
                && context.getLevel().setBlock(context.getClickedPos().above(2), blockState, 11);
    }

    @SuppressWarnings("all")
    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag tooltipFlag) {
        CompoundTag nbt = itemStack.getTagElement("BlockEntityTag");
        if (nbt == null) return;
        components.add(Component.translatable(getEffectFromString(nbt.getString("effect")).getDescriptionId()).withStyle(ChatFormatting.BLUE)
                .append(Component.literal(" " + romanNumerals.get(nbt.getInt("amplifier") + 1)))
                .append(Component.literal("(" + asTime(nbt.getInt("duration")) + ")")));
        components.add(Component.translatable("tooltip.potion_shrines.cooldown").withStyle(ChatFormatting.BLUE)
                .append(Component.literal(": " + asTime((int) (nbt.getInt("remaining_cooldown") * 0.05)) + "/" + asTime((int) (nbt.getInt("max_cooldown") * 0.05)))).withStyle(Style.EMPTY.withBold(false)));
    }

    @Override
    public InteractionResult place(BlockPlaceContext context) {
        if (!context.canPlace()) {
            return InteractionResult.FAIL;
        } else {
            BlockPlaceContext blockplacecontext = this.updatePlacementContext(context);
            if (blockplacecontext == null) {
                return InteractionResult.FAIL;
            } else {
                BlockState blockstate = this.getPlacementState(blockplacecontext);
                if (blockstate == null) {
                    return InteractionResult.FAIL;
                } else if (!this.placeBlock(blockplacecontext, blockstate)) {
                    return InteractionResult.FAIL;
                } else {
                    BlockPos blockpos = blockplacecontext.getClickedPos().above(2);
                    Level level = blockplacecontext.getLevel();
                    Player player = blockplacecontext.getPlayer();
                    ItemStack itemstack = blockplacecontext.getItemInHand();
                    BlockState blockstate1 = level.getBlockState(blockpos);
                    if (blockstate1.is(blockstate.getBlock())) {
                        blockstate1 = this.updateBlockStateFromTag(blockpos, level, itemstack, blockstate1);
                        this.updateCustomBlockEntityTag(blockpos, level, player, itemstack, blockstate1);
                        blockstate1.getBlock().setPlacedBy(level, blockpos, blockstate1, player, itemstack);
                        if (player instanceof ServerPlayer) {
                            CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) player, blockpos, itemstack);
                        }
                    }

                    level.setBlockAndUpdate(blockpos.below(1), getBaseBlock().defaultBlockState().setValue(HALF, Half.TOP));
                    level.setBlockAndUpdate(blockpos.below(2), getBaseBlock().defaultBlockState().setValue(HALF, Half.BOTTOM));

                    level.gameEvent(GameEvent.BLOCK_PLACE, blockpos, GameEvent.Context.of(player, blockstate1));
                    SoundType soundtype = blockstate1.getSoundType(level, blockpos, context.getPlayer());
                    level.playSound(player, blockpos, this.getPlaceSound(blockstate1, level, blockpos, context.getPlayer()), SoundSource.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                    if (player == null || !player.getAbilities().instabuild) {
                        itemstack.shrink(1);
                    }
                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
            }
        }
    }
    @SuppressWarnings("all")
    private BlockState updateBlockStateFromTag(BlockPos blockPos, Level level, ItemStack itemStack, BlockState blockState) {
      BlockState blockstate = blockState;
      CompoundTag compoundtag = itemStack.getTag();
      if (compoundtag != null) {
         CompoundTag compoundtag1 = compoundtag.getCompound("BlockStateTag");
         StateDefinition<Block, BlockState> statedefinition = blockState.getBlock().getStateDefinition();

         for(String s : compoundtag1.getAllKeys()) {
            Property<?> property = statedefinition.getProperty(s);
            if (property != null) {
               String s1 = compoundtag1.get(s).getAsString();
               blockstate = updateBlockState(blockstate, property, s1);
            }
         }
      }

      if (blockstate != blockState) {
         level.setBlock(blockPos, blockstate, 2);
      }

      return blockstate;
    }

    public Block getBaseBlock() {
        return baseBlock;
    }

    public static <T extends Comparable<T>> BlockState updateBlockState(BlockState blockState, Property<T> property, String value) {
        return property.getValue(value).map(comparable -> blockState.setValue(property, comparable)).orElse(blockState);
    }
}
