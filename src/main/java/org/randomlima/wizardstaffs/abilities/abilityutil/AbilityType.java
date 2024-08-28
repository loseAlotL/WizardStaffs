package org.randomlima.wizardstaffs.abilities.abilityutil;

public enum AbilityType {
    SHIELD(false, true),
    MELEE(false,true),
    RANGED(false,true);
    private boolean isToggled;
    private boolean onlyActiveWhenHeld;
    AbilityType(boolean isToggled, boolean onlyAvtiveWhenHeld){
        this.isToggled = isToggled;
        this.onlyActiveWhenHeld = onlyAvtiveWhenHeld;
    }
    public boolean isToggled(){
        return isToggled;
    }
    public boolean isOnlyActiveWhenHeld(){
        return onlyActiveWhenHeld;
    }
}
