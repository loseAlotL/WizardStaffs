package org.randomlima.wizardstaffs.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.randomlima.wizardstaffs.WizardStaffs;
import org.randomlima.wizardstaffs.utilities.Colorize;
import org.randomlima.wizardstaffs.utilities.Msg;

public class ReloadCommand implements CommandExecutor {

    private WizardStaffs plugin;

    public ReloadCommand(WizardStaffs plugin){
        this.plugin = plugin;
        plugin.getCommand("wsreload").setExecutor(this);
    }
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        double timeMS = System.currentTimeMillis();
        commandSender.sendMessage(Colorize.format(Msg.prefix + "&cReloading staffs..."));
        plugin.reload();
        commandSender.sendMessage(Colorize.format(Msg.prefix + "&cReload complete, time " + (System.currentTimeMillis()-timeMS+"ms.")));
        return true;
    }
}
