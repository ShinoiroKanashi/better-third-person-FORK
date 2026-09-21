package io.socol.betterthirdperson.api.util;

import io.socol.betterthirdperson.api.adapter.IMovementInputAdapter;
import io.socol.betterthirdperson.api.util.AngleUtils;

public enum WalkDirection {
    BACK(-180.0f, false, false, false, true),
    BACK_LEFT(-135.0f, true, false, false, true),
    LEFT(-90.0f, true, false, false, false),
    FORWARD_LEFT(-45.0f, true, false, true, false),
    FORWARD(0.0f, false, false, true, false),
    FORWARD_RIGHT(45.0f, false, true, true, false),
    RIGHT(90.0f, false, true, false, false),
    BACK_RIGHT(135.0f, false, true, false, true);

    private final float angle;
    private final boolean left;
    private final boolean right;
    private final boolean forward;
    private final boolean back;
    private final boolean diagonal;

    private WalkDirection(float angle, boolean left, boolean right, boolean forward, boolean back) {
        this.angle = angle;
        this.left = left;
        this.right = right;
        this.forward = forward;
        this.back = back;
        this.diagonal = (this.ordinal() & 1) == 1;
    }

    public float getAngle() {
        return this.angle;
    }

    public void setup(IMovementInputAdapter input) {
        this.applyKeys(input);
        input.setMoveStrafe(this.left || this.right ? (this.left ? 1.0f : -1.0f) : 0.0f);
        input.setMoveForward(this.forward || this.back ? (this.forward ? 1.0f : -1.0f) : 0.0f);
    }

    public void applyKeys(IMovementInputAdapter input) {
        input.setLeftKeyDown(this.left);
        input.setRightKeyDown(this.right);
        input.setForwardKeyDown(this.forward);
        input.setBackKeyDown(this.back);
    }

    public boolean isDiagonal() {
        return this.diagonal;
    }

    public float distanceTo(float angle) {
        return Math.abs(AngleUtils.normalize(angle - this.angle));
    }

    public double maxImpulse() {
        return this.diagonal ? 1.41421356237 : 1.0;
    }

    public static WalkDirection byKeys(boolean left, boolean right, boolean forward, boolean back) {
        if (forward != back) {
            if (left == right) {
                return forward ? FORWARD : BACK;
            }
            if (forward) {
                return left ? FORWARD_LEFT : FORWARD_RIGHT;
            }
            return left ? BACK_LEFT : BACK_RIGHT;
        }
        if (left != right) {
            return left ? LEFT : RIGHT;
        }
        return FORWARD;
    }

    public static WalkDirection byInput(IMovementInputAdapter input) {
        return WalkDirection.byKeys(input.isLeftKeyDown(), input.isRightKeyDown(), input.isForwardKeyDown(), input.isBackKeyDown());
    }

    public static WalkDirection approximate(float angle) {
        for (WalkDirection dir : WalkDirection.values()) {
            if (!((double)dir.distanceTo(angle) <= 22.5)) continue;
            return dir;
        }
        return FORWARD;
    }

    public static WalkDirection toLeftOf(float angle) {
        angle = AngleUtils.normalize(angle);
        return WalkDirection.byIndex(FORWARD.ordinal() + (int)Math.floor(angle / 45.0f));
    }

    public static WalkDirection toRightOf(float angle) {
        angle = AngleUtils.normalize(angle);
        return WalkDirection.byIndex(FORWARD.ordinal() + (int)Math.ceil(angle / 45.0f));
    }

    public static WalkDirection byIndex(int index) {
        return WalkDirection.values()[index % WalkDirection.values().length];
    }
}
