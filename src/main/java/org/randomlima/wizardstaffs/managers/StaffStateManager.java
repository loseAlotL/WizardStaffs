package org.randomlima.wizardstaffs.managers;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.randomlima.wizardstaffs.Staff;
import org.randomlima.wizardstaffs.StaffState;
import org.randomlima.wizardstaffs.WizardStaffs;

import java.util.Objects;

public class StaffStateManager implements Listener {
    private WizardStaffs plugin;
    private Staff staff;

    public StaffStateManager(WizardStaffs plugin, Staff staff){
        this.plugin = plugin;
        this.staff = staff;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        checkIfHeld(Bukkit.getPlayer(staff.getOwner()));
    }
    public void delete(){
        HandlerList.unregisterAll(this);
        staff.switchStaffState(StaffState.LOST);
    }
    public void unregister(){
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        if(event.getPlayer().getUniqueId() != staff.getOwner())return;
        delete();
    }
    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event){
        if(event.getPlayer().getUniqueId() != staff.getOwner())return;
        checkItemAndDelete(event.getItemDrop().getItemStack());
    }


    @EventHandler
    public void inventoryClose(InventoryCloseEvent event){
        if(event.getPlayer().getUniqueId() != staff.getOwner())return;
        checkInvAndDelete(event.getPlayer().getInventory());
        checkIfHeld((Player) event.getPlayer());
    }

    @EventHandler
    public void onKick(PlayerKickEvent event){
        if(staff.getState() == StaffState.LOST)return;
        if(event.getPlayer().getUniqueId() != staff.getOwner())return;
        delete();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent event){
        if(staff.getState() == StaffState.LOST)return;
        if(event.getPlayer().getUniqueId() != staff.getOwner())return;
        delete();
    }

    //for inventory state stuff
    @EventHandler
    public void onSwitchSLot(PlayerItemHeldEvent event){
        if(event.getPlayer().getUniqueId() != staff.getOwner())return;
        if(itemRingButNotThisRing(event.getPlayer().getInventory().getItemInOffHand())) {
            staff.switchStaffState(StaffState.INVENTORY);
            return;
        }
//            UUID uuid = plugin.getRingItemID(event.getPlayer().getInventory().getItemInOffHand());
//            for(Ring ring : plugin.getRings()){
//                if(ring.getUUID().equals(uuid) && ring.getRingState().equals(RingState.HELD))return;
//            }

        if(checkItem(event.getPlayer().getInventory().getItemInOffHand())){
            staff.switchStaffState(StaffState.HELD);
            return;
        }
        if(event.getPlayer().getInventory().getItem(event.getNewSlot()) != null && checkItem(event.getPlayer().getInventory().getItem(event.getNewSlot()))){
            //if (Objects.requireNonNull(ring.getRingState()) == RingState.INVENTORY)
            staff.switchStaffState(StaffState.HELD);
            return;
        }
        if(event.getPlayer().getInventory().getItem(event.getPreviousSlot()) != null && checkItem(event.getPlayer().getInventory().getItem(event.getPreviousSlot()))){
            //if (Objects.requireNonNull(ring.getRingState()) == RingState.HELD)
            staff.switchStaffState(StaffState.INVENTORY);
        }
//        ItemStack newItem = event.getPlayer().getInventory().getItem(event.getNewSlot());
//        ItemStack oldItem = event.getPlayer().getInventory().getItem(event.getPreviousSlot());
//        if(newItem != null && plugin.verifyRingItem(newItem) && Objects.equals(plugin.getRingItemID(newItem), ring.getUUID()) && ring.getRingState() == RingState.INVENTORY){
//            if(plugin.isPlayerHoldingRing(event.getPlayer()))return;
//            ring.switchRingState(RingState.HELD);
//        }
//        if(oldItem != null && plugin.verifyRingItem(oldItem) && Objects.equals(plugin.getRingItemID(oldItem), ring.getUUID()) && ring.getRingState() == RingState.HELD){
//            ring.switchRingState(RingState.INVENTORY);
//        }
    }
    public void checkInvAndDelete(Inventory inventory){
        for(ItemStack itemStack : inventory){
            if(itemStack == null) continue;
            if(plugin.verifyRingItem(itemStack) && plugin.getStaffItemID(itemStack).equals(staff.getUUID())) return;
        }
        delete();

//        if(!inventory.contains(ring.getItem())){
//            delete();
//        }
    }
    public void checkItemAndDelete(ItemStack itemStack){
        if(!plugin.verifyRingItem(itemStack))return;
        if(!plugin.getStaffItemID(itemStack).equals(staff.getUUID()))return;
        if(Objects.equals(plugin.getStaffItemID(itemStack), staff.getUUID())){
            delete();
        }
    }
    public void checkIfHeld(Player player){
        if(staff.getState() == StaffState.LOST)return;
        if(checkItem(player.getInventory().getItemInOffHand())){
            staff.switchStaffState(StaffState.HELD);
            return;
        }
        if(checkItem(player.getInventory().getItemInMainHand()) && !plugin.verifyRingItem(player.getInventory().getItemInOffHand())){
            staff.switchStaffState(StaffState.HELD);
            return;
        }
        if(itemRingButNotThisRing(player.getInventory().getItemInOffHand()) && checkItem(player.getInventory().getItemInMainHand())){
            staff.switchStaffState(StaffState.INVENTORY);
            return;
        }

        if(!checkItem(player.getInventory().getItemInMainHand()) && !checkItem(player.getInventory().getItemInOffHand())){
            staff.switchStaffState(StaffState.INVENTORY);
            return;
        }
    }

    public boolean checkItem(ItemStack itemStack){
        //if(plugin.verifyRingItem(itemStack) && !plugin.getRingItemID(itemStack).equals(ring.getUUID()) && plugin.isPlayerHoldingRing(ring.getOwner()) && ring.getRingState().equals(RingState.INVENTORY))return false;
        return plugin.verifyRingItem(itemStack) && plugin.getStaffItemID(itemStack).equals(staff.getUUID());
    }
    public boolean itemRingButNotThisRing(ItemStack itemStack){
        return plugin.verifyRingItem(itemStack) && !plugin.getStaffItemID(itemStack).equals(staff.getUUID());
    }
