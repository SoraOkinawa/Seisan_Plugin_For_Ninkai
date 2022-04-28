package me.Seisan.plugin.Features.commands.profil;

import me.Seisan.plugin.Features.PlayerData.PlayerInfo;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import me.Seisan.plugin.Main.Command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class LostCommand extends Command {
    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        if (split.length == 1) {
            Player target = (Player) sender;
            PlayerInfo targetInfo = PlayerInfo.getPlayerInfo(target);
            if (StringUtils.isNumeric(split[0])){
                int amount = Integer.parseInt(split[0]);
                targetInfo.removeMana(amount);
                String message = ChatColor.BLUE + "** " + ((Player) sender).getDisplayName() + ChatColor.BLUE + " perd " + ChatColor.GOLD + amount + ChatColor.BLUE + " de chakra ! **";
                target.sendMessage(message);
                for(Entity e : target.getNearbyEntities(25, 25, 25)){
                    if(e instanceof Player){
                        e.sendMessage(message);
                    }
                }
            }else {
                sender.sendMessage(ChatColor.RED + "La commande c'est /lost <quantité> !");
            }
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
