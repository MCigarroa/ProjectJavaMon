package project_javamon.homis_arena.Game;

import project_javamon.homis_arena.Game.States.AbstractState;
import project_javamon.homis_arena.Game.States.FlagEvent;
import project_javamon.homis_arena.Game.States.InitState;

import java.util.EnumSet;
import java.util.Set;

public class GameState {

    private static AbstractState currentState;
    private static Set<FlagEvent> flagEventList;
    private Player winner;

    private boolean canAddEnergy;
    private boolean canDraw;
    private boolean canGetPrize;
    private boolean canPlaceEnergy;


    public GameState(){
        currentState = new InitState();
        // Players need to flip a coin to determine who goes first
        flagEventList = EnumSet.noneOf(FlagEvent.class);
        winner = null;
    }

    public static boolean isMoveLegal() {
        return currentState.isMoveLegal();
    }

}
