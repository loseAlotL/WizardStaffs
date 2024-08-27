package org.randomlima.wizardstaffs.abilities;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.randomlima.wizardstaffs.Staff;
import org.randomlima.wizardstaffs.StaffState;
import org.randomlima.wizardstaffs.WizardStaffs;
import org.randomlima.wizardstaffs.utilities.Colorize;
import org.randomlima.wizardstaffs.utilities.Msg;

import java.util.UUID;

public class AbilitySuper implements Ability, Listener {
    String abilityName;
    String abilityDisplayName;
    UUID uuid;
    WizardStaffs plugin;
    Staff staff;
    Abilities abilityType;
    public AbilitySuper(WizardStaffs plugin, Staff staff, String abilityName){
        this.plugin = plugin;
        this.staff = staff;
        this.abilityName = abilityName;
        this.uuid = UUID.randomUUID();
        this.abilityDisplayName = plugin.getAbilityDataManager().getAbilityData(abilityName, "display-name");
        this.abilityType = plugin.getAbilityDataManager().getAbilityType(abilityName);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    public boolean abilityCanBeUsed(UUID playerID){
        if(abilityType.isToggled() && abilityType.isOnlyActiveWhenHeld())return playerID.equals(staff.getOwner()) && staff.getActiveAbility().getID().equals(uuid) && ring.isHeld();
        if(abilityType.isOnlyActiveWhenHeld()) return playerID.equals(staff.getOwner()) && staff.isHeld();
        if(abilityType.isToggled()) return playerID.equals(staff.getOwner()) && staff.getActiveAbility().getID().equals(uuid);
        return false;
    }
    public void sendLoadError(){
        plugin.getServer().getConsoleSender().sendMessage(Colorize.format(Msg.abilityLoadError(abilityName, abilityType.toString())));
    }
    public Staff getStaff(){
        return staff;
    }
    public WizardStaffs getPlugin(){
        return plugin;
    }

    @Override
    public void switchState(StaffState staffState) {
        if(staffState == StaffState.LOST) HandlerList.unregisterAll(this);
    }

    @Override
    public String getName() {
        return abilityName;
    }

    @Override
    public String getDisplayName() {
        return abilityDisplayName;
    }


    @Override
    public UUID getID() {
        return uuid;
    }

    @Override
    public AbilityType getAbilityType() {
        return abilityType;
    }

    @Override
    public void boot() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
}

