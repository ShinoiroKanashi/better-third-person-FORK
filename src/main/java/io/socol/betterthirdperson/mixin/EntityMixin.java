package io.socol.betterthirdperson.mixin;

import io.socol.betterthirdperson.BetterThirdPerson;
import io.socol.betterthirdperson.api.TickPhase;
import io.socol.betterthirdperson.impl.PlayerAdapter;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(method = "changeLookDirection", at = @At("HEAD"))
    private void betterThirdPerson$beforeLookChange(double cursorDeltaX, double cursorDeltaY, CallbackInfo ci) {
        Entity entity = (Entity) (Object) this;
        if (entity instanceof ClientPlayerEntity player) {
            BetterThirdPerson.getCameraManager().onPlayerTurn(
                    TickPhase.START,
                    new PlayerAdapter(player)
            );
        }
    }

    @Inject(method = "changeLookDirection", at = @At("TAIL"))
    private void betterThirdPerson$afterLookChange(double cursorDeltaX, double cursorDeltaY, CallbackInfo ci) {
        Entity entity = (Entity) (Object) this;
        if (entity instanceof ClientPlayerEntity player) {
            if (BetterThirdPerson.getCameraManager().onPlayerTurn(
                    TickPhase.END,
                    new PlayerAdapter(player)
            ) && player.getVehicle() != null) {
                player.getVehicle().updatePassengerPosition(player);
            }
        }
    }
}
