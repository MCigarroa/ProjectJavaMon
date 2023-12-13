package project_javamon.homis_arena.Game.Actions;

import project_javamon.homis_arena.Game.Player;
import project_javamon.homis_arena.Game.Pokemon.Card;

public interface IAction {
    void TakeAction(Card card, Player playerAttacking, Player playerDefending);

    String getAttackName();
}
