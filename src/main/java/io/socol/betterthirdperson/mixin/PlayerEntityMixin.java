package io.socol.betterthirdperson.mixin;

import io.socol.betterthirdperson.BetterThirdPerson;
import io.socol.betterthirdperson.api.TickPhase;
import io.socol.betterthirdperson.impl.PlayerAdapter;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    private void betterThirdPerson$beforePlayerTick(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (player instanceof ClientPlayerEntity clientPlayer) {
            BetterThirdPerson.getCameraManager().onPlayerTick(
                    new PlayerAdapter(clientPlayer),
                    TickPhase.START
            );
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void betterThirdPerson$afterPlayerTick(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (player instanceof ClientPlayerEntity clientPlayer) {
            BetterThirdPerson.getCameraManager().onPlayerTick(
                    new PlayerAdapter(clientPlayer),
                    TickPhase.END
            );
        }
    }
}
