package project_javamon.homis_arena.Game.Actions;

import project_javamon.homis_arena.Game.Player;
import project_javamon.homis_arena.Game.Pokemon.Card;
import project_javamon.homis_arena.Game.Pokemon.PokemonCard;

public class DamageBoost implements IAction{
    int dmg;
    int multiplication;

    public DamageBoost(int dmg, int multiplication) {
        this.dmg = dmg;
        this.multiplication = multiplication;
    }

    @Override
    public void TakeAction(Card cardActing) {

    }

    @Override
    public String getActionName() {
        return null;
    }
}
