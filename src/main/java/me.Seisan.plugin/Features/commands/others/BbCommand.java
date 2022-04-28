package me.Seisan.plugin.Features.commands.others;

import me.Seisan.plugin.Features.Inventory.SkillInventory;
import me.Seisan.plugin.Features.PlayerData.PlayerInfo;
import me.Seisan.plugin.Main.Command;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BbCommand extends Command {
    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        Player p = (Player)sender;
        PlayerInfo playerInfo = PlayerInfo.getPlayerInfo(p);
        ArrayList<ArrayList<Integer>> canGiveTo = new ArrayList<>(Arrays.asList(
                new ArrayList<>(Collections.emptyList()),
                new ArrayList<>(Collections.emptyList()),
                new ArrayList<>(Arrays.asList(0, 1)),
                new ArrayList<>(Arrays.asList(0, 1, 2)),
                new ArrayList<>(Arrays.asList(0, 1, 2)),
                new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4))
        ));
        if(playerInfo.getRank().getId() >= 2) {
            if(split.length == 1) {
                if(split[0].equals("check")) {
                    p.openInventory(SkillInventory.getInventoryBBCheck(p, 0));
                    return;
                }
                Player target = Bukkit.getPlayer(split[0]);
                if(target == null) {
                    p.sendMessage("§cHRP : §7Le joueur n'est pas connecté.");
                    return;
                }
                PlayerInfo targetInfo = PlayerInfo.getPlayerInfo(target);
                if(sender.isOp() || canGiveTo.get(playerInfo.getRank().getId()).contains(targetInfo.getRank().getId())) {
                    p.openInventory(SkillInventory.getInventoryBB(p, 0, target.getName()));
                    return;
                }
            } else {
                p.sendMessage("§cHRP : §7/bb (joueur)");
                return;
            }
        }
        p.sendMessage("§cHRP : §7Seuls les supérieurs hiérarchiques ont accès à cette commande, stop ouainouain comme un BB");
    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split)
    {
        List<String> completion = new ArrayList<>();
        if(split.length == 1) {
            complete(completion, "check", split[0]);
            for(Player p : Bukkit.getOnlinePlayers()) {
                complete(completion, p.getName(), split[0]);
            }
        }
        return completion;
    }

    @Override
    protected boolean isOpOnly() {
        return false;
    }
 }
