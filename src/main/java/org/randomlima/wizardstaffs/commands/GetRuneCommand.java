package org.randomlima.wizardstaffs.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.randomlima.wizardstaffs.WizardStaffs;
import org.randomlima.wizardstaffs.abilities.Ability;
import org.randomlima.wizardstaffs.managers.AbilityDataManager;
import org.randomlima.wizardstaffs.utilities.Colorize;
import org.randomlima.wizardstaffs.utilities.RuneKeys;
import org.randomlima.wizardstaffs.utilities.StaffKeys;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GetRuneCommand implements CommandExecutor {
    private WizardStaffs plugin;

    public GetRuneCommand(WizardStaffs plugin){
        this.plugin = plugin;
        plugin.getCommand("wsgetrune").setExecutor(this);
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) return true;
        if (args.length != 2) {
            sender.sendMessage(Colorize.format("&c[!] Usage: /getrune <rune name> <rune level>"));
            return true;
        }

        String runeName = args[0];
        int runeLevel;

        // Check if the rune name exists in the config
        if(plugin.getAbilityDataManager() == null){
            sender.sendMessage(Colorize.format("&c[!] Ability Data not found."));
            sender.sendMessage(Colorize.format("&cAbilities:" + plugin.getAbilityDataManager()));
            return true;
        }
        if(!plugin.getAbilityDataManager().getAbilities().contains(runeName)){
            sender.sendMessage(Colorize.format("&c[!] Rune not found: " + runeName));
            sender.sendMessage(Colorize.format("&c[|] Check CONFIG.YML for valid runes."));
            return true;
        }

        try {
            runeLevel = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage(Colorize.format("&c[!] Rune level must be a number."));
            return true;
        }

        String itemName = plugin.getAbilityDataManager().getAbilityStringData(runeName, "ability-type").toLowerCase();


        Player player = (Player) sender;
        ItemStack rune = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = rune.getItemMeta();
        meta.setDisplayName(itemName);
        List<String> lore = new ArrayList<>();
        lore.add(Colorize.format("&5Level: &d"+runeLevel));
        meta.setLore(lore);
        meta.getPersistentDataContainer().set(RuneKeys.runeIDKey, PersistentDataType.STRING, UUID.randomUUID().toString());
        meta.getPersistentDataContainer().set(RuneKeys.runeNameKey, PersistentDataType.STRING, UUID.randomUUID().toString());
        rune.setItemMeta(meta);
        player.getInventory().addItem(rune);
        plugin.addNewRune(rune, player.getUniqueId());
        player.sendMessage(Colorize.format("&2[!] Rune item added to your inventory."));
        return true;
    }
}
