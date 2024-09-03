package org.randomlima.wizardstaffs.abilities.code;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.randomlima.wizardstaffs.WizardStaffs;
import org.randomlima.wizardstaffs.abilities.AbilitySuper;
import org.randomlima.wizardstaffs.objects.Staff;
import org.randomlima.wizardstaffs.objects.StaffState;
import org.randomlima.wizardstaffs.utilities.DataParser;

public class BoostAbility extends AbilitySuper {
    private double velocity;
    private DataParser dataParser;
    public BoostAbility(WizardStaffs plugin, Staff staff, ItemStack runeItem, String abilityName){
        super(plugin, staff, runeItem, abilityName);
        dataParser = new DataParser();
        try{
            this.velocity = Double.parseDouble(dataParser.getStringData(runeItem,"velocity"));
        }catch (Exception e){
            sendLoadError();
        }
    }
    @Override
    public void switchState(StaffState staffState){
        if(staffState == StaffState.LOST){
            HandlerList.unregisterAll(this);
        }
    }
    @EventHandler
    public void playerInteract(PlayerInteractEvent event){

    }
}
