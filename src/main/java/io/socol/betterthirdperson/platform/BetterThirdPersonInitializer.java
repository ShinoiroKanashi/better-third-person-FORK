package io.socol.betterthirdperson.platform;

import io.socol.betterthirdperson.BetterThirdPerson;
import io.socol.betterthirdperson.integration.cloth.ClothModConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class BetterThirdPersonInitializer
implements ClientModInitializer {
    public void onInitializeClient() {
        if (FabricLoader.getInstance().isModLoaded("cloth-config")) {
            BetterThirdPerson.getCameraManager().setConfig(ClothModConfig.create());
        }
    }
}
