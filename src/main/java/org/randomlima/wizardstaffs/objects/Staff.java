package org.randomlima.wizardstaffs.objects;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitScheduler;
import org.randomlima.wizardstaffs.WizardStaffs;
import org.randomlima.wizardstaffs.abilities.Ability;
import org.randomlima.wizardstaffs.managers.SlotManager;
import org.randomlima.wizardstaffs.managers.StaffStateManager;
import org.randomlima.wizardstaffs.utilities.Colorize;
import org.randomlima.wizardstaffs.utilities.DataParser;
import org.randomlima.wizardstaffs.utilities.InventoryToBase64;
import org.randomlima.wizardstaffs.utilities.Msg;
import org.randomlima.wizardstaffs.utilities.keys.AbilityKeys;
import org.randomlima.wizardstaffs.utilities.keys.StaffKeys;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class Staff {
    private WizardStaffs plugin;
    private ArrayList<Ability> abilities;
    private UUID owner;
    private UUID staffID;
    private ItemStack item = null;

    private StaffState staffState;
    private String staffName;

    private SlotManager slotManager;
    private InventoryToBase64 inventoryToBase64;
    private StaffStateManager staffStateManager;
    private BukkitScheduler scheduler;

    public Staff(WizardStaffs plugin, ItemStack item, UUID owner) {
        this.plugin = plugin;
        this.owner = owner;
        this.scheduler = plugin.getServer().getScheduler();
        this.inventoryToBase64 = new InventoryToBase64(plugin);
        load(item, owner);
    }
    public void load(ItemStack item, UUID owner) {
        this.owner = owner;
        this.staffName = item.getItemMeta().getPersistentDataContainer().get(StaffKeys.staffNameKey, PersistentDataType.STRING);
        this.staffID = UUID.fromString(item.getItemMeta().getPersistentDataContainer().get(StaffKeys.staffIDKey, PersistentDataType.STRING));
        for(ItemStack itemStack : Bukkit.getPlayer(owner).getInventory()){
            if(itemStack != null){
                if(!itemStack.getType().isAir()
                        && itemStack.getItemMeta() != null
                        && itemStack.getItemMeta().getPersistentDataContainer().has(StaffKeys.staffIDKey)
                        && UUID.fromString(Objects.requireNonNull(itemStack.getItemMeta().getPersistentDataContainer().get(StaffKeys.staffIDKey, PersistentDataType.STRING))).equals(staffID)
                        && itemStack.getType().equals(item.getType())){
                    this.item = itemStack;
                }
            }
        }
        if(item.getType().isAir()){
            plugin.getServer().getConsoleSender().sendMessage(Colorize.format(Msg.failedRingLoad));
            return;
        }
        String invData = item.getItemMeta().getPersistentDataContainer().get(StaffKeys.staffGUI, PersistentDataType.STRING);
        Inventory inv = inventoryToBase64.fromBase64(invData);

        this.abilities = plugin.getAbilities(inv, this);


        if(abilities != null){
            for(Ability ability : abilities){
                ability.boot();
            }
            this.slotManager = new SlotManager(plugin, this, abilities);
            switchStaffState(StaffState.INVENTORY);
            this.staffStateManager = new StaffStateManager(plugin, this);
            return;
        }

        this.slotManager = new SlotManager(plugin, this, abilities);
        switchStaffState(StaffState.INVENTORY);
        this.staffStateManager = new StaffStateManager(plugin, this);
    }
    public void switchStaffState(StaffState staffState){
        if(staffState == this.staffState)return;
        this.staffState = staffState;
        //Bukkit.broadcastMessage("Item " + ringName + " state: " + ringState);
        if(abilities != null)for(Ability ability : abilities){
            ability.switchState(staffState);
        }
        if(this.staffState == StaffState.HELD && !abilities.isEmpty()){
            heldLogic();
        }
        if(this.staffState == StaffState.LOST)deleteStaff();
    }
    public void heldLogic(){
        if(!abilities.isEmpty()){
            Bukkit.getPlayer(getOwner()).sendActionBar(Colorize.format("&l[ "+getActiveAbility().getDisplayName()+" &r&l]"));
        }
    }
    public void deleteStaff(){
        //plugin.deleteRing(ringID);
        slotManager.delete();
    }
    public UUID getOwner(){
        return owner;
    }
    public void setOwner(UUID uuid){
        this.owner = uuid;
    }
    public UUID getUUID(){
        return staffID;
    }
    public StaffState getState(){
        return staffState;
    }
    public String getStaffName(){
        return staffName;
    }

    public Ability getActiveAbility(){
        return slotManager.getActiveAbility();
    }
    public ArrayList<Ability> getAbilities(){
        return abilities;
    }
    public ItemStack getItem(){
        return item;
    }

    //all the ises
    public boolean isHeld(){
        return staffState == StaffState.HELD;
    }
    public boolean isInventory(){
        return staffState == StaffState.INVENTORY;
    }
    public boolean isLost(){
        return staffState == StaffState.LOST;
    }
    public void removeAbilities(){abilities.clear();}
}
