package io.socol.betterthirdperson.api.action;

import io.socol.betterthirdperson.api.action.MouseAction;

import io.socol.betterthirdperson.api.adapter.IClientAdapter;
import java.util.function.IntSupplier;

public class ItemRepeatableUseAction
extends MouseAction {
    private final IClientAdapter client;
    private final IntSupplier itemUseCooldown;

    public ItemRepeatableUseAction(IClientAdapter client, IntSupplier itemUseCooldown, Runnable action) {
        super(action);
        this.client = client;
        this.itemUseCooldown = itemUseCooldown;
    }

    @Override
    public void play() {
        if (this.client.isUsePressed() && this.itemUseCooldown.getAsInt() == 0 && !this.client.getPlayer().isUsingItem()) {
            super.play();
        }
    }
}
