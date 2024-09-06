package org.randomlima.wizardstaffs.abilities.code;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.randomlima.wizardstaffs.WizardStaffs;
import org.randomlima.wizardstaffs.abilities.AbilitySuper;
import org.randomlima.wizardstaffs.abilities.CooldownManager;
import org.randomlima.wizardstaffs.objects.Staff;
import org.randomlima.wizardstaffs.objects.StaffState;
import org.randomlima.wizardstaffs.utilities.DataParser;
import org.randomlima.wizardstaffs.utilities.ParticleUtil;

public class ExplodeAbility extends AbilitySuper {
    private int power;
    private double cooldown;
    private int timer;
    private boolean alwaysActive;
    private CooldownManager cooldownManager;
    private DataParser dataParser;
    private ParticleUtil particleUtil;
    private WizardStaffs plugin;
    private ItemStack runeItem;
    public ExplodeAbility(WizardStaffs plugin, Staff staff, ItemStack runeItem, String abilityName){
        super(plugin, staff, runeItem, abilityName);
        this.plugin = plugin;
        dataParser = new DataParser();
        particleUtil = new ParticleUtil();
        this.runeItem = runeItem;
        try{
            this.power = Integer.parseInt(dataParser.getStringData(runeItem,"power"));
            this.cooldown = Double.parseDouble(dataParser.getStringData(runeItem,"cooldown"));
            this.timer = Integer.parseInt(dataParser.getStringData(runeItem, "timer"));
            this.alwaysActive = Boolean.parseBoolean(dataParser.getStringData(runeItem, "always-active"));
            this.cooldownManager = new CooldownManager(plugin, this, cooldown);
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
        if(!event.getAction().isRightClick())return;
        if(!abilityCanBeUsed(event.getPlayer().getUniqueId(), runeItem))return;
        if(cooldownManager.checkAndStartCooldown())return;
        Player player = event.getPlayer();

        new BukkitRunnable() {
            @Override
            public void run() {
                player.getWorld().createExplosion(player, power);
            }
        }.runTaskLater(plugin, timer*5);
    }
}
