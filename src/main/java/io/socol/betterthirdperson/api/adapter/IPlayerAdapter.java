package io.socol.betterthirdperson.api.adapter;

import io.socol.betterthirdperson.api.util.AngleUtils;
import io.socol.betterthirdperson.api.util.Rotation;
import net.minecraft.util.math.Vec3d;

public interface IPlayerAdapter {
    public float getRotationYaw();

    public float getRotationPitch();

    public float getPrevRotationYaw();

    public float getPrevRotationPitch();

    public void setRotationYaw(float yaw);

    public void setRotationPitch(float pitch);

    public void setPrevRotationYaw(float yaw);

    public void setPrevRotationPitch(float pitch);

    public void setVehicleYaw(float yaw);

    public Vec3d getPosition();

    public boolean isPassenger();

    public boolean isUsingItem();

    public boolean hasAllowedVehicle();

    public boolean isElytraFlying();

    default public void setRotationYawSafe(float value) {
        float fixedValue = AngleUtils.wrapAngle(this.getRotationYaw(), value);
        this.setRotationYaw(fixedValue);
        if (this.isPassenger()) {
            this.setVehicleYaw(fixedValue);
        }
    }

    default public Rotation getRotation() {
        return new Rotation(this.getRotationYaw(), this.getRotationPitch());
    }
}
