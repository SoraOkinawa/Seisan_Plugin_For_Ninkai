package me.Seisan.plugin.Features.commands.anothers;

import me.Seisan.plugin.Features.PlayerData.PlayerInfo;
import me.Seisan.plugin.Features.objectnum.RPRank;
import me.Seisan.plugin.Features.skill.SkillLevel;
import me.Seisan.plugin.Main.Command    ;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class RollResistanceCommand extends Command {
    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        if(split.length == 0 || split.length == 1) {
            sender.sendMessage("§cHRP : /rollresistance attaque (E ou D ou C ou B ou A ou S ou S+)");
            sender.sendMessage("§cHRP : /rollresistance defense (E ou D ou C ou B ou A ou S ou S+");
            return;
        }
        if(!(sender instanceof Player)) {
            sender.sendMessage("§cHRP : §7Commande interdite depuis la console.");
            return;
        }
        int bonus = 0;
        if(sender.isOp() && split.length == 3) {
            if(StringUtils.isNumeric(split[2])) {
                bonus = Math.min(200, Math.max(0, Integer.parseInt(split[2])));
            }
        }
        SkillLevel skillLevel = SkillLevel.getByCharName(split[1]);
        if(skillLevel == null) {
            sender.sendMessage("§cHRP : /rollresistance attaque (E ou D ou C ou B ou A ou S ou S+)");
            sender.sendMessage("§cHRP : /rollresistance defense (E ou D ou C ou B ou A ou S ou S+");
            return;
        }
        int resultat = Commands.getRandom(1, 100);
        Player p = (Player)sender;
        PlayerInfo pInfo = PlayerInfo.getPlayerInfo(p);
        String type;
        resultat = resultat + bonus + pInfo.getResistance();
        switch (split[0]) {
            case "defense":
                type = "Défense";
                break;
            case "attaque":
                type = "Attaque";
                resultat += 40 + skillLevel.getResistance();
                break;
            default:
                return;
        }

        for (Entity target : p.getNearbyEntities(50, 50, 50)) {
            if (target instanceof Player) {
                target.sendMessage(ChatColor.GRAY + "[ROLL RESISTANCE CHAKRAIQUE - "+type+" - §6["+skillLevel.getCharName()+"]§7] " + ChatColor.RESET + p.getDisplayName() + ChatColor.GRAY + " a tiré un " + resultat + ".*");
            }
        }
        System.out.println(ChatColor.GRAY + "[ROLL RESISTANCE CHAKRAIQUE - "+type+"- §6["+skillLevel.getCharName()+"]§7] " + ChatColor.RESET + p.getDisplayName() + ChatColor.GRAY + " a tiré un " + resultat + ".*");
        p.sendMessage(ChatColor.GRAY + "[ROLL RESISTANCE CHAKRAIQUE - "+type+"- §6["+skillLevel.getCharName()+"]§7] " + ChatColor.RESET + p.getDisplayName() + ChatColor.GRAY + " a tiré un " + resultat + ".*");
    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split)
    {
        List<String> completion = new ArrayList();
        if(split.length == 1) {
            complete(completion, "attaque", split[0]);
            complete(completion, "defense", split[0]);
        }
        else if(split.length == 2) {
            complete(completion, "E", split[1]);
            complete(completion, "D", split[1]);
            complete(completion, "C", split[1]);
            complete(completion, "B", split[1]);
            complete(completion, "A", split[1]);
            complete(completion, "S", split[1]);
            complete(completion, "S+", split[1]);
        }
        return completion;
    }

    @Override
    protected boolean isOpOnly() {
        return false;
    }

}
