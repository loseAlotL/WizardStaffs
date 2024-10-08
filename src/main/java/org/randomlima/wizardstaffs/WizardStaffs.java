package org.randomlima.wizardstaffs;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.units.qual.N;
import org.randomlima.wizardstaffs.abilities.Ability;
import org.randomlima.wizardstaffs.listeners.StaffGUI;
import org.randomlima.wizardstaffs.commands.GetRuneCommand;
import org.randomlima.wizardstaffs.commands.GetStaffCommand;
import org.randomlima.wizardstaffs.managers.AbilityDataManager;
import org.randomlima.wizardstaffs.managers.StaffDataManager;
import org.randomlima.wizardstaffs.managers.essentials.AbilityGenerator;
import org.randomlima.wizardstaffs.objects.Rune;
import org.randomlima.wizardstaffs.objects.Staff;
import org.randomlima.wizardstaffs.objects.StaffState;
import org.randomlima.wizardstaffs.utilities.DataParser;
import org.randomlima.wizardstaffs.utilities.InventoryToBase64;
import org.randomlima.wizardstaffs.utilities.keys.AbilityKeys;
import org.randomlima.wizardstaffs.utilities.keys.RuneKeys;
import org.randomlima.wizardstaffs.utilities.keys.StaffKeys;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public final class WizardStaffs extends JavaPlugin{
    private ArrayList<Staff> staffList = new ArrayList<>();
    private ArrayList<Rune> runeList = new ArrayList<>();
    private AbilityDataManager abilityDataManager;
    private AbilityGenerator abilityGenerator;
    private InventoryToBase64 inventoryToBase64;
    private StaffDataManager staffDataManager;
    private GetStaffCommand getStaffCommand;
    private GetRuneCommand getRuneCommand;
    private DataParser dataParser;

    @Override
    public void onEnable() {
        StaffKeys.staffNameKey = new NamespacedKey(this, "Wizard-staff-name");
        StaffKeys.staffIDKey = new NamespacedKey(this, "Wizard-staffID");
        StaffKeys.staffGUI = new NamespacedKey(this, "Wizard-staffGUI");
        RuneKeys.runeNameKey = new NamespacedKey(this, "rune-name");
        RuneKeys.runeIDKey = new NamespacedKey(this, "runeID");
        AbilityKeys.ability = new NamespacedKey(this, "runeData");

        this.abilityDataManager = new AbilityDataManager(this, "abilities.yml");
        //this.staffDataManager = new StaffDataManager(this,"staffs.yml");
        this.inventoryToBase64 = new InventoryToBase64(this);

        this.getStaffCommand = new GetStaffCommand(this);
        this.getRuneCommand = new GetRuneCommand(this);

        getServer().getPluginManager().registerEvents(new StaffGUI(this), this);

        this.abilityGenerator = new AbilityGenerator(this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void addStaff(ItemStack item, UUID owner){
        if(!verifyStaffItem(item))return;
        if(checkIfNewStaff(item)){
            Staff staff = new Staff(this, item, owner);
            staffList.add(staff);
            return;
        }
        for(Staff staff : staffList){
            if(staff.getUUID().equals(getStaffItemID(item))){
                for(Ability ability : staff.getAbilities()){
                    ability.deregister();
                }
                staff.deleteStaff();
                staff.load(item, owner);
                return;
            }
        }
    }
    public void addNewRune(ItemStack item, UUID owner){
        if(!verifyRuneItem(item))return;
        if(!checkIfNewRune(item))return;
        Rune rune = new Rune(this, item, owner);
        runeList.add(rune);
    }
    public boolean verifyStaffItem(ItemStack itemStack){
        if(itemStack.getItemMeta() == null)return false;
        if(!itemStack.getItemMeta().getPersistentDataContainer().has(StaffKeys.staffIDKey))return false;
        if(!itemStack.getItemMeta().getPersistentDataContainer().has(StaffKeys.staffNameKey))return false;
        //if(!ringDataManager.getRingNames().contains(itemStack.getItemMeta().getPersistentDataContainer().get(RingKeys.ringNameKey, PersistentDataType.STRING)))return false;
        return true;
    }
    public boolean verifyRuneItem(ItemStack itemStack){
        if(itemStack.getItemMeta() == null)return false;
        if(!itemStack.getItemMeta().getPersistentDataContainer().has(RuneKeys.runeIDKey))return false;
        if(!itemStack.getItemMeta().getPersistentDataContainer().has(RuneKeys.runeNameKey))return false;
        return true;
    }
    public UUID getStaffItemID(ItemStack itemStack){
        if(!verifyStaffItem(itemStack))return null;
        return UUID.fromString(Objects.requireNonNull(itemStack.getItemMeta().getPersistentDataContainer().get(StaffKeys.staffIDKey, PersistentDataType.STRING)));
    }
    public UUID getRuneItemID(ItemStack itemStack){
        if(!verifyRuneItem(itemStack))return null;
        return UUID.fromString(Objects.requireNonNull(itemStack.getItemMeta().getPersistentDataContainer().get(RuneKeys.runeIDKey, PersistentDataType.STRING)));
    }
    public boolean checkIfNewStaff(ItemStack itemStack){
        if(!verifyStaffItem(itemStack))return false;
        if(getStaffIDs().isEmpty())return true;
        return !getStaffIDs().contains(UUID.fromString(Objects.requireNonNull(itemStack.getItemMeta().getPersistentDataContainer().get(StaffKeys.staffIDKey, PersistentDataType.STRING))));
    }
    public boolean checkIfNewRune(ItemStack itemStack){
        if(!verifyRuneItem(itemStack))return false;
        if(getRuneIDs().isEmpty())return true;
//        if(getRingIDs() == null)return true;
        return !getRuneIDs().contains(UUID.fromString(Objects.requireNonNull(itemStack.getItemMeta().getPersistentDataContainer().get(RuneKeys.runeIDKey, PersistentDataType.STRING))));
    }
    public ArrayList<UUID> getStaffIDs(){
        ArrayList<UUID> staffIDs = new ArrayList<>();
        for(Staff staff : staffList){
            staffIDs.add(staff.getUUID());
        }
        return staffIDs;
    }
    public ArrayList<UUID> getRuneIDs(){
        ArrayList<UUID> runeIDs = new ArrayList<>();
        for(Rune rune : runeList){
            runeIDs.add(rune.getUUID());
        }
        return runeIDs;
    }
    public AbilityDataManager getAbilityDataManager(){
        return abilityDataManager;
    }
    public StaffDataManager getStaffDataManager(){return staffDataManager;}
    public ArrayList<Ability> getAbilities(Inventory inv, Staff staff){
        return abilityGenerator.getAbilities(inv, staff);
    }
}
