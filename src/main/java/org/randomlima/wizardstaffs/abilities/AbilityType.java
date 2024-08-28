package org.randomlima.wizardstaffs.abilities;

public enum AbilityType {
    SHIELD(false,true),
    BALANCE(false,true),
    MELEE(false,true),
    RANGED(false,true),
    TELEKINESIS(true,true);
    private boolean isToggled;
    private boolean onlyActiveWhenHeld;
    AbilityType(boolean isToggled, boolean onlyActiveWhenHeld){
        this.isToggled = isToggled;
        this.onlyActiveWhenHeld = onlyActiveWhenHeld;
    }
    public boolean isToggled(){
        return isToggled;
    }
    public boolean isOnlyActiveWhenHeld(){
        return onlyActiveWhenHeld;
    }
}
