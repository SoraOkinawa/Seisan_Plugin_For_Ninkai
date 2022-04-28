package me.Seisan.plugin.Features.listener;

import me.Seisan.plugin.Features.Feature;
import me.Seisan.plugin.Features.PlayerData.PlayerClone;
import me.Seisan.plugin.Features.PlayerData.PlayerConfig;
import me.Seisan.plugin.Features.PlayerData.PlayerInfo;
import me.Seisan.plugin.Features.commands.anothers.BuildCommand;
import me.Seisan.plugin.Features.commands.anothers.Commands;
import me.Seisan.plugin.Features.commands.others.MeditationCommand;
import me.Seisan.plugin.Features.data.DBManager;
import me.Seisan.plugin.Features.data.PlayerDB;
import me.Seisan.plugin.Features.utils.Nickname;
import me.Seisan.plugin.Main;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.scheduler.BukkitRunnable;

import static me.Seisan.plugin.Features.commands.anothers.Commands.PlayerBuildTemp;
import static me.Seisan.plugin.Main.dbManager;
import static me.Seisan.plugin.Main.serverOpen;
import static me.Seisan.plugin.Features.commands.anothers.Commands.perms;
import static org.bukkit.Bukkit.getServer;


public class DataListener extends Feature {

    Main main = Main.plugin();


    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        e.setJoinMessage("");
        if(serverOpen) {
            Player p = e.getPlayer();
            Nickname.updateNick(p);
            if(Main.getIsSaving().contains(e.getPlayer().getName())) {
                e.getPlayer().kickPlayer(ChatColor.GOLD + "Attention, vos données ne sont pas encore sauvegardées !");
                return;
            }
            String command = "execute as " + p.getName() + " unless entity @s[team=CacheJoueurs] run team join CacheJoueurs @s";
            getServer().dispatchCommand(getServer().getConsoleSender(), command);
            DBManager dbManager = Main.dbManager;
            if(PlayerInfo.getPlayerInfo(p) == null) {
                PlayerDB playerDB = dbManager.getPlayerDB();
                playerDB.loadData(p);
            }
            if (PlayerConfig.getPlayerConfig(p) == null) {
                dbManager.getPlayerConfigDB().loadConfig(e.getPlayer());
                PermissionAttachment attachment = p.addAttachment(main);
                perms.put(p.getUniqueId(), attachment);
                BuildCommand.newPermissionBuildMode(PlayerConfig.getPlayerConfig(e.getPlayer()));
            }
            if (!p.isOp())
            {
                Main.plugin().getServer().getOnlinePlayers().forEach((player) -> {
                    if(PlayerConfig.getPlayerConfig(player) != null) {
                        if (PlayerConfig.getPlayerConfig(player).isVanish())
                            p.hidePlayer(Main.plugin(), player);
                    }
                    else {
                        retryNextTime(p, player);
                    }
                });
            }
            else {
                if (PlayerConfig.getPlayerConfig(p).isVanish()) {
                    Bukkit.getScheduler().runTaskLater(Main.plugin(), () -> Commands.playerVanish(PlayerConfig.getPlayerConfig(p)), 50);
                }
            }
            searchPlayerInfo(e.getPlayer());
        } else {
            e.getPlayer().kickPlayer(ChatColor.GOLD + "Seisan restart !");
        }
    }


    private void retryNextTime(Player player, Player p) {
        if(PlayerConfig.getPlayerConfig(p) == null) {
            Bukkit.getScheduler().runTaskLater(Main.plugin(), () -> retryNextTime(player, p), 20);
        }
        else {
            if (PlayerConfig.getPlayerConfig(p).isVanish())
                player.hidePlayer(Main.plugin(), p);
        }
    }

    private void searchPlayerInfo(Player p) {
        if(PlayerInfo.getPlayerInfo(p) == null) {
            Bukkit.getScheduler().runTaskLater(Main.plugin(), () -> searchPlayerInfo(p), 20);
        }
        else {
            // On dégage s'il est dans la zone de méditation
            if(p.getWorld().getName().equals("meditation") && PlayerInfo.getPlayerInfo(p).getOldpos() != null && !PlayerInfo.getPlayerInfo(p).getOldpos().equals("")) {
                Main.getInMedit().remove(p.getName());
                Main.getInBulleMedit().remove(p.getName());
                MeditationCommand.exitMedit(p.getName(), true);
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        e.setQuitMessage("");
        if(Main.getIsSaving().contains(e.getPlayer().getName())) { return; }



        if(dbManager != null) {
            PlayerConfig pConfig = PlayerConfig.getPlayerConfig(e.getPlayer());
            Main.dbManager.getPlayerConfigDB().updatePlayerConfig(pConfig);
            Player p = e.getPlayer();
            Location l = p.getLocation();

            if(Main.getIsJumping().containsKey(p.getName())) {
                Location block = Main.getIsJumping().get(p.getName());
                boolean notfind = true;
                for(int i = 2; i < 15 && notfind; ++i) {
                    if(l.clone().subtract(0,i,0).getBlock().getType() != Material.AIR) {
                        notfind = false;
                        block.getBlock().setType(Material.AIR);
                        p.teleport(l.clone().subtract(0,i-1.5,0));
                    }
                }
                if(notfind) {
                    p.teleport(l.clone().subtract(0,12.5,0));
                    block.getBlock().setType(Material.AIR);
                }

                Main.getIsJumping().remove(p.getName());
            }

            if(perms.containsKey(p.getUniqueId())) {
                p.removeAttachment(perms.get(p.getUniqueId()));
            }
            pConfig.destroy();
            if(PlayerBuildTemp.contains(e.getPlayer().getName())) {
                e.getPlayer().setGameMode(GameMode.SURVIVAL);
                PlayerBuildTemp.remove(e.getPlayer().getName());
            }

            PlayerInfo pInfo = PlayerInfo.getPlayerInfo(e.getPlayer());

            if(pInfo.getPlayerClone() != null)
                pInfo.getPlayerClone().destroyAllClones();

            PlayerClone.getCloneTicket().remove(e.getPlayer());

            DBManager dbManager = Main.dbManager;
            PlayerDB playerDB = dbManager.getPlayerDB();

            if(Main.getIDninkenFromNamePlayer().containsKey(e.getPlayer().getName())) {
                Entity entity = getServer().getEntity(Main.getIDninkenFromNamePlayer().get(e.getPlayer().getName()));
                if(entity != null) {
                    entity.remove();
                }
                Main.getIDninkenFromNamePlayer().remove(e.getPlayer().getName());
            }

            if(Main.getIsSwitch().containsKey(e.getPlayer().getName())) {
                NPC npc = Main.getIsSwitch().get(e.getPlayer().getName());
                if(npc != null) {
                    npc.destroy();
                }
                Main.getIsSwitch().remove(e.getPlayer().getName());
            }

            if(Main.getCurrentSelectSkill().containsKey(e.getPlayer().getName())) {
                Bukkit.getScheduler().cancelTask(Main.getCurrentSelectSkill().get(e.getPlayer().getName()));
                Main.getCurrentSelectSkill().remove(e.getPlayer().getName());
            }

            Main.getInMedit().remove(p.getName());
            if(Main.getIdMedit().containsKey(p.getName()))
                Bukkit.getScheduler().cancelTask(Main.getIdMedit().get(p.getName()));
            Main.getInBulleMedit().remove(p.getName());
            Main.getIdMedit().remove(p.getName());

            playerDB.updatePlayer(pInfo);

            pInfo.destroy();
        }

    }
}
