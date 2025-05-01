package com.cem.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Inject(method = "hurtServer", at = @At("HEAD"), cancellable = true)
    private void cancelDamage(ServerLevel serverLevel, DamageSource damageSource, float f, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = (LivingEntity) (Object) this; // convert to LivingEntity
        Entity attacker = damageSource.getEntity();
        Entity directCause = damageSource.getDirectEntity();

        // System.out.printf("Thing getting hurt: %s, Damage Source: %s, Attacker: %s, Direct Cause: %s%n", entity, damageSource, attacker, directCause);

        if (entity instanceof ServerPlayer) {
            return;
        }

        if (entity instanceof EnderDragon) {
            return;
        }

        // player directly caused damage
        if (attacker instanceof ServerPlayer) {
            cir.setReturnValue(false);
        }

        if (attacker == null && directCause == null) {
            // various damage types that could be caused "indirectly" by a player
            // and should be canceled
            if (damageSource.is(DamageTypes.ON_FIRE) ||
                    damageSource.is(DamageTypes.IN_FIRE) ||
                    damageSource.is(DamageTypes.CAMPFIRE) ||
                    damageSource.is(DamageTypes.LAVA) ||
                    damageSource.is(DamageTypes.LIGHTNING_BOLT) ||
                    damageSource.is(DamageTypes.DROWN) ||
                    damageSource.is(DamageTypes.FALL) ||
                    damageSource.is(DamageTypes.IN_WALL) ||
					damageSource.is(DamageTypes.CACTUS) ||
					damageSource.is(DamageTypes.SWEET_BERRY_BUSH) ||
					damageSource.is(DamageTypes.STALAGMITE) ||
                    damageSource.is(DamageTypes.FIREWORKS) ||
					damageSource.is(DamageTypes.FALLING_BLOCK) ||
					damageSource.is(DamageTypes.FALLING_ANVIL) ||
					damageSource.is(DamageTypes.FALLING_STALACTITE) ||
					damageSource.is(DamageTypes.UNATTRIBUTED_FIREBALL) ||
					damageSource.is(DamageTypes.EXPLOSION) ||
					damageSource.is(DamageTypes.PLAYER_EXPLOSION) ||
					damageSource.is(DamageTypes.SONIC_BOOM) ||
                    damageSource.is(DamageTypes.FREEZE)
            ) {
                cir.setReturnValue(false);
            }
        }
    }
}