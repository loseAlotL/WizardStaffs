package org.randomlima.wizardstaffs.abilities.code;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.randomlima.wizardstaffs.WizardStaffs;
import org.randomlima.wizardstaffs.abilities.AbilitySuper;
import org.randomlima.wizardstaffs.abilities.abilityutil.CooldownManager;
import org.randomlima.wizardstaffs.objects.Staff;
import org.randomlima.wizardstaffs.objects.StaffState;
import org.randomlima.wizardstaffs.utilities.Colorize;
import org.randomlima.wizardstaffs.utilities.DataParser;
import org.randomlima.wizardstaffs.utilities.ParticleUtil;

import java.util.Random;

public class WhipAbility extends AbilitySuper {
    private double curve;
    private Particle particle;
    private int damage;
    private int fireTick;
    private double cooldown;
    private boolean alwaysActive;
    private CooldownManager cooldownManager;
    private DataParser dataParser;
    private ItemStack runeItem;
    private WizardStaffs plugin;
    private final Random random = new Random();
    public WhipAbility(WizardStaffs plugin, Staff staff, ItemStack runeItem, String abilityName){
        super(plugin, staff, runeItem, abilityName);
        dataParser = new DataParser();
        this.runeItem = runeItem;
        this.plugin = plugin;
        try{
            this.curve = Double.parseDouble(dataParser.getStringData(runeItem,"curve-height"));
            this.particle = Particle.valueOf(dataParser.getStringData(runeItem, "particle"));
            this.damage = Integer.valueOf(dataParser.getStringData(runeItem, "damage"));
            this.fireTick = Integer.valueOf(dataParser.getStringData(runeItem, "fire-ticks"));
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
        Location start = player.getLocation();

        Vector direction = player.getLocation().getDirection();
        direction.multiply(8);
        Location end = player.getLocation().add(direction);

        double randomOff = (random.nextDouble() * 2 * 0.5) - 0.5;
        spawnCurvedLineParticles(start, end, 50, curve + randomOff, player);
    }
    private void spawnCurvedLineParticles(Location start, Location end, int count, double curveHeight, Player player) {
        World world = start.getWorld();
        double spacing = 1.0 / count;

        for (double t = 0; t <= 1; t += spacing) {
            double x = (1 - t) * start.getX() + t * end.getX();
            double y = (1 - t) * start.getY() + t * end.getY() + curveHeight * Math.sin(Math.PI * t); // Adjust curve with sin function
            double z = (1 - t) * start.getZ() + t * end.getZ();

            Location particleLoc = new Location(world, x, y, z);
            world.spawnParticle(particle, particleLoc, 0);
            for (LivingEntity entity : particleLoc.getNearbyLivingEntities(1,1,1)){
                if(entity != player){
                    entity.damage(damage);
                    entity.setFireTicks(fireTick);
                    entity.setKiller(player);
                }
            }
        }
    }
}
