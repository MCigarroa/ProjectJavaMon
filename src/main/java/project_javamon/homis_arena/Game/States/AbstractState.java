package project_javamon.homis_arena.Game.States;

import project_javamon.homis_arena.Game.GameState;
import project_javamon.homis_arena.Main;

public abstract class AbstractState {
    private GameState gameState = Main.getGameState();
    public AbstractState() {
    }
    public abstract void onStateChange();
    public abstract boolean isStateComplete();
    public abstract boolean isMoveLegal();
    public abstract AbstractState lastState();
    public abstract AbstractState nextState();

}
