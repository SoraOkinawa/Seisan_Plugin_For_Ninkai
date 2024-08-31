package me.Seisan.plugin.Features.commands.profil;

import me.Seisan.plugin.Features.PlayerData.PlayerInfo;
import me.Seisan.plugin.Features.ability.Ability;
import me.Seisan.plugin.Main.Command;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProuesseCommand extends Command {
    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        if (!sender.isOp()) {
            if (split.length == 0) {
                PlayerInfo playerInfo = PlayerInfo.getPlayerInfo((Player) sender);
                afficherInfo(playerInfo, (Player) sender);
            }
            return;
        }

        if (split.length == 2 && split[0].equals("get")) {
            Player p = Bukkit.getPlayer(split[1]);
            if (p != null) {
                PlayerInfo playerInfo = PlayerInfo.getPlayerInfo(p);
                afficherInfo(playerInfo, (Player) sender);
            } else {
                sendHelpList(sender);
            }
        } else if (split.length >= 3 && split[0].equals("remove")) {
            Player p = Bukkit.getPlayer(split[1]);
            if (p != null) {
                PlayerInfo playerInfo = PlayerInfo.getPlayerInfo(p);
                String raison = getRaison(split, 2);
                ArrayList<String> res = playerInfo.getProuesse();

                res.remove(raison);
                playerInfo.setProuesse(res);

            } else {
                sender.sendMessage("§cHRP : §7Le joueur n'est pas connecté.");
            }
        } else if (split.length >= 3 && split[0].equals("add")) {

            Player p = Bukkit.getPlayer(split[1]);
            if (p == null) {
                sender.sendMessage("§cHRP : §7Le joueur n'est pas connecté.");
                return;
            }
            String raison = getRaison(split, 2);
            PlayerInfo playerInfo = PlayerInfo.getPlayerInfo(p);
            ArrayList<String> res = playerInfo.getProuesse();
            res.add(raison);
            playerInfo.setProuesse(res);

            sender.sendMessage("§cHRP : §7" + p.getDisplayName() + " a gagné le prouesse : \n" + raison);
            p.sendMessage("§cHRP : §7Votre personnage a gagné la prouesse : \n" + raison);
        } else {
            sendHelpList(sender);
        }
    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        List<String> completion = new ArrayList();
        if (sender.isOp()) {
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
            helpList.add("§6/resistance §7add (joueur) (raison) §8- Permet d'ajouter une prouesse");
            helpList.add("§6/resistance §7remove (joueur) (raison) §8- Permet de retirer une prouesse.");
            helpList.add("§6/resistance §7get (joueur) §8- Permet de connaître l'historique des prouesses.");
        }

        for (String s : helpList) {
            sender.sendMessage(s);
        }
    }

    private void afficherInfo(PlayerInfo playerInfo, Player sender) {
        ArrayList<String> res = playerInfo.getProuesse();
        int bonuschakra = (playerInfo.getManaMission());
        Player p = playerInfo.getPlayer();

        sender.sendMessage("§6Détail des prouesses de : " + p.getDisplayName());
        sender.sendMessage("§cNombre de point MJ donné : §7" + playerInfo.getNbmission());
        sender.sendMessage("§cBonus lié au chakra d'encadrement : §7" + bonuschakra);
        res.forEach((raison) -> sender.sendMessage("§cProuesse : §7" + raison));

    }

    private String getRaison(String[] args, int indice) {
        String raison = "";
        for (int i = indice; i < args.length; i++) {
            raison = raison.concat(args[i] + " ");
        }
        raison = raison.substring(0, raison.length() - 1);
        return raison;
    }

    @Override
    protected boolean isOpOnly() {
        return false;
    }
}
