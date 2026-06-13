package com.cursee.bijuu_scroll.mixin;

import com.cursee.bijuu_scroll.BijuuScroll;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {

  @Inject(at = @At("TAIL"), method = "<init>")
  private void bijuu_scroll$init(GameConfiguration gameConfig, CallbackInfo ci) {
    BijuuScroll.LOG.info("Client initialized.");
  }
}
