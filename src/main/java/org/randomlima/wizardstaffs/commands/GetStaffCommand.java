package org.randomlima.wizardstaffs.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.randomlima.wizardstaffs.WizardStaffs;
import org.randomlima.wizardstaffs.utilities.Colorize;
import org.randomlima.wizardstaffs.utilities.Msg;
import org.randomlima.wizardstaffs.utilities.StaffKeys;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GetStaffCommand implements CommandExecutor, TabCompleter {

    private WizardStaffs plugin;

    public GetStaffCommand(WizardStaffs plugin){
        this.plugin = plugin;
        plugin.getCommand("wsgetstaff").setExecutor(this);
        plugin.getCommand("wsgetstaff").setTabCompleter(this);
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(!(sender instanceof Player))return true;
        Player player = (Player) sender;
        ArrayList<String> argsList = new ArrayList<>();
        for(int j = 0; args.length > j; j++){
            argsList.add(args[j]);
        }
        if(argsList.isEmpty()){
            player.sendMessage(Colorize.format(Msg.noArgs));
            return true;
        }
        String staffName = args[0];
        if(!plugin.getRingDataManager().getRingNames().contains(staffName)){
            player.sendMessage(Colorize.format(Msg.invalidRingName));
            return true;
        }

        ItemStack itemStack = new ItemStack(plugin.getRingDataManager().getRingMaterial(staffName));
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.getPersistentDataContainer().set(StaffKeys.staffIDKey, PersistentDataType.STRING, UUID.randomUUID().toString());
        itemMeta.getPersistentDataContainer().set(StaffKeys.staffNameKey, PersistentDataType.STRING, staffName);
        itemStack.setItemMeta(itemMeta);
        player.getInventory().addItem(itemStack);
        plugin.addNewStaff(itemStack, player.getUniqueId());
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return plugin.getRingDataManager().getRingNames();
    }
}
