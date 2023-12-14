package project_javamon.homis_arena.Util;

import javafx.collections.ObservableList;
import project_javamon.homis_arena.Game.Actions.Attack;
import project_javamon.homis_arena.Game.Actions.IAction;
import project_javamon.homis_arena.Game.Pokemon.Card;
import project_javamon.homis_arena.Game.Pokemon.PokemonCard;

import java.util.ArrayList;

public class AbilityBinder {
    private static ArrayList<IAction> attackArrayList = GsonParser.getAllAttack("attacks");
    public AbilityBinder(){

    }

    public static void attackGenerator(PokemonCard card) {
        switch (card.getName()) {
            case "blastoise" -> card.addiAction(attackArrayList.getFirst());
//            case "charizard"-> addToPokemon("fire spin");
//            case "gyardos"-> {"dragon rage";
//                                "bubble beam";}
//
//
//            gyarados:
//
//
//            ninetales:
//            "lure"
//            "fire blast"
//
//            raichu:
//            "agility"
//            "thunder"
//
//            charmeleon:
//            "slash"
//            "flamethrower"
//
//            dewgong:
//            "aurora beam"
//            "ice beam"
//
//            magikarp:
//            "tackle"
//            "flail"
//
//            seel:
//            "headbutt"
//
//            wartortle:
//            "withdraw"
//            "bite"
        }
    }
}
