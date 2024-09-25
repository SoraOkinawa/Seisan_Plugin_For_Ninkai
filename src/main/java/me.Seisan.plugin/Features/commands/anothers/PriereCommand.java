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

//    private static Map<UUID, String> halfWrittenMessage = new HashMap<>();
private static List<UUID> playersPraying = new ArrayList<>();

    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {

        Player player = (Player) sender;
        if (sender instanceof Player) {

            // Si le joueur met un paramètre
            if (split.length != 0) {
                player.sendMessage("§4HRP : §7Usage : /priere");
            }
            // Si le joueur tape le /priere et qu'il n'a aucune priere en cours
            else if(!playersPraying.contains(player.getUniqueId())) {
                playersPraying.add(player.getUniqueId());
                player.sendMessage("§4HRP : §7Vous débuttez votre prière. Écrivez la dans le tchat comme une parole classique de votre personnage, §2sans aucun préfixe§7. La fonction '§2>§7' est compatible avec la prière.");
            }
            // Si le joueur tape /priere alors qu'il a déjà une priere en cours
            else {
                player.sendMessage("§4HRP : §7Vous êtes déjà en train de prier !");
            }
        }
    }

    public static boolean isPlayerPraying(Player player) {
        return playersPraying.contains(player.getUniqueId());
    }

    public static void playerFinishPraying(Player player, String message) {
        player.sendMessage("§4HRP : §7Envoi de votre prière...");
        sendLog(player, message);
    }

    private static void sendLog (Player player, String message){
        DiscordWebhook webhook = new DiscordWebhook("https://discord.com/api/webhooks/1288410639655506004/HbE3tnoqPjLI4C53STL-1LvdiBm6n47obryqPsZsz0oEwFqQ-IvpU71hj5WsA912XoOE");
        webhook.setContent(message);
        webhook.setUsername("Prière de " + ChatColor.stripColor(player.getDisplayName()) + " [" + player.getName() + "]");
        try {
            webhook.execute();
            playersPraying.remove(player.getUniqueId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        player.sendMessage("§bHRP : Votre prière a été envoyée !");
    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        return Collections.emptyList();
    }
}










