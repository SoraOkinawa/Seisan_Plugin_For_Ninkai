package me.Seisan.plugin.Features.commands.profil;

import me.Seisan.plugin.Features.PlayerData.PlayerInfo;
import me.Seisan.plugin.Main.Command;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Helliot on 05/05/2018
 */
public class AgeCommand extends Command {
    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        if(split.length == 2){
            Player target = sender.getServer().getPlayer(split[0]);
            if(target != null){
                if(StringUtils.isNumeric(split[1])){
                    int age = Integer.parseInt(split[1]);
                    PlayerInfo tInfo = PlayerInfo.getPlayerInfo(target);
                    tInfo.setAge(age);
                    target.sendMessage(ChatColor.GRAY + "Vous avez maintenant " + age + " ans");
                    sender.sendMessage(target.getDisplayName() + ChatColor.GRAY + " a maintenant " + age + " ans");
                }else{
                    sender.sendMessage(ChatColor.RED + "L'age doit être un nombre positif");
                }
            }else{
                sender.sendMessage(ChatColor.RED + "Ce joueur n'est pas connecté !");
            }
        }else{
            sender.sendMessage(ChatColor.RED + "Syntaxe: /age (joueur) (age)");
        }
    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split)
    {
        List<String> completion = new ArrayList();
        if(sender.isOp()) {
            if (split.length == 1) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    complete(completion, p.getName(), split[0]);
                }
            }
        }
        return completion;
    }
}
