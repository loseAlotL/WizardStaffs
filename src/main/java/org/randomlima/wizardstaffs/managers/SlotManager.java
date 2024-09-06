package org.randomlima.wizardstaffs.managers;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.randomlima.wizardstaffs.WizardStaffs;
import org.randomlima.wizardstaffs.abilities.Ability;
import org.randomlima.wizardstaffs.objects.Staff;
import org.randomlima.wizardstaffs.objects.StaffState;
import org.randomlima.wizardstaffs.utilities.Colorize;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SlotManager implements Listener {
    private WizardStaffs plugin;
    private Staff manager;
    private List<Ability> abilities = new ArrayList<>();
    private int slot = 0;
    private int maxSlots;

    public SlotManager(WizardStaffs plugin, Staff manager, ArrayList<Ability> abilities){
        this.manager = manager;
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
        //Collections.reverse(abilities); //yea yea I have to reverse it again because of stupid lore list stuff
        if(abilities != null){
            this.abilities.addAll(abilities);
        }
        this.maxSlots = this.abilities.size()-1;
    }
    public void nextSlot(){
        slot += 1;
        if(slot > maxSlots){
            slot = 0;
        }
        if(!abilities.isEmpty())
            Bukkit.getPlayer(manager.getOwner()).sendActionBar(Colorize.format("&l[ "+abilities.get(slot).getDisplayName()+" &r&l]"));

    }
    public Ability getActiveAbility(){
        if(!abilities.isEmpty())return abilities.get(slot);
        return null;
    }
    @EventHandler
    public void onShiftLeftClick(PlayerInteractEvent event){
        if(!event.getPlayer().getUniqueId().equals(manager.getOwner()))return;
        if(!event.getAction().isLeftClick())return;
        if(manager.getState() != StaffState.HELD)return;
        if(!event.getPlayer().isSneaking())return;
        if(abilities != null && !abilities.isEmpty()){
            nextSlot();
        }
    }
    @EventHandler
    public void onRun(PlayerJumpEvent event){
        //Bukkit.broadcastMessage("jump");
    }
    public void delete(){
        HandlerList.unregisterAll(this);
    }
}
