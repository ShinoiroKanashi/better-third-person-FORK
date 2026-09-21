package io.socol.betterthirdperson.api;

import io.socol.betterthirdperson.api.CustomCamera;
import io.socol.betterthirdperson.api.TickPhase;
import io.socol.betterthirdperson.api.adapter.IPlayerAdapter;
import io.socol.betterthirdperson.api.util.MathUtils;
import io.socol.betterthirdperson.api.util.Rotation;

public class MouseInputHandler {
    private boolean reading = false;
    private double inputX = 0.0;
    private double inputY = 0.0;
    private float prevYaw = 0.0f;
    private float prevPitch = 0.0f;

    public boolean update(CustomCamera camera, TickPhase phase, IPlayerAdapter player) {
        if (phase == TickPhase.START) {
            this.inputX = 0.0;
            this.inputY = 0.0;
            this.prevYaw = player.getRotationYaw();
            this.prevPitch = player.getRotationPitch();
            player.setRotationPitch(0.0f);
            player.setPrevRotationPitch(player.getPrevRotationPitch() - this.prevPitch);
            return false;
        }
        float deltaYaw = player.getRotationYaw() - this.prevYaw;
        float deltaPitch = player.getRotationPitch();
        if (deltaYaw != 0.0f || deltaPitch != 0.0f) {
            camera.handleCameraTurn(deltaYaw, deltaPitch);
            player.setRotationYaw(player.getRotationYaw() - deltaYaw);
            player.setRotationPitch(player.getRotationPitch() - deltaPitch);
            player.setPrevRotationYaw(player.getPrevRotationYaw() - deltaYaw);
            player.setPrevRotationPitch(player.getPrevRotationPitch() - deltaPitch);
        }
        player.setRotationPitch(MathUtils.clamp(player.getRotationPitch() + this.prevPitch, -90.0f, 90.0f));
        player.setPrevRotationPitch(MathUtils.clamp(player.getPrevRotationPitch() + this.prevPitch, -90.0f, 90.0f));
        return deltaYaw != 0.0f || deltaPitch != 0.0f;
    }

    public void start(double inputX, double inputY) {
        this.reading = true;
        this.inputX = inputX;
        this.inputY = inputY;
    }

    public Rotation getUnusedInputValues() {
        if (this.reading && (this.inputX != 0.0 || this.inputY != 0.0)) {
            return new Rotation((float)this.inputX, (float)this.inputY);
        }
        return null;
    }

    public void stop() {
        this.reading = false;
    }
}
