package me.Seisan.plugin.Features.commands.anothers;

import me.Seisan.plugin.Main.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import oshi.jna.platform.mac.SystemB;

import java.util.List;

public class PrierCommand extends Command {

    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {

        if (sender instanceof Player) {
            Player player = (Player) sender;//message d'erreur si il y a pas d'argument dans la commande
            if (split.length == 0) {
                player.sendMessage("§4HRP : §7Il faut faire la commande /priere <message>");
            }

        }
    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        return List.of();
    }
}