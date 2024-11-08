package me.Seisan.plugin.Features.commands.others;

import me.Seisan.plugin.Features.PlayerData.PlayerInfo;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import me.Seisan.plugin.Main.Command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GiveChakraCommand extends Command {
    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        if(split.length == 5) {
            Player origine = Bukkit.getPlayer(split[0]);
            PlayerInfo origineInfo = PlayerInfo.getPlayerInfo(origine);
            Player target = Bukkit.getPlayer(split[1]);
            PlayerInfo targetInfo = PlayerInfo.getPlayerInfo(target);
            if (origineInfo != null && targetInfo != null && StringUtils.isNumeric(split[2])
                    && StringUtils.isNumeric(split[3]) && StringUtils.isNumeric(split[4])) {
                int montant_give = Integer.parseInt(split[2]);
                int montant_gain = Integer.parseInt(split[3]);
                int portee = Integer.parseInt(split[4]);
                if(origineInfo.getPlayer().getLocation().distanceSquared(targetInfo.getPlayer().getLocation()) <= portee) {
                    if (montant_give > origineInfo.getMana()) {
                        montant_give = targetInfo.getMana();
                    }
                    if(montant_gain > montant_give) {
                        montant_gain = montant_give;
                    }
                    targetInfo.addMana(montant_gain);
                    origineInfo.removeMana(montant_give);
                }
                else {
                    sender.sendMessage("§cHRP : §7Le joueur n'a pas la portée nécessaire pour donner.");
                }
            } else {
                sender.sendMessage("§cHRP : §7/stealjutsu (origine) (cible) (montant volé) (montant récup) (portée)");
            }
        }
        else {
            sender.sendMessage("§cHRP : §7/stealjutsu (origine) (cible) (montant volé) (montant récup) (portée)");
        }
    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split)
    {
        return new ArrayList<>();
    }

}

