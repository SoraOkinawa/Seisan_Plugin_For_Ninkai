package me.Seisan.plugin.Features.commands.profil;

import me.Seisan.plugin.Features.PlayerData.PlayerConfig;
import me.Seisan.plugin.Features.PlayerData.PlayerInfo;
import me.Seisan.plugin.Main.Command;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class LostFuinCommand extends Command {
    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        if(!(sender instanceof Player)) return;
        if (split.length != 1) { sender.sendMessage("§cHRP : §7/lostfuin (nb)"); return; }
        if(!StringUtils.isNumeric(split[0])) {sender.sendMessage("§cHRP : §7/lostfuin (nb)"); return; }

        Player p = (Player) sender;
        PlayerInfo pInfo = PlayerInfo.getPlayerInfo(p);
        PlayerConfig pConfig = PlayerConfig.getPlayerConfig(p);
        int amount = Integer.parseInt(split[0]);
        String message;
        if(pConfig.isSwapfuin())     {
            pInfo.usePaper(amount);
            message = ChatColor.BLUE + "** [FUINJUTSU]" + p.getDisplayName() + ChatColor.BLUE + " perd " + ChatColor.GOLD + amount + ChatColor.BLUE + " papiers Seju ! **";
        }
        else {
            pInfo.useInk(amount);
            message = ChatColor.BLUE + "** [FUINJUTSU] " + p.getDisplayName() + ChatColor.BLUE + " perd " + ChatColor.GOLD + amount + ChatColor.BLUE + " doses d'encre. ! **";
        }
        p.sendMessage(message);
        for(Entity e : p.getNearbyEntities(25, 25, 25)){
            if(e instanceof Player){
                e.sendMessage(message);
            }
        }
    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split)
    {
        return new ArrayList<>();
    }


}
