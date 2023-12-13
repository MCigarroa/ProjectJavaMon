package project_javamon.homis_arena.Game;

import javafx.collections.ObservableList;
import javafx.collections.ListChangeListener;
import javafx.collections.FXCollections;
import project_javamon.homis_arena.Controller.GameController;
import project_javamon.homis_arena.Game.Pokemon.Card;
import project_javamon.homis_arena.Game.Pokemon.EnergyCard;
import project_javamon.homis_arena.Main;
import project_javamon.homis_arena.Util.CardPosition;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Player {
    static int playerNumber = 1;
    String name;
    private ObservableList<Card> deck = FXCollections.observableArrayList();
    private ObservableList<Card> hand = FXCollections.observableArrayList();
    private ObservableList<Card> bench = FXCollections.observableArrayList();
    private ObservableList<Card> active = FXCollections.observableArrayList();
    private ObservableList<Card> discard = FXCollections.observableArrayList();
    private ObservableList<Card> prize = FXCollections.observableArrayList();
    private HashMap<String, Integer> energyBanked;

    GameController gameController = Main.getMainGameController();

    public Player() {
        this.name = "Player " + playerNumber;
        playerNumber++;
    }

    public Card drawCard(){
        if ( deck != null && !deck.isEmpty()){
            Card card = deck.removeLast();
            card.setCardPositions(CardPosition.HAND);
            hand.add(card);
            return card;
        }
        return null;
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

    private final String[] types = {"fire", "water", "grass", "colorless",
            "psychic", "fighting", "darkness", "metal", "fairy"};
    public HashMap<String, Integer> getEnergyBanked() {
        // sums count of types to array, to perform attack
        HashMap<String, Integer> typeCounts = new HashMap<>();
        for (String type : types) {
            typeCounts.put(type, 0);
        }

        for (Card card : active) {
            if (card instanceof EnergyCard){
                String type = ((EnergyCard) card).getType();
                if (typeCounts.containsKey(type)) {
                    typeCounts.put(type, typeCounts.get(type) + 1);
                }
            }
        }
        return typeCounts;
    }

    public void setEnergyBanked(HashMap<String, Integer> energyBanked) {
        this.energyBanked = energyBanked;
    }

    // Utilities
    public void RemoveFromPrevious(Card card, CardPosition newCardPosition) {
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

    public void setGameController(GameController gameController) {
        System.out.println("Setting GameController in Player: " + gameController);
        this.gameController = gameController;
        //setupHandListener();
    }

    private void setupHandListener() {
        this.hand.addListener((ListChangeListener<Card>) change -> {
            while (change.next()) {
                if (change.wasAdded() || change.wasRemoved()) {
                    if (gameController != null) {
                        gameController.updateHandUI();
                    } else {
                        System.out.println("Error: GameController is null when trying to update UI");
                    }
                }
            }
        });
    }
}
