package org.randomlima.wizardstaffs.abilities.code;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.randomlima.wizardstaffs.WizardStaffs;
import org.randomlima.wizardstaffs.abilities.AbilitySuper;
import org.randomlima.wizardstaffs.abilities.abilityutil.CooldownManager;
import org.randomlima.wizardstaffs.objects.Staff;
import org.randomlima.wizardstaffs.objects.StaffState;
import org.randomlima.wizardstaffs.utilities.DataParser;
import org.randomlima.wizardstaffs.utilities.ParticleUtil;

public class ArrowBurstAbility extends AbilitySuper {
    private double velocity;
    private double radius;
    private int numArrows;
    private double cooldown;
    private boolean alwaysActive;
    private CooldownManager cooldownManager;
    private DataParser dataParser;
    private ParticleUtil particleUtil;
    private ItemStack runeItem;
    public ArrowBurstAbility(WizardStaffs plugin, Staff staff, ItemStack runeItem, String abilityName){
        super(plugin, staff, runeItem, abilityName);
        dataParser = new DataParser();
        particleUtil = new ParticleUtil(plugin);
        this.runeItem = runeItem;
        try{
            this.velocity = Double.parseDouble(dataParser.getStringData(runeItem,"velocity"));
            this.numArrows = Integer.parseInt(dataParser.getStringData(runeItem, "arrow-count"));
            this.radius = Double.parseDouble(dataParser.getStringData(runeItem, "radius"));
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
        shootArrows(player, radius, velocity, numArrows);

    }
    public void shootArrows(Player player, double radius, double velocity, int numArrows) {
        Location playerLocation = player.getEyeLocation();
        double angleIncrement = 360.0 / numArrows;
        for (int i = 0; i < numArrows; i++) {
            double angle = Math.toRadians(i * angleIncrement);
            double xOffset = radius * Math.cos(angle);
            double zOffset = radius * Math.sin(angle);
            Location spawnLocation = playerLocation.clone().add(xOffset, 0.1, zOffset);
            Arrow arrow = player.getWorld().spawn(spawnLocation, Arrow.class);
            Vector direction = spawnLocation.toVector().subtract(playerLocation.toVector()).normalize();
            arrow.setVelocity(direction.multiply(velocity));
        }
    }

}
