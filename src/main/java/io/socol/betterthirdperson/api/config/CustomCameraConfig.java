package io.socol.betterthirdperson.api.config;

public interface CustomCameraConfig {
    public static final String ALIGN_PLAYER_ON_INTERACT_DESC = "Align player to camera on left & right clicks";
    public static final String ALIGNMENT_DURATION_DESC = "How long player will be aligned to camera after left & right clicks";
    public static final int ALIGNMENT_DURATION_MIN = 10;
    public static final int ALIGNMENT_DURATION_MAX = 200;
    public static final String FOLLOW_CAMERA_YAW_DESC = "Angle in degrees within the player will slightly follow camera yaw (while standing still)";
    public static final int FOLLOW_CAMERA_YAW_MIN = 0;
    public static final int FOLLOW_CAMERA_YAW_MAX = 90;
    public static final String FREE_CAMERA_DURING_ELYTRA_DESC = "Does camera should rotate freely during elytra flight";
    public static final String DISABLE_THIRD_PERSON_FRONT_VIEW_DESC = "Completely remove third-person front view";
    public static final String MOVEMENT_ROTATION_SPEED_DESC = "How fast player changes movement direction in third-person";
    public static final int MOVEMENT_ROTATION_SPEED_MIN = 10;
    public static final int MOVEMENT_ROTATION_SPEED_MAX = 100;
    public static final String PITCH_FOLLOW_SPEED_DESC = "How fast player pitch follows camera pitch in third-person";
    public static final int PITCH_FOLLOW_SPEED_MIN = 10;
    public static final int PITCH_FOLLOW_SPEED_MAX = 100;

    public boolean shouldAlignPlayerOnInteract();

    public int getAlignmentDurationTicks();

    public int getFollowCameraYawAngle();

    public boolean hasFreeCameraDuringElytra();

    public boolean isThirdPersonFrontViewDisabled();

    public int getMovementRotationSpeed();

    public int getPitchFollowSpeed();
}
