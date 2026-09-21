package io.socol.betterthirdperson.api;

import io.socol.betterthirdperson.api.CustomCameraManager;
import io.socol.betterthirdperson.api.TickPhase;
import io.socol.betterthirdperson.api.adapter.IClientAdapter;
import io.socol.betterthirdperson.api.adapter.IMovementInputAdapter;
import io.socol.betterthirdperson.api.adapter.IPlayerAdapter;
import io.socol.betterthirdperson.api.config.CustomCameraConfig;
import io.socol.betterthirdperson.api.util.AngleUtils;
import io.socol.betterthirdperson.api.util.Rotation;
import net.minecraft.util.math.Vec3d;

public class CustomCamera {
    private final CustomCameraManager manager;
    private final CustomCameraConfig config;
    private final IClientAdapter client;
    private float followYawAccumulation;
    private Rotation cameraRotation;
    private Rotation playerRotation;
    private Vec3d lastPlayerPosition;
    private float targetMovementYaw;
    private float movementYawOffset = 0.0f;
    private Rotation mouseRotationDelta = Rotation.ZERO;
    private boolean wasMovingLastTick = false;
    private boolean isMoving = false;
    private boolean delayMouseActions;
    private long alignmentTicks = 0L;

    public CustomCamera(CustomCameraManager manager, IClientAdapter client, IPlayerAdapter player, CustomCameraConfig config) {
        this.manager = manager;
        this.config = config;
        this.client = client;
        this.resetToPlayerView(player);
        this.lastPlayerPosition = player.getPosition();
    }

    public void handleMovementInput(IMovementInputAdapter input, TickPhase phase) {
        this.isMoving = input.isMoving();
        if (this.alignmentTicks > 0L || !this.isMoving) {
            return;
        }
        if (phase == TickPhase.START) {
            this.targetMovementYaw = input.getInputDirection();
            input.redirect(0.0f);
        } else if (input.getMoveForward() <= 0.0f || input.getMoveStrafe() != 0.0f) {
            this.targetMovementYaw = input.getRawDirection();
            input.redirect(0.0f);
        }
    }

    private void startAlignment() {
        this.alignmentTicks = this.config.getAlignmentDurationTicks();
    }

    public void handleCameraTurn(float deltaYaw, float deltaPitch) {
        this.mouseRotationDelta = this.mouseRotationDelta.add(deltaYaw, deltaPitch);
    }

    public void tick(TickPhase phase, IPlayerAdapter player) {
        Vec3d currentPos = player.getPosition();
        boolean mousePressed = this.client.isMousePressed();
        if (phase == TickPhase.START) {
            if (!player.isPassenger() && !this.lastPlayerPosition.equals((Object)currentPos)) {
                this.resetToPlayerView(player);
            }
            this.wasMovingLastTick = this.isMoving;
            if (mousePressed && this.config.shouldAlignPlayerOnInteract()) {
                this.startAlignment();
            } else if (this.alignmentTicks > 0L) {
                --this.alignmentTicks;
                if (this.alignmentTicks == 0L) {
                    this.movementYawOffset = 0.0f;
                }
            }
        } else {
            this.lastPlayerPosition = currentPos;
            if (this.isMoving) {
                if (this.alignmentTicks <= 0L && !this.wasMovingLastTick) {
                    this.movementYawOffset = -AngleUtils.normalize(this.cameraRotation.getYaw() - this.playerRotation.getYaw());
                }
                float rotationSpeed = (float)this.config.getMovementRotationSpeed() / 100.0f;
                this.movementYawOffset = AngleUtils.smoothAngle(rotationSpeed, this.movementYawOffset, this.targetMovementYaw);
            }
            if (!this.isMoving && !mousePressed) {
                float pitchFollowSpeed = (float)this.config.getPitchFollowSpeed() / 100.0f;
                this.playerRotation = this.playerRotation.withPitch(AngleUtils.smoothAngle(pitchFollowSpeed, this.playerRotation.getPitch(), this.cameraRotation.getPitch()));
            }
        }
    }

    public void setup(IClientAdapter client, IPlayerAdapter player, float partialTicks) {
        this.cameraRotation = this.cameraRotation.add(this.mouseRotationDelta);
        if (this.alignmentTicks > 0L) {
            this.playerRotation = this.cameraRotation;
        } else if (this.isMoving) {
            this.playerRotation = this.cameraRotation.addYaw(this.movementYawOffset);
        } else if (this.config.getFollowCameraYawAngle() > 0 && !client.isMousePressed()) {
            if (Math.signum(this.mouseRotationDelta.getYaw()) != Math.signum(this.followYawAccumulation)) {
                this.followYawAccumulation = 0.0f;
            }
            this.followYawAccumulation += this.mouseRotationDelta.getYaw();
            if (Math.abs(this.followYawAccumulation) <= (float)this.config.getFollowCameraYawAngle()) {
                this.playerRotation = this.playerRotation.addYaw((float)((double)this.mouseRotationDelta.getYaw() * (1.0 - this.easeInExpo(Math.abs(this.followYawAccumulation) / (float)this.config.getFollowCameraYawAngle()))));
            }
        }
        this.playerRotation.applySafe(player);
        this.mouseRotationDelta = Rotation.ZERO;
    }

    public float getYaw() {
        return this.cameraRotation.getYaw();
    }

    public float getPitch() {
        return this.cameraRotation.getPitch();
    }

    public void handleMouseReset() {
        this.delayMouseActions = false;
    }

    public boolean handleMouseAction(IPlayerAdapter player, IClientAdapter client) {
        if (this.delayMouseActions) {
            return true;
        }
        if (this.alignmentTicks == 0L && this.config.shouldAlignPlayerOnInteract()) {
            this.startAlignment();
            this.cameraRotation.applySafe(player);
            client.updateHitResult();
            this.resetToPlayerView(player);
            this.delayMouseActions = true;
            return true;
        }
        return false;
    }

    private void resetToPlayerView(IPlayerAdapter player) {
        this.cameraRotation = this.playerRotation = player.getRotation();
        this.followYawAccumulation = 0.0f;
    }

    private double easeInExpo(double x) {
        return x == 0.0 ? 0.0 : Math.pow(2.0, 10.0 * x - 10.0);
    }

    public void onDisable(IPlayerAdapter player) {
        this.cameraRotation.applySafeFully(player);
    }

    public Rotation getCameraRotation() {
        return this.cameraRotation;
    }

    public Rotation getPlayerRotation() {
        return this.playerRotation;
    }

    public CustomCamera setCameraRotation(Rotation cameraRotation) {
        this.cameraRotation = cameraRotation;
        return this;
    }
}
