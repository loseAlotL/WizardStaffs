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
import org.bukkit.persistence.PersistentDataType;
import org.randomlima.wizardstaffs.WizardStaffs;
import org.randomlima.wizardstaffs.managers.AbilityDataManager;
import org.randomlima.wizardstaffs.managers.essentials.DataManager;
import org.randomlima.wizardstaffs.utilities.Colorize;
import org.randomlima.wizardstaffs.utilities.DataParser;
import org.randomlima.wizardstaffs.utilities.InventoryToBase64;
import org.randomlima.wizardstaffs.utilities.keys.RuneKeys;
import org.randomlima.wizardstaffs.utilities.keys.StaffKeys;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StaffGUI implements Listener {
    private WizardStaffs plugin;
    private DataManager dataManager;
    private AbilityDataManager abilityDataManager;
    private List<Inventory> menuList = new ArrayList<>();

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
    }
    public void openMenu(Player player, ItemStack item) throws IOException {
        String name = item.getItemMeta().getDisplayName();
        if(!item.getItemMeta().getPersistentDataContainer().has(StaffKeys.staffGUI, PersistentDataType.STRING)){
            Inventory gui = Bukkit.createInventory(null, 9, Colorize.format("&6"+name+" Menu"));
            player.openInventory(gui);
            menuList.add(gui);
        } else{
            String inv = item.getItemMeta().getPersistentDataContainer().get(StaffKeys.staffGUI, PersistentDataType.STRING);
            Inventory gui = Bukkit.createInventory(null, 9, Colorize.format("&6&l"+name+" Menu"));
            Inventory saved = InventoryToBase64.fromBase64(inv);
            gui.setStorageContents(saved.getStorageContents());
            player.openInventory(gui);
            menuList.add(gui);
            menuList.remove(saved);
        }
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
        if(!plugin.verifyRuneItem(rune))event.setCancelled(true);
    }
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event){
        if (menuList.contains(event.getInventory())){
            menuList.remove(event.getInventory());
            String inv = InventoryToBase64.toBase64(event.getInventory());
            ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
            ItemMeta meta = item.getItemMeta();
            meta.getPersistentDataContainer().set(StaffKeys.staffGUI, PersistentDataType.STRING, inv);
            item.setItemMeta(meta);
        }
    }
}
