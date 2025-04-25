package me.Seisan.plugin.Features.commands.anothers;

import me.Seisan.plugin.Features.PlayerData.PlayerConfig;
import me.Seisan.plugin.Features.utils.ItemUtil;
import me.Seisan.plugin.Features.utils.Nickname;
import me.Seisan.plugin.Main.Command;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class NickCommand extends Command {
    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        if(sender instanceof Player) {
            PlayerConfig pConfig = PlayerConfig.getPlayerConfig((Player)sender);
        }
        if(split.length < 2) {
            sender.sendMessage("§cHRP : §7/nick [player] [nickname]");
            return;
        }

        Player p = Bukkit.getPlayer(split[0]);
        if(p == null) {
            sender.sendMessage("§cHRP : §7Le joueur n'est pas connecté.");
            return;
        }

        String line = "";
        for (int i = 1; i != split.length; i++) {
            line = line.concat(split[i]+" ");
        }
        line = line.substring(0, line.length()-1);
        line = ItemUtil.translateHexCodes(line);
        line = line.replace("&","§");
        line += "§r";

        Nickname.setNick(sender, p, line);
    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split)
    {
        List<String> completion = new ArrayList<>();
        if(split.length == 1) for(Player p : Bukkit.getOnlinePlayers()) complete(completion, p.getName(), split[0]);
        return completion;
    }
}
