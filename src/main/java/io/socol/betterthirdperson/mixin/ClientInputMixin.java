package io.socol.betterthirdperson.mixin;

import io.socol.betterthirdperson.api.adapter.MutableClientInput;
import net.minecraft.client.player.ClientInput;
import net.minecraft.world.phys.Vec2;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value={ClientInput.class})
public class ClientInputMixin
implements MutableClientInput {
    @Shadow
    protected Vec2 moveVector;

    @Override
    public void betterThirdPerson$setMoveVector(Vec2 vector) {
        this.moveVector = vector.normalized();
    }
}
