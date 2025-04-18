package me.Seisan.plugin.Features.commands.profil;
import me.Seisan.plugin.Features.PlayerData.PlayerInfo;
import me.Seisan.plugin.Main.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Created by Helliot on 16/08/2018
 */
public class ApparenceCommand extends Command {
    // Utiliser un Map pour stocker les messages en cours d'écriture par joueur
    private static final Map<Player, StringBuilder> halfWrittenMessage = new HashMap<>();

    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            PlayerInfo playerInfo = PlayerInfo.getPlayerInfo(player);

            if (args.length == 0) {
                player.sendMessage("§cUtilisation : /apparence <message> ou /apparence show");
                return;
            }

            // Vérifier si la commande est "/apparence show" pour afficher l'apparence stockée
            if (args[0].equalsIgnoreCase("show")) {
                player.sendMessage("§4HRP : §aVotre apparence actuelle : §2" + playerInfo.getAppearance());
                return; // On arrête l'exécution ici pour éviter d'interpréter "show" comme message
            }

            // Concaténer tous les mots du message (après la commande) en une seule chaîne
            String message = String.join(" ", args).replace("&", "§");

            // Vérifie si le message se termine par ">" pour savoir s'il est incomplet
            if (message.endsWith(">")) {
                // Supprime le ">" et stocke le message comme une partie incomplète
                String incompleteMessage = message.substring(0, message.length() - 1);
                halfWrittenMessage.computeIfAbsent(player, k -> new StringBuilder()).append(incompleteMessage).append(" ");
                player.sendMessage("§cHRP : §7Message d'apparence partiel enregistré. Continuez en tapant la suite avec la commande /apparence.");
            } else {
                // Si le message est complet, vérifier si on a un message stocké pour ce joueur
                if (halfWrittenMessage.containsKey(player)) {
                    // Ajoute la partie finale au message complet stocké et l'envoie
                    message = halfWrittenMessage.get(player).append(message).toString();
                    halfWrittenMessage.remove(player);
                }
                playerInfo.setAppearance(message);
                player.sendMessage("§4HRP : §aVotre apparence est désormais : §2" + message);
            }
        }
    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        List<String> completion = new ArrayList<>();
        if (split.length == 1) {
            completion.add("show"); // Ajout de l'auto-complétion pour "show"
        }
        return completion;
    }


}