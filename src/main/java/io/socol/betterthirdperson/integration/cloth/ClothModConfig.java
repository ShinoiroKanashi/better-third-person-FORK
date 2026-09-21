package io.socol.betterthirdperson.integration.cloth;

import io.socol.betterthirdperson.api.config.CustomCameraConfig;
import io.socol.betterthirdperson.api.config.DefaultCustomCameraConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name="betterthirdperson")
public class ClothModConfig
implements ConfigData,
CustomCameraConfig {
    @Comment(value="Align player to camera on left & right clicks")
    public boolean alignPlayerOnInteract = DefaultCustomCameraConfig.INSTANCE.shouldAlignPlayerOnInteract();
    @Comment(value="How long player will be aligned to camera after left & right clicks")
    @ConfigEntry.BoundedDiscrete(min=10L, max=200L)
    public int alignmentDurationTicks = DefaultCustomCameraConfig.INSTANCE.getAlignmentDurationTicks();
    @Comment(value="Angle in degrees within the player will slightly follow camera yaw (while standing still)")
    @ConfigEntry.BoundedDiscrete(min=0L, max=90L)
    public int followCameraYawAngle = DefaultCustomCameraConfig.INSTANCE.getFollowCameraYawAngle();
    @Comment(value="Does camera should rotate freely during elytra flight")
    public boolean freeCameraDuringElytra = DefaultCustomCameraConfig.INSTANCE.hasFreeCameraDuringElytra();
    @Comment(value="Completely remove third-person front view")
    public boolean disableThirdPersonFrontView = DefaultCustomCameraConfig.INSTANCE.isThirdPersonFrontViewDisabled();
    @Comment(value="How fast player changes movement direction in third-person")
    @ConfigEntry.BoundedDiscrete(min=10L, max=100L)
    public int movementRotationSpeed = DefaultCustomCameraConfig.INSTANCE.getMovementRotationSpeed();
    @Comment(value="How fast player pitch follows camera pitch in third-person")
    @ConfigEntry.BoundedDiscrete(min=10L, max=100L)
    public int pitchFollowSpeed = DefaultCustomCameraConfig.INSTANCE.getPitchFollowSpeed();

    public static CustomCameraConfig create() {
        AutoConfig.register(ClothModConfig.class, JanksonConfigSerializer::new);
        return (CustomCameraConfig)AutoConfig.getConfigHolder(ClothModConfig.class).getConfig();
    }

    @Override
    public boolean shouldAlignPlayerOnInteract() {
        return this.alignPlayerOnInteract;
    }

    @Override
    public int getAlignmentDurationTicks() {
        return this.alignmentDurationTicks;
    }

    @Override
    public int getFollowCameraYawAngle() {
        return this.followCameraYawAngle;
    }

    @Override
    public boolean hasFreeCameraDuringElytra() {
        return this.freeCameraDuringElytra;
    }

    @Override
    public boolean isThirdPersonFrontViewDisabled() {
        return this.disableThirdPersonFrontView;
    }

    @Override
    public int getMovementRotationSpeed() {
        return this.movementRotationSpeed;
    }

    @Override
    public int getPitchFollowSpeed() {
        return this.pitchFollowSpeed;
    }
}
