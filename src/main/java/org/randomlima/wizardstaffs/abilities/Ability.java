package org.randomlima.wizardstaffs.abilities;

import org.randomlima.wizardstaffs.StaffState;

import java.util.UUID;

public interface Ability {
    void switchState(StaffState staffState);
    String getName();
    String getDisplayName();
    UUID getID();

    Abilities getAbilityType();
    void boot();

}
