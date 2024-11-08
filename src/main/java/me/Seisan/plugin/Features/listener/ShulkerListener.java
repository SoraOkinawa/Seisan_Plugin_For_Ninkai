package me.Seisan.plugin.Features.listener;

import me.Seisan.plugin.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import me.Seisan.plugin.Features.Feature;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ShulkerListener extends Feature {
    public Main main = Main.plugin();
    Map<Player, ItemStack> openshulkers = new HashMap<>();
    Map<UUID, Inventory> openinventories = new HashMap<>();
    String defaultname = ChatColor.BLUE + "Coffret";
    float volume = 10;

    /*
    Saves the shulker on inventory drag if its open
     */
    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(main, () -> {
                if (!saveShulker(player, event.getView().getTitle())) {
                    event.setCancelled(true);
                }
            }, 1);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            if (openshulkers.containsKey(player)) {
                if(event.getCurrentItem() != null) {
                    if (event.getCurrentItem().getType().toString().contains("SHULKER")) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }


    /*
    Saves the shulker if its open, then removes the current open shulker from the player data
     */
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getPlayer() instanceof Player) {
            Player player = (Player) event.getPlayer();
            if (saveShulker(player, player.getOpenInventory().getTitle())) {
                player.playSound(player.getLocation(), Sound.BLOCK_SHULKER_BOX_CLOSE, volume, 1);
            }
            openshulkers.remove(player);
        }
    }

    /*
    Opens the shulker if the air was clicked with one
     */
    @EventHandler
    public void onClickAir(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null || event.getClickedBlock().getType() == Material.AIR) {
            if (event.getPlayer().isSneaking()) {
                if (event.getAction() == Action.RIGHT_CLICK_AIR) {
                    ItemStack item = event.getItem();
                    openInventoryIfShulker(item, event.getPlayer());
                }
            }
        }
    }

    /*
    Saves the shulker data in the itemmeta
     */
    public boolean saveShulker(Player player, String title) {
        try {
            if (openshulkers.containsKey(player)) {
                if (title.equals(defaultname) || (openshulkers.get(player).hasItemMeta() &&
                        openshulkers.get(player).getItemMeta().hasDisplayName() &&
                        (openshulkers.get(player).getItemMeta().getDisplayName().equals(title)))) {
                    ItemStack item = openshulkers.get(player);
                    if (item != null) {
                        BlockStateMeta meta = (BlockStateMeta) item.getItemMeta();
                        ShulkerBox shulker = (ShulkerBox) meta.getBlockState();
                        shulker.getInventory().setContents(openinventories.get(player.getUniqueId()).getContents());
                        meta.setBlockState(shulker);
                        item.setItemMeta(meta);
                        openshulkers.put(player, item);
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            openshulkers.remove(player);
            player.closeInventory();
            return false;
        }
        return false;
    }

    /*
    Opens the shulker inventory with the contents of the shulker
     */
    public boolean openInventoryIfShulker(ItemStack item, Player player) {
        if (item != null) {
            if (item.getAmount() == 1 && item.getType().toString().contains("SHULKER")) {
                if (item.getItemMeta() instanceof BlockStateMeta) {
                    BlockStateMeta meta = (BlockStateMeta) item.getItemMeta();
                    if (meta != null && meta.getBlockState() instanceof ShulkerBox) {
                        ShulkerBox shulker = (ShulkerBox) meta.getBlockState();
                        Inventory inv;
                        if (meta.hasDisplayName()) {
                            inv = Bukkit.createInventory(null, InventoryType.SHULKER_BOX, meta.getDisplayName());
                        } else {
                            inv = Bukkit.createInventory(null, InventoryType.SHULKER_BOX, defaultname);
                        }
                        inv.setContents(shulker.getInventory().getContents());

                        player.openInventory(inv);
                        player.playSound(player.getLocation(), Sound.BLOCK_SHULKER_BOX_OPEN, volume, 1);
                        openshulkers.put(player, item);
                        openinventories.put(player.getUniqueId(), player.getOpenInventory().getTopInventory());
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
