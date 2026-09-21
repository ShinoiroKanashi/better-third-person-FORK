package io.socol.betterthirdperson.impl;

import io.socol.betterthirdperson.api.adapter.IPlayerAdapter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.equine.Horse;
import net.minecraft.world.entity.animal.pig.Pig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public record PlayerAdapter(Player player) implements IPlayerAdapter
{
    @Override
    public float getRotationYaw() {
        return this.player.getYRot();
    }

    @Override
    public float getRotationPitch() {
        return this.player.getXRot();
    }

    @Override
    public float getPrevRotationYaw() {
        return this.player.yRotO;
    }

    @Override
    public float getPrevRotationPitch() {
        return this.player.xRotO;
    }

    @Override
    public void setRotationYaw(float value) {
        this.player.setYRot(value);
    }

    @Override
    public void setRotationPitch(float pitch) {
        this.player.setXRot(pitch);
    }

    @Override
    public void setPrevRotationYaw(float yaw) {
        this.player.yRotO = yaw;
    }

    @Override
    public void setPrevRotationPitch(float pitch) {
        this.player.xRotO = pitch;
    }

    @Override
    public void setVehicleYaw(float value) {
        this.player.setYHeadRot(value);
        Entity vehicle = this.player.getVehicle();
        if (vehicle != null) {
            vehicle.setYRot(value);
            vehicle.yRotO = value;
        }
    }

    @Override
    public Vec3 getPosition() {
        return this.player.position();
    }

    @Override
    public boolean isPassenger() {
        return this.player.isPassenger();
    }

    @Override
    public boolean isUsingItem() {
        return this.player.isUsingItem();
    }

    @Override
    public boolean hasAllowedVehicle() {
        Entity vehicle = this.player.getVehicle();
        return vehicle instanceof Horse || vehicle instanceof Pig;
    }

    @Override
    public boolean isElytraFlying() {
        return this.player.isFallFlying();
    }
}
