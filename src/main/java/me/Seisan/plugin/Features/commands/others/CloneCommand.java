package me.Seisan.plugin.Features.commands.others;


import me.Seisan.plugin.Features.PlayerData.PlayerClone;
import me.Seisan.plugin.Features.PlayerData.PlayerInfo;
import me.Seisan.plugin.Main.Command;
import net.citizensnpcs.api.npc.NPC;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Helliot on 15/04/2018
 */
public class CloneCommand extends Command {
    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        if(sender instanceof Player){
            final Player p = (Player) sender;
            PlayerInfo pInfo = PlayerInfo.getPlayerInfo(p);
            if(split.length > 0) {
                switch (split[0]) {
                    case "create":
                        if (split.length == 2) {
                            if(p.isOp() || PlayerClone.getCloneTicket().containsKey(p)){
                                if(StringUtils.isNumeric(split[1])){
                                    int nbClone = Integer.parseInt(split[1]);
                                    if(nbClone > 0){
                                        if(p.isOp() || nbClone <= PlayerClone.getCloneTicket().get(p)){
                                            for(int i = 0; i<nbClone; i++){
                                                pInfo.getPlayerClone().createClone();
                                            }
                                            p.sendMessage(ChatColor.GREEN + "Vous avez créé " + nbClone + (nbClone > 1 ? " clones":" clone")); //Gestion du pluriel, eh oui, un dev grammar nazi :)
                                            PlayerClone.getCloneTicket().remove(p);
                                        }else{
                                            p.sendMessage(ChatColor.RED + "Vous ne pouvez pas créer autant de clones");
                                        }
                                    }else{
                                        p.sendMessage(ChatColor.RED + "C'est idiot d'utiliser la commande pour créer 0 clones ou moins...");
                                    }
                                }else{
                                    p.sendMessage(ChatColor.RED + "Le nombre de clones doit être un chiffre");
                                }
                            }else{
                                p.sendMessage(ChatColor.RED + "Vous n'êtes pas autorisé à utiliser cette commande !");
                            }
                        } else {
                            p.sendMessage(ChatColor.RED + "Syntaxe: /clone create (nombre)");
                        }
                        break;
                    case "removeall":
                        pInfo.getPlayerClone().destroyAllClones();
                        break;
                    case "tphere":
                        if (split.length == 2) {
                            if (StringUtils.isNumeric(split[1])) {
                                int id = Integer.parseInt(split[1]);
                                NPC npc = pInfo.getPlayerClone().get(id);

                                if (npc != null) {
                                    npc.teleport(p.getLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
                                } else {
                                    p.sendMessage(ChatColor.RED + "Vous n'avez pas de clone portant l'id " + id);
                                }
                            } else {
                                p.sendMessage(ChatColor.RED + "L'id du clone doit être un chiffre");
                            }
                        } else {
                            p.sendMessage(ChatColor.RED + "Syntaxe: /clone tphere (id)");
                        }
                        break;
                    case "switch":
                        if (split.length == 2) {
                            if (StringUtils.isNumeric(split[1])) {
                                int id = Integer.parseInt(split[1]);
                                NPC npc = pInfo.getPlayerClone().get(id);

                                if (npc != null) {
                                    Location npcLoc = npc.getStoredLocation();
                                    Location pLoc = p.getLocation();

                                    npc.teleport(pLoc, PlayerTeleportEvent.TeleportCause.PLUGIN);
                                    p.teleport(npcLoc);
                                } else {
                                    p.sendMessage(ChatColor.RED + "Vous n'avez pas de clone portant l'id " + id);
                                }
                            } else {
                                p.sendMessage(ChatColor.RED + "L'id du clone doit être un chiffre");
                            }
                        } else {
                            p.sendMessage(ChatColor.RED + "Syntaxe: /clone tphere (id)");
                        }
                        break;
                    case "remove":
                        if (split.length == 2) {
                            if (StringUtils.isNumeric(split[1])) {
                                int id = Integer.parseInt(split[1]);
                                NPC npc = pInfo.getPlayerClone().get(id);

                                if (npc != null) {
                                    pInfo.getPlayerClone().destroy(npc);
                                    p.sendMessage(ChatColor.GREEN + "Le clone numéro " + id + " a bien été supprimé !");
                                } else {
                                    p.sendMessage(ChatColor.RED + "Vous n'avez pas de clone portant l'id " + id);
                                }
                            } else {
                                p.sendMessage(ChatColor.RED + "L'id du clone doit être un chiffre");
                            }
                        } else {
                            p.sendMessage(ChatColor.RED + "Syntaxe: /clone tphere (id)");
                        }
                        break;
                    case "addperm":
                        if (p.isOp()) {
                            if (split.length == 2) {
                                if (StringUtils.isNumeric(split[1])) {
                                    int nbMax = Integer.parseInt(split[1]);
                                    if(nbMax > 0) {
                                        PlayerClone.put(p, nbMax);
                                    }else{
                                        p.sendMessage(ChatColor.RED + "C'est idiot d'autoriser un joueur à créer 0 clones ou moins...");
                                    }
                                } else {
                                    p.sendMessage(ChatColor.RED + "Le nombre de clones doit être un chiffre");
                                }
                            } else {
                                p.sendMessage(ChatColor.RED + "Syntaxe: /clone addperm (nb)");
                            }
                        } else {
                            p.sendMessage(ChatColor.RED + "Vous n'avez pas la permission d'utiliser cette commande !");
                        }
                        break;
                    default:
                        sendHelpMessage(p);
                        break;

                }
            }else{
                sendHelpMessage(p);
            }
        }else if(sender instanceof ConsoleCommandSender) {
            if(split.length != 3 || !split[0].equals("addperm") || !StringUtils.isNumeric(split[1]) || Bukkit.getPlayer(split[2]) == null) {
                sendHelpMessageConsole((ConsoleCommandSender)sender);
                return;
            }
            int nbMax = Integer.parseInt(split[1]);
            Player p = Bukkit.getPlayer(split[2]);
            if(nbMax > 0) {
                PlayerClone.put(p, nbMax);
            }else{
                sender.sendMessage(ChatColor.RED + "C'est idiot d'autoriser un joueur à créer 0 clones ou moins...");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Player & Console command only");
        }
    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split)
    {
        return new ArrayList<>();
    }


    private static void sendHelpMessage(Player p){
        ArrayList<String> helpList = new ArrayList<>();

        if(p.isOp()){
            helpList.add("§6/clone §eaddperm §7(nb) §8- Utilisé par les jutsus (Voir Helliot pour info)");
        }

        helpList.add("§6/clone §ecreate §7(nb) §8- Crée un nombre de clones donné à votre position");
        helpList.add("§6/clone §eremoveall §8- Permet de faire disparaître tous vos clones");
        helpList.add("§6/clone §eremove §7(id) §8- Permet de faire disparaître un clone en particulier");
        helpList.add("§6/clone §etphere §7(id) §8- Permet de téléporter un clone donné sur votre position");
        helpList.add("§6/clone §eswitch §7(id) §8- Permet d'échanger votre position avec celle de votre clone");

        for(String s : helpList){
            p.sendMessage(s);
        }
    }

    private void sendHelpMessageConsole(ConsoleCommandSender sender) {
        sender.sendMessage("§6/clone §eaddperm $6joueur §7(nb) §8- Utilisé par les jutsus");
    }


}
