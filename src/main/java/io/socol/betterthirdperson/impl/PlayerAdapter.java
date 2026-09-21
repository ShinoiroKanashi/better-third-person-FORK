package io.socol.betterthirdperson.impl;

import io.socol.betterthirdperson.api.adapter.IPlayerAdapter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

public record PlayerAdapter(PlayerEntity player) implements IPlayerAdapter
{
    @Override
    public float getRotationYaw() {
        return this.player.getYaw();
    }

    @Override
    public float getRotationPitch() {
        return this.player.getPitch();
    }

    @Override
    public float getPrevRotationYaw() {
        return this.player.lastYaw;
    }

    @Override
    public float getPrevRotationPitch() {
        return this.player.lastPitch;
    }

    @Override
    public void setRotationYaw(float value) {
        this.player.setYaw(value);
    }

    @Override
    public void setRotationPitch(float pitch) {
        this.player.setPitch(pitch);
    }

    @Override
    public void setPrevRotationYaw(float yaw) {
        this.player.lastYaw = yaw;
    }

    @Override
    public void setPrevRotationPitch(float pitch) {
        this.player.lastPitch = pitch;
    }

    @Override
    public void setVehicleYaw(float value) {
        this.player.setHeadYaw(value);
        Entity vehicle = this.player.getVehicle();
        if (vehicle != null) {
            vehicle.setYaw(value);
            vehicle.lastYaw = value;
        }
    }

    @Override
    public Vec3d getPosition() {
        return this.player.getPos();
    }

    @Override
    public boolean isPassenger() {
        return this.player.hasVehicle();
    }

    @Override
    public boolean isUsingItem() {
        return this.player.isUsingItem();
    }

    @Override
    public boolean hasAllowedVehicle() {
        Entity vehicle = this.player.getVehicle();
        return vehicle instanceof HorseEntity || vehicle instanceof PigEntity;
    }

    @Override
    public boolean isElytraFlying() {
        return this.player.isGliding();
    }
}
