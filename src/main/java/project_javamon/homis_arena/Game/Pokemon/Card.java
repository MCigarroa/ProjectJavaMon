package project_javamon.homis_arena.Game.Pokemon;

import project_javamon.homis_arena.Util.CardPosition;

public abstract class Card {
    private String name;
    private String image;

    private static int uniqueNum = 0;
    private final String cardID;

    // Utility - to track card movements
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

    @Override
    public String toString() {
        return "Name: " + name + " CardID: " + cardID + " CardPosition: " + cardPosition;
    }
}