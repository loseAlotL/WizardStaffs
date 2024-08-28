package org.randomlima.wizardstaffs.commands;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.randomlima.wizardstaffs.WizardStaffs;
import org.randomlima.wizardstaffs.utilities.Colorize;
import org.randomlima.wizardstaffs.utilities.StaffKeys;

import java.util.UUID;

public class GetStaffCommand implements CommandExecutor {
    private WizardStaffs plugin;
    public GetStaffCommand(WizardStaffs plugin){
        this.plugin = plugin;
        plugin.getCommand("wsgetstaff").setExecutor(this);
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length != 2) {
            sender.sendMessage(Colorize.format("&c[!] Usage: /wsgetstaff <name> <modelData>"));
            return true;
        }
        Player player = (Player) sender;
        String itemName = args[0].replace("_", " ");
        int customModelData;
        try {
            customModelData = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage(Colorize.format("&c[!] Custom model data must be a valid integer."));
            return true;
        }
        ItemStack wandItem = createWandItem(itemName, customModelData);
        player.getInventory().addItem(wandItem);
        player.sendMessage(Colorize.format("&2[!] Staff added to your inventory: " + itemName));
        return true;
    }
    private ItemStack createWandItem(String name, int customModelData) {
        ItemStack wand = new ItemStack(Material.STICK);
        ItemMeta meta = wand.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(Colorize.format("&6" + name));
            meta.setCustomModelData(customModelData);
            meta.getPersistentDataContainer().set(StaffKeys.staffIDKey, PersistentDataType.STRING, UUID.randomUUID().toString());
            meta.getPersistentDataContainer().set(StaffKeys.staffNameKey, PersistentDataType.STRING, UUID.randomUUID().toString());
            wand.setItemMeta(meta);
        }

        return wand;
    }
}
