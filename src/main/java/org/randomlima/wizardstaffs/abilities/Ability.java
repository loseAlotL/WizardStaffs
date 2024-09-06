package org.randomlima.wizardstaffs.abilities;

import org.randomlima.wizardstaffs.objects.StaffState;

import java.util.UUID;

public interface Ability {
    void switchState(StaffState staffState);
    String getName();
    String getDisplayName();
    UUID getID();
    AbilityType getAbilityType();
    void boot();
    void deregister();
}
