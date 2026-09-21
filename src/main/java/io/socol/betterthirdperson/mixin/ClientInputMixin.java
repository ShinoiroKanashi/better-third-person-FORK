package io.socol.betterthirdperson.mixin;

import io.socol.betterthirdperson.api.adapter.MutableClientInput;
import net.minecraft.util.math.Vec2f;
import net.minecraft.client.input.Input;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value={Input.class})
public class ClientInputMixin
implements MutableClientInput {
    @Shadow
    protected Vec2f movementVector;

    @Override
    public void betterThirdPerson$setMoveVector(Vec2f vector) {
        this.movementVector = vector.normalize();
    }
}
