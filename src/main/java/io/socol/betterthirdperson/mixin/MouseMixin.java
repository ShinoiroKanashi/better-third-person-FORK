package io.socol.betterthirdperson.mixin;

import io.socol.betterthirdperson.BetterThirdPerson;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Mouse.class, priority = 1000)
public class MouseMixin {
    @Shadow
    private double cursorDeltaX;

    @Shadow
    private double cursorDeltaY;

    @Inject(method = "updateMouse", at = @At("HEAD"))
    private void betterThirdPerson$beforeMouseLook(double timeDelta, CallbackInfo ci) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player != null) {
            BetterThirdPerson.getCameraManager().startPlayerTurning(
                    this.cursorDeltaX,
                    this.cursorDeltaY
            );
        }
    }

    @Inject(method = "updateMouse", at = @At("TAIL"))
    private void betterThirdPerson$afterMouseLook(double timeDelta, CallbackInfo ci) {
        if (MinecraftClient.getInstance().player != null) {
            BetterThirdPerson.getCameraManager().stopPlayerTurning();
        }
    }
}
