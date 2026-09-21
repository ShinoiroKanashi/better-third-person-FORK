package io.socol.betterthirdperson.impl;

import io.socol.betterthirdperson.api.adapter.IMovementInputAdapter;
import io.socol.betterthirdperson.api.adapter.MutableClientInput;
import net.minecraft.util.math.Vec2f;
import net.minecraft.client.input.Input;
import net.minecraft.util.PlayerInput;

public record MovementInputAdapter(Input input) implements IMovementInputAdapter
{
    @Override
    public boolean isLeftKeyDown() {
        return this.input.playerInput.left();
    }

    @Override
    public void setLeftKeyDown(boolean value) {
        this.input.playerInput = new PlayerInput(this.input.playerInput.forward(), this.input.playerInput.backward(), value, this.input.playerInput.right(), this.input.playerInput.jump(), this.input.playerInput.sneak(), this.input.playerInput.sprint());
    }

    @Override
    public boolean isRightKeyDown() {
        return this.input.playerInput.right();
    }

    @Override
    public void setRightKeyDown(boolean value) {
        this.input.playerInput = new PlayerInput(this.input.playerInput.forward(), this.input.playerInput.backward(), this.input.playerInput.left(), value, this.input.playerInput.jump(), this.input.playerInput.sneak(), this.input.playerInput.sprint());
    }

    @Override
    public boolean isForwardKeyDown() {
        return this.input.playerInput.forward();
    }

    @Override
    public void setForwardKeyDown(boolean value) {
        this.input.playerInput = new PlayerInput(value, this.input.playerInput.backward(), this.input.playerInput.left(), this.input.playerInput.right(), this.input.playerInput.jump(), this.input.playerInput.sneak(), this.input.playerInput.sprint());
    }

    @Override
    public boolean isBackKeyDown() {
        return this.input.playerInput.backward();
    }

    @Override
    public void setBackKeyDown(boolean value) {
        this.input.playerInput = new PlayerInput(this.input.playerInput.forward(), value, this.input.playerInput.left(), this.input.playerInput.right(), this.input.playerInput.jump(), this.input.playerInput.sneak(), this.input.playerInput.sprint());
    }

    @Override
    public float getMoveForward() {
        return this.input.getMovementInput().y;
    }

    @Override
    public void setMoveForward(float value) {
        Vec2f moveVector = new Vec2f(this.input.getMovementInput().x, value);
        ((MutableClientInput)this.input).betterThirdPerson$setMoveVector(moveVector);
    }

    @Override
    public float getMoveStrafe() {
        return this.input.getMovementInput().x;
    }

    @Override
    public void setMoveStrafe(float value) {
        Vec2f moveVector = new Vec2f(value, this.input.getMovementInput().y);
        ((MutableClientInput)this.input).betterThirdPerson$setMoveVector(moveVector);
    }
}
