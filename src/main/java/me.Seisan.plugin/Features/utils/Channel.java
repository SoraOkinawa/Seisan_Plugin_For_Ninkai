package me.Seisan.plugin.Features.utils;

import me.Seisan.plugin.Features.Feature;
import me.Seisan.plugin.Features.data.Config;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;


public class Channel extends Feature {

    private static Config config = new Config("mj.yml");
    private static ArrayList<UUID> adminList = loadMJlist();

    @Override
    protected void doRegister() {
        saveMJlist();
    }
    private static ArrayList<UUID> loadMJlist(){
        ArrayList<UUID> list = new ArrayList<>();
        if(config.isInConfig("mjlist")){
            ArrayList<String> stringUUID = new ArrayList<>(config.getStringList("mjlist"));
            for(String s : stringUUID){
                try{
                    list.add(UUID.fromString(s));
                }catch (IllegalArgumentException e){
                    Bukkit.getLogger().severe("Invalid UUID in list chatadmins from config.yml: " + s + " is not an UUID");
                }
            }
        }
        return list;
    }

    public static boolean isMJ(Player p){
        return adminList.contains(p.getUniqueId());
    }

    public static void setMJ(Player p){
        adminList.add(p.getUniqueId());
        saveMJlist();
        p.sendMessage(ChatColor.GREEN + "Vous avez été ajouté à la liste des administrateurs du chat !");
    }

    public static void removeMJ(Player p){
        adminList.remove(p.getUniqueId());
        saveMJlist();
        p.sendMessage(ChatColor.RED + "Vous avez été retiré de la liste des administrateurs du chat !");
    }

    public static void saveMJlist(){
        ArrayList<String> list = new ArrayList<>();
        for(UUID u : adminList){
            list.add(u.toString());
        }

        config.set("mjlist", list);
    }

}
