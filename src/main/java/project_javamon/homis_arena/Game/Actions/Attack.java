package project_javamon.homis_arena.Game.Actions;

import project_javamon.homis_arena.Game.Game;
import project_javamon.homis_arena.Game.Player;
import project_javamon.homis_arena.Game.Pokemon.PokemonCard;
import project_javamon.homis_arena.Util.CardPosition;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class Attack implements IAction {
    String attackName;
    HashMap<String, Integer> energyCost;
    int damage;
    String extraActions;

    public Attack() {

    }

    public Attack(String attackName, HashMap<String, Integer> energyCost, int dmg, String extraActions) {
        this.attackName = attackName;
        this.damage = dmg;
        this.extraActions = extraActions;

        // We need to be sure that this is Map is properly instantiated to how Player is
        this.energyCost = new HashMap<>();
        String[] types = {"fire", "water", "grass", "colorless",
                "psychic", "fighting", "darkness", "metal", "fairy"};
        for (String type : types) {
            this.energyCost.put(type, 0);
        }
        this.energyCost.putAll(energyCost);
    }

    @Override
    public void TakeAction(PokemonCard cardAttacking) {
        PokemonCard cardDefending = (PokemonCard) (cardAttacking.getPlayerOwner() == Game.getWaitingPlayer()
            ? Game.getActivePlayer().getActive().getFirst()
            : Game.getWaitingPlayer().getActive().getFirst());
        if (cardDefending.getPlayerOwner() == null) {
            throw new RuntimeException(cardDefending + ": NULL @ TakeAction : iAction");
        }

        System.out.println("Attack Attempt");
        if (canAfford(cardAttacking) && attackLegal(cardAttacking, cardDefending)) {
            cardDefending.setHp(calculateDamage(cardAttacking, cardDefending));
            System.out.println(cardDefending + " has " + cardDefending.getHp() + " hp");
            if (cardDefending.getHp() <= 0) {
                cardDefending.getPlayerOwner().getActive().remove(cardDefending); // "should" update UI
                cardDefending.getPlayerOwner().getDiscard().add(cardDefending); // "should" update UI
                cardDefending.setFormerCardPosition(cardDefending.getCardPosition());
                cardDefending.setCardPositions(CardPosition.DISCARD);
                System.out.println("POW KILLED IT");
            }
            cardDefending.getPlayerOwner().printPlayerCards();
        }
    }

    private boolean attackLegal(PokemonCard cardAttacking, PokemonCard cardDefending) {
        // TODO determine if legal move
        return true;
    }

    private int calculateDamage(PokemonCard cardAttacking, PokemonCard cardDefending) {
        final int resistance = 30;
        final int weakness = 2;

        int baseDamage = damage;
        int damageAdjustment = 0;
        int multiplication = 1;

        // Currently only Hydro Pump actually increases dmg based on an element\
        if (Objects.equals(cardAttacking.getName(), "hydro pump")) {
            int waterEnergy = cardAttacking.getEnergyBanked().getOrDefault("water", 0);
            if (waterEnergy == 4) {
                damageAdjustment += 10;
            } else if (waterEnergy == 5) {
                damageAdjustment += 20;
            }
        }

        // Check for resistance and weakness
        if (Objects.equals(cardAttacking.getType(), cardDefending.getResistance())) {
            damageAdjustment -= resistance;
        } else if (Objects.equals(cardAttacking.getType(), cardDefending.getWeakness())) {
            multiplication *= weakness;
        }

        // Kept having to refactor as pokemon keep hitting for zero
        int totalDamage = Math.max(0, (baseDamage * multiplication) + damageAdjustment);
        int newHp = cardDefending.getHp() - totalDamage;

        System.out.println("Pow hit for " + newHp);

        return newHp;
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
                    System.out.println("Not enough Energy: " + energyType);
                    return false;
                } else {
                    totalAvailableForColorless -= costAmount;
                }
            }
        }
        return totalAvailableForColorless >= colorlessNeeded;
    }


    // Getters and Setters ==========================================================
    public String getActionName() {
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

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public String getExtraActions() {
        return extraActions;
    }

    public void setExtraActions(String extraActions) {
        this.extraActions = extraActions;
    }
}
