package me.Seisan.plugin.Features.commands.anothers;

import me.Seisan.plugin.Features.Chat.ChatFormat;
import me.Seisan.plugin.Features.utils.DiscordWebhook;
import me.Seisan.plugin.Main.Command;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentBuilder;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.awt.*;
import java.util.*;
import java.util.List;

import static org.apache.commons.lang.StringUtils.split;

public class PriereCommand extends Command {

    private static Map<UUID, String> halfWrittenMessage = new HashMap<>();

    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {

        Player player = (Player) sender;
        if (sender instanceof Player) {

            //message d'erreur si il y a pas d'argument dans la commande
            if (split.length == 0) {
                player.sendMessage("§4HRP : §7Il faut faire la commande /priere <message>");
            }
            else if (!(split[0].equals(("send")))) {
                String message = "";
                if (halfWrittenMessage.containsKey(player.getUniqueId()))
                    message = halfWrittenMessage.get(player.getUniqueId()) + " ";
                message += String.join(" ", split);

                halfWrittenMessage.put(player.getUniqueId(), message);
                player.sendActionBar(Component.text("HRP : ", NamedTextColor.DARK_RED)
                        .append(Component.text("Prière allongée avec ce nouveau texte. Si vous avez terminé, tapez ", NamedTextColor.DARK_GREEN))
                        .append(Component.text("/priere send ", NamedTextColor.GREEN))
                        .append(Component.text("pour l'envoyer.", NamedTextColor.DARK_GREEN))
                );
            }
            //Si le joueur envoi la prière avec le send
            else {
                if (halfWrittenMessage.containsKey(player.getUniqueId())) {
                    String message = halfWrittenMessage.get(player.getUniqueId());
                    sendLog(player, message);
                    player.sendMessage("§bHRP : Votre prière a été envoyé !");
                }
            }
        }
    }

    private void sendLog (Player player, String message){
        DiscordWebhook webhook = new DiscordWebhook("https://discord.com/api/webhooks/1288410639655506004/HbE3tnoqPjLI4C53STL-1LvdiBm6n47obryqPsZsz0oEwFqQ-IvpU71hj5WsA912XoOE");
        webhook.setContent(message);
        webhook.setUsername("Prière de " + ChatColor.stripColor(player.getDisplayName()) + " [" + player.getName() + "]");
        try {
            webhook.execute();
            halfWrittenMessage.remove(player.getUniqueId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Pour la complétion
    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        if (split.length == 1 && halfWrittenMessage.containsKey(((Player) sender).getUniqueId())) {
            return StringUtil.copyPartialMatches(split[0], Collections.singletonList("send"), new ArrayList<>());
        }
        return Collections.emptyList();
    }
}










