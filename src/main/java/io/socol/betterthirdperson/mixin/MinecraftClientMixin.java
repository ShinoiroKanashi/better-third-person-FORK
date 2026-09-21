package io.socol.betterthirdperson.mixin;

import io.socol.betterthirdperson.BetterThirdPerson;
import io.socol.betterthirdperson.api.action.ItemRepeatableUseAction;
import io.socol.betterthirdperson.api.action.MouseAction;
import io.socol.betterthirdperson.impl.ClientAdapter;
import io.socol.betterthirdperson.impl.PlayerAdapter;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public abstract class MinecraftClientMixin {
    @Shadow
    private int rightClickDelay;

    @Shadow
    @Final
    public Options options;

    @Shadow
    abstract boolean startAttack();

    @Shadow
    abstract void startUseItem();

    @Shadow
    abstract void continueAttack(boolean breaking);

    @Inject(method = "handleKeybinds", at = @At("HEAD"))
    private void betterThirdPerson$handleInputEvents(CallbackInfo ci) {
        LocalPlayer player = ((Minecraft) (Object) this).player;
        if (player != null) {
            BetterThirdPerson.getCameraManager().onInputEvents(new PlayerAdapter(player));
        }
    }

    @Inject(method = "startAttack", at = @At("HEAD"), cancellable = true)
    private void betterThirdPerson$interceptAttack(CallbackInfoReturnable<Boolean> cir) {
        LocalPlayer player = ((Minecraft) (Object) this).player;
        if (player == null) {
            return;
        }

        MouseAction action = new MouseAction(this::startAttack);
        if (BetterThirdPerson.getCameraManager().onMouseAction(
                new PlayerAdapter(player),
                action
        )) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "continueAttack", at = @At("HEAD"), cancellable = true)
    private void betterThirdPerson$interceptBlockBreaking(boolean breaking, CallbackInfo ci) {
        LocalPlayer player = ((Minecraft) (Object) this).player;
        if (player == null) {
            return;
        }

        MouseAction action = new MouseAction(() -> this.continueAttack(true));
        if (breaking && BetterThirdPerson.getCameraManager().onMouseAction(
                new PlayerAdapter(player),
                action
        )) {
            ci.cancel();
        }
    }

    @Redirect(
            method = "handleKeybinds",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/Minecraft;startUseItem()V",
                    ordinal = 0
            )
    )
    private void betterThirdPerson$interceptInitialItemUse(Minecraft client) {
        LocalPlayer player = client.player;
        if (player == null) {
            return;
        }

        MouseAction action = new MouseAction(this::startUseItem);
        if (!BetterThirdPerson.getCameraManager().onMouseAction(
                new PlayerAdapter(player),
                action
        )) {
            this.startUseItem();
        }
    }

    @Redirect(
            method = "handleKeybinds",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/Minecraft;startUseItem()V",
                    ordinal = 1
            )
    )
    private void betterThirdPerson$interceptRepeatableItemUse(Minecraft client) {
        LocalPlayer player = client.player;
        if (player == null) {
            return;
        }

        ItemRepeatableUseAction action = new ItemRepeatableUseAction(
                ClientAdapter.INSTANCE,
                () -> this.rightClickDelay,
                this::startUseItem
        );

        if (!BetterThirdPerson.getCameraManager().onMouseAction(
                new PlayerAdapter(player),
                action
        )) {
            this.startUseItem();
        }
    }

    @Inject(
            method = "handleKeybinds",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/Options;setCameraType(Lnet/minecraft/client/CameraType;)V",
                    shift = At.Shift.AFTER
            )
    )
    private void betterThirdPerson$preventThirdPersonFrontView(CallbackInfo ci) {
        if (!BetterThirdPerson.getCameraManager().getConfig().isThirdPersonFrontViewDisabled()) {
            return;
        }

        if (this.options.getCameraType() == CameraType.THIRD_PERSON_FRONT) {
            this.options.setCameraType(CameraType.THIRD_PERSON_BACK);
        }
    }
}
