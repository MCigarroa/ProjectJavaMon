package project_javamon.homis_arena.Game.Actions;

import project_javamon.homis_arena.Game.Player;
import project_javamon.homis_arena.Game.Pokemon.Card;
import project_javamon.homis_arena.Game.Pokemon.PokemonCard;
import project_javamon.homis_arena.Util.CardPosition;

import java.util.HashMap;
import java.util.Map;


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
    public void TakeAction(PokemonCard cardAttacking, PokemonCard cardDefending, Player playerAttacking, Player playerDefending) {
        System.out.println("Attack Attempt");
        if (canAfford(cardAttacking)){
            cardDefending.setHp(cardDefending.getHp() - dmg);
            if (cardDefending.getHp() <= 0) {
                playerDefending.getDiscard().add(cardDefending); // updates UI
                playerDefending.getActive().remove(cardDefending); // updates UI
                cardDefending.setFormerCardPosition(cardDefending.getCardPosition());
                cardDefending.setCardPositions(CardPosition.DISCARD);
                System.out.println("POW KILLED IT");
            }
            System.out.println("POW HIT IT FOR " + dmg);
        } else {
            System.out.println("Can't Afford Attack");
        }
    }

    private boolean canAfford(PokemonCard cardAttacking) {
        // First iterates to check if card can afford attack for example: 1 red balance != 2 red cost
        // Second checks if something can pay for its colorless: 2 red balance == 1 red & 1 colorless cost
        HashMap<String, Integer> availableEnergy = cardAttacking.getEnergyBanked();
        int colorlessNeeded = energyCost.getOrDefault("colorless", 0);
        int totalAvailableForColorless = availableEnergy.values().stream().mapToInt(Integer::intValue).sum();

        for (Map.Entry<String, Integer> costEntry : energyCost.entrySet()) {
            String energyType = costEntry.getKey();
            int costAmount = costEntry.getValue();

            if (!energyType.equals("colorless")) {
                if (availableEnergy.getOrDefault(energyType, 0) < costAmount) {
                    return false;
                } else {
                    totalAvailableForColorless -= costAmount;
                }
            }
        }
        return totalAvailableForColorless >= colorlessNeeded;
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
