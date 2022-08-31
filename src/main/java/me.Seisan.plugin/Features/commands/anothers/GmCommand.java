package me.Seisan.plugin.Features.commands.anothers;

import me.Seisan.plugin.Features.PlayerData.PlayerConfig;
import me.Seisan.plugin.Main;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class GmCommand extends Main.Command {
    @Override
    protected void myOnCommand(CommandSender sender, Command command, String label, String[] split) {
        Player p = (Player) sender;

        if ((sender.isOp()) || PlayerConfig.getPlayerConfig(p).isBuildmode()){
            switch (split[0]){
                case "0":
                    p.setGameMode(GameMode.SURVIVAL);
                    p.sendMessage(ChatColor.AQUA + "Tu es maintenant en survie !");
                    break;
                case "1":
                    p.setGameMode(GameMode.CREATIVE);
                    p.sendMessage(ChatColor.AQUA + "Tu es maintenant en créatif !");
                    break;
                case "2":
                    p.setGameMode(GameMode.ADVENTURE);
                    p.sendMessage(ChatColor.AQUA + "Tu es maintenant en adventure !");
                    break;
                case "3":
                    p.setGameMode(GameMode.SPECTATOR);
                    p.sendMessage(ChatColor.AQUA + "Tu es maintenant en spectateur !");
                    break;
            }
        }

    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, Command command, String label, String[] split) {
        return null;
    }

    @Override
    protected boolean isOpOnly() {
        return false;
    }
}
