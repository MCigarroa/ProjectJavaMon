package project_javamon.homis_arena.Game;

import project_javamon.homis_arena.Main;
import project_javamon.homis_arena.Util.PokemonCardParser;

import java.io.File;
import java.util.ArrayList;

public class Game {
    private static ArrayList<Player> playerList;
    private Player player1;
    private Player player2;

    private int turnNumber;

    public Game(){
        player1 = new Player();
        player2 = new Player();

        player1.setDeck(PokemonCardParser.getAllAttributes(new File("src/main/resources/data/Deck_1.json")));
        player2.setDeck(PokemonCardParser.getAllAttributes(new File("src/main/resources/data/Deck_2.json")));

        playerList = new ArrayList<>();
        playerList.add(player1);
        playerList.add(player2);

    }



    public ArrayList<Player> getPlayerList() {
        return playerList;
    }

    public static void setPlayerList(ArrayList<Player> playerList) {
        Game.playerList = playerList;
    }
}
