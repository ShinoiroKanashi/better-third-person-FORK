package io.socol.betterthirdperson.mixin;

import io.socol.betterthirdperson.api.TickPhase;
import io.socol.betterthirdperson.impl.PlayerAdapter;

import io.socol.betterthirdperson.BetterThirdPerson;
import io.socol.betterthirdperson.impl.MovementInputAdapter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.ClientInput;
import net.minecraft.client.player.KeyboardInput;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public class KeyboardInputMixin {
    @Inject(method = "tick", at = @At("TAIL"))
    private void betterThirdPerson$handleMovementInput(CallbackInfo ci) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null && BetterThirdPerson.getCameraManager().hasCustomCamera()) {
            BetterThirdPerson.getCameraManager().handleMovementInputs(
                    new PlayerAdapter(player),
                    new MovementInputAdapter((ClientInput) (Object) this),
                    TickPhase.START
            );
        }
    }
}
