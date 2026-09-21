package io.socol.betterthirdperson.impl;

import io.socol.betterthirdperson.api.adapter.IPlayerAdapter;
import io.socol.betterthirdperson.impl.PlayerAdapter;
import io.socol.betterthirdperson.mixin.MinecraftAccessor;

import io.socol.betterthirdperson.api.adapter.IClientAdapter;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

public class ClientAdapter
implements IClientAdapter {
    public static final ClientAdapter INSTANCE = new ClientAdapter();

    private ClientAdapter() {
    }

    @Override
    public boolean isFirstPerson() {
        return Minecraft.getInstance().options.getCameraType().isFirstPerson();
    }

    @Override
    public boolean isCameraOnPlayer() {
        return Minecraft.getInstance().getCameraEntity() == null || Minecraft.getInstance().getCameraEntity() == Minecraft.getInstance().player;
    }

    @Override
    public boolean isCameraMirrored() {
        return Minecraft.getInstance().options.getCameraType().isMirrored();
    }

    @Override
    public IPlayerAdapter getPlayer() {
        return new PlayerAdapter((Player)Minecraft.getInstance().player);
    }

    @Override
    public boolean isUsePressed() {
        return Minecraft.getInstance().options.keyUse.isDown();
    }

    @Override
    public boolean isAttackPressed() {
        return Minecraft.getInstance().options.keyAttack.isDown();
    }

    @Override
    public void updateHitResult() {
        ((MinecraftAccessor) (Object) Minecraft.getInstance()).betterThirdPerson$pick(1.0f);
    }
}
