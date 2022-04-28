package me.Seisan.plugin.Features.commands.anothers;

import me.Seisan.plugin.Features.objectnum.ItemsGive;
import me.Seisan.plugin.Features.utils.ItemUtil;
import me.Seisan.plugin.Main.Command;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HRPCommand extends Command {
    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        /* Ouverture de l'inventaire */
        if(!(sender instanceof Player)) {
            return;
        }
        Player p = (Player)sender;
        if(split.length == 1 && split[0].equals("poubelle")) {
            p.openInventory(getPoubelleInventory(p));
        }
        else {
            p.openInventory(getHRPInventory(p, 1));
        }
    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split)
    {
        return new ArrayList<>();
    }


    public static Inventory getHRPInventory(Player p, int page){
        Inventory inv = Bukkit.createInventory(p, 45, "§8HRP : §7Don d'objets");

        for(int i = 0; i < 27 && i < ItemsGive.values().length; i++) {
            ItemsGive item = ItemsGive.values()[i];
            inv.setItem(i, ItemUtil.createItemStack(item.getMaterial(), item.getAmount(), item.getName().replace("%name%", p.getName()), Collections.singletonList(item.getLore()), item.getKeytag(), item.getValuetag()));
        }
        for(int i = 27; i < 36; i++) {
            inv.setItem(i, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
        }
        if(page > 1) {
            inv.setItem(39, ItemUtil.createItemStack(Material.ARROW, 1, "§6Retour", Collections.singletonList("§7Amène à la page " + (page-1) + ".")));
        }
        inv.setItem(40, ItemUtil.createItemStack(Material.OAK_BUTTON, 1, "§6Page "+page));
        if(page-1 < ItemsGive.values().length/27) {
            inv.setItem(41, ItemUtil.createItemStack(Material.ARROW, 1, "§6Retour", Collections.singletonList("§7Amène à la page " + (page-1) + ".")));
        }

        return inv;
    }

    public static Inventory getPoubelleInventory(Player p){
        return Bukkit.createInventory(p, 9, "§8HRP : §7Poubelle §4(VOUS PERDEZ VOS ITEMS DEDANS)");
    }


    @Override
    protected boolean isOpOnly() {
        return false;
    }
}
