package me.Seisan.plugin.Features.commands.anothers;

import me.Seisan.plugin.Main.Command;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class WalkCommand extends Command {

    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        Player p = Bukkit.getPlayer(sender.getName());
        if(p != null) {
            if(!p.getScoreboardTags().isEmpty()) {
                if(p.getScoreboardTags().contains("Marche")) {
                    sender.sendMessage(ChatColor.AQUA + "** Vous marchez d'un bon pas.");
                    p.setWalkSpeed((p.getWalkSpeed()-0.015f)*2);
                    p.getScoreboardTags().remove("Marche");
                }
                else {
                    sender.sendMessage(ChatColor.AQUA + "** Vous marchez tranquillement.");
                    p.setWalkSpeed(p.getWalkSpeed()/2+0.015f);
                    p.getScoreboardTags().add("Marche");
                }
            }
            else {
                sender.sendMessage(ChatColor.AQUA + "** Vous marchez tranquillement.");
                p.setWalkSpeed(p.getWalkSpeed()/2+0.015f);
                p.getScoreboardTags().add("Marche");
            }
        }
    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split)
    {
        return null;
    }

    @Override
    protected boolean isOpOnly() {
        return false;
    }
}
