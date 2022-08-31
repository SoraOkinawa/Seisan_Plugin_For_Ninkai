package me.Seisan.plugin.Features.utils;

import me.Seisan.plugin.Features.Feature;
import me.Seisan.plugin.Features.data.Config;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Nickname extends Feature {

    private static Config config = new Config("nickname.yml");

    private static String getNick(String uuid){
        if(config.isInConfig(uuid)) {
            return config.getString(uuid);
        }
        return null;
    }

    public static void updateNick(Player p) {
        String uuid = p.getUniqueId().toString();
        String name = getNick(uuid);
        if(name != null) {
            p.setDisplayName(name);
            p.setPlayerListName(name);
        }
    }

    public static void setNick(CommandSender sender, Player p, String nick) {
        config.set(p.getUniqueId().toString(), nick);
        p.setDisplayName(nick);
        p.setPlayerListName(nick);
        if(sender != p)
            sender.sendMessage("§cHRP : §7"+p.getName()+" a été renommé en "+p.getDisplayName() +"§7.");
        p.sendMessage("§cHRP : §7Vous avez été renommé en " + p.getDisplayName() + "§7.");
    }


}
