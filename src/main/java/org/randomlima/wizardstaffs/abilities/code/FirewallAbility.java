package org.randomlima.wizardstaffs.abilities.code;

import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.randomlima.wizardstaffs.WizardStaffs;
import org.randomlima.wizardstaffs.abilities.AbilitySuper;
import org.randomlima.wizardstaffs.abilities.abilityutil.CooldownManager;
import org.randomlima.wizardstaffs.objects.Staff;
import org.randomlima.wizardstaffs.objects.StaffState;
import org.randomlima.wizardstaffs.utilities.DataParser;
import org.randomlima.wizardstaffs.utilities.ParticleUtil;

public class FirewallAbility extends AbilitySuper {
    private double cooldown;
    private double radius;
    private Particle particle;
    private long time;
    private double speed;
    private int turns;
    private boolean alwaysActive;
    private CooldownManager cooldownManager;
    private DataParser dataParser;
    private ParticleUtil particleUtil;
    private WizardStaffs plugin;
    private ItemStack runeItem;
    public FirewallAbility(WizardStaffs plugin, Staff staff, ItemStack runeItem, String abilityName){
        super(plugin, staff, runeItem, abilityName);
        this.plugin = plugin;
        dataParser = new DataParser();
        particleUtil = new ParticleUtil(plugin);
        this.runeItem = runeItem;
        try{
            this.cooldown = Double.parseDouble(dataParser.getStringData(runeItem,"cooldown"));
            this.radius = Double.parseDouble(dataParser.getStringData(runeItem, "radius"));
            this.speed = Double.parseDouble(dataParser.getStringData(runeItem, "speed"));
            this.particle = Particle.valueOf(dataParser.getStringData(runeItem, "particle"));
            this.turns = Integer.parseInt(dataParser.getStringData(runeItem, "turns"));
            this.time = Long.parseLong(dataParser.getStringData(runeItem, "time"));
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
        particleWall(player);
    }
    public void particleWall(Player player){
        particleUtil.spiral(player.getLocation(), radius, radius, particle, turns, speed, time);
    }
}
