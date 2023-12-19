package project_javamon.homis_arena.Game.Actions;

import project_javamon.homis_arena.Game.Pokemon.Card;
import project_javamon.homis_arena.Util.CardPosition;

public class Draw implements IAction {
    String name;
    int amount;


    public Draw(String name,int amount) {
        this.name = name;
        this.amount = amount;
    }

    @Override
    public void TakeAction(Card cardActing) {
        for (int index = 0; index < amount; index++) {
            cardActing.getPlayerOwner().drawCard();
        }
        cardActing.getPlayerOwner().removeFromPrevious(cardActing, CardPosition.DISCARD);
    }

    @Override
    public String getActionName() {
        return name;
    }




}
