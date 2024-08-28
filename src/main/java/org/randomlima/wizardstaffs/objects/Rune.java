package org.randomlima.wizardstaffs.objects;

import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;
import org.randomlima.wizardstaffs.WizardStaffs;
import org.randomlima.wizardstaffs.abilities.Ability;

import java.util.ArrayList;
import java.util.UUID;

public class Rune {
    private WizardStaffs plugin;
    private ArrayList<Ability> abilities;
    private UUID owner;
    private UUID runeID;
    private ItemStack item = null;

    //private StaffState staffState = staffState.INVENTORY;
    private String staffName;

    //private SlotManager slotManager;
    //private StaffStateManager staffStateManager;
    private BukkitScheduler scheduler;

    public Rune(WizardStaffs plugin, ItemStack item, UUID rune){
        this.plugin = plugin;
        this.runeID = rune;
        this.scheduler = plugin.getServer().getScheduler();
    }
}
