package io.socol.betterthirdperson.integration.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import io.socol.betterthirdperson.integration.cloth.ClothModConfig;
import me.shedaniel.autoconfig.AutoConfigClient;
import net.minecraft.client.gui.screens.Screen;

public class ModMenuIntegration
implements ModMenuApi {
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> (Screen)AutoConfigClient.getConfigScreen(ClothModConfig.class, (Screen)parent).get();
    }
}
