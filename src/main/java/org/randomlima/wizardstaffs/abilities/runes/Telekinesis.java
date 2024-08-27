package org.randomlima.wizardstaffs.abilities.runes;

import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerInteractEvent;
import org.randomlima.wizardstaffs.Staff;
import org.randomlima.wizardstaffs.StaffState;
import org.randomlima.wizardstaffs.WizardStaffs;
import org.randomlima.wizardstaffs.abilities.AbilitySuper;

public class Telekinesis extends AbilitySuper {
//    private double power;
//    private CooldownManager cooldownManager;
//    private Sound sound;
    public Telekinesis(WizardStaffs plugin, Staff staff, String abilityName){
        super(plugin, staff, abilityName);
        try {
//            this.power = plugin.getAbilityDataManager().getAbilityFloatData(abilityName, "magnitude").doubleValue();
//            this.cooldownManager = new CooldownManager(plugin, this, plugin.getAbilityDataManager().getAbilityFloatData(abilityName, "cooldown-seconds").doubleValue());
//            this.sound = Sound.valueOf(plugin.getAbilityDataManager().getAbilityStringData(abilityName, "sound").toUpperCase());
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

//    @EventHandler
//    public void playerInteract(PlayerInteractEvent event){
//        if(!event.getAction().isRightClick())return;
//        if(!abilityCanBeUsed(event.getPlayer().getUniqueId()))return;
//        if(cooldownManager.checkAndStartCooldown())return;
//        event.getPlayer().setVelocity(event.getPlayer().getLocation().getDirection().multiply(power));
//        event.getPlayer().getWorld().playSound(event.getPlayer().getLocation(), sound, 10, 1);
//    }
}
