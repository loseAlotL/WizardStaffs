package org.randomlima.wizardstaffs.abilities.code;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.randomlima.wizardstaffs.WizardStaffs;
import org.randomlima.wizardstaffs.abilities.AbilitySuper;
import org.randomlima.wizardstaffs.abilities.abilityutil.CooldownManager;
import org.randomlima.wizardstaffs.objects.Staff;
import org.randomlima.wizardstaffs.objects.StaffState;
import org.randomlima.wizardstaffs.utilities.DataParser;
import org.randomlima.wizardstaffs.utilities.keys.AbilityKeys;

public class ShieldAbility extends AbilitySuper {
    private double reduction;
    private DataParser dataParser;
    public ShieldAbility(WizardStaffs plugin, Staff staff, ItemStack runeItem, String abilityName){
        super(plugin, staff, runeItem, abilityName);
        dataParser = new DataParser();
        System.out.println(staff);
        System.out.println(runeItem);
        System.out.println("AAAAAAAAAAAAA");
        System.out.println(dataParser.getStringData(runeItem,"damage-reduction"));
        System.out.println("EEEEEEEEE");
        try{
            this.reduction = Double.parseDouble(dataParser.getStringData(runeItem,"damage-reduction"));
            System.out.println("YAYYYYYYYYY");
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
