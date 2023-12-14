package project_javamon.homis_arena.Game.Actions;

import project_javamon.homis_arena.Controller.GameController;
import project_javamon.homis_arena.Game.Player;
import project_javamon.homis_arena.Game.Pokemon.PokemonCard;
import project_javamon.homis_arena.Main;
import project_javamon.homis_arena.Util.StatusEffect;

public class ApplyStatusEffect implements IAction{
    GameController gameController = Main.getGameController();
    StatusEffect statusEffect;

    public ApplyStatusEffect(StatusEffect statusEffect) {
        this.statusEffect = statusEffect;
    }
    @Override
    public void TakeAction(PokemonCard cardAttacking, PokemonCard cardDefending, Player playerAttacking, Player playerDefending) {
        cardDefending.addStatusEffects(statusEffect);
    }

    @Override
    public String getActionName() {
        return null;
    }
}
