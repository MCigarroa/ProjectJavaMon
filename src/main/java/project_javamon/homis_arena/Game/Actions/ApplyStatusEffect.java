package project_javamon.homis_arena.Game.Actions;

import project_javamon.homis_arena.Controller.GameController;
import project_javamon.homis_arena.Game.Game;
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
    public void TakeAction(PokemonCard cardAttacking) {
        PokemonCard cardDefending = (PokemonCard) (cardAttacking.getPlayerOwner() == Game.getWaitingPlayer()
                ? Game.getActivePlayer().getActive().getFirst()
                : Game.getWaitingPlayer().getActive().getFirst());
        cardDefending.addStatusEffects(statusEffect);
    }

    @Override
    public String getActionName() {
        return null;
    }
}
