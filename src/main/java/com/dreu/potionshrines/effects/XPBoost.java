package com.dreu.potionshrines.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

import static com.dreu.potionshrines.PotionShrines.EFFECTS;
import static com.dreu.potionshrines.PotionShrines.MODID;
import static com.dreu.potionshrines.config.General.XP_BONUS;
@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class XPBoost extends MobEffect {
    public static final RegistryObject<MobEffect> XP_BOOST = EFFECTS.register("xp_boost", XPBoost::new);
    public XPBoost() {
        super(MobEffectCategory.BENEFICIAL, 0x00FF00);
    }
    @SubscribeEvent
    public static void boostXP(PlayerXpEvent.PickupXp event){
        if (event.getEntity().hasEffect(XP_BOOST.get())) {
            event.getOrb().value *= (int) (1 + XP_BONUS * (event.getEntity().getEffect(XP_BOOST.get()).getAmplifier() + 1));
            event.setResult(Event.Result.ALLOW);
        }
    }
}
