package project_javamon.homis_arena.Game.Actions;

import project_javamon.homis_arena.Game.Pokemon.Card;
import project_javamon.homis_arena.Game.Pokemon.PokemonCard;

public interface IAction {
    void TakeAction(Card cardActing);

    // Getters and Setters ==========================================================
    String getActionName();
}
