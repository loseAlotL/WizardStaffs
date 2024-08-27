package org.randomlima.wizardstaffs.utilities;

import org.bukkit.ChatColor;

public class Colorize {
    public static String format(String str){
        return ChatColor.translateAlternateColorCodes('&', str);
    }
}
