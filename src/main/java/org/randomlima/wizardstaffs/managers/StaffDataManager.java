package org.randomlima.wizardstaffs.managers;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.YamlConfiguration;
import org.randomlima.wizardstaffs.WizardStaffs;
import org.randomlima.wizardstaffs.managers.essentials.DataFile;
import org.randomlima.wizardstaffs.managers.essentials.DataManager;
import org.randomlima.wizardstaffs.utilities.Colorize;
import org.randomlima.wizardstaffs.utilities.Msg;
import org.randomlima.wizardstaffs.utilities.NiceTools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class StaffDataManager implements DataFile {
    private WizardStaffs plugin;
    private String fileName;
    private DataManager data;
//    private DataManager lesserStaffData;
    private ArrayList<String> staffNames = new ArrayList<>();
    private HashMap<String, List<String>> staffLore = new HashMap<>();
    private HashMap<String, String> staffDisplayNames = new HashMap<>();
    private HashMap<String, List<String>> staffAbilities = new HashMap<>();
    private HashMap<String, HashMap<Attribute, Integer>> staffAttributes = new HashMap<>();
    private HashMap<String, Material> staffMaterials = new HashMap<>();
    private HashMap<String, Integer> staffModelData = new HashMap<>();

    public StaffDataManager(WizardStaffs plugin, String fileName){
        this.fileName = fileName;
        this.plugin = plugin;
        this.data = new DataManager(plugin, this, fileName);
        update(data.getInternalConfig());
        //load();
        updateLore();
    }
    public void load(){
        for(String staffName : getConfig().getConfigurationSection("staffs").getKeys(false)){
            staffNames.add(staffName);
            for (String staffDetail : getConfig().getConfigurationSection("staffs." + staffName).getKeys(false)) {
                try {
                    String path = "staffs." + staffName + "." + staffDetail;
                    if (staffDetail.equals("display-name")) {
                        staffDisplayNames.put(staffName, getConfig().getString(path));
                    }
                    if (staffDetail.equals("abilities")) {
                        List<String> reverseList = getConfig().getStringList(path);
                        Collections.reverse(reverseList);
                        staffAbilities.put(staffName, reverseList);
                        for(String str : staffAbilities.get(staffName)){
                            if(!plugin.getAbilityDataManager().getAbilities().contains(str)) {
                                plugin.getServer().getConsoleSender().sendMessage(Colorize.format(Msg.prefix +
                                        "&4Error&c, the ability " + str + " in staff " + staffName + " does not exist."));
                                staffAbilities.get(staffName).remove(str);
                            }
                        }
                    }
                    if (staffDetail.equals("attributes")) {
                        for (String attributeSTR : getConfig().getConfigurationSection(path).getKeys(false)) {
                            try {
                                staffAttributes.put(staffName, NiceTools.castHashMap(Attribute.valueOf(attributeSTR), getConfig().getInt(path + "." + attributeSTR))); //this is broken.
                            } catch (Exception exception) {
                                plugin.getServer().getConsoleSender().sendMessage(Colorize.format(Msg.prefix + Msg.failedAttribute));
                            }
                        }
                    }
                    if (staffDetail.equals("item")) {
                        try {
                            staffMaterials.put(staffName, Material.valueOf(getConfig().getString(path)));
                        } catch (Exception e) {
                            plugin.getServer().getConsoleSender().sendMessage(Colorize.format(Msg.prefix + Msg.failedMaterial(staffDetail, staffName)));
                        }
                    }
                    if (staffDetail.equals("custom-model-data")) {
                        this.staffModelData.put(staffName, getConfig().getInt(path));
                    }
                } catch (Exception e) {
                    plugin.getServer().getConsoleSender().sendMessage(Colorize.format(Msg.failedRingLoad));
                    plugin.getServer().getConsoleSender().sendMessage(Colorize.format(Msg.usefulFailedRingLoad(staffName)));
                }
            }
        }
    }
    public void updateLore(){
        for(String staffName : getConfig().getConfigurationSection("staffs").getKeys(false)) {
            String path = "staffs."+staffName+".lore";
            List<String> loreEdit = getConfig().getStringList(path);
            for(int j = 0; j < loreEdit.size(); j++){
                if(loreEdit.get(j).equals(Msg.abilityDataConfigKey)){
                    loreEdit.remove(j);
                    for(String abilityName : staffAbilities.get(staffName)){
                        int jCount = 0;
                        try{
                            for(String loreElement : (ArrayList<String>)plugin.getAbilityDataManager().getAbilityData(abilityName, "description")){
                                loreEdit.add(j + jCount, loreElement);
                                jCount+=1;
                            }
                        }catch (Exception exception){
                            plugin.getServer().getConsoleSender().sendMessage(Colorize.format(Msg.usefulFailedRingLoad(staffName)));
                        }


                    }
                }
            }
            for(int j = 0; j < loreEdit.size(); j++){
                loreEdit.set(j, Colorize.format(loreEdit.get(j))); //colorize it all.
            }
            staffLore.put(staffName, loreEdit);
        }
    }
    public ArrayList<Attribute> getAttributes(String staffName){
        return (ArrayList<Attribute>) staffAttributes.get(staffName).keySet();
    }
    public List<String> getStaffLore(String staffName){
        return staffLore.get(staffName);
    }
    public List<String> getAbilities(String staffName){
        return staffAbilities.get(staffName);
    }
    public HashMap<Attribute, Integer> getAttributeHash(String staffName){
        return staffAttributes.get(staffName);
    }
    public String getStaffDisplayName(String staffName){
        return staffDisplayNames.get(staffName);
    }
    public ArrayList<String> getStaffNames(){
        return staffNames;
    }
    public int getModelData(String staffName){
        return staffModelData.get(staffName);
    }
    public Material getStaffMaterial(String staffName){
        return staffMaterials.get(staffName);
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
        //then needs to loop through to see if it does not have a new added item.
    }
}

