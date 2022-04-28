package me.Seisan.plugin.Features.commands.anothers;

import me.Seisan.plugin.Main.Command;
import org.bukkit.ChatColor;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


import java.util.List;

public class HatCommand extends Command{

  //  final private String HAT_TAG = ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + ChatColor.BOLD + "HAT" + ChatColor.DARK_GRAY + "] ";

    @Override
    protected void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {


        if (sender instanceof Player){
            Player p = (Player)sender;

            final ItemStack hand = p.getInventory().getItemInMainHand();
            final ItemStack head = p.getInventory().getHelmet();
            if (hand.getType() == Material.AIR){
            //    p.sendMessage(HAT_TAG + ChatColor.DARK_RED + "Tu dois avoir un item dans ta main pour le mettre sur ta tête !");
                p.sendMessage(ChatColor.RED + "HRP : " + ChatColor.GRAY + "Tu dois avoir un item dans ta main pour le mettre sur ta tête !");
                return;
            }

            p.getInventory().setHelmet(hand);
            p.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
          //  p.sendMessage(HAT_TAG + ChatColor.AQUA + "Tu viens de mettre un chapeau.");

            if (head != null){
                p.getInventory().setItemInMainHand(head);
            }

        }
    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        return null;
    }
    @Override
    protected boolean isOpOnly() {
        return false;
    }
}
