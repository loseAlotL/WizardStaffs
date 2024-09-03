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
import org.randomlima.wizardstaffs.managers.essentials.DataManager;
import org.randomlima.wizardstaffs.utilities.Colorize;
import org.randomlima.wizardstaffs.utilities.DataParser;
import org.randomlima.wizardstaffs.utilities.Msg;
import org.randomlima.wizardstaffs.utilities.keys.AbilityKeys;
import org.randomlima.wizardstaffs.utilities.keys.RuneKeys;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GetRuneCommand implements CommandExecutor {
    private WizardStaffs plugin;
    private DataManager data;
    private DataParser dataParser;

    public GetRuneCommand(WizardStaffs plugin){
        this.plugin = plugin;
        this.dataParser = new DataParser();
        plugin.getCommand("wsgetrune").setExecutor(this);
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) return true;
        if (args.length != 1) {
            sender.sendMessage(Colorize.format("&c[!] Usage: /getrune <rune name>"));
            return true;
        }

        String runeName = args[0];

        // Check if the rune name exists in the config
        if(plugin.getAbilityDataManager() == null){
            sender.sendMessage(Colorize.format("&c[!] Ability Data not found."));
            sender.sendMessage(Colorize.format("&cAbilities:" + plugin.getAbilityDataManager()));
            return true;
        }
        if(!plugin.getAbilityDataManager().getAbilities().contains(runeName)){
            sender.sendMessage(Colorize.format("&c[!] Rune not found: " + runeName));
            sender.sendMessage(Colorize.format("&c[|] Check ABILITIES.YML for valid runes."));
            return true;
        }

        Player player = (Player) sender;
        ItemStack item = getRune(runeName);
        player.getInventory().addItem(item);
        plugin.addNewRune(item, player.getUniqueId());
        player.sendMessage(Colorize.format("&2[!] Rune item added to your inventory."));
        return true;
    }
    public ItemStack getRune(String rune){
        String runeData = dataParser.dataToString(plugin.getAbilityDataManager().getConfig(), rune);
        ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = item.getItemMeta();
        String displayName = plugin.getAbilityDataManager().getAbilityStringData(rune, "display-name");
        meta.setDisplayName(Colorize.format(displayName));
        List<String> lore = plugin.getAbilityDataManager().getAbilityData(rune, "lore");
        List<String> colorizedLore = new ArrayList<>();
        for(String line : lore){
            colorizedLore.add(Colorize.format(line));
        }
        meta.setLore(colorizedLore);
        meta.getPersistentDataContainer().set(RuneKeys.runeIDKey, PersistentDataType.STRING, UUID.randomUUID().toString());
        meta.getPersistentDataContainer().set(RuneKeys.runeNameKey, PersistentDataType.STRING, rune);
        meta.getPersistentDataContainer().set(AbilityKeys.ability, PersistentDataType.STRING, runeData);

        item.setItemMeta(meta);
        return item;
    }
}
