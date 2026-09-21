package io.socol.betterthirdperson.mixin;

import io.socol.betterthirdperson.BetterThirdPerson;
import io.socol.betterthirdperson.impl.PlayerAdapter;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Keeps vanilla Camera positioning/interpolation intact and only replaces the
 * view rotation used by Camera.alignWithEntity(). This is the important part
 * of the 1.21.11 implementation: the camera must still be positioned by
 * vanilla using the interpolated player/entity position.
 */
@Mixin(Camera.class)
public abstract class CameraMixin {
    @Inject(method = "alignWithEntity", at = @At("HEAD"))
    private void betterThirdPerson$prepareCamera(float partialTicks, CallbackInfo ci) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            BetterThirdPerson.getCameraManager().onRenderTickStart(
                    new PlayerAdapter(player),
                    partialTicks
            );
        }
    }

    @Redirect(
            method = "alignWithEntity",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;getViewYRot(F)F"
            )
    )
    private float betterThirdPerson$overrideCameraYaw(Entity entity, float partialTicks) {
        if (BetterThirdPerson.getCameraManager().hasCustomCamera()) {
            return BetterThirdPerson.getCameraManager().getCustomCamera().getYaw();
        }
        return entity != null ? entity.getViewYRot(partialTicks) : 0.0F;
    }

    @Redirect(
            method = "alignWithEntity",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;getViewXRot(F)F"
            )
    )
    private float betterThirdPerson$overrideCameraPitch(Entity entity, float partialTicks) {
        if (BetterThirdPerson.getCameraManager().hasCustomCamera()) {
            return BetterThirdPerson.getCameraManager().getCustomCamera().getPitch();
        }
        return entity != null ? entity.getViewXRot(partialTicks) : 0.0F;
    }
}
