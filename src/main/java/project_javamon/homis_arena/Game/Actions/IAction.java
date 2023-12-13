package project_javamon.homis_arena.Game.Actions;

import project_javamon.homis_arena.Game.Player;
import project_javamon.homis_arena.Game.Pokemon.Card;
import project_javamon.homis_arena.Game.Pokemon.PokemonCard;

public interface IAction {
    void TakeAction(PokemonCard cardAttacking, PokemonCard cardDefending, Player playerAttacking, Player playerDefending);

    String getAttackName();
}
