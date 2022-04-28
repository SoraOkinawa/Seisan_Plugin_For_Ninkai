package me.Seisan.plugin.Features.commands.anothers;

import me.Seisan.plugin.Main.Command;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;


public class LayCommand extends Command {

    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        Player p = (Player)sender;
        String name = p.getName();
        if(p.isOnGround()) {
            if (!p.getScoreboardTags().contains("lay")) {
                p.getScoreboardTags().add("lay");
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "displayer " + name + " Player " + name + " setEntityPose SLEEPING");
            } else {
                p.getScoreboardTags().remove("lay");
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "undisplayer " + name);
            }
        }
        else {
            p.sendMessage("§4HRP §7: Vous devez toucher le sol pour faire le /lay");
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
