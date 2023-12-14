package project_javamon.homis_arena.Game.Pokemon;

import kotlin.jvm.JvmStatic;
import project_javamon.homis_arena.Game.Actions.Attack;
import project_javamon.homis_arena.Game.Actions.IAction;
import project_javamon.homis_arena.Util.StatusEffect;

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

    private transient int maxHp;
    private transient ArrayList<StatusEffect> statusEffects;
    private transient ArrayList<IAction> iAction = new ArrayList<>();
    private transient HashMap<String, Integer> energyBanked;
    private final transient String[] types = {"fire", "water", "grass", "colorless",
            "psychic", "fighting", "darkness", "metal", "fairy"};

    public PokemonCard() {
        super();
        this.maxHp = hp;
        this.energyBanked = new HashMap<>();
        for (String type : types) {
            energyBanked.put(type, 0);
        }
    }
    public HashMap<String, Integer> getEnergyBanked() {
        return energyBanked;
    }

    public void addEnergy(EnergyCard energyCard) {
        String type = energyCard.getType();
        int currentEnergy = energyBanked.getOrDefault(type, 0);
        energyBanked.put(type, currentEnergy + 1);
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

    public void addiAction(IAction iAction) {
        this.iAction.add(iAction);
    }

    public ArrayList<StatusEffect> getStatusEffects() {
        return statusEffects;
    }

    public void setStatusEffects(ArrayList<StatusEffect> statusEffects) {
        this.statusEffects = statusEffects;
    }

    public void addStatusEffects(StatusEffect statusEffect) {
        statusEffects.add(statusEffect);
    }
}
