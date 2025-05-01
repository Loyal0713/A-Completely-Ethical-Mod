package com.cem.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public abstract class PumpkinPlacementMixin {
    @Inject(method = "useOn", at = @At("HEAD"), cancellable = true)
    private void onUseOn(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        Player player = context.getPlayer();
        if (player == null) return;

        String dimension = context.getLevel().dimension().location().toString();
        ItemStack itemStack = context.getItemInHand();
        BlockPos blockPos = context.getClickedPos();
        BlockPos twoBelowPumpkinPos = blockPos.below();

        if (dimension.equals("minecraft:the_nether") && itemStack.getItem() instanceof BlockItem) {
            BlockState blockState = ((BlockItem) itemStack.getItem()).getBlock().defaultBlockState();
            if (blockState.is(Blocks.CARVED_PUMPKIN)) {
                BlockState belowPumpkinState = context.getLevel().getBlockState(blockPos);
                BlockState twoBelowPumpkinState = context.getLevel().getBlockState(twoBelowPumpkinPos);

                // If the two blocks below are snow blocks, cancel the placement
                if (belowPumpkinState.is(Blocks.SNOW_BLOCK) && twoBelowPumpkinState.is(Blocks.SNOW_BLOCK)) {
                    cir.setReturnValue(InteractionResult.FAIL);
                }
            }
        }
    }
}