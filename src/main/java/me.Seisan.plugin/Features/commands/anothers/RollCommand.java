package me.Seisan.plugin.Features.commands.anothers;

import me.Seisan.plugin.Main.Command;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class RollCommand extends Command {

    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {

        Player p = (Player) sender;
        boolean global = false;
        int lower = 1, upper = 20, resultat = 10;
        // Verifie s'il n'y a pas d'argument pour faire un roll basique qu'est le roll 20
        if (split.length != 0) {
            // Si argument on vérifie que c'est un entier, si non : erreur, si oui : roll sur l'entier
            if (estUnEntier(split[0])) {
                upper = Integer.parseInt(split[0]);
                if (upper < 1) {
                    upper = 20;
                }
                resultat = Commands.getRandom(lower, upper);
            } else {
                p.sendMessage(ChatColor.RED + "HRP : " + ChatColor.GRAY + "Il est nécessaire d'entrer un chiffre pour un tirage de dé.");
                return;
            }
        }
        resultat = Commands.getRandom(lower, upper);



        // Si on est pas entré dans l'erreur à cause de l'argument pas entier
        if (split.length > 1) {
            if (split[1].equals("global")) {
                p.sendMessage(ChatColor.RED + "Attention, l'abus du \"global\" peut être sanctionné.");
                global = true;
            }
        }
        if(global) {
            Bukkit.getServer().broadcastMessage(ChatColor.GRAY + "[GLOBAL] " + ChatColor.RESET + p.getDisplayName() + ChatColor.GRAY + " a tiré un " + resultat + " sur son dé à " + upper + " faces.*");
        }
        else {
            for (Entity target : p.getNearbyEntities(50, 50, 50)) {
                if (target instanceof Player) {
                    target.sendMessage(ChatColor.GRAY + "[LOCAL] " + ChatColor.RESET + p.getDisplayName() + ChatColor.GRAY + " a tiré un " + resultat + " sur son dé à " + upper + " faces.*");
                }
            }
            p.sendMessage(ChatColor.GRAY + "[LOCAL] " + ChatColor.RESET + p.getDisplayName() + ChatColor.GRAY + " a tiré un " + resultat + " sur son dé à " + upper + " faces.*");
            System.out.println(ChatColor.GRAY + "[LOCAL] " + ChatColor.RESET + p.getDisplayName() + ChatColor.GRAY + " a tiré un " + resultat + " sur son dé à " + upper + " faces.*");

        }
    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split)
    {
        List<String> completion = new ArrayList();
        if (split.length == 2) {
            complete(completion, "global", split[1]);
        }
        return completion;
    }


    // Permet de savoir si le string est un entier ou non
    private boolean estUnEntier(String chaine) {
        try {
            Integer.parseInt(chaine);
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

    @Override
    protected boolean isOpOnly() {
        return false;
    }
}
