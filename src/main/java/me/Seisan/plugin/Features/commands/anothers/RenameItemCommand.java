package me.Seisan.plugin.Features.commands.anothers;

import me.Seisan.plugin.Features.utils.ItemUtil;
import me.Seisan.plugin.Main.Command;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;


public class RenameItemCommand extends Command
{
    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split)
    {
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

        if(!sender.hasPermission(ValiderItemCommand.PERMISSION) && meta.hasDisplayName() && !meta.getDisplayName().endsWith("§4*")) {
            sender.sendMessage("§cHRP : §7L'item est déjà validé. Impossible de le modifier.");
            return;
        }

        String result = "";
        int taille = 0;
        for(String word : split)
        {
            result = result.concat(word+" ");
            taille += word.length();
        }
        result = result.substring(0, result.length()-1);
        if(taille > 40 && !sender.hasPermission(ValiderItemCommand.PERMISSION)) {
            sender.sendMessage(ChatColor.RED + "HRP :" + ChatColor.GRAY + " Veuillez saisir un nom plus petit.");
            return;
        }
        if(taille == 0) {
            sender.sendMessage(ChatColor.RED + "HRP :" + ChatColor.GRAY + " Veuillez devez rentrer un nom d'item");
            return;
        }
        result = result.replace("&","§");
        if(!sender.hasPermission(ValiderItemCommand.PERMISSION)) {
            result = result.concat("§4*");
            result = result.replace("§k", "");
            result = result.replace("§l", "");
            result = result.replace("§m", "");
            result = result.replace("§n", "");
            result = result.replace("§o", "");
        }
        result = ItemUtil.translateHexCodes(result);
        meta.setDisplayName(result);
        item.setItemMeta(meta);
        player.getInventory().setItemInMainHand(item);
    }

    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split)
    {
        return null;
    }

    @Override
    protected boolean isOpOnly() {
        return false;
    }
}