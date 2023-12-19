package project_javamon.homis_arena.Game.Actions;

import project_javamon.homis_arena.Game.Player;
import project_javamon.homis_arena.Game.Pokemon.Card;
import project_javamon.homis_arena.Game.Pokemon.PokemonCard;

public class Heal implements IAction {
    int quantity;

    public Heal(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public void TakeAction(Card cardActing) {

    }

    @Override
    public String getActionName() {
        return null;
    }
}
