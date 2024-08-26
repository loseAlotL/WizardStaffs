package org.randomlima.wizardstaffs.abilities;

public enum Abilities {
    TEKEKINESIS(true),
    CONFUSION(true),
    LIGHT(true),
    HORSE(true),
    SHIELD(false);

    private boolean isToggled;
    Abilities(boolean isToggled){
        this.isToggled = isToggled;
    }
    public boolean isToggled(){
        return isToggled;
    }
}
