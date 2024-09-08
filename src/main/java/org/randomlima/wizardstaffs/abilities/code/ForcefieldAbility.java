package org.randomlima.wizardstaffs.abilities.code;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.randomlima.wizardstaffs.WizardStaffs;
import org.randomlima.wizardstaffs.abilities.AbilitySuper;
import org.randomlima.wizardstaffs.abilities.abilityutil.CooldownManager;
import org.randomlima.wizardstaffs.objects.Staff;
import org.randomlima.wizardstaffs.objects.StaffState;
import org.randomlima.wizardstaffs.utilities.DataParser;
import org.randomlima.wizardstaffs.utilities.ParticleUtil;

public class ForcefieldAbility extends AbilitySuper {
    private int length;
    private double cooldown;
    private boolean alwaysActive;
    private CooldownManager cooldownManager;
    private DataParser dataParser;
    private ParticleUtil particleUtil;
    private ItemStack runeItem;
    public ForcefieldAbility(WizardStaffs plugin, Staff staff, ItemStack runeItem, String abilityName){
        super(plugin, staff, runeItem, abilityName);
        dataParser = new DataParser();
        particleUtil = new ParticleUtil(plugin);
        this.runeItem = runeItem;
        try{
            this.length = Integer.parseInt(dataParser.getStringData(runeItem,"length"));
            this.cooldown = Double.parseDouble(dataParser.getStringData(runeItem,"cooldown"));
            this.alwaysActive = Boolean.parseBoolean(dataParser.getStringData(runeItem, "always-active"));
            this.cooldownManager = new CooldownManager(plugin, this, cooldown);
        }catch (Exception e){
            sendLoadError();
        }
    }
    @EventHandler
    public void playerInteract(PlayerInteractEvent event){
        if(!event.getAction().isRightClick())return;
        if(!abilityCanBeUsed(event.getPlayer().getUniqueId(), runeItem))return;
        if(cooldownManager.checkAndStartCooldown())return;
        Player player = event.getPlayer();
        particleUtil.circle(player.getLocation(), 3, Particle.SOUL_FIRE_FLAME, 20);

    }
}
