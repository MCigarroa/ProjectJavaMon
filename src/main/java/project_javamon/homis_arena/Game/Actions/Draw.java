package project_javamon.homis_arena.Game.Actions;

import project_javamon.homis_arena.Game.Player;
import project_javamon.homis_arena.Game.Pokemon.PokemonCard;

public class Draw implements IAction {
    int quantity;
    String type;


    public Draw(int quantity, String type) {
        this.quantity = quantity;
        this.type = type;
    }

    @Override
    public void TakeAction(PokemonCard cardAttacking, PokemonCard cardDefending, Player playerAttacking, Player playerDefending) {
        // This might have to be a recursive call
    }

    @Override
    public String getActionName() {
        return null;
    }
}
