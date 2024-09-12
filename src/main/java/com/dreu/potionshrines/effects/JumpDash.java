package com.dreu.potionshrines.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

import static com.dreu.potionshrines.PotionShrines.EFFECTS;
import static com.dreu.potionshrines.PotionShrines.MODID;
@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class JumpDash extends MobEffect {
    public static final RegistryObject<MobEffect> JUMP_DASH = EFFECTS.register("jump_dash", JumpDash::new);
    public JumpDash() {
        super(MobEffectCategory.BENEFICIAL, 0x00FFFF);
    }
    @SubscribeEvent
    public static void sprintJump(LivingEvent.LivingJumpEvent event){
        if (event.getEntity().hasEffect(JUMP_DASH.get()) && event.getEntity().isSprinting()) {
            float yaw = event.getEntity().getYRot();
            double boostStrength = event.getEntity().getEffect(JUMP_DASH.get()).getAmplifier() * 0.5 + 0.5;
            double motionX = -Math.sin(Math.toRadians(yaw)) * boostStrength;
            double motionZ = Math.cos(Math.toRadians(yaw)) * boostStrength;
            event.getEntity().setDeltaMovement(motionX, event.getEntity().getDeltaMovement().y + 0.1, motionZ);
        }
    }
}
