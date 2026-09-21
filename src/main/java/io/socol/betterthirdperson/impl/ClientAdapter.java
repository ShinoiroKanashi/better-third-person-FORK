package io.socol.betterthirdperson.impl;

import io.socol.betterthirdperson.api.adapter.IClientAdapter;
import io.socol.betterthirdperson.api.adapter.IPlayerAdapter;
import io.socol.betterthirdperson.impl.PlayerAdapter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.client.MinecraftClient;

public class ClientAdapter
implements IClientAdapter {
    public static final ClientAdapter INSTANCE = new ClientAdapter();

    private ClientAdapter() {
    }

    @Override
    public boolean isFirstPerson() {
        return MinecraftClient.getInstance().options.getPerspective().isFirstPerson();
    }

    @Override
    public boolean isCameraOnPlayer() {
        return MinecraftClient.getInstance().getCameraEntity() == null || MinecraftClient.getInstance().getCameraEntity() == MinecraftClient.getInstance().player;
    }

    @Override
    public boolean isCameraMirrored() {
        return MinecraftClient.getInstance().options.getPerspective().isFrontView();
    }

    @Override
    public IPlayerAdapter getPlayer() {
        return new PlayerAdapter((PlayerEntity)MinecraftClient.getInstance().player);
    }

    @Override
    public boolean isUsePressed() {
        return MinecraftClient.getInstance().options.useKey.isPressed();
    }

    @Override
    public boolean isAttackPressed() {
        return MinecraftClient.getInstance().options.attackKey.isPressed();
    }

    @Override
    public void updateHitResult() {
        MinecraftClient.getInstance().gameRenderer.updateCrosshairTarget(1.0f);
    }
}
