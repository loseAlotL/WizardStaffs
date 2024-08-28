package org.randomlima.wizardstaffs;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.randomlima.wizardstaffs.commands.GetRuneCommand;
import org.randomlima.wizardstaffs.commands.GetStaffCommand;
import org.randomlima.wizardstaffs.managers.AbilityDataManager;
import org.randomlima.wizardstaffs.objects.Staff;
import org.randomlima.wizardstaffs.objects.StaffState;
import org.randomlima.wizardstaffs.utilities.RuneKeys;
import org.randomlima.wizardstaffs.utilities.StaffKeys;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public final class WizardStaffs extends JavaPlugin{
    private ArrayList<Staff> staffList = new ArrayList<>();
    private AbilityDataManager abilityDataManager;
    private GetStaffCommand getStaffCommand;
    private GetRuneCommand getRuneCommand;

    @Override
    public void onEnable() {
        StaffKeys.staffNameKey = new NamespacedKey(this, "Wizard-staff-name");
        StaffKeys.staffIDKey = new NamespacedKey(this, "Wizard-staffID");
        RuneKeys.runeNameKey = new NamespacedKey(this, "rune-name");
        RuneKeys.runeIDKey = new NamespacedKey(this, "runeID");

        this.abilityDataManager = new AbilityDataManager(this, "abilities.yml");

        this.getStaffCommand = new GetStaffCommand(this);
        this.getRuneCommand = new GetRuneCommand(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void addNewRing(ItemStack item, UUID owner){
        if(!verifyStaffItem(item))return;
        //if(!checkIfNewRing(item)) return;
        if(!checkIfNewStaff(item)) {
//            Bukkit.broadcastMessage("oh no");
            for(Staff staff : staffList){
                if(staff.getUUID().equals(getStaffItemID(item)) && staff.getState().equals(StaffState.LOST))
                    staff.load(item, owner);
            }
            return;
        }
        Staff staff = new Staff(this, item, owner);
        staffList.add(staff);
    }
    public boolean verifyStaffItem(ItemStack itemStack){
        if(itemStack.getItemMeta() == null)return false;
        if(!itemStack.getItemMeta().getPersistentDataContainer().has(StaffKeys.staffIDKey))return false;
        if(!itemStack.getItemMeta().getPersistentDataContainer().has(StaffKeys.staffNameKey))return false;
        //if(!ringDataManager.getRingNames().contains(itemStack.getItemMeta().getPersistentDataContainer().get(RingKeys.ringNameKey, PersistentDataType.STRING)))return false;
        return true;
    }
    public UUID getStaffItemID(ItemStack itemStack){
        if(!verifyStaffItem(itemStack))return null;
        return UUID.fromString(Objects.requireNonNull(itemStack.getItemMeta().getPersistentDataContainer().get(StaffKeys.staffIDKey, PersistentDataType.STRING)));
    }
    public boolean checkIfNewStaff(ItemStack itemStack){
        if(!verifyStaffItem(itemStack))return false;
        if(getStaffIDs().isEmpty())return true;
//        if(getRingIDs() == null)return true;
        return !getStaffIDs().contains(UUID.fromString(Objects.requireNonNull(itemStack.getItemMeta().getPersistentDataContainer().get(StaffKeys.staffIDKey, PersistentDataType.STRING))));
    }
    public ArrayList<UUID> getStaffIDs(){
        ArrayList<UUID> staffIDs = new ArrayList<>();
        for(Staff staff : staffList){
            staffIDs.add(staff.getUUID());
        }
        return staffIDs;
    }
    public AbilityDataManager getAbilityDataManager(){
        return abilityDataManager;
    }
}
