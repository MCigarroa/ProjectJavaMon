package project_javamon.homis_arena.Game;

import project_javamon.homis_arena.Util.GsonParser;

import java.io.File;
import java.util.ArrayList;

public class Game {
    private static ArrayList<Player> playerList;
    private Player player1;
    private Player player2;
    private static Player activePlayer;
    private static Player waitingPlayer;

    private int turnNumber;

    public Game(){
        player1 = new Player();
        player2 = new Player();

        player1.setDeck(GsonParser.parseDeck(new File("src/main/resources/data/Deck_1.json")));
        player2.setDeck(GsonParser.parseDeck(new File("src/main/resources/data/Deck_2.json")));

        activePlayer = player1;
        waitingPlayer = player2;
    }

    public static void changeActivePlayer() {
        Player tempPlayer = activePlayer;
        activePlayer = waitingPlayer;
        waitingPlayer = tempPlayer;
    }

    public static Player getActivePlayer() {
        return activePlayer;
    }

    public void setActivePlayer(Player activePlayer) {
        Game.activePlayer = activePlayer;
    }

    public static Player getWaitingPlayer() {
        return waitingPlayer;
    }

    public void setWaitingPlayer(Player waitingPlayer) {
        Game.waitingPlayer = waitingPlayer;
    }

}
