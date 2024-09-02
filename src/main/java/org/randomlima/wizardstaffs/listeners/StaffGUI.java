package org.randomlima.wizardstaffs.listeners;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataType;
import org.randomlima.wizardstaffs.WizardStaffs;
import org.randomlima.wizardstaffs.managers.AbilityDataManager;
import org.randomlima.wizardstaffs.managers.essentials.DataManager;
import org.randomlima.wizardstaffs.utilities.Colorize;
import org.randomlima.wizardstaffs.utilities.DataParser;
import org.randomlima.wizardstaffs.utilities.InventoryToBase64;
import org.randomlima.wizardstaffs.utilities.keys.RuneKeys;
import org.randomlima.wizardstaffs.utilities.keys.StaffKeys;

import java.io.IOException;

public class StaffGUI implements Listener {
    private WizardStaffs plugin;

    public StaffGUI(WizardStaffs plugin){
        this.plugin = plugin;
    }
    @EventHandler
    public void onLeftClick(PlayerInteractEvent event) throws IOException {
        if(!event.getAction().isLeftClick())return;
        if(!plugin.verifyStaffItem(event.getPlayer().getInventory().getItemInMainHand()))return;
        if(event.getPlayer().isSneaking()){
            //cycle ability
            return;
        }
        openMenu(event.getPlayer(), event.getPlayer().getInventory().getItemInMainHand());
        event.getPlayer().setMetadata("openMenu", new FixedMetadataValue(plugin, true));
    }
    public void openMenu(Player player, ItemStack item) throws IOException {
        String name = item.getItemMeta().getDisplayName();
        if(!item.getItemMeta().getPersistentDataContainer().has(StaffKeys.staffGUI, PersistentDataType.STRING)){
            Inventory gui = Bukkit.createInventory(null, 9, Colorize.format("&6"+name+" Menu"));
            player.openInventory(gui);
        } else{
            String inv = item.getItemMeta().getPersistentDataContainer().get(StaffKeys.staffGUI, PersistentDataType.STRING);
            Inventory gui = Bukkit.createInventory(null, 9, Colorize.format("&6&l"+name+" Menu"));
            Inventory saved = InventoryToBase64.fromBase64(inv);
            gui.setStorageContents(saved.getStorageContents());
            player.openInventory(gui);
        }
    }
    @EventHandler
    public void onInventoryShiftClick(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();
        if(!player.hasMetadata("openMenu"))return;
        if(!event.getClick().isShiftClick())return;
        Inventory clicked = event.getInventory();
        if(clicked == event.getWhoClicked().getInventory())return;
        ItemStack clickedOn = event.getCurrentItem();
        if(clickedOn == null)return;
        if(!plugin.verifyRuneItem(clickedOn))event.setCancelled(true);
    }
    @EventHandler
    public void onInventoryclick(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();
        if(!player.hasMetadata("openMenu"))return;
        if(event.getClick().isShiftClick())return;
        ItemStack item = event.getCurrentItem();
        if(item == null)return;
        if(!plugin.verifyRuneItem(item))event.setCancelled(true);
    }
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event){
        if (event.getPlayer().hasMetadata("openMenu")){
            for(ItemStack i : event.getInventory().getContents()){
                if(i != null && !plugin.verifyRuneItem(i)){
                    event.getInventory().remove(i);
                    event.getPlayer().getWorld().dropItem(event.getPlayer().getLocation(), i);
                }
            }
            event.getPlayer().removeMetadata("openMenu", plugin);
            String inv = InventoryToBase64.toBase64(event.getInventory());
            ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
            ItemMeta meta = item.getItemMeta();
            meta.getPersistentDataContainer().set(StaffKeys.staffGUI, PersistentDataType.STRING, inv);
            item.setItemMeta(meta);
        }
    }
}
