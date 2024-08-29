package org.randomlima.wizardstaffs.abilities.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.randomlima.wizardstaffs.WizardStaffs;
import org.randomlima.wizardstaffs.objects.Staff;
import org.randomlima.wizardstaffs.utilities.Colorize;
import org.randomlima.wizardstaffs.utilities.RuneKeys;
import org.randomlima.wizardstaffs.utilities.StaffKeys;

import java.util.ArrayList;
import java.util.List;

public class StaffGUI implements Listener {
    private WizardStaffs plugin;
    private List<Inventory> menuList = new ArrayList<>();
    public StaffGUI(WizardStaffs plugin){
        this.plugin = plugin;
    }
    @EventHandler
    public void onLeftClick(PlayerInteractEvent event){
        if(!event.getAction().isLeftClick())return;
        if(!plugin.verifyStaffItem(event.getPlayer().getInventory().getItemInMainHand()))return;
        if(event.getPlayer().isSneaking()){
            //cycle ability
            return;
        }
        openMenu(event.getPlayer(), event.getPlayer().getInventory().getItemInMainHand());
    }
    public void openMenu(Player player, ItemStack item) {
        String name = item.getItemMeta().getDisplayName();
        Inventory gui = Bukkit.createInventory(null, 9, Colorize.format("&6"+name+" Menu"));
        player.openInventory(gui);
        menuList.add(gui);
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        if(!menuList.contains(event.getClickedInventory()))return;
        if(!(event.getWhoClicked() instanceof Player))return;
        if(     event.getAction() != InventoryAction.PLACE_ALL &&
                event.getAction() != InventoryAction.PLACE_ONE &&
                event.getAction() != InventoryAction.PLACE_SOME &&
                event.getAction() != InventoryAction.SWAP_WITH_CURSOR)return;
        ItemStack rune = event.getCursor();
        if(!plugin.verifyRuneItem(rune))return;
        String runeName = rune.getItemMeta().getPersistentDataContainer().get(RuneKeys.runeNameKey, PersistentDataType.STRING);
        System.out.println(runeName);
    }
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event){
        if (menuList.contains(event.getInventory())){
            menuList.remove(event.getInventory());
            //save inventory
        }
    }
}
