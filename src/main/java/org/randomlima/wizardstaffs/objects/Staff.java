package org.randomlima.wizardstaffs.objects;

import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;
import org.randomlima.wizardstaffs.WizardStaffs;
import org.randomlima.wizardstaffs.abilities.Ability;
import org.randomlima.wizardstaffs.managers.SlotManager;

import java.util.ArrayList;
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
    //private StaffStateManager staffStateManager;
    private BukkitScheduler scheduler;

    public Staff(WizardStaffs plugin, ItemStack item, UUID owner){
        this.plugin = plugin;
        this.owner = owner;
        this.scheduler = plugin.getServer().getScheduler();
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
    public String getRingName(){
        return staffName;
    }

    public Ability getActiveAbility(){
        return slotManager.getActiveAbility();
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
}
