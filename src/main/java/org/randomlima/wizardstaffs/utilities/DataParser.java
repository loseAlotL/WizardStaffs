package org.randomlima.wizardstaffs.utilities;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.randomlima.wizardstaffs.managers.AbilityDataManager;

import java.util.ArrayList;

public class DataParser {
    private AbilityDataManager abilityDataManager;
    public String dataToString(YamlConfiguration config, String rune){
        String fail = "[!] Config is NULL";
        String fail2 = "[!] Config Section is NULL";
        String path = "abilities."+rune;
        if(config == null)return fail;
        ArrayList<String> dataArray = new ArrayList<>();
        ConfigurationSection configSec = config.getConfigurationSection(path);
        if(configSec == null)return fail2;
        for(String s : configSec.getKeys(false)){
            dataArray.add(s);
            if(configSec.get(s) != null){
                dataArray.add(":");
                dataArray.add(configSec.getString(s));

            }
            dataArray.add(",");
        }
        return dataArray.toString();
    }
}
