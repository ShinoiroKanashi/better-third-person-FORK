package io.socol.betterthirdperson.api.adapter;

import io.socol.betterthirdperson.api.adapter.IPlayerAdapter;


public interface IClientAdapter {
    public boolean isFirstPerson();

    public boolean isCameraMirrored();

    public void updateHitResult();

    public boolean isUsePressed();

    public boolean isAttackPressed();

    default public boolean isMousePressed() {
        return this.isUsePressed() || this.isAttackPressed();
    }

    public IPlayerAdapter getPlayer();

    public boolean isCameraOnPlayer();
}
