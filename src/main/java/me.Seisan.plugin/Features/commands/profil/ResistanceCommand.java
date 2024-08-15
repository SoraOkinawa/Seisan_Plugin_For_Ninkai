package me.Seisan.plugin.Features.commands.profil;

import me.Seisan.plugin.Features.PlayerData.PlayerInfo;
import me.Seisan.plugin.Features.ability.Ability;
import me.Seisan.plugin.Features.objectnum.ArtNinja;
import me.Seisan.plugin.Features.objectnum.Clan;
import me.Seisan.plugin.Main.Command;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ResistanceCommand extends Command {
    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        if(!sender.isOp()) {
            if(split.length == 0) {
                PlayerInfo playerInfo = PlayerInfo.getPlayerInfo((Player) sender);
                afficherInfo(playerInfo, (Player) sender);
            }
            return;
        }

        if(split.length == 2 && split[0].equals("get")) {
            Player p = Bukkit.getPlayer(split[1]);
            if(p != null) {
                PlayerInfo playerInfo = PlayerInfo.getPlayerInfo(p);
                afficherInfo(playerInfo, (Player)sender);
            }
            else {
                sendHelpList(sender);
            }
        }
        else if(split.length >= 3 && split[0].equals("remove")) {
            Player p = Bukkit.getPlayer(split[1]);
            if(p != null) {
                PlayerInfo playerInfo = PlayerInfo.getPlayerInfo(p);
                String raison = getRaison(split, 2);
                HashMap<String, Integer> res = playerInfo.getResistancebonus();

                if(!res.containsKey(raison)) {
                    sender.sendMessage("§cHRP : §7Ce joueur n'a pas la raison définie pour ce bonus.");
                }
                else {
                    playerInfo.updateResistance();
                    sender.sendMessage("§cHRP : §7Le joueur a perdu "+res.get(raison)+" points de résistance chakraïque");
                    res.remove(raison);
                    playerInfo.setResistancebonus(res);
                }
            }
            else {
                sender.sendMessage("§cHRP : §7Le joueur n'est pas connecté.");
            }
        } else if(split.length >= 4 && split[0].equals("add")) {
            if(!StringUtils.isNumeric(split[2])) {
                sender.sendMessage("§cHRP : §7\"nb\" n'est pas un nombre entier.");
                return;
            }
            Player p = Bukkit.getPlayer(split[1]);
            if(p == null) {
                sender.sendMessage("§cHRP : §7Le joueur n'est pas connecté.");
                return;
            }
            String raison = getRaison(split, 3);
            int nb = Integer.parseInt(split[2]);
            PlayerInfo playerInfo = PlayerInfo.getPlayerInfo(p);
            HashMap<String, Integer> res = playerInfo.getResistancebonus();
            res.put(raison, nb);
            playerInfo.updateResistance();

            sender.sendMessage("§cHRP : §7"+p.getDisplayName()+" a gagné "+nb+" avec comme raison : \n"+raison);
            p.sendMessage("§cHRP : §7Votre personnage a gagné "+nb+" de points de résistance chakraïque avec comme raison : \n"+raison);
        }
        else {
            sendHelpList(sender);
        }
    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split)
    {
        List<String> completion = new ArrayList();
        if(sender.isOp()) {
            if (split.length == 1) {
                complete(completion, "get", split[0]);
                complete(completion, "remove", split[0]);
                complete(completion, "add", split[0]);
            } else if (split.length == 2) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    complete(completion, p.getName(), split[1]);
                }
            }
        }
            return completion;
    }


    private void sendHelpList(CommandSender sender) {
        ArrayList<String> helpList = new ArrayList<>();

        if (sender.isOp()) {
            helpList.add("§6/resistance §7add (joueur) (nb) (raison) §8- Permet d'ajouter de la résistance chakraïque");
            helpList.add("§6/resistance §7remove (joueur) (raison) §8- Permet de retirer la résistance chakraïque.");
            helpList.add("§6/resistance §7get (joueur) §8- Permet de connaître l'historique de bonus chakraique.");
        }

        for(String s : helpList){
            sender.sendMessage(s);
        }
    }

    private void afficherInfo(PlayerInfo playerInfo, Player sender) {
        HashMap<String, Integer> res = playerInfo.getResistancebonus();
        int nbres = playerInfo.getResistance();
        int bonusrank = playerInfo.getRank().getResistance();
        int bonuschakra = (playerInfo.getManamaze()+playerInfo.getManaMission())/100;
        Player p = playerInfo.getPlayer();

        sender.sendMessage("§6Détail de la résistance chakraïque (§f"+ nbres +"§6) de : "+p.getDisplayName());
        sender.sendMessage("§cBonus de rang : §7"+bonusrank);
        sender.sendMessage("§cBonus lié au chakra de méditation et de mission : §7"+bonuschakra);
        res.forEach((raison,nb) -> sender.sendMessage("§cBonus (§f"+ nb +"§c) : §7"+raison+""));
        for(Ability ability : playerInfo.getAbilities()) {
            if(ability.getResistance() > 0) {
                sender.sendMessage("§c"+ability.getName()+" : §7"+ability.getResistance());
            }
        }
    }

    private String getRaison(String[] args, int indice) {
        String raison = "";
        for(int i = indice; i < args.length; i++) {
            raison = raison.concat(args[i] + " ");
        }
        raison = raison.substring(0, raison.length()-1);
        return raison;
    }
    @Override
    protected boolean isOpOnly() {
        return false;
    }
}
