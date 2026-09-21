package io.socol.betterthirdperson.mixin;

import io.socol.betterthirdperson.BetterThirdPerson;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MouseHandler.class, priority = 1000)
public class MouseMixin {
    @Shadow
    private double accumulatedDX;

    @Shadow
    private double accumulatedDY;

    @Inject(method = "turnPlayer", at = @At("HEAD"))
    private void betterThirdPerson$beforeMouseLook(double timeDelta, CallbackInfo ci) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            BetterThirdPerson.getCameraManager().startPlayerTurning(
                    this.accumulatedDX,
                    this.accumulatedDY
            );
        }
    }

    @Inject(method = "turnPlayer", at = @At("TAIL"))
    private void betterThirdPerson$afterMouseLook(double timeDelta, CallbackInfo ci) {
        if (Minecraft.getInstance().player != null) {
            BetterThirdPerson.getCameraManager().stopPlayerTurning();
        }
    }
}
