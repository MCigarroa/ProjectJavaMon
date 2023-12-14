package project_javamon.homis_arena.Game;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import project_javamon.homis_arena.Game.Pokemon.Card;
import project_javamon.homis_arena.Util.CardPosition;

import java.util.ArrayList;

public class Player {
    static int playerNumber = 1;
    String name;
    private ObservableList<Card> deck = FXCollections.observableArrayList();
    private ObservableList<Card> hand = FXCollections.observableArrayList();
    private ObservableList<Card> bench = FXCollections.observableArrayList();
    private ObservableList<Card> active = FXCollections.observableArrayList();
    private ObservableList<Card> discard = FXCollections.observableArrayList();
    private ObservableList<Card> prize = FXCollections.observableArrayList();

    public Player() {
        this.name = "Player " + playerNumber;
        playerNumber++;
    }

    public void drawCard(){
        if ( deck != null && !deck.isEmpty()){
            Card card = deck.removeLast();
            card.setCardPositions(CardPosition.HAND);
            hand.add(card);
        }
    }


    public void printPlayerCards() {
        System.out.println("Player Name: " + name);

        printCardList("Deck", deck);
        printCardList("Hand", hand);
        printCardList("Bench", bench);
        printCardList("Active", active);
        printCardList("Discard", discard);
        printCardList("Prize", prize);
    }

    private void printCardList(String listName, ObservableList<Card> cards) {
        System.out.println(listName + ":");
        if (cards.isEmpty()) {
            System.out.println("  [No cards]");
        } else {
            for (Card card : cards) {
                System.out.println("  " + card.toString());
            }
        }
    }

    public Card findCardById(String cardId) {
        ArrayList<ObservableList<Card>> allCardLists = new ArrayList<>();
        allCardLists.add(hand);
        allCardLists.add(bench);
        allCardLists.add(active);
        allCardLists.add(discard);
        allCardLists.add(prize);

        for (ObservableList<Card> cardList : allCardLists) {
            for (Card card : cardList) {
                if (card.getCardID().equals(cardId)) {
                    return card;
                }
            }
        }
        throw new RuntimeException("Couldn't find the Card: " + cardId);
    }


    public static int getPlayerNumber() {
        return playerNumber;
    }

    public static void setPlayerNumber(int playerNumber) {
        Player.playerNumber = playerNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ObservableList<Card> getDeck() {
        return deck;
    }

    public void setDeck(ObservableList<Card> deck) {
        this.deck = deck;
    }

    public ObservableList<Card> getHand() {
        return hand;
    }

    public ObservableList<Card> getBench() {
        return bench;
    }

    public ObservableList<Card> getActive() {
        return active;
    }

    public ObservableList<Card> getDiscard() {
        return discard;
    }

    public ObservableList<Card> getPrize() {
        return prize;
    }

    // Utilities
    public void removeFromPrevious(Card card, CardPosition newCardPosition) {
        CardPosition previousPosition = card.getCardPosition();
        card.setFormerCardPosition(previousPosition);
        card.setCardPositions(newCardPosition);

        System.out.println("Removing card from previous position: " + previousPosition);

        ObservableList<Card> previousList = switch (previousPosition) {
            case HAND -> hand;
            case PRIZE -> prize;
            case DISCARD -> discard;
            case ACTIVE -> active;
            case BENCH -> bench;
            case DECK -> deck;
            case UNKNOWN -> {
                System.out.println("Error: Unknown previous position for card " + card.getCardID());
                yield null;
            }
        };

        if (previousList != null && !previousList.remove(card)) {
            System.out.println("Error: Failed to remove card from " + previousPosition + " for card " + card.getCardID());
        }
    }
}
