package me.Seisan.plugin.Features.commands.profil;


import me.Seisan.plugin.Features.PlayerData.PlayerInfo;
import me.Seisan.plugin.Features.ability.Ability;
import org.bukkit.Bukkit;
import me.Seisan.plugin.Main.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class EntrainementCommand extends Command {
    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        // entrainemant add (joueur) (ability)
        if(!sender.isOp()) {
            sender.sendMessage("§cHRP : §7Vous n'avez pas la permission.");
            return;
        }

        if(!(split.length == 3 && (split[0].equals("add") || split[0].equals("remove"))) && !(split.length == 2 && split[0].equals("reset"))) {
            sender.sendMessage("§cHRP : §6/entrainement add <joueur> <ability>");
            sender.sendMessage("§cHRP : §6/entrainement remove <joueur> <ability>");
            sender.sendMessage("§cHRP : §6/entrainement reset <joueur>");
            return;
        }
        Player target = Bukkit.getPlayer(split[1]);
        if(target == null) {
            sender.sendMessage("§cHRP : §7Le joueur n'est pas connecté.");
            return;
        }
        PlayerInfo tInfo = PlayerInfo.getPlayerInfo(target);
        if(split[0].equals("reset")) {
            tInfo.setPoints(0);
            tInfo.setPointsAbilities("");
            tInfo.getPlayer().sendMessage("§cHRP : §7Vos entraînements ont été réinitialisés et avez ainsi perdu tout vos points de compétence.");
            sender.sendMessage("§cHRP : §7Le joueur a perdu tous ses points et ses points entraînements.");
        }
        else {
            int val = 1;
            if (split[0].equals("remove")) {
                val = -1;
            }
            if (split[2].equals("general")) {
                tInfo.setPoints(tInfo.getPoints() + val);
                sender.sendMessage("§cHRP : §7Le joueur a désormais §6" + tInfo.getPoints() + " §7points globaux.");
            } else {
                Ability ability = Ability.getByPluginName(split[2]);
                if (ability == null) {
                    sender.sendMessage("§cHRP : §7L'abilité n'existe pas.");
                    return;
                }
                int i = tInfo.incrementePointsAbility(ability, val);
                sender.sendMessage("§cHRP : §7Le joueur a désormais §6" + i + " §7points dans la compétence : " + ability.getName());
            }
        }
    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split)
    {
        List<String> completion = new ArrayList();
        switch (split.length) {
            case 1:
                complete(completion, "add", split[0]);
                complete(completion, "remove", split[0]);
                complete(completion, "reset", split[0]);
                break;
            case 2:
                for(Player p : Bukkit.getOnlinePlayers()) complete(completion, p.getName(), split[1]);
                break;
            case 3:
                if(split[0].equals("add") || split[0].equals("remove")) {
                    for(Ability ability : Ability.getInstanceList()) {
                        if(ability.getPts() > 0) {
                            complete(completion, ability.getNameInPlugin(), split[2]);
                        }
                    }
                }
        }
        return completion;
    }
}
