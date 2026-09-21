package io.socol.betterthirdperson.api;

import io.socol.betterthirdperson.api.action.MouseAction;
import java.util.ArrayDeque;
import java.util.Queue;

public class DelayedActionManager {
    private final Queue<MouseAction> actionQueue = new ArrayDeque<MouseAction>();
    private boolean replayingActions = false;

    public void writeAction(MouseAction action) {
        this.actionQueue.add(action);
    }

    public void replayActions() {
        this.replayingActions = true;
        try {
            while (!this.actionQueue.isEmpty()) {
                this.actionQueue.remove().play();
            }
        }
        finally {
            this.replayingActions = false;
        }
    }

    public boolean isReplayingActions() {
        return this.replayingActions;
    }
}
