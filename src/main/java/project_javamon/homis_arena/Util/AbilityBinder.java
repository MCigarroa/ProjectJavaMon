package project_javamon.homis_arena.Util;

import project_javamon.homis_arena.Game.Actions.Attack;
import project_javamon.homis_arena.Game.Actions.IAction;
import project_javamon.homis_arena.Game.Pokemon.PokemonCard;

import java.util.*;

public class AbilityBinder {
    private static final ArrayList<IAction> attackArrayList = GsonParser.getAllAttack("src/main/resources/data/attacks.json");
    private static final Map<String, List<String>> cardToAttacksMap = new HashMap<>();
    private static final Map<String, Attack> abilitiesMap = new HashMap<>();

    public static void attackGenerator(PokemonCard card) {
        card.getiAction().clear();

        List<String> attackNames = cardToAttacksMap.get(card.getName());
        if (attackNames != null) {
            for (String attackName : attackNames) {
                Attack ability = abilitiesMap.get(attackName);
                if (ability != null) {
                    card.addiAction(ability);
                }
            }
        }
    }
    static {
        // The card names and their attacks
        cardToAttacksMap.put("blastoise", Arrays.asList("hydro pump"));
        cardToAttacksMap.put("charizard", Arrays.asList("fire spin"));
        cardToAttacksMap.put("gyarados", Arrays.asList("dragon rage", "bubble beam"));
        cardToAttacksMap.put("ninetales", Arrays.asList("lure beam","fire blast"));
        cardToAttacksMap.put("raichu", Arrays.asList("agility","thunder"));
        cardToAttacksMap.put("charmeleon", Arrays.asList("slash","flamethrower"));
        cardToAttacksMap.put("dewgong", Arrays.asList("aurora beam","ice beam"));
        cardToAttacksMap.put("magikarp", Arrays.asList("tackle","flail"));
        cardToAttacksMap.put("seel", Arrays.asList("headbutt"));
        cardToAttacksMap.put("wartortle", Arrays.asList("withdraw", "bite"));
        cardToAttacksMap.put("charmander", Arrays.asList("scratch", "ember"));
        cardToAttacksMap.put("pikachu", Arrays.asList("gnaw", "thunder jolt"));
        cardToAttacksMap.put("squirtle", Arrays.asList("bubble", "withdraw"));
        cardToAttacksMap.put("vulpix", Arrays.asList("confuse ray"));

        for (IAction action : attackArrayList) {
            if (action instanceof Attack) {
                Attack attack = (Attack) action;
                abilitiesMap.put(attack.getActionName(), attack);
            }
        }
    }
}
