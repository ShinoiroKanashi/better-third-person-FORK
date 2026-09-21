package io.socol.betterthirdperson.mixin;

import io.socol.betterthirdperson.BetterThirdPerson;
import io.socol.betterthirdperson.impl.PlayerAdapter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraMixin {
    @Inject(method = "update", at = @At("HEAD"))
    private void betterThirdPerson$prepareCamera(
            BlockView area,
            Entity focusedEntity,
            boolean thirdPerson,
            boolean inverseView,
            float tickProgress,
            CallbackInfo ci
    ) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player != null) {
            BetterThirdPerson.getCameraManager().onRenderTickStart(
                    new PlayerAdapter(player),
                    tickProgress
            );
        }
    }

    @Redirect(
            method = "update",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/Entity;getYaw(F)F"
            )
    )
    private float betterThirdPerson$overrideCameraYaw(Entity entity, float tickProgress) {
        if (BetterThirdPerson.getCameraManager().hasCustomCamera()) {
            return BetterThirdPerson.getCameraManager().getCustomCamera().getYaw();
        }
        return entity != null ? entity.getYaw(tickProgress) : 0.0F;
    }

    @Redirect(
            method = "update",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/Entity;getPitch(F)F"
            )
    )
    private float betterThirdPerson$overrideCameraPitch(Entity entity, float tickProgress) {
        if (BetterThirdPerson.getCameraManager().hasCustomCamera()) {
            return BetterThirdPerson.getCameraManager().getCustomCamera().getPitch();
        }
        return entity != null ? entity.getPitch(tickProgress) : 0.0F;
    }
}
