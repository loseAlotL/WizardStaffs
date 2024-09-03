package org.randomlima.wizardstaffs.abilities;

import org.bukkit.NamespacedKey;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.randomlima.wizardstaffs.WizardStaffs;
import org.randomlima.wizardstaffs.objects.Staff;
import org.randomlima.wizardstaffs.objects.StaffState;
import org.randomlima.wizardstaffs.utilities.Colorize;
import org.randomlima.wizardstaffs.utilities.DataParser;
import org.randomlima.wizardstaffs.utilities.Msg;
import org.randomlima.wizardstaffs.utilities.keys.AbilityKeys;

import java.util.UUID;

public class AbilitySuper implements Ability, Listener {
    String abilityName;
    String abilityDisplayName;
    UUID uuid;
    WizardStaffs plugin;
    Staff staff;
    AbilityType abilityType;
    ItemStack staffItem;
    DataParser dataParser;
    public AbilitySuper(WizardStaffs plugin, Staff staff, ItemStack staffItem, String abilityName){
        this.plugin = plugin;
        this.staff = staff;
        this.staffItem = staffItem;
        this.abilityName = abilityName;
        this.uuid = UUID.randomUUID();
        this.dataParser = new DataParser();
        this.abilityDisplayName = dataParser.getStringData(staffItem,"display-name");
        //this.abilityDisplayName = plugin.getAbilityDataManager().getAbilityData(abilityName, "display-name");
        //this.abilityType = plugin.getAbilityDataManager().getAbilityType(abilityName);
        this.abilityType = AbilityType.valueOf(dataParser.getStringData(staffItem, "ability-type"));
        System.out.println("ABILITY TYP{EEEEEEEEEEEE: "+AbilityType.valueOf(dataParser.getStringData(staffItem, "ability-type")));
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    public boolean abilityCanBeUsed(UUID playerID){
        if(abilityType.isToggled() && abilityType.isOnlyActiveWhenHeld())return playerID.equals(staff.getOwner()) && staff.getActiveAbility().getID().equals(uuid) && staff.isHeld();
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
