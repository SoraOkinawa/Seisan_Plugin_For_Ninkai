package me.Seisan.plugin.Features.commands.anothers;

import me.Seisan.plugin.Features.skill.SkillLevel;
import me.Seisan.plugin.Features.utils.DiscordWebhook;
import me.Seisan.plugin.Main.Command;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.bukkit.ChatColor.COLOR_CHAR;

public class PrierCommand extends Command {

    private final Map<UUID, String[]> waitingForAction = new HashMap<>();


    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {

        Player player = (Player) sender;
        if (sender instanceof Player) {

            //message d'erreur si il y a pas d'argument dans la commande
            if (split.length == 0) {
                player.sendMessage("§4HRP : §7Il faut faire la commande /priere <message>");
            }             //si le joueur met sa prière sans l'envoyer

            if (split.length >= 1 && !(split[0].equals("send"))) {
                waitingForAction.get(split);
                player.sendMessage("§aHRP : Votre prière: " + (Arrays.toString(split)) + " a été enregistré pour la suite !");
            }
            //Si le joueur envoi la prière avec le send
            if (split[0].equals("send")) {
                if (waitingForAction.containsKey(player.getUniqueId())) {
                    waitingForAction.get(player.getUniqueId());
                    waitingForAction.remove(player.getUniqueId());
                    sendLog(player, split);
                    player.sendMessage("HRP : Votre prière:" + (Arrays.toString(split)) + " a été envoyé !");


                }
            }

        }
    }




    //Pour la complétion
    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        if (split.length == 1 && waitingForAction.containsKey(((Player) sender).getUniqueId())) {
            return StringUtil.copyPartialMatches(split[0], Collections.singletonList("send"), new ArrayList<>());
        } else if (split.length > 1 && !split[0].equalsIgnoreCase("send")) {

            List<String> completions = new ArrayList<>();
            for (SkillLevel skillLevel : SkillLevel.values()) {
                completions.add(skillLevel.getCharName());
            }
            return StringUtil.copyPartialMatches(split[split.length - 1], completions, new ArrayList<>());
        }
        return Collections.emptyList();
    }
    private void sendLog(Player player, String[] split){
        DiscordWebhook webhook = new DiscordWebhook("https://discord.com/api/webhooks/1279553496244289556/nfnloSQZN9y6eqy_ANfFMvRuo_qK2XA1z-sWes5UntesPKs9t9pk5lq_jKHYWDoLbeFV");
        webhook.setContent("Le joueur " + player.getDisplayName() + " à prié : " + (Arrays.toString(split)));
        webhook.setUsername(ChatColor.stripColor(player.getDisplayName()) + " [" + player.getName() + "]");
        try {
            webhook.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}










