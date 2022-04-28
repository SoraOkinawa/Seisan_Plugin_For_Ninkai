package me.Seisan.plugin.Features.listener;

import me.Seisan.plugin.Features.Feature;
import me.Seisan.plugin.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class LayListener extends Feature {
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void removeArmorStandSit(PlayerMoveEvent event) {
        if(event.getPlayer().getScoreboardTags().contains("lay") || Main.getIsJumping().containsKey(event.getPlayer().getName())) {
            if (event.getTo().getX() != event.getFrom().getX() || event.getTo().getZ() != event.getFrom().getZ()) {
                event.setCancelled(true);
            }
            if(event.getPlayer().getScoreboardTags().contains("lay") && (event.getTo().getY() != event.getFrom().getY())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void PlayerQuitListener(PlayerQuitEvent event)
    {
        event.getPlayer().getScoreboardTags().remove("lay");
        if(Main.getIsJumping().containsKey(event.getPlayer().getName())) {
            Main.getIsJumping().get(event.getPlayer().getName()).getBlock().setType(Material.AIR);
            Main.getIsJumping().remove(event.getPlayer().getName());
        }
    }

}
