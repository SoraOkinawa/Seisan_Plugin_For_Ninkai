package me.Seisan.plugin.Features.commands.profil;

import me.Seisan.plugin.Features.PlayerData.PlayerInfo;
import me.Seisan.plugin.Main.Command;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class FeuilleCommand extends Command {
    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        // Si c'est la console
        if(sender.isOp()) {
            if(split.length == 1) {
                Player p = Bukkit.getPlayer(split[0]);
                if(p != null) {
                    PlayerInfo playerInfo = PlayerInfo.getPlayerInfo(p);
                    if (playerInfo.getFuin_uzumaki() > 0) {
                        playerInfo.addPaperUzumaki();
                        p.sendMessage("§b** Vous décrochez les feuilles de Seji de l'assembleur Uzumaki et avez désormais " + playerInfo.getFuin_paper()+" feuilles de Seji.");
                    }
                    else{
                        p.sendMessage("§b** L'assembleur Uzumaki est vide. Vous restez à " + playerInfo.getFuin_paper()+" feuilles de Seji.");
                    }
                }
            }
        }

    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split)
    {
        return new ArrayList<>();
    }

}

