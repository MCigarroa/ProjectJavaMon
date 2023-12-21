package project_javamon.homis_arena.Game.Pokemon;

import project_javamon.homis_arena.Game.Actions.IAction;
import project_javamon.homis_arena.Game.Player;
import project_javamon.homis_arena.Util.CardPosition;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class Card {
    private String name;
    private String image;

    private static int uniqueNum = 0;
    private final String cardID;

    private transient ArrayList<IAction> iAction = new ArrayList<>();

    // Utility - to track card movements
    private transient Player playerOwner;

    private CardPosition cardPosition = CardPosition.DECK;
    private CardPosition formerCardPosition = CardPosition.DECK;

    protected Card() {
        this.cardID = "cardID" + uniqueNum;
        uniqueNum++;
    }

    public String getCardID() {
        return cardID;
    }
    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }
    public CardPosition getCardPosition() {
        return cardPosition;
    }

    public void setCardPositions(CardPosition cardPosition){
        this.cardPosition = cardPosition;
    }

    public CardPosition getFormerCardPosition() {
        return formerCardPosition;
    }

    public void setFormerCardPosition(CardPosition formerCardPosition) {
        this.formerCardPosition = formerCardPosition;
    }
    public ArrayList<IAction> getiAction() {
        return iAction;
    }

    public void setiAction(ArrayList<IAction> iAction) {
        this.iAction = iAction;
    }

    public void addiAction(IAction iAction) {
        this.iAction.add(iAction);
    }

    public Player getPlayerOwner() {
        return playerOwner;
    }
    public void setPlayerOwner(Player player) {
        this.playerOwner = player;
    }

    @Override
    public String toString() {
        return "Name: " + name + " CardID: " + cardID + " CardPosition: " + cardPosition;
    }


}