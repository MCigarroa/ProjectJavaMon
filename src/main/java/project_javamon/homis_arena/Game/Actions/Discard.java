package project_javamon.homis_arena.Game.Actions;

import project_javamon.homis_arena.Game.Game;
import project_javamon.homis_arena.Game.Player;
import project_javamon.homis_arena.Game.Pokemon.Card;
import project_javamon.homis_arena.Game.Pokemon.PokemonCard;
import project_javamon.homis_arena.Util.CardPosition;

public class Discard implements IAction{
    String name;
    int amount;
    boolean both;

    public Discard(String name, int amount, boolean both) {
        this.name = name;
        this.amount = amount;
        this.both = both;
    }

    @Override
    public void TakeAction(Card cardActing) {
        if (-1 == amount) {
            amount = cardActing.getPlayerOwner().getHand().size();
        }
        for (int index = 0; index < amount; index++) {
            cardActing.getPlayerOwner().getHand().removeFirst();
        }
        if (both) {
            Player player = cardActing.getPlayerOwner() == Game.getWaitingPlayer()
                    ? Game.getActivePlayer()
                    : Game.getWaitingPlayer();
            amount = player.getHand().size();
            for (int index = 0; index < amount; index++) {
                player.getHand().removeFirst();
            }
        }
        if (cardActing.getName() == "professor_oak") {
            new Draw("Drawing",7).TakeAction(cardActing);
        }
        cardActing.getPlayerOwner().removeFromPrevious(cardActing, CardPosition.DISCARD);
    }

    @Override
    public String getActionName() {
        return name;
    }

    // Getters and Setters ==========================================================

}
