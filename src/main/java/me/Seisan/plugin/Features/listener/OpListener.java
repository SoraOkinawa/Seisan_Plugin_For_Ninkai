package me.Seisan.plugin.Features.listener;

import me.Seisan.plugin.Features.Feature;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;


import static me.Seisan.plugin.Features.commands.anothers.Commands.perms;

public class OpListener  extends Feature {

    @EventHandler(priority = EventPriority.HIGH)
    public void GamemodeVerif(PlayerJoinEvent event){
        Player p = event.getPlayer();
        newPermissionOp(p);
        perms.get(p.getUniqueId()).setPermission("parrotglue.command.releaseparrots", true);
    }

    private static void newPermissionOp(Player p){
        perms.get(p.getUniqueId()).setPermission("mv.bypass.gamemode.*", true);
    }
}
