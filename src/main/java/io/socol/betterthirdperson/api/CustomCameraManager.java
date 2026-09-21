package io.socol.betterthirdperson.api;

import io.socol.betterthirdperson.api.CustomCamera;
import io.socol.betterthirdperson.api.DelayedActionManager;
import io.socol.betterthirdperson.api.MouseInputHandler;
import io.socol.betterthirdperson.api.TickPhase;
import io.socol.betterthirdperson.api.action.MouseAction;
import io.socol.betterthirdperson.api.adapter.IPlayerAdapter;

import io.socol.betterthirdperson.api.adapter.IClientAdapter;
import io.socol.betterthirdperson.api.adapter.IMovementInputAdapter;
import io.socol.betterthirdperson.api.config.CustomCameraConfig;
import io.socol.betterthirdperson.api.config.DefaultCustomCameraConfig;
import io.socol.betterthirdperson.api.util.Rotation;

public class CustomCameraManager {
    private final IClientAdapter client;
    private CustomCamera customCamera;
    private CustomCameraConfig config = DefaultCustomCameraConfig.INSTANCE;
    private final DelayedActionManager delayedActions = new DelayedActionManager();
    private final MouseInputHandler mouseInputHandler = new MouseInputHandler();

    public CustomCameraManager(IClientAdapter client) {
        this.client = client;
    }

    public CustomCamera getCustomCamera() {
        return this.customCamera;
    }

    public boolean hasCustomCamera() {
        return this.customCamera != null;
    }

    public void setConfig(CustomCameraConfig config) {
        this.config = config;
    }

    public CustomCameraConfig getConfig() {
        return this.config;
    }

    private boolean mustHaveCustomCamera(IPlayerAdapter player) {
        boolean customCameraInitialized;
        boolean mustHaveCustomCamera = !(this.client.isFirstPerson() || !this.client.isCameraOnPlayer() || this.client.isCameraMirrored() || player.isPassenger() && !player.hasAllowedVehicle() || !this.config.hasFreeCameraDuringElytra() && player.isElytraFlying());
        boolean bl = customCameraInitialized = this.customCamera != null;
        if (mustHaveCustomCamera != customCameraInitialized) {
            if (mustHaveCustomCamera) {
                this.customCamera = new CustomCamera(this, this.client, player, this.config);
            } else {
                this.customCamera.onDisable(player);
                this.customCamera = null;
            }
        }
        return mustHaveCustomCamera;
    }

    public void onPlayerTick(IPlayerAdapter player, TickPhase phase) {
        if (this.hasCustomCamera()) {
            this.customCamera.tick(phase, player);
        }
    }

    public void onInputEvents(IPlayerAdapter player) {
        if (this.mustHaveCustomCamera(player)) {
            this.customCamera.handleMouseReset();
        }
        this.delayedActions.replayActions();
    }

    public void handleMovementInputs(IPlayerAdapter player, IMovementInputAdapter inputs, TickPhase phase) {
        if (this.mustHaveCustomCamera(player)) {
            this.customCamera.handleMovementInput(inputs, phase);
        }
    }

    public void onRenderTickStart(IPlayerAdapter player, float partialTicks) {
        if (!this.mustHaveCustomCamera(player)) {
            return;
        }
        this.customCamera.setup(this.client, player, partialTicks);
    }

    public boolean onMouseAction(IPlayerAdapter player, MouseAction action) {
        if (!this.delayedActions.isReplayingActions() && this.mustHaveCustomCamera(player)) {
            boolean delay = this.customCamera.handleMouseAction(player, this.client);
            if (delay) {
                this.delayedActions.writeAction(action);
            }
            return delay;
        }
        return false;
    }

    public void startPlayerTurning(double turnX, double turnY) {
        if (this.hasCustomCamera()) {
            this.mouseInputHandler.start(turnX, turnY);
        }
    }

    public boolean onPlayerTurn(TickPhase phase, IPlayerAdapter player) {
        if (this.hasCustomCamera()) {
            return this.mouseInputHandler.update(this.customCamera, phase, player);
        }
        return false;
    }

    public void stopPlayerTurning() {
        if (this.hasCustomCamera()) {
            this.mouseInputHandler.stop();
        }
    }

    public Rotation restorePlayerTurnValues() {
        if (this.hasCustomCamera()) {
            return this.mouseInputHandler.getUnusedInputValues();
        }
        return null;
    }
}
