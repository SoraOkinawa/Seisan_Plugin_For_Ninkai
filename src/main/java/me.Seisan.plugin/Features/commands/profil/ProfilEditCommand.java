package me.Seisan.plugin.Features.commands.profil;

import me.Seisan.plugin.Features.PlayerData.PlayerInfo;
import me.Seisan.plugin.Features.objectnum.Clan;
import me.Seisan.plugin.Features.objectnum.ClanAttribut;
import me.Seisan.plugin.Features.objectnum.ArtNinja;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import me.Seisan.plugin.Main.Command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ProfilEditCommand extends Command {
    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        if(sender.isOp()){
            if(split.length == 2 && split[0].equals("reset")) {
                Player target = sender.getServer().getPlayer(split[1]);
                if (target != null) {
                    PlayerInfo tInfo = PlayerInfo.getPlayerInfo(target);
                    tInfo.reset();
                    sender.sendMessage("§cHRP : §7La fiche personnage de "+ target.getDisplayName()+" a été réinitialisée.");
                }
            }
            else {
                if (split.length > 1) {
                    modify(sender, split);
                } else {
                    sendHelpList(sender);
                }
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Vous n'avez pas la permission !");
        }
    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split)
    {
        List<String> completion = new ArrayList();
        switch(split.length) {
            case 1:
                complete(completion, "style", split[0]);
                complete(completion, "voie", split[0]);
                complete(completion, "clan", split[0]);
                complete(completion, "encre", split[0]);
                complete(completion, "reset", split[0]);
                break;
            case 2:
                if(split[0].equals("style") || split[0].equals("voie") || split[0].equals("clan")) {
                    complete(completion, "list", split[1]);
                    complete(completion, "set", split[1]);
                }
                if(split[0].equals("reset")) {
                    for(Player p : Bukkit.getOnlinePlayers()) {
                        complete(completion,p.getName(), split[1]);
                    }
                }
                break;
            case 3:
                if(split[0].equals("style") || split[0].equals("voie") || split[0].equals("clan")) {
                    if(split[1].equals("set")) {
                        for(Player p : Bukkit.getOnlinePlayers()) {
                            complete(completion,p.getName(), split[2]);
                        }
                    }
                }
                break;
            case 4:
                if(split[1].equals("set")) {
                    switch (split[0]) {
                        case "style":
                        case "voie":
                            for (ArtNinja styleCombat : ArtNinja.allNinjaArts) {
                                complete(completion, styleCombat.getIdentifiant(), split[3]);
                            }
                            break;
                        case "clan":
                            for (Clan clan : Clan.allClans) {
                                complete(completion, clan.getIdentifiant(), split[3]);
                            }
                            break;
                    }
                }
        }
        return completion;
    }


    private void modify(CommandSender sender, String[] args) {
        switch (args[1]) {
            case "set":
                if (args.length == 4) {
                    Player target = sender.getServer().getPlayer(args[2]);
                    if (target != null) {
                        PlayerInfo tInfo = PlayerInfo.getPlayerInfo(target);
                        switch (args[0]) {
                            case "clan":
                                Clan clan = Clan.getFromIdentifiant(args[3]);
                                tInfo.setClan(clan);
                                sender.sendMessage(ChatColor.GREEN + "Le clan de " + target.getDisplayName() + ChatColor.GREEN + " est désormais " + ChatColor.GOLD + clan.getName());
                                break;
                            case "style":
                                ArtNinja styleCombat = ArtNinja.getFromIdentifiant(args[3]);
                                tInfo.setStyleCombat(styleCombat);
                                sender.sendMessage(ChatColor.GREEN + "Le style de combat de " + target.getDisplayName() + ChatColor.GREEN + " est désormais " + ChatColor.GOLD + styleCombat.getName());
                                break;
                            case "voie":
                                ArtNinja voieNinja = ArtNinja.getFromIdentifiant(args[3]);
                                tInfo.setVoieNinja(voieNinja);
                                sender.sendMessage(ChatColor.GREEN + "La voie ninja de " + target.getDisplayName() + ChatColor.GREEN + " est désormais " + ChatColor.GOLD + voieNinja.getName());
                                break;
                            case "attribut":
                                // profiledit clan set Veziah (nom)
                                if(tInfo.getClan().getName().equals("Inuzuka")) {
                                    tInfo.setAttributClan(args[3]);
                                    sender.sendMessage(ChatColor.GREEN + "L'attribut de clan de " + target.getDisplayName() + ChatColor.GREEN + " est désormais " + ChatColor.GOLD + args[3]);
                                }
                                else {
                                    sender.sendMessage("§cHRP : §7Ce joueur n'est pas du clan Inuzuka.");
                                }
                                if(tInfo.getClan().getName().equals("Sabaku")) {
                                    if(StringUtils.isNumeric(args[3])) {
                                        int i = Integer.parseInt(args[3]);
                                        if(i < 5 && i > 0) {
                                            ClanAttribut clanAttribut = ClanAttribut.getFromID(i);
                                            if(clanAttribut != null) {
                                                tInfo.setAttributClan(clanAttribut.getName());
                                                sender.sendMessage("§cHRP : §7Le Sabaku a désormais le type de sable suivant : "+clanAttribut.getName());
                                            }
                                        }
                                    }
                                    else {
                                        sendHelpList(sender);
                                    }
                                }
                                break;
                            case "encre":
                                // nb d'encre
                                int ink = Integer.parseInt(args[3]);
                                tInfo.setInk(ink);
                                sender.sendMessage("§cHRP : "+tInfo.getPlayer().getDisplayName() + "§7 a désormais " + ink+" doses d'encre.");
                                break;
                            default:
                                sendHelpList(sender);
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "Le joueur n'est pas connecté !");
                    }
                } else {
                    sendList(args[0], sender);
                }
                break;
            case "list":
                if (args.length == 2) {
                    sendList(args[0], sender);
                } else {
                    sendHelpList(sender);
                }
                break;
            case "see":
                if (args.length == 3) {
                    Player target = sender.getServer().getPlayer(args[2]);
                    if (target != null) {
                        PlayerInfo tInfo = PlayerInfo.getPlayerInfo(target);
                        switch (args[0]) {
                            case "clan":
                                sender.sendMessage(ChatColor.GRAY + "Le clan de " + target.getDisplayName() + ChatColor.GRAY + " est " + ChatColor.GOLD + tInfo.getClan().getName());
                                break;
                            case "style":
                                sender.sendMessage(ChatColor.GRAY + "Le style de combat de " + target.getDisplayName() + ChatColor.GRAY + " est " + ChatColor.GOLD + tInfo.getStyleCombat());
                                break;
                            case "voie":
                                sender.sendMessage(ChatColor.GRAY + "La voie ninja de " + target.getDisplayName() + ChatColor.GRAY + " est " + ChatColor.GOLD + tInfo.getVoieNinja().getName());
                                break;
                            default:
                                sendHelpList(sender);
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "Le joueur n'est pas connecté !");
                    }
                } else {
                    sendHelpList(sender);
                }
                break;
            default:
                sendHelpList(sender);
        }
    }
    private void sendHelpList(CommandSender sender){
        if(sender.isOp()){
            // /profiledit clan|voie|style set|see|list joueur(si set ou see) id(si set)
            sender.sendMessage("§6/profiledit §aclan§7|§bvoie§7|§cstyle §eset §7(joueur) (id) §8- Change le clan, style ou voie du joueur");
            sender.sendMessage("§6/profiledit §aclan§7|§bvoie§7|§cstyle §elist §8- Liste les clans, les styles ou les voies (avec leurs id)");
            sender.sendMessage("§6/profiledit §aclan§7|§bvoie§7|§cstyle §esee §7(joueur) §8- Informe sur le clan, voie ou style d'un joueur");
            sender.sendMessage("§6/profiledit §aattribut set (joueur) §6(attribut) - §8L'attribut est un nom si le joueur est Inuzuka, si il est Sabaku : 1/2/3/4");
            sender.sendMessage("§6/profiledit §aencre set (joueur) §6(nb) - §8Donne un montant des doses d'encre");
        }
    }

    private void sendClanList(CommandSender sender){
        sender.sendMessage(ChatColor.GRAY + "Liste des clans: ");
        for(Clan clan : Clan.allClans){
            sender.sendMessage(ChatColor.GRAY + "- " + ChatColor.GOLD + clan.getName() + ChatColor.GRAY+ " (" + clan.getId() + ")");
        }
    }

    private void sendVoieList(CommandSender sender){
        sender.sendMessage(ChatColor.GRAY + "Liste des voies ninja : ");
        for(ArtNinja voieNinja : ArtNinja.allNinjaArts){
            sender.sendMessage(ChatColor.GRAY + "- " + ChatColor.GOLD + voieNinja.getName() + ChatColor.GRAY+ " (" + voieNinja.getId() + ")");
        }
    }

    private void sendStyleList(CommandSender sender){
        sender.sendMessage(ChatColor.GRAY + "Liste des styles de combat : ");
        for(ArtNinja styleCombat : ArtNinja.allNinjaArts){
            sender.sendMessage(ChatColor.GRAY + "- " + ChatColor.GOLD + styleCombat.getName() + ChatColor.GRAY+ " (" + styleCombat.getId() + ")");
        }
    }

    private void sendList(String name, CommandSender sender) {
        switch (name) {
            case "clan":
                sendClanList(sender);
                break;
            case "style":
                sendStyleList(sender);
                break;
            case "voie":
                sendVoieList(sender);
                break;
            default:
                sendHelpList(sender);
        }
    }
}
