package org.randomlima.wizardstaffs.managers.essentials;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.units.qual.A;
import org.randomlima.wizardstaffs.WizardStaffs;
import org.randomlima.wizardstaffs.abilities.Ability;
import org.randomlima.wizardstaffs.abilities.code.*;
import org.randomlima.wizardstaffs.objects.Staff;
import org.randomlima.wizardstaffs.utilities.DataParser;

import java.util.ArrayList;
import java.util.List;

public class AbilityGenerator {
    private WizardStaffs plugin;
    private DataParser dataParser;
    public AbilityGenerator(WizardStaffs plugin){
        this.plugin = plugin;
        this.dataParser = new DataParser();
    }

    public ArrayList<Ability> getAbilities(Inventory inventory, Staff staff){
        ArrayList<Ability> abilities = new ArrayList<>();
        if(inventory!=null)for(ItemStack item : inventory.getContents()){
            if(item!=null) {
                switch(dataParser.getStringData(item, "ability-type")){
                    case "SHIELD":
                        abilities.add(new ShieldAbility(plugin, staff, item, dataParser.getStringData(item, "display-name")));
                        break;
                    case "BOOST":
                        abilities.add(new BoostAbility(plugin, staff, item, dataParser.getStringData(item, "display-name")));
                        break;
                    case "EXPLODE":
                        abilities.add(new ExplodeAbility(plugin, staff, item, dataParser.getStringData(item, "display-name")));
                        break;
                    case "HAMBURGER":
                        abilities.add(new LaunchBurgerAbility(plugin,staff,item,dataParser.getStringData(item,"display-name")));
                        break;
                    case "WHIP":
                        abilities.add(new WhipAbility(plugin,staff,item,dataParser.getStringData(item,"display-name")));
                        break;
                    case "ARROWBURST":
                        abilities.add(new ArrowBurstAbility(plugin,staff,item,dataParser.getStringData(item, "display-name")));
                        break;
                    case "RUNE":
                        abilities.add(new RuneAbility(plugin,staff,item,dataParser.getStringData(item,"display-name")));
                        break;
                }
            }

        }
        return abilities;
    }
}
