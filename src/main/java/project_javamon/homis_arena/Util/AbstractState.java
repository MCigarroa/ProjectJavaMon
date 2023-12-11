package project_javamon.homis_arena.Util;

public abstract class AbstractState {
    public abstract boolean isStateComplete();
    public abstract AbstractState lastState();
    public abstract AbstractState nextState();
    public abstract void updateState();


    String name;
    String description;
    public AbstractState(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
