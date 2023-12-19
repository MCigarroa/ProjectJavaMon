package project_javamon.homis_arena.Game.Pokemon;

import project_javamon.homis_arena.Game.Actions.IAction;

import java.util.ArrayList;

public class TrainerCard extends Card {
    private transient ArrayList<IAction> iAction = new ArrayList<>();

    public ArrayList<IAction> getiAction() {
        return iAction;
    }

    public void setiAction(ArrayList<IAction> iAction) {
        this.iAction = iAction;
    }
    public void addiAction(IAction iAction) {
        this.iAction.add(iAction);
    }
}
