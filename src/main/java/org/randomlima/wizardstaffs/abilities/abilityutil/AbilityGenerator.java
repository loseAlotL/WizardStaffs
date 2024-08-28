//package org.randomlima.wizardstaffs.abilities.abilityutil;
//
//import org.randomlima.wizardstaffs.WizardStaffs;
//import org.randomlima.wizardstaffs.abilities.Ability;
//import org.randomlima.wizardstaffs.objects.Staff;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class AbilityGenerator {
//    private WizardStaffs plugin;
//    public AbilityGenerator(WizardStaffs plugin){
//        this.plugin = plugin;
//    }
//
//    public ArrayList<Ability> getAbilities(Staff staff){
//        List<String> listAbil = plugin.getRingDataManager().getAbilities(staff.getStaffName());
//        ArrayList<Ability> abilities = new ArrayList<>();
//
//        for(String strAb : listAbil){
//            switch (plugin.getAbilityDataManager().getAbilityType(strAb)){
//                case SHIELD:
//                    abilities.add(new DisabledAbility(plugin, staff, strAb));
//                    break;
//                case MELEE:
//                    abilities.add(new FireBallAbility(plugin, staff, strAb));
//                    break;
//                case RANGED:
//                    abilities.add(new HeldPotionEffectAbility(plugin, staff, strAb));
//                    break;
//
//            }
//        }
//        return abilities;
//    }
//}
