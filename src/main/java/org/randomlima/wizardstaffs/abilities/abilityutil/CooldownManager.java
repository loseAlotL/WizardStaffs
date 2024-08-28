package org.randomlima.wizardstaffs.abilities.abilityutil;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;
import org.randomlima.wizardstaffs.WizardStaffs;
import org.randomlima.wizardstaffs.abilities.AbilitySuper;
import org.randomlima.wizardstaffs.utilities.Colorize;
import org.randomlima.wizardstaffs.utilities.Msg;

public class CooldownManager{
    private WizardStaffs plugin;
    private double cooldown;
    private double cooldownLeft = 0;
    private AbilitySuper ability;
    private BukkitScheduler scheduler;
    private boolean isTaskRunning = false;

    public CooldownManager(WizardStaffs plugin, AbilitySuper ability, double cooldown){
        this.plugin = plugin;
        this.cooldown = cooldown;
        this.scheduler = plugin.getServer().getScheduler();
        this.ability = ability;
    }
    public void startCooldown(){
        if(cooldownLeft > 0)return;
        this.cooldownLeft = cooldown;
        run();
    }
    public void setCooldownLeft(double cooldownLeft){
        this.cooldownLeft = cooldownLeft;
    }
    public boolean isOnCooldown(){
        if(cooldownLeft > 0) Bukkit.getPlayer(ability.getStaff().getOwner()).sendMessage(Colorize.format(Msg.abilityCooldownMessage(ability.getDisplayName(), getCooldownLeftInt())));
        return cooldownLeft > 0;
    }
    public int getCooldownLeftInt(){
        return (int) cooldownLeft;
    }
    public double getCooldown(){
        return cooldown;
    }
    public void changeCooldown(double cooldown){
        this.cooldown = cooldown;
    }
    public void stopTask(){
        isTaskRunning = false;
    }
    public boolean checkAndStartCooldown(){
        if(isOnCooldown())return true;
        startCooldown();
        return false;
    }

    private void run() {
        if(isTaskRunning)return;
        isTaskRunning = true;
        scheduler.runTaskTimer(plugin, (task) ->{
            if(!isTaskRunning) task.cancel();
            if(cooldownLeft > 0){
                cooldownLeft -= 0.25;
            }
            //Bukkit.broadcastMessage("cooldown left: " + cooldownLeft);
            if(cooldownLeft <= 0 ){
                //Bukkit.broadcastMessage("Exact Time: " + ((System.currentTimeMillis() - exTime)/1000));
                stopTask();
                task.cancel();
            }
        }, 5l, 5L);
    }
}
