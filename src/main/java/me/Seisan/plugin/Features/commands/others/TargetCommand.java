package me.Seisan.plugin.Features.commands.others;

import me.Seisan.plugin.Features.PlayerData.PlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import me.Seisan.plugin.Main.Command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Helliot on 19/03/2018.
 */
public class TargetCommand extends Command {
    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        if(sender instanceof Player){
            Player p = (Player) sender;
            PlayerInfo pInfo = PlayerInfo.getPlayerInfo(p);
            if(pInfo != null && split.length == 1){
                if(p.getServer().getPlayer(split[0]) != null) {
                    Player target = p.getServer().getPlayer(split[0]);
                    pInfo.setTarget(target);

                    p.sendMessage(ChatColor.GREEN + "Votre cible est désormais " + target.getDisplayName());
                }else if(getByDisplayName(split[0]) != null){
                    Player target = getByDisplayName(split[0]);
                    pInfo.setTarget(target);

                    p.sendMessage(ChatColor.GREEN + "Votre cible est désormais " + target.getDisplayName());
                }else{
                    p.sendMessage(ChatColor.RED + "Erreur: Le joueur n'est pas connecté !");
                }
            }else{
                p.sendMessage(ChatColor.RED + "Erreur: Syntaxe: /target (cible)");
            }
        }
    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split)
    {
        List<String> completion = new ArrayList<>();
        if(split.length == 1) for(Player p : Bukkit.getOnlinePlayers()) complete(completion, p.getName(), split[0]);
        return completion;
    }

    @Override
    protected boolean isOpOnly() {
        return false;
    }

    private Player getByDisplayName(String dName){
        for(Player p : Bukkit.getOnlinePlayers()){
            String displayName = p.getDisplayName();
            while(displayName.startsWith("§") || displayName.startsWith("&")){
                displayName = displayName.substring(2);
            }

            if(displayName.equals(dName)){
                return p;
            }
        }

        return null;
    }
}
