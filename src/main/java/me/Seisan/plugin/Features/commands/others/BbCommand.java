package me.Seisan.plugin.Features.commands.others;

import me.Seisan.plugin.Features.Inventory.SkillInventory;
import me.Seisan.plugin.Features.PlayerData.PlayerInfo;
import me.Seisan.plugin.Main.Command;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.NotImplementedException;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BbCommand extends Command {
    public static final String PERMISSION_POINTS_ADD = "ninkai.bb.points.add";
    public static final String PERMISSION_POINTS_REMOVE = "ninkai.bb.points.remove";
    public static final String PERMISSION_POINTS_SET = "ninkai.bb.points.set";
    
    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        Player p = (Player)sender;
        
        switch (split.length) {
            case 0:
                throw new NotImplementedException("Feature à venir soon !");
            case 1:
            case 2:
            case 3:
                p.sendMessage(ChatColor.RED + "Usage : /bb <add|remove|set> <player> <amount>");
                break;
            case 4:
                Player target = p.getServer().getPlayer(split[2]);
                if (target == null) p.sendMessage(ChatColor.RED + "Usage : /bb <add|remove|set> <player> <amount>");
                PlayerInfo targetInfo = PlayerInfo.getPlayerInfo(target);
                
                if (split[0].equals("points")) {
                    int amount = Integer.parseInt(split[3]);
                    if ((split[1].equals("add") && p.hasPermission(PERMISSION_POINTS_ADD)) || (split[1].equals("remove") && p.hasPermission(PERMISSION_POINTS_REMOVE))) {
                        int modifier = (split[1].equals("add") ? 1 : -1);
    
                        targetInfo.setJutsuPoints(targetInfo.getJutsuPoints() + (amount * modifier));
                        target.sendMessage("§7Vous avez " + (split[1].equals("add") ? "§aobtenu" : "§cperdu") + " §r§6" + amount + " §r§7points de techniques. Nouveau solde : §6" + targetInfo.getJutsuPoints());
                        p.sendMessage("§b" + target.getName() + " §7a " + (split[1].equals("add") ? "§aobtenu" : "§cperdu") + " §r§6" + amount + " §r§7points de techniques. Nouveau solde : §6" + targetInfo.getJutsuPoints());
                    }
                    else if (split[1].equals("set") && p.hasPermission(PERMISSION_POINTS_SET)) {
                        targetInfo.setJutsuPoints(amount);
                        target.sendMessage("§7Vous avez maintenant §6" + targetInfo.getJutsuPoints() + " §r§7points de compétences.");
                        p.sendMessage("§b" + target.getName() + " §7a maintenant §6" + targetInfo.getJutsuPoints() + " §r§7points de compétences.");
                    }
                }
                break;
        }

//        Player p = (Player)sender;
//        PlayerInfo playerInfo = PlayerInfo.getPlayerInfo(p);
//        ArrayList<ArrayList<Integer>> canGiveTo = new ArrayList<>(Arrays.asList(
//                new ArrayList<>(Collections.emptyList()),
//                new ArrayList<>(Collections.emptyList()),
//                new ArrayList<>(Arrays.asList(0, 1)),
//                new ArrayList<>(Arrays.asList(0, 1, 2)),
//                new ArrayList<>(Arrays.asList(0, 1, 2)),
//                new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4))
//        ));
//        if(playerInfo.getRank().getId() >= 2) {
//            if(split.length == 1) {
//                if(split[0].equals("check")) {
//                    p.openInventory(SkillInventory.getInventoryBBCheck(p, 0));
//                    return;
//                }
//                Player target = Bukkit.getPlayer(split[0]);
//                if(target == null) {
//                    p.sendMessage("§cHRP : §7Le joueur n'est pas connecté.");
//                    return;
//                }
//                PlayerInfo targetInfo = PlayerInfo.getPlayerInfo(target);
//                if(sender.isOp() || canGiveTo.get(playerInfo.getRank().getId()).contains(targetInfo.getRank().getId())) {
//                    p.openInventory(SkillInventory.getInventoryBB(p, 0, target.getName()));
//                    return;
//                }
//            } else {
//                p.sendMessage("§cHRP : §7/bb (joueur)");
//                return;
//            }
//        }
//        p.sendMessage("§cHRP : §7Seuls les supérieurs hiérarchiques ont accès à cette commande, stop ouainouain comme un BB");
    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split)
    {
        Player p = (Player) sender;
        
        List<String> completion = new ArrayList<>();
        switch (split.length) {
            case 1:
                if (p.hasPermission(PERMISSION_POINTS_ADD) || p.hasPermission(PERMISSION_POINTS_REMOVE) || p.hasPermission(PERMISSION_POINTS_SET))
                    complete(completion, "points", split[0]);
                break;
            case 2:
                if (p.hasPermission(PERMISSION_POINTS_ADD)) complete(completion, "add", split[1]);
                if (p.hasPermission(PERMISSION_POINTS_REMOVE)) complete(completion, "remove", split[1]);
                if (p.hasPermission(PERMISSION_POINTS_SET)) complete(completion, "set", split[1]);
                break;
            case 3:
                if (p.hasPermission(PERMISSION_POINTS_ADD) || p.hasPermission(PERMISSION_POINTS_REMOVE) || p.hasPermission(PERMISSION_POINTS_SET)) {
                    for (Player onlinePlayer: Bukkit.getOnlinePlayers())
                        complete(completion, onlinePlayer.getName(), split[2]);
                }
                break;
            case 4:
                if (p.hasPermission(PERMISSION_POINTS_ADD) || p.hasPermission(PERMISSION_POINTS_REMOVE) || p.hasPermission(PERMISSION_POINTS_SET))
                    complete(completion, "nombreEntier", split[3]);
                break;
        }
        
        return completion;
    }
 }
