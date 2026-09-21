package io.socol.betterthirdperson.impl;

import io.socol.betterthirdperson.api.adapter.IMovementInputAdapter;
import io.socol.betterthirdperson.api.adapter.MutableClientInput;
import net.minecraft.client.player.ClientInput;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.phys.Vec2;

public record MovementInputAdapter(ClientInput input) implements IMovementInputAdapter
{
    @Override
    public boolean isLeftKeyDown() {
        return this.input.keyPresses.left();
    }

    @Override
    public void setLeftKeyDown(boolean value) {
        this.input.keyPresses = new Input(this.input.keyPresses.forward(), this.input.keyPresses.backward(), value, this.input.keyPresses.right(), this.input.keyPresses.jump(), this.input.keyPresses.shift(), this.input.keyPresses.sprint());
    }

    @Override
    public boolean isRightKeyDown() {
        return this.input.keyPresses.right();
    }

    @Override
    public void setRightKeyDown(boolean value) {
        this.input.keyPresses = new Input(this.input.keyPresses.forward(), this.input.keyPresses.backward(), this.input.keyPresses.left(), value, this.input.keyPresses.jump(), this.input.keyPresses.shift(), this.input.keyPresses.sprint());
    }

    @Override
    public boolean isForwardKeyDown() {
        return this.input.keyPresses.forward();
    }

    @Override
    public void setForwardKeyDown(boolean value) {
        this.input.keyPresses = new Input(value, this.input.keyPresses.backward(), this.input.keyPresses.left(), this.input.keyPresses.right(), this.input.keyPresses.jump(), this.input.keyPresses.shift(), this.input.keyPresses.sprint());
    }

    @Override
    public boolean isBackKeyDown() {
        return this.input.keyPresses.backward();
    }

    @Override
    public void setBackKeyDown(boolean value) {
        this.input.keyPresses = new Input(this.input.keyPresses.forward(), value, this.input.keyPresses.left(), this.input.keyPresses.right(), this.input.keyPresses.jump(), this.input.keyPresses.shift(), this.input.keyPresses.sprint());
    }

    @Override
    public float getMoveForward() {
        return this.input.getMoveVector().y;
    }

    @Override
    public void setMoveForward(float value) {
        Vec2 moveVector = new Vec2(this.input.getMoveVector().x, value);
        ((MutableClientInput)this.input).betterThirdPerson$setMoveVector(moveVector);
    }

    @Override
    public float getMoveStrafe() {
        return this.input.getMoveVector().x;
    }

    @Override
    public void setMoveStrafe(float value) {
        Vec2 moveVector = new Vec2(value, this.input.getMoveVector().y);
        ((MutableClientInput)this.input).betterThirdPerson$setMoveVector(moveVector);
    }
}
