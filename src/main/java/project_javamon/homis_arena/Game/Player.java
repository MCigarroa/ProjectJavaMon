package project_javamon.homis_arena.Game;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import project_javamon.homis_arena.Game.Pokemon.Card;

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

    public Player(ObservableList<Card> deck) {
        this.name = "Player " + playerNumber;
        playerNumber++;
        this.deck = deck;
    }
    public Player(){
        this.name = "Player " + playerNumber;
        playerNumber++;
        this.deck = FXCollections.observableArrayList();;
    }

    public Card drawCard(){
        if ( deck != null && !deck.isEmpty()){
            Card card = deck.getLast();
            hand.add(card);
            return card;
        }
        return null;
    }

    public void printPlayerCards() {

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

    public ObservableList<Card> getDiscardPile() {
        return discard;
    }

    public ObservableList<Card> getPrize() {
        return prize;
    }

    // Utilities
    public void RemoveFromPrevious(Card card) {
        card.setFormerCardPosition(card.getCardPostition());
        switch (card.getCardPostition()) {
            case HAND -> hand.remove(card);
            case PRIZE -> prize.remove(card);
            case DISCARD -> discard.remove(card);
            case ACTIVE -> active.remove(card);
            case BENCH -> bench.remove(card);
            case DECK -> deck.remove(card);
            default -> throw new RuntimeException("Couldn't remove card from Previous: " + card);
        }
        System.out.println("Failed to remove card from HAND:");
        System.out.println(card);
    }

    public void addToActive(Card card) {
    }
}
