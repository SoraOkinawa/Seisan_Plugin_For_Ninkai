package me.Seisan.plugin.Features.commands.anothers;

import me.Seisan.plugin.Features.PlayerData.PlayerInfo;
import me.Seisan.plugin.Features.skill.SkillLevel;
import me.Seisan.plugin.Main.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

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
            if (action.length() >= 1 && !(split[0].equals("send"))) {
                waitingForAction.put(player.getUniqueId(), split);
                player.sendMessage("§4HRP : §b Votre Message est enregistré pour la suite !");
            }

            if (split[0].equals("send")) {
                sendPrier(player, split);
                List<String> completions = new ArrayList<>();
                for (SkillLevel skillLevel : SkillLevel.values()) {
                    completions.add(skillLevel.getCharName());
                }


            }


        }
    }


    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String
            label, String[] args) {
        if (args.length == 1 && waitingForAction.containsKey(((Player) sender).getUniqueId())) {
            return StringUtil.copyPartialMatches(args[0], Collections.singletonList("send"), new ArrayList<>());
        } else if (args.length > 1 && !(args[0].equals("send"))) {

            List<String> completions = new ArrayList<>();
            return StringUtil.copyPartialMatches(args[args.length - 1], completions, new ArrayList<>());
        }
        return Collections.emptyList();
    }


    private void sendPrier(Player player, String[] split) {
        player.sendMessage("§bHRP: Votre message a bien été envoyé !");
    }
}