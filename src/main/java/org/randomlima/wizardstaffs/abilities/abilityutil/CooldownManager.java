package org.randomlima.wizardstaffs.abilities.abilityutil;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.randomlima.wizardstaffs.WizardStaffs;
import org.randomlima.wizardstaffs.abilities.AbilitySuper;
import org.randomlima.wizardstaffs.utilities.Colorize;
import org.randomlima.wizardstaffs.utilities.Msg;

public class CooldownManager {
    private WizardStaffs plugin;
    private double cooldown;
    private double cooldownLeft = 0;
    private AbilitySuper ability;
    private BukkitScheduler scheduler;
    private boolean isTaskRunning = false;
    private BossBar bossBar;
    private Player owner;

    public CooldownManager(WizardStaffs plugin, AbilitySuper ability, double cooldown) {
        this.plugin = plugin;
        this.cooldown = cooldown;
        this.scheduler = plugin.getServer().getScheduler();
        this.ability = ability;
        this.owner = Bukkit.getPlayer(ability.getStaff().getOwner());
    }

    public void startCooldown() {
        if (cooldownLeft > 0) return;

        this.cooldownLeft = cooldown;
        createBossBar();
        run();
    }

    public void setCooldownLeft(double cooldownLeft) {
        this.cooldownLeft = cooldownLeft;
    }

    public boolean isOnCooldown() {
        //if (cooldownLeft > 0)owner.sendMessage(Colorize.format(Msg.abilityCooldownMessage(ability.getDisplayName(), getCooldownLeftInt())));
        return cooldownLeft > 0;
    }

    public int getCooldownLeftInt() {
        return (int) cooldownLeft;
    }

    public double getCooldown() {
        return cooldown;
    }

    public void changeCooldown(double cooldown) {
        this.cooldown = cooldown;
    }

    public void stopTask() {
        isTaskRunning = false;
        if (bossBar != null) {
            bossBar.removeAll(); // Remove the boss bar when task stops
        }
    }

    public boolean checkAndStartCooldown() {
        if (isOnCooldown()) return true;
        startCooldown();
        return false;
    }

    private void run() {
        if (isTaskRunning) return;
        isTaskRunning = true;
        scheduler.runTaskTimer(plugin, (task) -> {
            if (!isTaskRunning) task.cancel();

            if (cooldownLeft > 0) {
                cooldownLeft -= 0.25;
                updateBossBar();
            }

            if (cooldownLeft <= 0) {
                stopTask();
                task.cancel();
            }
        }, 5L, 5L);// 5 ticks = .25 second
    }

    private void createBossBar() {
        if (bossBar == null) {
            bossBar = Bukkit.createBossBar(
                    Colorize.format("&6Cooldown: " + ability.getDisplayName()),
                    BarColor.RED,
                    BarStyle.SOLID
            );
        }

        // Add the player to the boss bar
        bossBar.addPlayer(owner);
        bossBar.setProgress(1.0); // Full progress at start
    }

    private void updateBossBar() {
        if (bossBar != null) {
            double progress = Math.max(0, cooldownLeft / cooldown); // Calculate remaining progress
            bossBar.setProgress(progress);
        }
    }
}

