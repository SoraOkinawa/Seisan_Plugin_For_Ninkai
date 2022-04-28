package me.Seisan.plugin.Features.commands.others;

import me.Seisan.plugin.Features.PlayerData.PlayerInfo;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import me.Seisan.plugin.Main.Command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TransferCommand extends Command {
    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        if (split.length == 2) {
            if (sender.getServer().getPlayer(split[1]) != null) {
                Player target = sender.getServer().getPlayer(split[1]);
                PlayerInfo targetInfo = PlayerInfo.getPlayerInfo(target);
                Player psender = (Player) sender;
                PlayerInfo senderInfo = PlayerInfo.getPlayerInfo(psender);


                if (StringUtils.isNumeric(split[0])) {
                    int amout = Integer.parseInt(split[0]);
                    senderInfo.removeMana(amout);
                    targetInfo.addMana(amout);
                    String message = ChatColor.BLUE + "** " + ((Player) sender).getDisplayName() + ChatColor.BLUE + " transfère " + ChatColor.GOLD + amout + ChatColor.BLUE + " de chakra à " + target.getDisplayName() + ChatColor.BLUE + " ! **";

                    psender.sendMessage(message);
                    for (Entity e : psender.getNearbyEntities(25, 25, 25)) {
                        if (e instanceof Player) {
                            e.sendMessage(message);
                        }
                    }
                }
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
