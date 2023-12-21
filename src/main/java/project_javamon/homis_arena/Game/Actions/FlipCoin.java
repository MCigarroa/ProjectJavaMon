package project_javamon.homis_arena.Game.Actions;

import project_javamon.homis_arena.Game.Player;
import project_javamon.homis_arena.Game.Pokemon.Card;
import project_javamon.homis_arena.Game.Pokemon.PokemonCard;
import project_javamon.homis_arena.Main;
import project_javamon.homis_arena.Util.CardPosition;

import java.util.Objects;
import java.util.Random;

public class FlipCoin implements IAction{
    Random random  = new Random();
    @Override
    public void TakeAction(Card cardActing) {
        if (random.nextBoolean()) {
            if (Objects.equals(cardActing.getName(), "thunder") || Objects.equals(cardActing.getName(), "thunder jolt")) {
                PokemonCard pokemonCard = (PokemonCard)cardActing;
                pokemonCard.setHp(pokemonCard.getHp() - (cardActing.getName() == "thunder"? 30 : 10));
                if (pokemonCard.getHp() < 0) {
                    cardActing.getPlayerOwner().removeFromPrevious(pokemonCard, CardPosition.DISCARD);
                }
            }
        }
    }

    @Override
    public String getActionName() {
        return null;
    }
}
