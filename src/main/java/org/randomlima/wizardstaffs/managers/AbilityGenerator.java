package org.randomlima.wizardstaffs.managers;

import org.randomlima.wizardstaffs.Staff;
import org.randomlima.wizardstaffs.WizardStaffs;
import org.randomlima.wizardstaffs.abilities.Ability;

import java.util.ArrayList;
import java.util.List;

public class AbilityGenerator {
    private WizardStaffs plugin;
    public AbilityGenerator(WizardStaffs plugin){
        this.plugin = plugin;
    }

    public ArrayList<Ability> getAbilities(Staff staff){
        List<String> listAbil = plugin.getStaffDataManager().getAbilities(staff.getStaffName());
        ArrayList<Ability> abilities = new ArrayList<>();

        for(String strAb : listAbil){
            switch(plugin.getAbilityDataManager().getAbilityType(strAb)){
                case TEKEKINESIS:
                    abilities.add(new Telekinesis(plugin, staff, strAb));
                    break;
            }
        }

        return abilities;
    }
}
