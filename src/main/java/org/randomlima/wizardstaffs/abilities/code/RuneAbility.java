package org.randomlima.wizardstaffs.abilities.code;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.LivingEntity;
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

import java.util.Locale;

public class RuneAbility extends AbilitySuper {
    private double cooldown;
    private long delay;
    private int power;
    private int damage;
    private int damage2;
    private boolean alwaysActive;
    private CooldownManager cooldownManager;
    private DataParser dataParser;
    private ParticleUtil particleUtil;
    private ItemStack runeItem;
    private WizardStaffs plugin;
    public RuneAbility(WizardStaffs plugin, Staff staff, ItemStack runeItem, String abilityName){
        super(plugin, staff, runeItem, abilityName);
        dataParser = new DataParser();
        particleUtil = new ParticleUtil(plugin);
        this.runeItem = runeItem;
        this.plugin = plugin;
        try{
            this.damage = Integer.parseInt(dataParser.getStringData(runeItem, "damage"));
            this.damage2 = Integer.parseInt(dataParser.getStringData(runeItem, "damage2"));
            this.power = Integer.parseInt(dataParser.getStringData(runeItem, "power"));
            this.delay = Long.parseLong(dataParser.getStringData(runeItem, "delay"));
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
        Location playerLoc = player.getLocation();
        playerLoc.setY(playerLoc.getY()+0.25);
        particleUtil.circle(playerLoc, 5, Particle.SOUL_FIRE_FLAME, 50);
        player.playSound(player.getLocation(), Sound.ENTITY_EVOKER_PREPARE_ATTACK,1,1);
        new BukkitRunnable() {
            @Override
            public void run() {
                particleUtil.circle(playerLoc, 5, Particle.SOUL_FIRE_FLAME, 50);
                player.playSound(player.getLocation(), Sound.ENTITY_WARDEN_SONIC_CHARGE,1,1);
            }
        }.runTaskLater(plugin, 7);
        Location loc2 = playerLoc.clone().set(playerLoc.getX(), playerLoc.getY() + 100, playerLoc.getZ());
        player.getWorld().spawnParticle(Particle.SONIC_BOOM, playerLoc, 0);
        new BukkitRunnable() {
            @Override
            public void run() {
                player.getWorld().createExplosion(playerLoc, power);
                player.playSound(player.getLocation(), Sound.ENTITY_WARDEN_SONIC_BOOM,1,1);
                for(Location loc : particleUtil.linePoints(playerLoc, loc2, 25)){
                    for(Entity e : loc.getNearbyLivingEntities(1,1,1)){
                        if(e instanceof LivingEntity)((LivingEntity) e).damage(damage);
                    }
                    for(Entity e : loc.getNearbyLivingEntities(5,2,5)){
                        if(e instanceof LivingEntity)((LivingEntity) e).damage(damage2);
                    }
                    particleUtil.circle(loc, 1, Particle.SOUL_FIRE_FLAME, 15);
                }
            }
        }.runTaskLater(plugin, 20*delay);
    }
}
