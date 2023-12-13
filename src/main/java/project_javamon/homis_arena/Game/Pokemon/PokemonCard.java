package project_javamon.homis_arena.Game.Pokemon;

import project_javamon.homis_arena.Game.Actions.Attack;
import project_javamon.homis_arena.Game.Actions.IAction;

import java.util.ArrayList;
import java.util.HashMap;

public class PokemonCard extends Card {
    private String type;
    private int hp;
    private String weakness;
    private String resistance;
    private int retreatCost;
    private String previousStage;
    private String nextStage;
    private transient ArrayList<IAction> iAction;
    private transient HashMap<String, Integer> energyBanked;
    private final transient String[] types = {"fire", "water", "grass", "colorless",
            "psychic", "fighting", "darkness", "metal", "fairy"};

    public PokemonCard() {
        super();
        this.energyBanked = new HashMap<>();
        for (String type : types) {
            energyBanked.put(type, 0);
        }
    }
    public HashMap<String, Integer> getEnergyBanked() {
        return energyBanked;
    }
    public void addEnergy(EnergyCard energyCard) {
        energyBanked.put(energyCard.getType(), energyBanked.get(energyCard.getType() + 1));
    }
    public void energyConsumed(HashMap<String, Integer> energyCost) {
        for (HashMap.Entry<String, Integer> costEntry : energyCost.entrySet()) {
            String energyType = costEntry.getKey();
            int costAmount = costEntry.getValue();

            int currentBanked = energyBanked.get(energyType);
            int newBankedAmount = currentBanked - costAmount;

            energyBanked.put(energyType, newBankedAmount);
        }
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public String getWeakness() {
        return weakness;
    }

    public void setWeakness(String weakness) {
        this.weakness = weakness;
    }

    public String getResistance() {
        return resistance;
    }

    public void setResistance(String resistance) {
        this.resistance = resistance;
    }

    public int getRetreatCost() {
        return retreatCost;
    }

    public void setRetreatCost(int retreatCost) {
        this.retreatCost = retreatCost;
    }

    public String getPreviousStage() {
        return previousStage;
    }

    public void setPreviousStage(String previousStage) {
        this.previousStage = previousStage;
    }

    public String getNextStage() {
        return nextStage;
    }

    public void setNextStage(String nextStage) {
        this.nextStage = nextStage;
    }

    public ArrayList<IAction> getiAction() {
        return iAction;
    }

    public void setiAction(ArrayList<IAction> iAction) {
        this.iAction = iAction;
    }
}
