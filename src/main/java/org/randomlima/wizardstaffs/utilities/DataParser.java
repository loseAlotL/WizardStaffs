package org.randomlima.wizardstaffs.utilities;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.randomlima.wizardstaffs.managers.AbilityDataManager;
import org.randomlima.wizardstaffs.utilities.keys.AbilityKeys;


public class DataParser {
    public String dataToString(YamlConfiguration config, String rune) {
        String fail = null;
        String path = "abilities." + rune;
        if (config == null) return fail;
        ConfigurationSection configSec = config.getConfigurationSection(path);
        if (configSec == null) return fail;

        StringBuilder dataString = new StringBuilder();
        for (String key : configSec.getKeys(false)) {
            String value = configSec.getString(key);
            if (value != null) {
                dataString.append(key).append(":").append(value).append(",");
            }
        }
        int length = dataString.length();
        if (length > 0) {
            dataString.setLength(length - 1); // remove the last comma
        }

        return dataString.toString().replace(" ", "");
    }

    public String getStringData(ItemStack item, String key){
        if(item == null || item.getItemMeta() == null)return null;
        if(!item.getItemMeta().getPersistentDataContainer().has(AbilityKeys.ability))return null;
        String abilityData = item.getItemMeta().getPersistentDataContainer().get(AbilityKeys.ability, PersistentDataType.STRING);
        String[] dataParts = abilityData.split(",");
        System.out.println("parts:");
        for(String part : dataParts){
            System.out.println(part);
            if(part.startsWith(key + ":"))return part.split(":")[1];
        }
        return null;
    }

    public Double getDoubleData(ItemStack item, String key){
        if(item == null || !item.hasItemMeta())return null;
        if(!item.getItemMeta().getPersistentDataContainer().has(AbilityKeys.ability))return null;
        String abilityData = item.getItemMeta().getPersistentDataContainer().get(AbilityKeys.ability, PersistentDataType.STRING);
        String[] dataParts = abilityData.split(",");
        for (String part : dataParts) {
            if (part.startsWith(key + ":")) {
                try {
                    return Double.parseDouble(part.split(": ")[1]);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("[!] Value for key '" + key + "' is not a valid Double");
                }
            }
        }
        throw new IllegalArgumentException("[!] Key not found in 'runedata'");
    }
}
