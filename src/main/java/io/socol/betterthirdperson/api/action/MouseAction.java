package io.socol.betterthirdperson.api.action;

public class MouseAction {
    private final Runnable action;

    public MouseAction(Runnable action) {
        this.action = action;
    }

    public void play() {
        this.action.run();
    }
}
