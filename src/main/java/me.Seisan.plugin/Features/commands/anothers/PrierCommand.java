package me.Seisan.plugin.Features.commands.anothers;

import me.Seisan.plugin.Features.PlayerData.PlayerInfo;
import me.Seisan.plugin.Main.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class PrierCommand extends Command {

    private final Map<UUID, String[]> waitingForAction = new HashMap<>();


    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        String action;
        Player player;
        if (sender instanceof Player) {
            player = (Player) sender;
            PlayerInfo playerInfo = PlayerInfo.getPlayerInfo(player);
            //message d'erreur si il y a pas d'argument dans la commande
            if (split.length == 0) {
                player.sendMessage("§4HRP : §7Il faut faire la commande /priere <message>");
            }
            action = Arrays.toString(split);
            if (action.length() >= 1) {
                if (action.length() >= 200) {
                    waitingForAction.put(player.getUniqueId(), split);
                    player.sendMessage("§4HRP : §b Votre Message est enregistré pour la suite !");
                } else {
                    waitingForAction.remove(player.getUniqueId());
                    player.sendMessage("§bHRP: Votre message a bien été envoyé !");
                }
            }
        }


    }


    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String
            label, String[] split) {
        return List.of();
    }
}
