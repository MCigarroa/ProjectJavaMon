package project_javamon.homis_arena.Game.Actions;

import project_javamon.homis_arena.Game.Player;
import project_javamon.homis_arena.Game.Pokemon.PokemonCard;

public class Discard implements IAction{
    String discard;
    int quantity;
    String energyType;

    public Discard() {

    }


    @Override
    public void TakeAction(PokemonCard cardAttacking) {

    }

    // Getters and Setters ==========================================================
    @Override
    public String getActionName() {
        return discard;
    }
}
