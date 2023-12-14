package project_javamon.homis_arena.Game.States;

import project_javamon.homis_arena.Game.GameState;

public class InitState extends AbstractState {

    public InitState(){
        super();
    }

    @Override
    public void onStateChange() {
    }

    @Override
    public boolean isStateComplete() {
        return false;
    }

    @Override
    public boolean isMoveLegal() {

        return true;
    }

    @Override
    public AbstractState lastState() {
        return null;
    }

    @Override
    public AbstractState nextState() {
        return null;
    }
}
