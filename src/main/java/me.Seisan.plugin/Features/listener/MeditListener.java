package me.Seisan.plugin.Features.listener;

import me.Seisan.plugin.Features.Feature;
import me.Seisan.plugin.Features.PlayerData.Meditation;
import me.Seisan.plugin.Features.PlayerData.PlayerInfo;
import me.Seisan.plugin.Features.utils.MeditationArea;
import me.Seisan.plugin.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.Arrays;
import java.util.UUID;

public class MeditListener extends Feature {

    @EventHandler
    public void onTP(PlayerTeleportEvent event) {
        String name = event.getPlayer().getName();
        if(Main.getInMedit().containsKey(name) && !event.getPlayer().isOp()) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("§cHRP : Interdiction de vous téléporter en méditation.");
        }
    }
/*
    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if(e.getEntity() instanceof Player) {
            if(e.getEntity().getWorld().getName().equals("meditation")) e.setCancelled(true);
            // Les hoshigaki et hozuki ne se noient pas
            if(e.getCause() == EntityDamageEvent.DamageCause.DROWNING) {
                Player p = (Player) e.getEntity();
                PlayerInfo playerInfo = PlayerInfo.getPlayerInfo(p);
                if(playerInfo.getClan().getId() == 8 || (playerInfo.getClan().getId() == 9 && playerInfo.getLvL(playerInfo.getClan().getName()) >= 2)) {
                    e.setCancelled(true);
                }
            }
        }
    }
*/
    @EventHandler
    public void onInteractEvent(PlayerInteractEvent event) {
        if(!event.getPlayer().getWorld().getName().equals("meditation")) {
            return;
        }

        Player breaker = event.getPlayer();

        if(breaker.isOp()) {
            return;
        }

        if(!Main.getInBulleMedit().containsKey(breaker.getName())) {
            event.setCancelled(true);
            return;
        }


        String namemc_propro = Main.getInBulleMedit().get(breaker.getName());
        Player proprio = Bukkit.getPlayer(namemc_propro);
        if(proprio == null) {
            breaker.sendMessage("§cHRP : §7Impossible d'altérer la zone si votre partenaire n'est pas connecté.");
            event.setCancelled(true);
            return;
        }

        if(!Main.getInBulleMedit().containsKey(proprio.getName()) || !Main.getInBulleMedit().get(proprio.getName()).equals(proprio.getName())) {
            breaker.sendMessage("§cHRP : §7Impossible d'altérer la zone si votre partenaire n'est pas dans sa bulle.");
            event.setCancelled(true);
            return;
        }

        PlayerInfo pInfo = PlayerInfo.getPlayerInfo(proprio);
        if(!namemc_propro.equals(breaker.getName())) {
            if(pInfo.getManamaze() < 150) {
                breaker.sendMessage("§b** Le propriétaire de la bulle ne semble pas avoir obtenu le niveau adéquat pour vous lier davantage.");
                event.setCancelled(true);
                return;
            }
        }


    }

    @EventHandler
    public void onBreakBlock(BlockBreakEvent breakEvent) {
        if(!breakEvent.getPlayer().getWorld().getName().equals("meditation")) {
            return;
        }

        Player breaker = breakEvent.getPlayer();

        if(breaker.isOp()) {
            return;
        }

        if(!Main.getInBulleMedit().containsKey(breaker.getName())) {
            breakEvent.setCancelled(true);
            return;
        }

        if(breakEvent.getBlock().getType() == Material.BARRIER) {
            breakEvent.setCancelled(true);
            return;
        }

        String namemc_propro = Main.getInBulleMedit().get(breaker.getName());
        Player proprio = Bukkit.getPlayer(namemc_propro);
        if(proprio == null) {
            breaker.sendMessage("§cHRP : §7Impossible d'altérer la zone si votre partenaire n'est pas connecté.");
            breakEvent.setCancelled(true);
            return;
        }

        if(!Main.getInBulleMedit().containsKey(proprio.getName()) || !Main.getInBulleMedit().get(proprio.getName()).equals(proprio.getName())) {
            breaker.sendMessage("§cHRP : §7Impossible d'altérer la zone si votre partenaire n'est pas dans sa bulle.");
            breakEvent.setCancelled(true);
            return;
        }

        PlayerInfo pInfo = PlayerInfo.getPlayerInfo(proprio);
        if(!namemc_propro.equals(breaker.getName())) {
            if(pInfo.getManamaze() < 150) {
                breaker.sendMessage("§b** Le propriétaire de la bulle ne semble pas avoir obtenu le niveau adéquat pour vous lier davantage.");
                breakEvent.setCancelled(true);
                return;
            }
        }
        int chunks;
        if(pInfo.getManamaze() >= 250) {
            chunks = 4;
        }
        else if(pInfo.getManamaze() >= 200) {
            chunks = 2;
        }
        else {
            chunks = 1;
        }
        Location location_start = MeditationArea.getInit(proprio.getUniqueId().toString());
        if(location_start == null) {
            breaker.sendMessage("§cJe... Je deviens fou. Par pitié, arrêtez...");
            return;
        }

        if(!isInAreaPlayer(location_start, breakEvent.getBlock().getLocation(), chunks)) {
            breaker.sendMessage("§b** Vous n'avez pas l'emprise d'altérer plus loin.");
            breakEvent.setCancelled(true);
        }

    }


    @EventHandler
    public void onPlaceBlock(BlockPlaceEvent placeEvent) {
        if(!placeEvent.getPlayer().getWorld().getName().equals("meditation")) {
            return;
        }

        Player breaker = placeEvent.getPlayer();

        if(breaker.isOp()) {
            return;
        }

        if(!Main.getInBulleMedit().containsKey(breaker.getName())) {
            placeEvent.setCancelled(true);
            return;
        }

        if(placeEvent.getBlock().getType() == Material.BARRIER) {
            placeEvent.setCancelled(true);
            return;
        }

        String namemc_proprio = Main.getInBulleMedit().get(breaker.getName());
        Player proprio = Bukkit.getPlayer(namemc_proprio);
        if(proprio == null) {
            breaker.sendMessage("§cHRP : §7Impossible d'altérer la zone si votre partenaire n'est pas connecté.");
            placeEvent.setCancelled(true);
            return;
        }

        if(!Main.getInBulleMedit().containsKey(namemc_proprio) || !Main.getInBulleMedit().get(namemc_proprio).equals(namemc_proprio)) {
            breaker.sendMessage("§cHRP : §7Impossible d'altérer la zone si votre partenaire n'est pas dans sa bulle.");
            placeEvent.setCancelled(true);
            return;
        }

        PlayerInfo pInfo = PlayerInfo.getPlayerInfo(proprio);

        if(!namemc_proprio.equals(breaker.getName())) {
            if(pInfo.getManamaze() < 150) {
                breaker.sendMessage("§b** Le propriétaire de la bulle ne semble pas avoir obtenu le niveau adéquat pour vous lier davantage.");
                placeEvent.setCancelled(true);
                return;
            }
        }
        int chunks;
        if(pInfo.getManamaze() >= 250) {
            chunks = 4;
        }
        else if(pInfo.getManamaze() >= 200) {
            chunks = 2;
        }
        else {
            chunks = 1;
        }
        Location location_start = MeditationArea.getInit(proprio.getUniqueId().toString());
        if(location_start == null) {
            breaker.sendMessage("§cJe... Je deviens fou. Par pitié, arrêtez...");
            return;
        }

        if(!isInAreaPlayer(location_start, placeEvent.getBlock().getLocation(), chunks)) {
            breaker.sendMessage("§b** Vous n'avez pas l'emprise d'altérer plus loin.");
            placeEvent.setCancelled(true);
        }
    }

    public boolean isInAreaPlayer(Location loc_init, Location loc_target, int chunks) {
        int x_min = loc_init.getBlockX();
        int z_min = loc_init.getBlockZ();
        int x_max = x_min+chunks*16;
        int z_max = z_min+chunks*16;
        int x_target = loc_target.getBlockX();
        int z_target = loc_target.getBlockZ();

        if(x_min > x_target || x_max < x_target)
            return false;

        return z_min <= z_target && z_max >= z_target;
    }
}
