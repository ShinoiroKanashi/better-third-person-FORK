package io.socol.betterthirdperson.api.config;

import io.socol.betterthirdperson.api.config.CustomCameraConfig;

public class DefaultCustomCameraConfig
implements CustomCameraConfig {
    public static final CustomCameraConfig INSTANCE = new DefaultCustomCameraConfig();

    private DefaultCustomCameraConfig() {
    }

    @Override
    public boolean shouldAlignPlayerOnInteract() {
        return true;
    }

    @Override
    public int getAlignmentDurationTicks() {
        return 40;
    }

    @Override
    public int getFollowCameraYawAngle() {
        return 45;
    }

    @Override
    public boolean hasFreeCameraDuringElytra() {
        return false;
    }

    @Override
    public boolean isThirdPersonFrontViewDisabled() {
        return false;
    }

    @Override
    public int getMovementRotationSpeed() {
        return 50;
    }

    @Override
    public int getPitchFollowSpeed() {
        return 65;
    }
}
