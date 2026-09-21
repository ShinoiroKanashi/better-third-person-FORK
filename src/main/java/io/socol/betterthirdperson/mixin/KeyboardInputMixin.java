package io.socol.betterthirdperson.mixin;

import io.socol.betterthirdperson.BetterThirdPerson;
import io.socol.betterthirdperson.api.TickPhase;
import io.socol.betterthirdperson.impl.MovementInputAdapter;
import io.socol.betterthirdperson.impl.PlayerAdapter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.Input;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public class KeyboardInputMixin {
    @Inject(method = "tick", at = @At("TAIL"))
    private void betterThirdPerson$handleMovementInput(CallbackInfo ci) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player != null && BetterThirdPerson.getCameraManager().hasCustomCamera()) {
            BetterThirdPerson.getCameraManager().handleMovementInputs(
                    new PlayerAdapter(player),
                    new MovementInputAdapter((Input) (Object) this),
                    TickPhase.START
            );
        }
    }
}