//    public boolean checkOffHand(Player player){
//        if(ring.getRingState() == RingState.LOST)return true;
//        if(!plugin.verifyRingItem(player.getInventory().getItemInOffHand())
//                || !plugin.getRingItemID(player.getInventory().getItemInOffHand()).equals(ring.getUUID())
//                && ring.getRingState() == RingState.HELD){
//            ring.switchRingState(RingState.INVENTORY);
//            return false;
//        }
//        if(!plugin.verifyRingItem(player.getInventory().getItemInOffHand()))return false;
//        if(player.getInventory().getItemInOffHand().getType() == Material.AIR)return false;
//        if(plugin.getRingItemID(player.getInventory().getItemInOffHand()).equals(ring.getUUID())){
//            if(ring.getRingState() == RingState.INVENTORY && plugin.isPlayerHoldingRing(player)){
//                ring.switchRingState(RingState.HELD);
//                return true;
//            }
//            if(ring.getRingState() == RingState.HELD && plugin.isPlayerHoldingRing(player))return true;
//            return false;
//        }
//        return false;
//    }
//    public void checkMainHand(Player player){
//        if(ring.getRingState() == RingState.LOST)return;
//        if(!plugin.verifyRingItem(player.getInventory().getItemInMainHand())
//                || !plugin.getRingItemID(player.getInventory().getItemInMainHand()).equals(ring.getUUID()) && ring.getRingState() == RingState.HELD){
//            ring.switchRingState(RingState.INVENTORY);
//            return;
//        }
//        if(!plugin.verifyRingItem(player.getInventory().getItemInMainHand()))return;
//        if(player.getInventory().getItemInMainHand().getType() == Material.AIR)return;
//        if(!plugin.isPlayerHoldingRing(player) && ring.getRingState() == RingState.INVENTORY && plugin.getRingItemID(player.getInventory().getItemInMainHand()).equals(ring.getUUID())){
//            ring.switchRingState(RingState.HELD);
//        }
//    }





