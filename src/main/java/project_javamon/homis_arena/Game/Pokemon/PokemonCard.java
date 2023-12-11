package project_javamon.homis_arena.Game.Pokemon;

public class PokemonCard extends Card {
    private String type;
    private int hp;
    private String weakness;
    private String resistance;
    private int retreatCost;
    private String previousStage;
    private String nextStage;


    public PokemonCard() {
        super();
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
}
