package org.randomlima.wizardstaffs.abilities.code;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
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

public class LaunchBurgerAbility extends AbilitySuper {
    private double velocity;
    private double cooldown;
    private boolean alwaysActive;
    private CooldownManager cooldownManager;
    private DataParser dataParser;
    private ParticleUtil particleUtil;
    private ItemStack runeItem;
    private WizardStaffs plugin;
    public LaunchBurgerAbility(WizardStaffs plugin, Staff staff, ItemStack runeItem, String abilityName){
        super(plugin, staff, runeItem, abilityName);
        dataParser = new DataParser();
        particleUtil = new ParticleUtil(plugin);
        this.runeItem = runeItem;
        this.plugin = plugin;
        try{
            this.velocity = Double.parseDouble(dataParser.getStringData(runeItem,"velocity"));
            this.cooldown = Double.parseDouble(dataParser.getStringData(runeItem,"cooldown"));
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
        ItemStack hamburger = new ItemStack(Material.COOKED_BEEF);
        ItemMeta meta = hamburger.getItemMeta();
        meta.setDisplayName(Colorize.format("&6Hamburger"));
        meta.setCustomModelData(694);
        hamburger.setItemMeta(meta);
        Vector direction = player.getLocation().getDirection().normalize();
        Item dropped = player.getWorld().dropItem(player.getEyeLocation(), hamburger);
        Vector velocity = direction.multiply(this.velocity);
        dropped.setVelocity(velocity);
        new BukkitRunnable() {
            @Override
            public void run() {
                player.getWorld().createExplosion(dropped.getLocation(), 1);
            }
        }.runTaskLater(plugin, 10);
    }
}

