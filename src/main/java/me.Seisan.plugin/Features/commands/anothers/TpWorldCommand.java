package me.Seisan.plugin.Features.commands.anothers;

import me.Seisan.plugin.Main;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TpWorldCommand  extends Main.Command {
    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        //tpworld (joueur) (world) (x) (y) (z)
        if (split.length != 5) {
            sender.sendMessage("§cHRP : §7/tpworld (joueur) (world) (x) (y) (z)");
            return;
        }
        if(!isNumeric(split[2]) && !isNumeric(split[3]) && !isNumeric(split[4])){
            sender.sendMessage("§cHRP : §7/tpworld (joueur) (world)  (x) (y) (z) §2- x y z doivent être des chiffres !");
            return;
        }

        World w = Bukkit.getWorld(split[1]);
        if (w == null) {
            sender.sendMessage("Erreur nom monde.");
            return;
        }
        float x = Float.parseFloat(split[2]);
        float y = Float.parseFloat(split[3]);
        float z = Float.parseFloat(split[4]);
        if(sender instanceof BlockCommandSender) {
            for(Entity e : ((BlockCommandSender) sender).getBlock().getWorld().getNearbyEntities(((BlockCommandSender) sender).getBlock().getLocation(), 2.5, 2.5, 2.5)) {
                if(e instanceof Player) {
                    e.teleport(new Location(w, x, y, z));
                    return;
                }
            }
        }
        else {
            Player p = Bukkit.getPlayer(split[0]);
            if(p != null) {
                p.teleport(new Location(w, x, y, z));
            }
        }
    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split)
    {
        return new ArrayList<>();
    }

    private static boolean isNumeric(String str)
    {
        return str.matches("[+-]?\\d*(\\.\\d+)?" );
    }
}
