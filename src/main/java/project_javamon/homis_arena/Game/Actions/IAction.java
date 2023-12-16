package project_javamon.homis_arena.Game.Actions;

import project_javamon.homis_arena.Game.Player;
import project_javamon.homis_arena.Game.Pokemon.PokemonCard;

public interface IAction {
    void TakeAction(PokemonCard cardAttacking);

    // Getters and Setters ==========================================================
    String getActionName();
}
