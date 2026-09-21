package io.socol.betterthirdperson;

import io.socol.betterthirdperson.api.CustomCameraManager;
import io.socol.betterthirdperson.impl.ClientAdapter;

public class BetterThirdPerson {
    public static final String MOD_ID = "betterthirdperson";
    private static CustomCameraManager manager = new CustomCameraManager(ClientAdapter.INSTANCE);

    public static CustomCameraManager getCameraManager() {
        return manager;
    }
}
