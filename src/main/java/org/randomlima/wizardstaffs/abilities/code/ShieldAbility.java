package org.randomlima.wizardstaffs.abilities.code;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.randomlima.wizardstaffs.WizardStaffs;
import org.randomlima.wizardstaffs.abilities.AbilitySuper;
import org.randomlima.wizardstaffs.abilities.abilityutil.CooldownManager;
import org.randomlima.wizardstaffs.objects.Staff;
import org.randomlima.wizardstaffs.objects.StaffState;

public class ShieldAbility extends AbilitySuper {
    private double reduction;
    //private CooldownManager cooldownManager;
    public ShieldAbility(WizardStaffs plugin, Staff staff, String abilityName){
        super(plugin, staff, abilityName);
        try{
            this.reduction = plugin.getAbilityDataManager().getAbilityFloatData(abilityName, "damage-reduction").doubleValue();
            //this.cooldownManager = new CooldownManager(plugin, this, plugin.getAbilityDataManager().getAbilityFloatData(abilityName, "cooldown").doubleValue());
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
    public void playerInteract(EntityDamageEvent event){
        if(!(event.getEntity() instanceof Player))return;
        Player player = (Player) event.getEntity();
        if(!abilityCanBeUsed(player.getUniqueId()))return;
        //if(cooldownManager.checkAndStartCooldown())return;
        event.setCancelled(true);

        double damage = event.getDamage();
        double reduced = damage*(reduction/100);
        damage-=reduced;
        player.damage(damage);
    }
}
