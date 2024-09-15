package me.Seisan.plugin.Features.commands.anothers;

import me.Seisan.plugin.Features.Chat.ChatFormat;
import me.Seisan.plugin.Features.utils.DiscordWebhook;
import me.Seisan.plugin.Main.Command;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.util.StringUtil;

import java.util.*;

import static org.apache.commons.lang.StringUtils.split;

public class PrierCommand extends Command {

    private static Map<UUID, String[]> halfWrittenMessage = new HashMap<>();


    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {

        Player player = (Player) sender;
        if (sender instanceof Player) {

            //message d'erreur si il y a pas d'argument dans la commande
            if (split.length == 0) {
                player.sendMessage("§4HRP : §7Il faut faire la commande /priere <message>");

            }
            if (!(split[0].equals(("send")))) {
                halfWrittenMessage.put(player.getUniqueId(), split);
                player.sendMessage("§aHRP : §bVotre prière a été enregistré pour l'envoyer !");
            }
            //Si le joueur envoi la prière avec le send
            if (split[0].equals("send")) {
                if (halfWrittenMessage.containsKey(player.getUniqueId())) {
                    String[] message = halfWrittenMessage.get(player.getUniqueId());
                    sendLog(player, Arrays.toString(message));
                    player.sendMessage("§bHRP : Votre prière a été envoyé !");
                }

            }
        }


    }
        private void sendLog (Player player, String message){
            DiscordWebhook webhook = new DiscordWebhook("https://discord.com/api/webhooks/1279553496244289556/nfnloSQZN9y6eqy_ANfFMvRuo_qK2XA1z-sWes5UntesPKs9t9pk5lq_jKHYWDoLbeFV");
            webhook.setContent("Le joueur " + player.getDisplayName() + " à prié : " + message);
            webhook.setUsername(ChatColor.stripColor(player.getDisplayName()) + " [" + player.getName() + "]");
            try {
                webhook.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    //Pour la complétion
    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String
            label, String[] split){
                        if (split.length == 1 && halfWrittenMessage.containsKey(((Player) sender).getUniqueId())) {
                            return StringUtil.copyPartialMatches(split[0], Collections.singletonList("send"), new ArrayList<>());
                        } else if (split.length > 1 && !split[0].equalsIgnoreCase("send")) {
                        }
                        return Collections.emptyList();
                    }
}










