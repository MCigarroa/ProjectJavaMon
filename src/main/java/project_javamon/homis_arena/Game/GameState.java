package project_javamon.homis_arena.Game;

import project_javamon.homis_arena.Util.AbstractState;
import project_javamon.homis_arena.Util.FlagEvent;

import java.util.EnumSet;
import java.util.Set;

public class GameState {
    public static AbstractState INITIAL_START = new AbstractState(
            "Initial Start",
            "Players must draw until they have a basic monster type"
    ) {
        @Override
        public boolean isStateComplete() {
            GameState.flagEventList.add(FlagEvent.PLAYER_CAN_DRAW_FROM_DECK);
            // Both players must have basic monster Pokémon
            return false;
        }

        @Override
        public AbstractState lastState() {
            return null;
        }

        @Override
        public AbstractState nextState() {
            // players draw new hands
            return INITIAL_START_DRAW_CARDS;
        }

        @Override
        public void updateState() {
        }
    };
    public static AbstractState INITIAL_START_DRAW_CARDS = new AbstractState(
            "Initial Draw Hands",
            "Players draw new hands"
    ) {
        @Override
        public boolean isStateComplete() {
            // Both players must draw cards
            return false;
        }

        @Override
        public AbstractState lastState() {
            return INITIAL_START;
        }

        @Override
        public AbstractState nextState() {
            return null;
        }

        @Override
        public void updateState() {

        }
    };
    private AbstractState currentState;
    private Player playerTurn;
    private Player nonPlayerTurn;
    private static Set<FlagEvent> flagEventList;
    private Player winner;

    private boolean canPlayerDraw;
    private boolean canGetPrize;
    private boolean canPlaceEnergy;


    public GameState(){
        currentState = INITIAL_START;
        // Players need to flip a coin to determine who goes first
        flagEventList = EnumSet.noneOf(FlagEvent.class);
        winner = null;
    }

    public AbstractState getCurrentState() {
        return currentState;
    }
    public void setCurrentState(AbstractState currentState) {
        this.currentState = currentState;
    }

    public Player getPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(Player playerTurn) {
        this.playerTurn = playerTurn;
    }

    public Player getNonPlayerTurn() {
        return nonPlayerTurn;
    }

    public void setNonPlayerTurn(Player nonPlayerTurn) {
        this.nonPlayerTurn = nonPlayerTurn;
    }

    public static Set<FlagEvent> getFlagEventList() {
        return flagEventList;
    }

    public void setFlagEventList(Set<FlagEvent> flagEventList) {
        GameState.flagEventList = flagEventList;
    }
    public void addFlagEventList(FlagEvent flagEvent) {
        flagEventList.add(flagEvent);
    }

    public void clearFlagEventList(){
        flagEventList.clear();
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }
}
