package org.randomlima.wizardstaffs;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.randomlima.wizardstaffs.abilities.Ability;
import org.randomlima.wizardstaffs.commands.GetStaffCommand;
import org.randomlima.wizardstaffs.commands.ReloadCommand;
import org.randomlima.wizardstaffs.data.AbilityDataManager;
import org.randomlima.wizardstaffs.data.StaffDataManager;
import org.randomlima.wizardstaffs.managers.AbilityGenerator;
import org.randomlima.wizardstaffs.managers.NewStaffTracker;
import org.randomlima.wizardstaffs.managers.StaffInvencibility;
import org.randomlima.wizardstaffs.utilities.StaffKeys;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public final class WizardStaffs extends JavaPlugin implements Listener {
    private AbilityDataManager abilityDataManager;
    private StaffDataManager staffDataManager;
    private AbilityGenerator abilityGenerator;
    private GetStaffCommand getStaffCommand;
    private NewStaffTracker newStaffTracker;
    private ArrayList<Staff> staffList = new ArrayList<>();
    private ArrayList<UUID> disconnectedList = new ArrayList<>();
    private BukkitScheduler scheduler;

    @Override
    public void onEnable() {
        this.scheduler = getServer().getScheduler();
        getServer().getPluginManager().registerEvents(this, this);
        // Plugin startup logic
        StaffKeys.staffNameKey = new NamespacedKey(this, "LOTRing-ring-name");
        StaffKeys.staffIDKey = new NamespacedKey(this, "LOTRings-ringID");
        this.abilityDataManager = new AbilityDataManager(this, "abilities.yml");
        this.staffDataManager = new StaffDataManager(this, "rings.yml");
        this.abilityGenerator = new AbilityGenerator(this);
        this.newStaffTracker = new NewStaffTracker(this);
        //commands
        this.getStaffCommand = new GetStaffCommand(this);
        new ReloadCommand(this);
        new StaffInvencibility(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    public void reload(){
        for(Staff staff : staffList){
            staff.reloadPrepare();
        }
        staffList.clear();
        this.abilityDataManager = new AbilityDataManager(this, "abilities.yml");
        this.staffDataManager = new StaffDataManager(this, "rings.yml");

        for(Player player : Bukkit.getOnlinePlayers()){
            newStaffTracker.scanPlayerForRing(player);
        }
        //delete all the ring objects.
        //reset the configs.
        //check online players for rings.
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        removeDisconnectedList(event.getPlayer().getUniqueId());
    }
    @EventHandler
    public void onKick(PlayerKickEvent event){
        addDisconnectedList(event.getPlayer().getUniqueId());
    }
    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        addDisconnectedList(event.getPlayer().getUniqueId());
    }
    public void addDisconnectedList(UUID uuid){
        if(!disconnectedList.contains(uuid)) disconnectedList.add(uuid);
    }
    public void removeDisconnectedList(UUID uuid){
        disconnectedList.remove(uuid);
    }
    public ArrayList<UUID> getDisconnectedList(){
        return disconnectedList;
    }
    public ArrayList<Staff> getRings(){
        return staffList;
    }
    public ArrayList<UUID> getStaffIDs(){
        ArrayList<UUID> staffIDs = new ArrayList<>();
        for(Staff staff : staffList){
            staffIDs.add(staff.getUUID());
        }
        return staffIDs;
    }


    public AbilityDataManager getAbilityDataManager(){
        return abilityDataManager;
    }
    public StaffDataManager getStaffDataManager(){return staffDataManager;}
    public ArrayList<Ability> getAbilities(Staff staff){
        return abilityGenerator.getAbilities(staff);
    }
    public boolean verifyRingItem(ItemStack itemStack){
        if(itemStack.getItemMeta() == null)return false;
        if(!itemStack.getItemMeta().getPersistentDataContainer().has(StaffKeys.staffIDKey))return false;
        if(!itemStack.getItemMeta().getPersistentDataContainer().has(StaffKeys.staffNameKey))return false;
        if(!staffDataManager.getRingNames().contains(itemStack.getItemMeta().getPersistentDataContainer().get(StaffKeys.staffNameKey, PersistentDataType.STRING)))return false;
        return true;
    }
    public boolean checkIfNewRing(ItemStack itemStack){
        if(!verifyRingItem(itemStack))return false;
        if(getStaffIDs().isEmpty())return true;
//        if(getStaffIDs() == null)return true;
        return !getStaffIDs().contains(UUID.fromString(Objects.requireNonNull(itemStack.getItemMeta().getPersistentDataContainer().get(StaffKeys.staffIDKey, PersistentDataType.STRING))));
    }
    //    public boolean checkInvForRing(Inventory inventory){
//        for(ItemStack itemStack : inventory){
//            if(itemStack != null){
//                if(verifyRingItem(itemStack)){
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//    public boolean isPlayerHoldingRing(UUID ownerUUID){
//        for(Ring ring : ringList){
//            if(ring.getState() == RingState.HELD && ring.getOwner().equals(ownerUUID)){
//                return true;
//            }
//        }
//        return false;
//    }
//    public UUID getPlayerHeldRingID(UUID uuid){
//        for(Ring ring : ringList){
//            if(ring.getState().equals(RingState.HELD) && ring.getOwner().equals(uuid))return ring.getUUID();
//        }
//        return UUID.randomUUID();
//    }
//    public boolean isPlayerHoldingThisRing(Player player, Ring ring){
//        for(Ring r : ringList){
//            if(ring.getRingState() == RingState.HELD && r.getOwner() == player.getUniqueId()){
//                return true;
//            }
//        }
//        return false;
//    }
    public UUID getStaffItemID(ItemStack itemStack){
        if(!verifyRingItem(itemStack))return null;
        return UUID.fromString(Objects.requireNonNull(itemStack.getItemMeta().getPersistentDataContainer().get(StaffKeys.staffIDKey, PersistentDataType.STRING)));
    }

    public void addNewStaff(ItemStack item, UUID owner){
        if(!verifyRingItem(item))return;
        //if(!checkIfNewRing(item)) return;
        if(!checkIfNewRing(item)) {
//            Bukkit.broadcastMessage("oh no");
            for(Staff staff : staffList){
                if(staff.getUUID().equals(getStaffItemID(item)) && staff.getState().equals(StaffState.LOST))
                    staff.load(item, owner);
            }
            return;
        }
        Staff staff = new Staff(this, item, owner);
        staffList.add(staff);
    }
    public void deleteStaff(UUID uuid){
        //Bukkit.broadcastMessage("deleting: " + ringList.toString());
        //Bukkit.broadcastMessage("dleeting Ring");
        Staff staffToDelete = null;
        for(Staff staff : staffList){
            if(staff.getUUID().equals(uuid)) staffToDelete = staff;
        }
        staffList.remove(staffToDelete);
    }
}
