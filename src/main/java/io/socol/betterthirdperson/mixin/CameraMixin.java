package io.socol.betterthirdperson.mixin;

import io.socol.betterthirdperson.BetterThirdPerson;
import io.socol.betterthirdperson.impl.PlayerAdapter;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraMixin {
    @Inject(method = "setup", at = @At("HEAD"))
    private void betterThirdPerson$prepareCamera(
            Level area,
            Entity focusedEntity,
            boolean thirdPerson,
            boolean inverseView,
            float tickProgress,
            CallbackInfo ci
    ) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            BetterThirdPerson.getCameraManager().onRenderTickStart(
                    new PlayerAdapter(player),
                    tickProgress
            );
        }
    }

    @Redirect(
            method = "setup",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;getViewYRot(F)F"
            )
    )
    private float betterThirdPerson$overrideCameraYaw(Entity entity, float tickProgress) {
        if (BetterThirdPerson.getCameraManager().hasCustomCamera()) {
            return BetterThirdPerson.getCameraManager().getCustomCamera().getYaw();
        }
        return entity != null ? entity.getViewYRot(tickProgress) : 0.0F;
    }

    @Redirect(
            method = "setup",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;getViewXRot(F)F"
            )
    )
    private float betterThirdPerson$overrideCameraPitch(Entity entity, float tickProgress) {
        if (BetterThirdPerson.getCameraManager().hasCustomCamera()) {
            return BetterThirdPerson.getCameraManager().getCustomCamera().getPitch();
        }
        return entity != null ? entity.getViewXRot(tickProgress) : 0.0F;
    }
}
