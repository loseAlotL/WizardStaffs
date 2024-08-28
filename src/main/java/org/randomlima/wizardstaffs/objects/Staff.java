package org.randomlima.wizardstaffs.objects;

import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;
import org.randomlima.wizardstaffs.WizardStaffs;
import org.randomlima.wizardstaffs.abilities.Ability;

import java.util.ArrayList;
import java.util.UUID;

public class Staff {
    private WizardStaffs plugin;
    private ArrayList<Ability> abilities;
    private UUID owner;
    private UUID staffID;
    private ItemStack item = null;

    //private StaffState staffState = staffState.INVENTORY;
    private String staffName;

    //private SlotManager slotManager;
    //private StaffStateManager staffStateManager;
    private BukkitScheduler scheduler;

    public Staff(WizardStaffs plugin, ItemStack item, UUID owner){
        this.plugin = plugin;
        this.owner = owner;
        this.scheduler = plugin.getServer().getScheduler();
    }
}
