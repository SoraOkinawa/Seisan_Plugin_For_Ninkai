package me.Seisan.plugin.Features.listener;

import me.Seisan.plugin.Features.Feature;
import me.Seisan.plugin.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by Helliot on 27/04/2018
 */
public class TrainListener extends Feature {
/*
    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent e){
        if(Main.getTempBanList().contains(e.getPlayer().getUniqueId().toString())){
            e.getPlayer().kickPlayer("§aUne manipulation sur vos données est en cours, veuillez vous reconnecter dans 1 minute");
            return;
        }

        if(!Main.getTrainTickets().isEmpty() && e.getPlayer().isOp()){
            e.getPlayer().sendMessage(ChatColor.AQUA + "Il y a des demandes d'entraînement en attente. " + ChatColor.DARK_AQUA + "(/train list pour les voir)");
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        if(Main.getMeditationList().containsKey(e.getPlayer())){
            Bukkit.getScheduler().cancelTask(Main.getMeditationList().get(e.getPlayer()));
            Bukkit.getScheduler().cancelTask(Main.getMeditationTrigger().get(e.getPlayer()));
            Main.getMeditationList().remove(e.getPlayer());
            Main.getMeditationTrigger().remove(e.getPlayer());

            if(Main.getMeditationConfirm().containsKey(e.getPlayer())){
                Bukkit.getScheduler().cancelTask(Main.getMeditationConfirm().get(e.getPlayer()));
                Main.getMeditationConfirm().remove(e.getPlayer());
            }

            for(Player online : Bukkit.getOnlinePlayers()){
                if(online.isOp()){
                    online.sendMessage(e.getPlayer().getDisplayName() + ChatColor.RED + " s'est déconnecté pendant sa méditation.");
                }
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e){
        if(Main.getMeditationList().containsKey(e.getPlayer())){
            boolean differentX = e.getFrom().getX() != e.getTo().getX();
            boolean differentY = e.getFrom().getY() != e.getTo().getY();
            boolean differentZ = e.getFrom().getZ() != e.getTo().getZ();

            if (differentX || differentY || differentZ){
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncPlayerChatEvent e){
        if(Main.getMeditationList().containsKey(e.getPlayer())) {
            if (!(e.getMessage().startsWith("*") || e.getMessage().startsWith("$") || e.getMessage().startsWith("(") || e.getMessage().startsWith("=") || e.getMessage().startsWith(";") || e.getMessage().startsWith("@") || e.getMessage().startsWith(":"))) { //Si le message est RP
                e.setCancelled(true);
                e.getPlayer().sendMessage(ChatColor.RED + "Il n'est pas logique de parler pendant une méditation, vous pouvez néanmoins faire des actions. (*)");
            }else{
                if(Main.getMeditationTrigger().containsKey(e.getPlayer())){
                    if(e.getMessage().length() >= 50) {
                        Bukkit.getScheduler().cancelTask(Main.getMeditationTrigger().get(e.getPlayer()));
                        try {
                            Bukkit.getScheduler().cancelTask(Main.getMeditationConfirm().get(e.getPlayer()));
                        }catch (NullPointerException ex){

                        }
                        Main.getMeditationTrigger().remove(e.getPlayer());
                        TrainInventoryListener.launchMeditationTrigger(PlayerInfo.getPlayerInfo(e.getPlayer()));
                        //Reset du trigger de 4 min si message de + de 50 caractères
                    }
                }
            }
        }
    }

 */
}
