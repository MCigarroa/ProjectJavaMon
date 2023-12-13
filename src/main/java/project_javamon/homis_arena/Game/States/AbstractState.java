package project_javamon.homis_arena.Game.States;

import project_javamon.homis_arena.Game.GameState;
import project_javamon.homis_arena.Main;

public abstract class AbstractState {
    private GameState gameState;
    public AbstractState() {
        this.gameState = Main.getGameState();
    }
    public abstract void onStateChange();
    public abstract boolean isStateComplete();
    public abstract AbstractState lastState();
    public abstract AbstractState nextState();

}
