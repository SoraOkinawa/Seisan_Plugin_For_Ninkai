package me.Seisan.plugin.Features.listener;

import me.Seisan.plugin.Features.Feature;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Barrel;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Door;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * Created by Helliot on 17/03/2018.
 */
public class LockListener extends Feature {

    private static ArrayList<Material> doorList = new ArrayList<>(Arrays.asList(Material.ACACIA_DOOR, Material.BIRCH_DOOR, Material.DARK_OAK_DOOR, Material.JUNGLE_DOOR, Material.SPRUCE_DOOR, Material.OAK_DOOR));
    private static ArrayList<Material> chestList = new ArrayList<>(Arrays.asList(Material.CHEST, Material.TRAPPED_CHEST));
    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        if(e.getHand() == EquipmentSlot.HAND){
            if(e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_BLOCK){
                Player p = e.getPlayer();
                if(doorList.contains(e.getClickedBlock().getType())){
                    Door door = (Door) e.getClickedBlock().getState().getData();
                    if(chestList.contains(p.getWorld().getBlockAt(getChestLoc(e.getClickedBlock())).getType())){
                        Block chestBlock = p.getWorld().getBlockAt(getChestLoc(e.getClickedBlock()));
                        Chest chest = (Chest) chestBlock.getState();

                        if(!door.isOpen() && isLocked(p, chest)){
                            if(p.isOp()) {
                                p.sendMessage(ChatColor.GRAY + "Vous avez forcé l'ouverture d'une porte fermée");
                            }else {
                                e.setCancelled(true);
                                p.sendMessage(ChatColor.RED + "** Cette porte est verrouillée **");
                            }
                        }
                    }
                }else if (chestList.contains(e.getClickedBlock().getType())){
                    Chest chest = (Chest) e.getClickedBlock().getState();
                    if(isLocked(p, chest)){
                        if(p.isOp()) {
                            p.sendMessage(ChatColor.GRAY + "Vous avez forcé l'ouverture d'un coffre fermé");
                        }else {
                            e.setCancelled(true);
                            p.sendMessage(ChatColor.RED + "** Ce coffre est verrouillé **");
                        }
                    }
                }
                else if (e.getClickedBlock().getType().name().contains("SHULKER_BOX")) {
                    InventoryHolder shulkerBox = (InventoryHolder) (e.getClickedBlock().getState());
                    if(isLockedShulker(p, shulkerBox)){
                        if(p.isOp()) {
                            p.sendMessage(ChatColor.GRAY + "Vous avez forcé l'ouverture d'un coffret fermé");
                        }else {
                            e.setCancelled(true);
                            p.sendMessage(ChatColor.RED + "** Ce coffret est verrouillé **");
                        }
                    }
                }
                else if (e.getClickedBlock().getType().name().contains("BARREL")) {
                    Barrel barrel = (Barrel) e.getClickedBlock().getState();
                    if(isLockedBarrel(p, barrel)){
                        if(p.isOp()) {
                            p.sendMessage(ChatColor.GRAY + "Vous avez forcé l'ouverture d'un tonneau fermé");
                        }else {
                            e.setCancelled(true);
                            p.sendMessage(ChatColor.RED + "** Ce coffret est verrouillé **");
                        }
                    }
                }
            }
        }
    }


    private boolean isLocked(Player p, Chest c){
        boolean locked = false;

        if(checkForPaper(p, c))
            locked = true;

        Chest otherChest = checkForOtherChest(c.getBlock());
        if(otherChest != null && checkForPaper(p,otherChest))
            locked = true;

        return locked;
    }

    private boolean isLockedShulker(Player p, InventoryHolder inv){
        boolean locked = false;

        for(int i = 0; i < 10; i++) {
            ItemStack itemStack = inv.getInventory().getItem(i);
            if(itemStack != null) {
                if(itemStack.getType() == Material.TRIPWIRE_HOOK) {
                    if(!itemStack.getItemMeta().getDisplayName().contains(p.getName())) {
                            locked = true;
                        }
                    }
                }
            }
        return locked;
    }

    private boolean isLockedBarrel(Player p, Barrel b){
        boolean locked = false;

        for(int i = 0; i < 10; i++) {
            ItemStack itemStack = b.getInventory().getItem(i);
            if(itemStack != null) {
                if(itemStack.getType() == Material.TRIPWIRE_HOOK) {
                    if(!itemStack.getItemMeta().getDisplayName().contains(p.getName())) {
                        locked = true;
                    }
                }
            }
        }
        return locked;
    }

    private boolean checkForPaper(Player p, Chest c){
        boolean locked = false;
        boolean wasPaperOnLine = false;
        boolean paperFound = false;

        for(int i= 0; i<9; i++) {
            if (c.getBlockInventory().getItem(i) != null && c.getBlockInventory().getItem(i).getType() == Material.TRIPWIRE_HOOK) {
                ItemStack paper = c.getBlockInventory().getItem(i);
                if (paper.hasItemMeta() && paper.getItemMeta().hasDisplayName()) {
                    if (!wasPaperOnLine)
                        wasPaperOnLine = true;

                    if (paper.getItemMeta().getDisplayName().contains(p.getName())) {
                        paperFound = true;
                        break;
                    }

                }
            }
        }

        if(wasPaperOnLine && !paperFound)
            locked = true;

        return locked;
    }

    private Chest checkForOtherChest(Block b){
        Chest chest = null;

        if(b.getRelative(BlockFace.EAST).getState() instanceof Chest)
            chest = (Chest) b.getRelative(BlockFace.EAST).getState();

        if(b.getRelative(BlockFace.NORTH).getState() instanceof Chest)
            chest = (Chest) b.getRelative(BlockFace.NORTH).getState();

        if(b.getRelative(BlockFace.SOUTH).getState() instanceof Chest)
            chest = (Chest) b.getRelative(BlockFace.SOUTH).getState();

        if(b.getRelative(BlockFace.WEST).getState() instanceof Chest)
            chest = (Chest) b.getRelative(BlockFace.WEST).getState();

        return chest;
    }

    private Location getChestLoc(Block b){
        if(doorList.contains(b.getWorld().getBlockAt(b.getLocation().add(new Vector(0,1,0))).getType())){
            return b.getLocation().add(new Vector(0, -2, 0));
        }else{
            return b.getLocation().add(new Vector(0, -3, 0));
        }
    }
}
