package me.Seisan.plugin.Features.commands.profil;


import me.Seisan.plugin.Features.PlayerData.PlayerInfo;
import me.Seisan.plugin.Features.objectnum.RPRank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import me.Seisan.plugin.Main.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Helliot on 16/03/2018.
 */
public class RankCommand extends Command {
    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        if(split.length == 2) {
            Player target = sender.getServer().getPlayer(split[1]);
            if(target == null) {
                sender.sendMessage("§cHRP : §7La cible n'est pas connectée.");
                return;
            }
            RPRank rank;
            PlayerInfo targetInfo = PlayerInfo.getPlayerInfo(target);
            switch (split[0]) {
                case "promote":
                        rank = RPRank.getById(targetInfo.getRank().getId() + 1);
                        if (rank != RPRank.NULL) {
                            targetInfo.setRank(rank);
                            sender.sendMessage(ChatColor.GREEN + "Le rang de " + target.getDisplayName() + ChatColor.GREEN + " est désormais " + ChatColor.GOLD + rank.getDisplayName());
                        } else {
                            sender.sendMessage(ChatColor.RED + "Ce joueur ne peut être promu à un plus haut grade");
                        }
                    break;
                case "demote":
                        rank = RPRank.getById(targetInfo.getRank().getId() - 1);
                        if (rank != RPRank.NULL) {
                            targetInfo.setRank(rank);
                            sender.sendMessage(ChatColor.GREEN + "Le rang de " + target.getDisplayName() + ChatColor.GREEN + " est désormais " + ChatColor.GOLD + rank.getDisplayName());
                        } else {
                            sender.sendMessage(ChatColor.RED + "Ce joueur ne peut être rétrogradé à un plus bas grade");
                        }
                    break;
            }
        }
    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split)
    {
        List<String> completion = new ArrayList();
        if(split.length == 1) {
            complete(completion, "promote", split[0]);
            complete(completion, "demote", split[0]);
        }
        else if (split.length == 2) {
            for(Player p : Bukkit.getOnlinePlayers()) {
                complete(completion, p.getName(), split[1]);
            }
        }
        return completion;
    }
}
