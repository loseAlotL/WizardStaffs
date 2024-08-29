package org.randomlima.wizardstaffs.abilities.code;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.randomlima.wizardstaffs.WizardStaffs;
import org.randomlima.wizardstaffs.objects.Staff;
import org.randomlima.wizardstaffs.utilities.Colorize;

public class StaffGUI implements Listener {
    private WizardStaffs plugin;
    public StaffGUI(WizardStaffs plugin){
        this.plugin = plugin;
    }
    @EventHandler
    public void onLeftClick(PlayerInteractEvent event){
        if(!event.getAction().isLeftClick())return;
        if(!plugin.verifyStaffItem(event.getPlayer().getInventory().getItemInMainHand()))return;
        if(event.getPlayer().isSneaking()){
            cycleAbilities(event.getPlayer().getInventory().getItemInMainHand());
            return;
        }
        openMenu(event.getPlayer());
    }
    public void openMenu(Player player) {
        Inventory gui = Bukkit.createInventory(null, 9, Colorize.format("&6Staff Menu"));
        player.openInventory(gui);
    }
    public void cycleAbilities(ItemStack staff){

    }
}
