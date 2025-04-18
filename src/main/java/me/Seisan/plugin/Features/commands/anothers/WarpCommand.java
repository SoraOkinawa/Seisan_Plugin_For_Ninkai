package me.Seisan.plugin.Features.commands.anothers;

import me.Seisan.plugin.Features.PlayerData.PlayerConfig;
import me.Seisan.plugin.Features.data.Config;
import me.Seisan.plugin.Main.Command;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class WarpCommand extends Command {

    private static final Config config = new Config("warp.yml");


    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if ((sender.isOp()) || PlayerConfig.getPlayerConfig(p).isBuildmode()) {
                if (split.length < 1) {
                    p.sendMessage(ChatColor.RED + "Syntaxe: /warp (add/remove/list) (name)");
                } else {
                    switch (split[0]) {
                        case "add":
                            if (split.length < 2) {
                                p.sendMessage(ChatColor.RED + "Syntaxe: /warp add (name)");
                            }
                            config.set(split[1] + ".world", p.getWorld().getName());
                            config.set(split[1] + ".x", p.getLocation().getX());
                            config.set(split[1] + ".y", p.getLocation().getY());
                            config.set(split[1] + ".z", p.getLocation().getZ());
                            config.set(split[1] + ".rp", p.getLocation().getPitch());
                            config.set(split[1] + ".ry", p.getLocation().getYaw());
                            p.sendMessage("§cHRP : §7Warp §b" + split[1] + " §7ajouté.");
                            break;
                        case "remove":
                            if (split.length < 2) {
                                p.sendMessage(ChatColor.RED + "Syntaxe: /warp remove (name)");
                            }
                            if (config.isInConfig(split[1])) {
                                config.set(split[1], null);
                                p.sendMessage("§cHRP : §7Warp §b" + split[1] + " §7supprimé.");
                            }
                            break;
                        case "list":
                            if (config.getRootKeys().size() != 0) {
                                Set<String> warps = config.getRootKeys();
                                p.sendMessage("§cHRP : §7Liste des warps : §b" + String.join("§7, §b", warps));
                            } else
                                p.sendMessage(ChatColor.GREEN + "§cHRP : §7Aucun warp.");
                            break;
                        default:
                            if (config.isInConfig(split[0])) {
                                World w = Bukkit.getWorld(config.getString(split[0] + ".world"));
                                double x = config.getDouble(split[0] + ".x");
                                double y = config.getDouble(split[0] + ".y");
                                double z = config.getDouble(split[0] + ".z");
                                float rp = (float) config.getDouble(split[0] + ".rp");
                                float ry = (float) config.getDouble(split[0] + ".ry");
                                p.teleport(new Location(w, x, y, z, rp, ry));
                                p.sendMessage(ChatColor.GREEN + "§cHRP : §7Vous avez été téléporté au warp §b" + split[0] + "§7.");
                            } else {
                                p.sendMessage("§cHRP : §7Le warp §b" + split[0] + " §7n'existe pas.");
                            }
                            break;
                    }
                }
            }
        }
    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.isOp()) {
                Set<String> warps = config.getRootKeys();
                ArrayList<String> completion = new ArrayList();
                for (String warp : warps) {
                    complete(completion, warp, split[0]);
                }
                return completion;
            }
        }
        return null;
    }



}
