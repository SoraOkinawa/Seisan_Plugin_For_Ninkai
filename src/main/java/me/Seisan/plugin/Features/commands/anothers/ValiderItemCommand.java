package me.Seisan.plugin.Features.commands.anothers;

import me.Seisan.plugin.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ValiderItemCommand extends Main.Command {
    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        Player player = (Player) sender;
        ItemStack item = player.getInventory().getItemInMainHand();
        if(item.getType()== Material.AIR)
        {
            sender.sendMessage(ChatColor.RED + "HRP : Vous ne pouvez pas rename un item invisible");
            return;
        }
        ItemMeta meta = item.getItemMeta();
        if(meta == null) {
            sender.sendMessage("§cHRP : §7Erreur dans l'objet. Donnez le à Isami pour consultation.");
            return;
        }

        if(meta.hasDisplayName() && meta.getDisplayName().endsWith("§4*")) {
            meta.setDisplayName(meta.getDisplayName().substring(0, meta.getDisplayName().length()-3));
            item.setItemMeta(meta);
            player.getInventory().setItemInMainHand(item);
            player.updateInventory();
        }
        else {
            sender.sendMessage("§cHRP : §7L'item est déjà validé ou n'a pas à l'être.");
        }
    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split)
    {
        return new ArrayList<>();
    }
}
