package project_javamon.homis_arena.Game.Actions;

import project_javamon.homis_arena.Game.Player;
import project_javamon.homis_arena.Game.Pokemon.Card;
import project_javamon.homis_arena.Game.Pokemon.PokemonCard;
import project_javamon.homis_arena.Util.CardPosition;

import java.util.HashMap;


public class Attack implements IAction{
    String attackName;
    HashMap<String, Integer> energyCost;
    int dmg;
    String extraActions;

    public Attack(String attackName, HashMap<String,Integer> energyCost, int dmg, String extraActions) {
        this.attackName = attackName;
        this.dmg = dmg;
        this.extraActions = extraActions;

        // We need to be sure that this is Map is properly instantiated to how Player is
        this.energyCost = new HashMap<>();
        String[] types = {"fire", "water", "grass", "colorless",
                "psychic", "fighting", "darkness", "metal", "fairy"};
        for (String type : types) {
            energyCost.put(type, 0);
        }
        this.energyCost.putAll(energyCost);
    }

    @Override
    public void TakeAction(Card card, Player playerAttacking, Player playerDefending) {
        System.out.println("Attack Attempt");
        if (canAfford(playerAttacking)){
            if (card instanceof PokemonCard) {
                ((PokemonCard) card).setHp(((PokemonCard) card).getHp() - dmg);
                if (((PokemonCard) card).getHp() <= 0) {
                    playerDefending.getDiscard().add(card);
                    playerDefending.getActive().remove(card);
                    card.setFormerCardPosition(card.getCardPosition());
                    card.setCardPositions(CardPosition.DISCARD);
                    System.out.println("POW KILLED IT");
                }
                System.out.println("POW HIT IT FOR " + dmg);
            }
        } else {
            System.out.println("Can't Afford Attack");
        }
    }

    private boolean canAfford(Player playerAttacking) {
        return energyCost.equals(playerAttacking.getEnergyBanked());
    }


    // Getters and Setters ==========================================================
    public String getAttackName() {
        return attackName;
    }

    public void setAttackName(String attackName) {
        this.attackName = attackName;
    }

    public HashMap<String, Integer> getEnergyCost() {
        return energyCost;
    }

    public void setEnergyCost(HashMap<String, Integer> energyCost) {
        this.energyCost = energyCost;
    }

    public int getDmg() {
        return dmg;
    }

    public void setDmg(int dmg) {
        this.dmg = dmg;
    }

    public String getExtraActions() {
        return extraActions;
    }

    public void setExtraActions(String extraActions) {
        this.extraActions = extraActions;
    }
}
