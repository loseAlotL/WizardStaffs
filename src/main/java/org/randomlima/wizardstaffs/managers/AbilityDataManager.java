package org.randomlima.wizardstaffs.managers;

import org.bukkit.configuration.file.YamlConfiguration;
import org.randomlima.wizardstaffs.WizardStaffs;
import org.randomlima.wizardstaffs.abilities.Ability;
import org.randomlima.wizardstaffs.abilities.AbilitySuper;
import org.randomlima.wizardstaffs.abilities.AbilityType;
import org.randomlima.wizardstaffs.managers.essentials.DataFile;
import org.randomlima.wizardstaffs.managers.essentials.DataManager;
import org.randomlima.wizardstaffs.utilities.Colorize;
import org.randomlima.wizardstaffs.utilities.Msg;
import org.randomlima.wizardstaffs.utilities.NiceTools;

import java.util.ArrayList;
import java.util.HashMap;

public class AbilityDataManager implements DataFile {
    private WizardStaffs plugin;
    private DataManager data;
    private ArrayList<String> abilities = new ArrayList<>();
    private HashMap<String, AbilityType> abilityTypes = new HashMap<>();

    //private HashMap<String, ArrayList<HashMap<String, ?>>> abilityData = new HashMap<>();
    private HashMap<String, HashMap<String, ArrayList<?>>> abilityData = new HashMap<>();
    public AbilityDataManager(WizardStaffs plugin, String fileName){
        this.plugin = plugin;
        this.data = new DataManager(plugin, this, fileName);
        update(data.getInternalConfig());
        load();
    }
    public void load(){
        for(String abilityName : data.getConfig().getConfigurationSection("abilities").getKeys(false)){
            abilities.add(abilityName);
            String abilityPath = "abilities."+abilityName;
            try{
                abilityData.put(abilityName, NiceTools.castHashMap("ability-type", NiceTools.castArray(AbilityType.valueOf(getConfig().getString(abilityPath+".ability-type").toUpperCase()))));
                abilityTypes.put(abilityName, AbilityType.valueOf(getConfig().getString(abilityPath+".ability-type").toUpperCase()));
                for(String restOfDataKey : getConfig().getConfigurationSection(abilityPath).getKeys(false)){
                    abilityData.get(abilityName).put(restOfDataKey, NiceTools.castArray(getConfig().get(abilityPath+"."+restOfDataKey)));
                }
            }catch (Exception e){
                plugin.getServer().getConsoleSender().sendMessage(Colorize.format(Msg.prefix + "&cYou must choose a valid Ability Type for ability &f&l" + abilityName + "&c."));
                plugin.getServer().getConsoleSender().sendMessage(Colorize.format(Msg.prefix + "&cPlease check &f" + abilityName + "&c in the abilities.yml file." ));
            }
        }
    }
    public <T> HashMap<String, ?> addAbilityCash(String index, T thing){
        HashMap<String, T> innerMap = new HashMap<>();
        innerMap.put(index, thing);
        return innerMap;
    }

    public ArrayList<String> getAbilities(){return abilities;}
    public AbilityType getAbilityType(String ability){
        return abilityTypes.get(ability);
    }
    public AbilityType getAbilityString(String ability){
        return abilityTypes.get(ability);
    }
    public <T> T getAbilityData(String ability, String meta){
        return (T) abilityData.get(ability).get(meta).get(0);
    }
    public String getAbilityStringData(String ability, String meta){
        return getAbilityData(ability, meta).toString();
    }
    public Float getAbilityFloatData(String ability, String meta){
        //Bukkit.broadcastMessage(" asdkfjasdkfasdf " + abilityData.get(ability).get(meta).get(0));
        return Float.parseFloat(abilityData.get(ability).get(meta).get(0).toString());
    }

    @Override
    public YamlConfiguration getConfig() {
        return data.getConfig();
    }

    @Override
    public void set(String path, Object object) {
        data.set(path, object);
    }

    @Override
    public void remove(String path) {
        data.remove(path);
    }

    @Override
    public void update(YamlConfiguration internalYamlConfig) {
        set("version", internalYamlConfig.get("version"));
    }
}
