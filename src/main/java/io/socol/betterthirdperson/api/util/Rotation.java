package io.socol.betterthirdperson.api.util;

import io.socol.betterthirdperson.api.adapter.IPlayerAdapter;
import io.socol.betterthirdperson.api.util.AngleUtils;
import io.socol.betterthirdperson.api.util.MathUtils;
import net.minecraft.util.math.Vec3d;

public class Rotation {
    public static final Rotation ZERO = new Rotation(0.0f, 0.0f);
    private final float yaw;
    private final float pitch;

    public Rotation(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = MathUtils.clamp(pitch, -90.0f, 90.0f);
    }

    public void apply(IPlayerAdapter player) {
        player.setRotationYaw(this.yaw);
        player.setRotationPitch(this.pitch);
    }

    public void applySafe(IPlayerAdapter player) {
        player.setRotationYawSafe(this.yaw);
        player.setRotationPitch(this.pitch);
    }

    public void applySafeFully(IPlayerAdapter player) {
        player.setRotationYawSafe(this.yaw);
        player.setPrevRotationYaw(this.yaw);
        player.setRotationPitch(this.pitch);
        player.setPrevRotationPitch(this.pitch);
    }

    public float getYaw() {
        return this.yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public Vec3d asDirection() {
        float pitchRadians = this.pitch * ((float) Math.PI / 180.0F);
        float yawRadians = -this.yaw * ((float) Math.PI / 180.0F);
        double cosYaw = Math.cos(yawRadians);
        double sinYaw = Math.sin(yawRadians);
        double cosPitch = Math.cos(pitchRadians);
        double sinPitch = Math.sin(pitchRadians);
        return new Vec3d(sinYaw * cosPitch, -sinPitch, cosYaw * cosPitch);
    }

    public Rotation add(Rotation rotation) {
        return this.add(rotation.yaw, rotation.pitch);
    }

    public Rotation addScaled(Rotation rotation, float scale) {
        return this.addScaled(rotation.yaw, rotation.pitch, scale);
    }

    public Rotation add(float yaw, float pitch) {
        if (yaw == 0.0f && pitch == 0.0f) {
            return this;
        }
        return new Rotation(this.yaw + yaw, this.pitch + pitch);
    }

    public Rotation addScaled(float yaw, float pitch, float scale) {
        if (yaw == 0.0f && pitch == 0.0f) {
            return this;
        }
        return new Rotation(this.yaw + yaw * scale, this.pitch + pitch * scale);
    }

    public double distanceTo(Rotation rotation) {
        double dy = AngleUtils.getDelta(this.yaw, rotation.getYaw());
        double dp = rotation.getPitch() - this.pitch;
        return Math.sqrt(dy * dy + dp * dp);
    }

    public static Rotation fromPlayer(IPlayerAdapter player) {
        return new Rotation(player.getRotationYaw(), player.getRotationPitch());
    }

    public static Rotation fromDirection(double x, double y, double z) {
        double dist = Math.sqrt(x * x + z * z);
        float yaw = (float)(Math.atan2(z, x) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(-(Math.atan2(y, dist) * 180.0 / Math.PI));
        return new Rotation(yaw, pitch);
    }

    public Rotation interpolate(Rotation target, float progress) {
        return new Rotation(AngleUtils.smoothAngle(progress, this.yaw, target.yaw), this.pitch + (target.pitch - this.pitch) * progress);
    }

    public Rotation clampedInterpolate(Rotation target, float progress, float maxAngle) {
        return new Rotation(AngleUtils.stepAngle(progress, maxAngle, this.yaw, target.yaw), AngleUtils.stepAngle(progress, maxAngle, this.pitch, target.pitch));
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        Rotation rotation = (Rotation)o;
        return AngleUtils.equals(rotation.yaw, this.yaw) && AngleUtils.equals(rotation.pitch, this.pitch);
    }

    public boolean isZero() {
        return this.yaw == 0.0f && this.pitch == 0.0f;
    }

    public double size() {
        return Math.sqrt(this.yaw * this.yaw + this.pitch * this.pitch);
    }

    public String toString() {
        return "(" + this.yaw + ", " + this.pitch + ")";
    }

    public Rotation addYaw(float yaw) {
        if (yaw == 0.0f) {
            return this;
        }
        return new Rotation(this.yaw + yaw, this.pitch);
    }

    public Rotation addPitch(float pitch) {
        if (pitch == 0.0f) {
            return this;
        }
        return new Rotation(this.yaw, this.pitch + pitch);
    }

    public Rotation withYaw(float yaw) {
        return new Rotation(yaw, this.pitch);
    }

    public Rotation withPitch(float pitch) {
        return new Rotation(this.yaw, pitch);
    }
}
