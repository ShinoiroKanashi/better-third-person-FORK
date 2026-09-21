package io.socol.betterthirdperson.mixin;

import io.socol.betterthirdperson.BetterThirdPerson;
import io.socol.betterthirdperson.api.util.Rotation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Mouse.class, priority = 1100)
public class MouseFixupMixin {
    @Shadow
    @Mutable
    private double cursorDeltaX;

    @Shadow
    @Mutable
    private double cursorDeltaY;

    @Inject(method = "updateMouse", at = @At("HEAD"))
    private void betterThirdPerson$restoreUnusedMouseInput(double timeDelta, CallbackInfo ci) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) {
            return;
        }

        Rotation unusedInput = BetterThirdPerson.getCameraManager().restorePlayerTurnValues();
        if (unusedInput != null) {
            this.cursorDeltaX = unusedInput.getYaw();
            this.cursorDeltaY = unusedInput.getPitch();
        }
    }
}
