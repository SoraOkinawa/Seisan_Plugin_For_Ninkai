package me.Seisan.plugin.Features.commands.anothers;

import me.Seisan.plugin.Features.utils.Channel;
import me.Seisan.plugin.Main;
import me.Seisan.plugin.Main.Command;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ChatMasterCommand extends Command {
    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (!p.isOp()) {
                p.sendMessage(ChatColor.RED + "Vous n'avez pas la permission !");
                return;
            }
        }

        if(args.length == 2){
            Player target = sender.getServer().getPlayer(args[1]);
            if(target == null){
                sender.sendMessage(ChatColor.RED + "Ce joueur n'est pas connecté !");
                return;
            }
            switch (args[0]){
                case "add":
                    if(!Channel.isMJ(target)){
                        Channel.setMJ(target);
                        preventMJ(true, target.getName());
                    }else{
                        sender.sendMessage(ChatColor.RED + "Ce joueur est déjà admin du chat !");
                    }
                    break;
                case "remove":
                    if(Channel.isMJ(target)){
                        Channel.removeMJ(target);
                        preventMJ(false, target.getName());
                    }else{
                        sender.sendMessage(ChatColor.RED + "Ce joueur n'est pas admin du chat !");
                    }
                    break;
                default:
                    sender.sendMessage(ChatColor.RED + "Erreur: Syntaxe: /masterchat (add/remove/addenca/removeenca) (joueur)");
            }
        }else {
            sender.sendMessage(ChatColor.RED + "Erreur: Syntaxe: /masterchat (add/remove/addenca/removeenca) (joueur)");
        }
    }
    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split)
    {
        List<String> completion = new ArrayList();
        switch (split.length) {
            case 1:
                for(String word: Commands.param)
                {
                    complete(completion, word, split[0]);
                }
                break;
            case 2:
                for(Player player : Main.plugin().getServer().getOnlinePlayers())
                {
                    complete(completion, player.getName(), split[1]);
                }
                break;
        }
        return completion;
    }

    public void preventMJ(boolean addremove, String target) {
        if(addremove) {
            for(Player player : Bukkit.getServer().getOnlinePlayers()) {
                if(Channel.isMJ(player)) {
                    player.sendMessage(ChatColor.GREEN + target + " a été ajouté aux admins du chat !");
                }
            }
        }
        else {
            for(Player player : Bukkit.getServer().getOnlinePlayers()) {
                if(Channel.isMJ(player)) {
                    player.sendMessage(ChatColor.GREEN + target + " a été retirés aux admins du chat !");
                }
            }
        }
    }
}
