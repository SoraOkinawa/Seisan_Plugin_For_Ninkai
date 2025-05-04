package me.Seisan.plugin.Features.commands.others;

import me.Seisan.plugin.Features.Inventory.SkillInventory;
import me.Seisan.plugin.Features.PlayerData.PlayerInfo;
import me.Seisan.plugin.Main.Command;
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
        PlayerInfo playerInfo = PlayerInfo.getPlayerInfo(p);
        
        switch (split.length) {
            case 0:
                throw new NotImplementedException("Feature à venir soon !");
            case 1:
                break;
            case 2:
                break;
            case 3:
                if (split[0].equals("points")) {
                    if ((split[1].equals("add") && p.hasPermission(PERMISSION_POINTS_ADD)) || (split[1].equals("remove") && p.hasPermission(PERMISSION_POINTS_REMOVE))) {
                        int amount = Integer.parseInt(split[2]);
                        int modifier = (split[1].equals("add") ? 1 : -1);
                        
                        playerInfo.setJutsuPoints(playerInfo.getJutsuPoints() + (amount * modifier));
                    }
                    else if (split[1].equals("set") && p.hasPermission(PERMISSION_POINTS_SET)) {
                        int amount = Integer.parseInt(split[2]);
                        
                        playerInfo.setJutsuPoints(amount);
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
                    complete(completion, "check", split[0]);
                break;
            case 2:
                if (p.hasPermission(PERMISSION_POINTS_ADD)) complete(completion, "add", split[1]);
                if (p.hasPermission(PERMISSION_POINTS_REMOVE)) complete(completion, "remove", split[1]);
                if (p.hasPermission(PERMISSION_POINTS_SET)) complete(completion, "set", split[1]);
                break;
            case 3:
                if (p.hasPermission(PERMISSION_POINTS_ADD) || p.hasPermission(PERMISSION_POINTS_REMOVE) || p.hasPermission(PERMISSION_POINTS_SET))
                    complete(completion, "nombre entier", split[2]);
                break;
        }
        
        return completion;
    }
 }
