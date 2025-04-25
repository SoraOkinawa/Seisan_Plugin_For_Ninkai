package me.Seisan.plugin.Features.commands.others;

import me.Seisan.plugin.Features.PlayerData.PlayerInfo;
import me.Seisan.plugin.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import me.Seisan.plugin.Main.Command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNumeric;

public class TPBackCommand extends Command {
    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        //tpworld (joueur) (world) (x) (y) (z)
        if (split.length != 5 && split.length != 7) {
            sender.sendMessage("§cHRP : §7/tpworld <joueur> <world> <x> <y> <z> [yaw] [pitch]");
            return;
        }
        Player p = Bukkit.getPlayer(split[0]);
        if (p == null) {
            sender.sendMessage("Erreur nom joueur.");
            return;
        }

        if (Main.inMedit.contains(p.getName())) {
            p.sendMessage("§cHRP : §7Non, pas de téléportation en méditation.");
            return;
        }

        PlayerInfo playerInfo = PlayerInfo.getPlayerInfo(p);
        World w;
        double x, y, z;
        float yaw = 0;
        float pitch = 0;
        if (playerInfo.getOldpos() != null && !playerInfo.getOldpos().equals("")) {
            String[] oldpos = playerInfo.getOldpos().split(";");
            w = Bukkit.getWorld(oldpos[0]);
            x = Double.parseDouble(oldpos[1]);
            y = Double.parseDouble(oldpos[2]);
            z = Double.parseDouble(oldpos[3]);

            // On modifie avant au cas où le chemn arrière n'est pas chargé
            if (w == null) {
                sender.sendMessage("Erreur nom monde.");
                return;
            }
            playerInfo.setOldpos("");
        } else {
            if (!isNumeric(split[2]) && !isNumeric(split[3]) && !isNumeric(split[4])) {
                sender.sendMessage("§cHRP : §7/tpworld <joueur> <world> <x> <y> <z> §2- x y z doivent être des chiffres !");
                return;
            }
            else if (split.length == 7 && !isNumeric(split[5]) && !isNumeric(split[6])) {
                sender.sendMessage("§cHRP : §7/tpworld <joueur> <world> <x> <y> <z> [yaw] [pitch] §2- yaw & pitch doivent être des nombres à virgule !");
                return;
            }

            w = Bukkit.getWorld(split[1]);
            x = Double.parseDouble(split[2]);
            y = Double.parseDouble(split[3]);
            z = Double.parseDouble(split[4]);
            if (split.length == 7) {
                yaw = Float.parseFloat(split[5]);
                pitch = Float.parseFloat(split[6]);
            }
            
            if (w == null) {
                sender.sendMessage("Erreur nom monde.");
                return;
            }
            playerInfo.setOldpos(p.getWorld().getName() + ";" + p.getLocation().getX() + ";" + p.getLocation().getY() + ";" + p.getLocation().getZ());
        }
        if (split.length == 7)
            p.teleport(new Location(w, x, y, z, yaw, pitch));
        else
            p.teleport(new Location(w, x, y, z));
    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        return new ArrayList<>();
    }
}
