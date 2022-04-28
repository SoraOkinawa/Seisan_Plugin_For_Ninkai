package me.Seisan.plugin.Features.commands.others;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import me.Seisan.plugin.Main.Command;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Helliot on 01/05/2018
 */
public class InvseeCommand extends Command {
    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        if(sender instanceof Player && sender.isOp()){
            if(split.length == 1) {
                Player p = (Player) sender;
                Player target = p.getServer().getPlayer(split[0]);
                if (target != null) {
                    p.openInventory(target.getInventory());
                } else {
                    p.sendMessage(ChatColor.RED + "Ce joueur n'est pas connecté !");
                }
            }else if(split.length == 2 && split[0].equalsIgnoreCase("enderchest")){
                Player p = (Player) sender;
                Player target = p.getServer().getPlayer(split[1]);
                if (target != null) {
                    p.openInventory(target.getEnderChest());
                } else {
                    p.sendMessage(ChatColor.RED + "Ce joueur n'est pas connecté !");
                }
            }
        }else{
            sender.sendMessage(ChatColor.RED + "Vous n'avez pas la permission !");
        }
    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split)
    {
        List<String> completion = new ArrayList<>();
        if(split.length == 1) for(Player p : Bukkit.getOnlinePlayers()) complete(completion, p.getName(), split[0]);
        return completion;
    }
}
