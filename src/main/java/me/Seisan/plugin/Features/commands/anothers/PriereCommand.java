package me.Seisan.plugin.Features.commands.anothers;

import me.Seisan.plugin.Features.Chat.ChatFormat;
import me.Seisan.plugin.Features.PlayerData.PlayerInfo;
import me.Seisan.plugin.Features.utils.DiscordWebhook;
import me.Seisan.plugin.Main;
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
import java.sql.Date;
import java.time.LocalDate;
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
            Date currentDate = Date.valueOf(LocalDate.now());

            // Si le joueur met un paramètre
            if (split.length != 0) {
                player.sendMessage("§4HRP : §cUsage : /priere");
            }
            // On vérifie le cooldown
            else if (!checkCooldown(player)) {
                player.sendMessage("§4HRP : §cVous avez déjà prié ce mois-ci. Attendez le mois prochain !");
            }
            // Si le joueur tape le /priere et qu'il n'a aucune priere en cours
            else if (!playersPraying.contains(player.getUniqueId())) {
                playersPraying.add(player.getUniqueId());
                player.sendMessage("§4HRP : §7Vous débuttez votre prière. Écrivez la dans le tchat comme une parole classique de votre personnage, §2sans aucun préfixe§7. La fonction '§2>§7' est compatible avec la prière.");
            }
            // Si le joueur tape /priere alors qu'il a déjà une priere en cours
            else {
                player.sendMessage("§4HRP : §7Vous êtes déjà en train de prier !");
            }
        }
    }

    @Override
    protected boolean isOpOnly() {
        return false;
    }

    public static void playerFinishPraying(Player player, String message) {
        player.sendMessage("§4HRP : §7Envoi de votre prière...");
        sendLog(player, message);
    }

    private static void sendLog (Player player, String message){
        DiscordWebhook webhook = new DiscordWebhook(DiscordWebhook.webhookConfig.getString("PriereCommand"));
        webhook.setContent(message);
        webhook.setUsername("Prière de " + ChatColor.stripColor(player.getDisplayName()) + " [" + player.getName() + "]");
        playersPraying.remove(player.getUniqueId());
        try {
            webhook.execute();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        PlayerInfo pInfo = PlayerInfo.getPlayerInfo(player);

        pInfo.setLastPrayer(Date.valueOf(LocalDate.now()));
        Main.dbManager.getPlayerDB().updatePlayer(pInfo);
        player.sendMessage("§bHRP : Votre prière a été envoyée !");
    }

    private boolean checkCooldown(Player player) {
        PlayerInfo pInfo = PlayerInfo.getPlayerInfo(player);

        LocalDate currentDate = LocalDate.now();
        LocalDate ancientDate = pInfo.getLastPrayer().toLocalDate();
        int dateComparison = currentDate.compareTo(ancientDate);

        Main.LOG.info(currentDate + " " + ancientDate + " " + dateComparison);

        return dateComparison > 0 && ancientDate.getMonth().getValue() != currentDate.getMonth().getValue();
    }

    public static boolean isPlayerPraying(Player player) {
        return playersPraying.contains(player.getUniqueId());
    }
    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        return Collections.emptyList();
    }
}










