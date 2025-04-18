package me.Seisan.plugin.Features.commands.profil;

import me.Seisan.plugin.Features.PlayerData.PlayerConfig;
import me.Seisan.plugin.Main.Command;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class RyojiCommand extends Command {
    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        if (sender.isOp()) {
            if(split.length == 2) {
                if(split[0].equals("add")) {
                    Player p = Bukkit.getPlayer(split[1]);
                    if(p == null) {
                        sender.sendMessage("§cHRP : §7Le joueur n'est pas connecté.");
                        return;
                    }
                    PlayerConfig pConfig = PlayerConfig.getPlayerConfig(p);
                    if(pConfig.isRyoji()) {
                        sender.sendMessage("§cHRP : §7Le joueur est déjà Ryoji.");
                        return;
                    }
                    pConfig.setRyoji(true);
                }
                else if(split[0].equals("remove")) {
                    Player p = Bukkit.getPlayer(split[1]);
                    if(p == null) {
                        sender.sendMessage("§cHRP : §7Le joueur n'est pas connecté.");
                        return;
                    }
                    PlayerConfig pConfig = PlayerConfig.getPlayerConfig(p);
                    if(!pConfig.isRyoji()) {
                        sender.sendMessage("§cHRP : §7Le joueur n'est pas Ryoji.");
                        return;
                    }
                    pConfig.setRyoji(false);
                }
                else {
                    sender.sendMessage("§cHRP : §7/ryoji add [joueur]");
                    sender.sendMessage("§cHRP : §7/ryoji remove [joueur]");
                }
            }
        }
        else {
            Player p = (Player)sender;
            PlayerConfig pConfig = PlayerConfig.getPlayerConfig(p);
            if(!pConfig.isRyoji()) {
                sender.sendMessage("§cHRP : §7Vous n'êtes pas ryoji et ne pouvez donc pas utiliser la commande.");
                return;
            }
            String line = "";
            for (int i = 0; i != split.length; i++) {
                line = line.concat(split[i]+" ");
            }
            line = line.substring(0, line.length()-1);
            World w = p.getWorld();
            for(Entity e : w.getPlayers()) {
                if(e instanceof Player) {
                    if(e.getLocation().distance(p.getLocation()) <= 600) {
                        e.sendMessage("§b[Haut Parleurs avec la voix de "+p.getDisplayName()+"§b] Tuduuum... "+line);
                    }
                }
            }
        }
    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split)
    {
        List<String> completion = new ArrayList();
        if(split.length == 1) {
            if(sender.isOp()) {
                complete(completion, "add", split[0]);
                complete(completion, "remove", split[0]);
            }
        }
        return completion;
    }



}
