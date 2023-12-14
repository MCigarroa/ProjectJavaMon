package project_javamon.homis_arena.Util;

public enum StatusEffect {
    PARALYZED("Paralyzed","A Paralyzed Pokemon, can't attack or retreat", 1, false, false, true),
    CONFUSED("Confused","Can't attack or retreat", -1, false,true, false),
    ASLEEP("Asleep","Can't attack or retreat", -1, false,false, true),
    //BURNED(),
    //POISONED("Poisoned","")
    PREVENT_DMG("PreventDmg", "Prevents all dmg to self", 1, true,true,true);

    final String name;
    final String description;
    boolean canAttack;
    int duration;
    boolean canRetreat;
    boolean coinFlip;


    StatusEffect(String name, String description, int duration, boolean canAttack, boolean canRetreat, boolean coinFlip) {
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.canAttack = canAttack;
        this.canRetreat = canRetreat;
        this.coinFlip = coinFlip;
    }

    public boolean isFinished() {
        duration--;
        return duration <= 0;
    }

    // getters and Setters =============================================================
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean isCanAttack() {
        return canAttack;
    }

    public void setCanAttack(boolean canAttack) {
        this.canAttack = canAttack;
    }

    public boolean isCanRetreat() {
        return canRetreat;
    }

    public void setCanRetreat(boolean canRetreat) {
        this.canRetreat = canRetreat;
    }

    public boolean isCoinFlip() {
        return coinFlip;
    }

    public void setCoinFlip(boolean coinFlip) {
        this.coinFlip = coinFlip;
    }
}
