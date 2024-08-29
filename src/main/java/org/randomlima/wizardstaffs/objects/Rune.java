package org.randomlima.wizardstaffs.objects;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitScheduler;
import org.randomlima.wizardstaffs.WizardStaffs;
import org.randomlima.wizardstaffs.abilities.Ability;
import org.randomlima.wizardstaffs.managers.SlotManager;
import org.randomlima.wizardstaffs.managers.StaffStateManager;
import org.randomlima.wizardstaffs.utilities.Colorize;
import org.randomlima.wizardstaffs.utilities.Msg;
import org.randomlima.wizardstaffs.utilities.RuneKeys;
import org.randomlima.wizardstaffs.utilities.StaffKeys;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class Rune {
    private WizardStaffs plugin;
    private ArrayList<Ability> abilities;
    private UUID owner;
    private UUID runeID;
    private ItemStack item = null;
    private String staffName;

    //private SlotManager slotManager;
    //private StaffStateManager staffStateManager;
    private SlotManager slotManager;
    private StaffStateManager staffStateManager;
    private BukkitScheduler scheduler;

    public Rune(WizardStaffs plugin, ItemStack item, UUID rune){
        this.plugin = plugin;
        this.runeID = rune;
        this.scheduler = plugin.getServer().getScheduler();
    }
    public void deleteRune(){
        slotManager.delete();
    }
    public UUID getOwner(){
        return owner;
    }
    public void setOwner(UUID uuid){
        this.owner = uuid;
    }
    public UUID getUUID(){
        return runeID;
    }
    public ItemStack getItem(){
        return item;
    }

}
