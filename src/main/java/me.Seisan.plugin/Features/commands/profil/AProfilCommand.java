package me.Seisan.plugin.Features.commands.profil;


import me.Seisan.plugin.Features.PlayerData.PlayerInfo;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import me.Seisan.plugin.Main.Command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;


public class AProfilCommand  extends Command {
    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        Player p = (Player)sender;
        if((split.length == 0) || (split.length == 1 && (!split[0].equals("list") && !split[0].equals("clear")))) {
            sender.sendMessage("§cHRP & Usage : §7/aprofil add <message> : ajoute une page de description");
            sender.sendMessage("§cHRP & Usage : §7/aprofil remove <nbpage> : retire la page de description");
            sender.sendMessage("§cHRP & Usage : §7/aprofil clear : supprime toutes les pages de description");
            sender.sendMessage("§cHRP & Usage : §7/aprofil list : liste le début des pages de description");
            sender.sendMessage("§cHRP & Usage : §7/aprofil see <joueur> : liste le début des pages de description");
            return;
        }
        PlayerInfo pInfo = PlayerInfo.getPlayerInfo(p);
        String apparence = pInfo.getApparenceprofil();
        String[] pages;
        switch (split[0]) {
            case "list":
                if(apparence == null || apparence.equals("")) {
                    sender.sendMessage("§cHRP : §7Vous n'avez aucune apparence créé sur votre profil");
                    return;
                }
                pages = apparence.split(";");
                int i = 1;
                for(String page : pages) {
                    int low = 40;
                    if(page.length() < 40) {
                        low = page.length();
                    }
                    sender.sendMessage("§9Page §6"+i+" §9: §7"+page.substring(0,low)+"[...]");
                    i++;
                }
                break;
            case "add":
                if(apparence == null) {
                    apparence = "";
                }
                pages = apparence.split(";");

                if(pages.length > 4) {
                    sender.sendMessage("§cHRP : §7Vous ne pouvez avoir plus de 5 pages de présentation, veuillez en supprimer.");
                    return;
                }
                String message = "";
                boolean param = true;
                for(String s : split){
                    if(!param) {
                        message = message.concat(s + " ");
                    }
                    param = false;
                }
                message = message.concat(";");
                message = message.replace("&","§");
                message = message.replace("\\n", "\n");
                pInfo.setApparenceprofil(apparence.concat(message));
                sender.sendMessage("§cHRP : §7Description ajoutée.");
                break;
            case "remove":
                pages = apparence.split(";");
                if(pages.length == 0) {
                    sender.sendMessage("§cHRP : §7Vous ne pouvez pas supprimer de page si vous n'en avez pas.");
                    return;
                }
                if(!StringUtils.isNumeric(split[1])) {
                    sender.sendMessage("§cHRP : §7Il faut un numéro de page.");
                    return;
                }
                int nbpage = Integer.parseInt(split[1])-1;
                if(nbpage > pages.length) {
                    sender.sendMessage("§cHRP : §7Out of range. (Ouais, c'est de l'anglais, c'est pour dire que vous ne pouvez delete la ligne X car vous n'avez pas autant de description)");
                    return;
                }
                String newpage = "";
                for(int j = 0; j < pages.length; j++) {
                    if(j != nbpage) {
                        newpage = newpage.concat(pages[j]);
                        newpage = newpage.concat(";");
                    }
                }
                pInfo.setApparenceprofil(newpage);
                sender.sendMessage("§cHRP : §7Partie de description supprimée.");
                break;
            case "clear":
                pInfo.setApparenceprofil("");
                sender.sendMessage("§cHRP : §7Vous n'avez plus de description d'apparence.");
                break;
            case "see":
                Player target = Bukkit.getPlayer(split[1]);
                if(target != null) {
                    PlayerInfo targetInfo = PlayerInfo.getPlayerInfo(target);
                    if (targetInfo.getApparenceprofil() != null && !targetInfo.getApparenceprofil().equals("")) {
                        PlayerInfo.getAppareanceBook(targetInfo.getApparenceprofil().split(";"), p);
                    } else {
                        p.closeInventory();
                        p.sendMessage("§cHRP : §7Ce joueur n'a pas d'apparence disponible sur son profil.");
                    }
                }
                else {
                    p.sendMessage("§cHRP : §7Le joueur n'est plus connecté.");
                }
                break;
        }
    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split)
    {
        return new ArrayList<>();
    }

    @Override
    protected boolean isOpOnly() {
        return false;
    }

}
