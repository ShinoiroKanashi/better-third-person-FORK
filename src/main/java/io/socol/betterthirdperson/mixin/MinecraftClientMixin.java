package io.socol.betterthirdperson.mixin;

import io.socol.betterthirdperson.BetterThirdPerson;
import io.socol.betterthirdperson.api.action.ItemRepeatableUseAction;
import io.socol.betterthirdperson.api.action.MouseAction;
import io.socol.betterthirdperson.impl.ClientAdapter;
import io.socol.betterthirdperson.impl.PlayerAdapter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Shadow
    private int itemUseCooldown;

    @Shadow
    @Final
    public GameOptions options;

    @Shadow
    abstract boolean doAttack();

    @Shadow
    abstract void doItemUse();

    @Shadow
    abstract void handleBlockBreaking(boolean breaking);

    @Inject(method = "handleInputEvents", at = @At("HEAD"))
    private void betterThirdPerson$handleInputEvents(CallbackInfo ci) {
        ClientPlayerEntity player = ((MinecraftClient) (Object) this).player;
        if (player != null) {
            BetterThirdPerson.getCameraManager().onInputEvents(new PlayerAdapter(player));
        }
    }

    @Inject(method = "doAttack", at = @At("HEAD"), cancellable = true)
    private void betterThirdPerson$interceptAttack(CallbackInfoReturnable<Boolean> cir) {
        ClientPlayerEntity player = ((MinecraftClient) (Object) this).player;
        if (player == null) {
            return;
        }

        MouseAction action = new MouseAction(this::doAttack);
        if (BetterThirdPerson.getCameraManager().onMouseAction(
                new PlayerAdapter(player),
                action
        )) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "handleBlockBreaking", at = @At("HEAD"), cancellable = true)
    private void betterThirdPerson$interceptBlockBreaking(boolean breaking, CallbackInfo ci) {
        ClientPlayerEntity player = ((MinecraftClient) (Object) this).player;
        if (player == null) {
            return;
        }

        MouseAction action = new MouseAction(() -> this.handleBlockBreaking(true));
        if (breaking && BetterThirdPerson.getCameraManager().onMouseAction(
                new PlayerAdapter(player),
                action
        )) {
            ci.cancel();
        }
    }

    @Redirect(
            method = "handleInputEvents",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/MinecraftClient;doItemUse()V",
                    ordinal = 0
            )
    )
    private void betterThirdPerson$interceptInitialItemUse(MinecraftClient client) {
        ClientPlayerEntity player = client.player;
        if (player == null) {
            return;
        }

        MouseAction action = new MouseAction(this::doItemUse);
        if (!BetterThirdPerson.getCameraManager().onMouseAction(
                new PlayerAdapter(player),
                action
        )) {
            this.doItemUse();
        }
    }

    @Redirect(
            method = "handleInputEvents",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/MinecraftClient;doItemUse()V",
                    ordinal = 1
            )
    )
    private void betterThirdPerson$interceptRepeatableItemUse(MinecraftClient client) {
        ClientPlayerEntity player = client.player;
        if (player == null) {
            return;
        }

        ItemRepeatableUseAction action = new ItemRepeatableUseAction(
                ClientAdapter.INSTANCE,
                () -> this.itemUseCooldown,
                this::doItemUse
        );

        if (!BetterThirdPerson.getCameraManager().onMouseAction(
                new PlayerAdapter(player),
                action
        )) {
            this.doItemUse();
        }
    }

    @Inject(
            method = "handleInputEvents",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/option/GameOptions;setPerspective(Lnet/minecraft/client/option/Perspective;)V",
                    shift = At.Shift.AFTER
            )
    )
    private void betterThirdPerson$preventThirdPersonFrontView(CallbackInfo ci) {
        if (!BetterThirdPerson.getCameraManager().getConfig().isThirdPersonFrontViewDisabled()) {
            return;
        }

        if (this.options.getPerspective() == Perspective.THIRD_PERSON_FRONT) {
            this.options.setPerspective(Perspective.THIRD_PERSON_BACK);
        }
    }
}
