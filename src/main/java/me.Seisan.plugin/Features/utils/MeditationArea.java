package me.Seisan.plugin.Features.utils;

import me.Seisan.plugin.Features.Feature;
import me.Seisan.plugin.Features.PlayerData.Meditation;
import me.Seisan.plugin.Features.data.Config;
import me.Seisan.plugin.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.UUID;


public class MeditationArea extends Feature {

    private static final Config config = new Config("meditation.yml");


    private static int xinit = getXinit();
    private static int zinit = getZinit();


    private static int getXinit() {
        int xinit = -641;
        if(config.isInConfig("xinit")) {
            xinit = config.getInt("xinit");
        }
        return xinit;
    }


    private static int getZinit() {
        int zinit = 127;
        if(config.isInConfig("zinit")) {
            zinit = config.getInt("zinit");
        }
        return zinit;
    }


    public static void initMeditation(Meditation m){
        Player p = Bukkit.getPlayer(UUID.fromString(m.getNameplayer()));
        if(p == null) return;
        int xborder = xinit;
        int zborder = zinit;
        m.setXborder(xborder);
        m.setZborder(zborder);
        m.setXspawn(xinit+5);
        m.setYspawn(2.5);
        m.setZspawn(zinit+5);
        m.setHasmedit(true);

        xinit += 80;
        if(xinit == 719) {
            xinit = -641;
            zinit += 80;
        }


        config.set("xinit", xinit);
        config.set("zinit", zinit);
        Main.dbManager.getMeditationDB().updateMedit(m);

        World w = Bukkit.getWorld("meditation");
        assert w != null;

        p.sendMessage("§cHRP : §7Création de la bulle à 0%...");
        for(int x = xborder; x < xborder+65; x++) {
            for(int y = 1; y < 255; y++) {
                w.getBlockAt(x, y, zborder).setType(Material.BARRIER);
            }
        }
        p.sendMessage("§cHRP : §7Création de la bulle à 20%...");
        for(int x = xborder; x < xborder+65; x++) {
            for(int y = 1; y < 255; y++) {
                w.getBlockAt(x, y, zborder+65).setType(Material.BARRIER);
            }
        }

        p.sendMessage("§cHRP : §7Création de la bulle à 40%...");
        for(int z = zborder; z < zborder+65; z++) {
            for(int y = 1; y < 255; y++) {
                w.getBlockAt(xborder, y, z).setType(Material.BARRIER);
            }
        }

        p.sendMessage("§cHRP : §7Création de la bulle à 60%...");
        for(int z = zborder; z < zborder+65; z++) {
            for(int y = 1; y < 255; y++) {
                w.getBlockAt(xborder+65, y, z).setType(Material.BARRIER);
            }
        }

        p.sendMessage("§cHRP : §7Création de la bulle à 80%...");
        for(int x = xborder; x < xborder+65;x++) {
            for(int z = zborder; z < zborder+65; z++) {
                w.getBlockAt(x, 254, z).setType(Material.BARRIER);
            }
        }

        p.sendMessage("§cHRP : §7Création de la bulle à 100%...");
    }

    public static void addInventory(Player p, ItemStack[] inventory){
        Meditation m = Meditation.getMeditationFromUUID(p.getUniqueId().toString());
        if(m == null)  {
            System.out.println("AAAA");
            return;
        }
        m.setInventory(ItemUtil.itemStackArrayToBase64(inventory));
        Main.dbManager.getMeditationDB().updateMedit(m);
    }

    public static ItemStack[] getInventory(Player p) throws IOException {
        Meditation m = Meditation.getMeditationFromUUID(p.getUniqueId().toString());
        if(m == null)
            return null;
        return ItemUtil.itemStackArrayFromBase64(m.getInventory());
    }

    public static void switchInventory(Player p) {
        ItemStack[] old_inv = p.getInventory().getContents();
        ItemStack[] new_inv = null;
        try {
            new_inv = getInventory(p);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        addInventory(p, old_inv);
        if(new_inv == null) {
            p.getInventory().clear();
        }
        else {
            p.getInventory().setContents(new_inv);
        }
    }


    public static void changeSpawn(Player p) {
        String uuid = p.getUniqueId().toString();
        Location l = p.getLocation();
        int x = l.getBlockX();
        int y = l.getBlockY();
        int z = l.getBlockZ();
        Meditation meditation = Meditation.getMeditationFromUUID(uuid);
        if(meditation == null) {
            p.sendMessage("§cHRP : §7Erreur lors du /setspawn");
            return;
        }
        meditation.setXspawn(x);
        meditation.setYspawn(y);
        meditation.setZspawn(z);

        Main.dbManager.getMeditationDB().updateMedit(meditation);
    }

    public static Location getSpawn(Meditation m) {

        World w = Bukkit.getWorld("meditation");
        if(w == null || m == null) {
            return null;
        }

        double x = m.getXspawn();
        double y = m.getYspawn();
        double z = m.getZspawn();
        return new Location(w, x, y, z);
    }

    public static Location getInit(String uuid) {

        Meditation meditation = Meditation.getMeditationFromUUID(uuid);
        World w = Bukkit.getWorld("meditation");
        if(w == null || meditation == null) {
            return null;
        }

        int x = meditation.getXborder();
        int z = meditation.getZborder();
        return new Location(w, x, 1, z);
    }

}