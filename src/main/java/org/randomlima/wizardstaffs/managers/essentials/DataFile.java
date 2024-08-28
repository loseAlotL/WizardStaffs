package org.randomlima.wizardstaffs.managers.essentials;

import org.bukkit.configuration.file.YamlConfiguration;

public interface DataFile {
    YamlConfiguration getConfig();
    void set(String path, Object object);
    void remove(String path);
    void update(YamlConfiguration internalYamlConfig);
}
