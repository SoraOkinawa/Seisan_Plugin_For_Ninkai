package me.Seisan.plugin.Features.Inventory;

import me.Seisan.plugin.Features.utils.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;

import static me.Seisan.plugin.Features.Inventory.PNJInventory.getRyos;

public class CasinoInventory {
    public static Inventory getCasino(Player p){
        Inventory inv = Bukkit.createInventory(p.getPlayer(), 27, "§6Casino");
        for(int i = 0; i < 27; i++){
            inv.setItem(i, ItemUtil.createItemStack(Material.GRAY_STAINED_GLASS_PANE, "§8-"));
            if (i > 8 && i%9 == 0 && i < 16){
                i += 7;
            }
        }

        inv.setItem(11, ItemUtil.createItemStack(Material.CLOCK,1, "§6Tentez votre chance pour 10 ryôs !", Arrays.asList("§7Il s'agit d'un §lseul §r§7tirage.", "§7La maison ne rembourse pas."), "seisan", "clef_or"));
        inv.setItem(13, ItemUtil.createItemStack(Material.CLOCK,1, "§6Tentez votre chance pour 50 ryôs !", Arrays.asList("§7Il s'agit de §lsix §r§7tirages.", "§7La maison ne rembourse pas."), "seisan", "clef_or"));
        inv.setItem(15, ItemUtil.createItemStack(Material.CLOCK,1, "§6Tentez votre chance pour 100 ryôs !", Arrays.asList("§7Il s'agit de §ldouze §r§7tirages.", "§7La maison ne rembourse pas."), "seisan", "clef_or"));
        inv.setItem(22, ItemUtil.createItemStack(Material.GOLD_NUGGET, 1, "§6Solde de vos ryôs", Arrays.asList("§7Voici les ryôs en votre possession : ", "§7Solde : "+getRyos(p), "§cHRP : §7Si votre solde est erroné, contactez §9Isami §7ou un §4MJ§7."), "seisan", "ryos"));
        return inv;
    }
}
