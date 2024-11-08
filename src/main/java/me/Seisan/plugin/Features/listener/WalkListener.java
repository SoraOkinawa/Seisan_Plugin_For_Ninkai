package me.Seisan.plugin.Features.listener;

import me.Seisan.plugin.Features.Feature;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

public class WalkListener extends Feature {
    @EventHandler(priority = EventPriority.HIGH)
    public void WalkVerif(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        if(!p.getScoreboardTags().isEmpty()) {
            if(!p.getScoreboardTags().contains("Marche")) {
                if(p.getWalkSpeed() != 0.2f) {
                    p.setWalkSpeed(0.2f);
                    // Ici, vérif de l'armure pour le slow
                }
            }
        }
    }
}
