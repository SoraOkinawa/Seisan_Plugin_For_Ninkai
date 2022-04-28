package me.Seisan.plugin.Features.commands.others;

import me.Seisan.plugin.Features.objectnum.ItemsPerso;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import me.Seisan.plugin.Main.Command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class GiveItemCommand extends Command {
    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        if (split.length == 2) {
            Player p = Bukkit.getPlayer(split[0]);
            if (p != null && StringUtils.isNumeric(split[1])) {
                ItemStack item = ItemsPerso.correctID(Integer.parseInt(split[1]));
                if (item != null) {
                    p.getInventory().addItem(item);
                } else {
                    sender.sendMessage("§cHRP : §7L'item selectionné n'existe pas.");
                }
            } else {
                sender.sendMessage("§cHRP & utilisation : §7Le joueur n'est pas connecté ou l'id n'est pas un chiffre.");
            }
        } else {
            sender.sendMessage("§cHRP & utilisation : §7/giveitem (joueur) (id de l'item)");
        }
    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split)
    {
        return new ArrayList<>();
    }

}
