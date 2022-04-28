package me.Seisan.plugin.Features.Inventory;

import me.Seisan.plugin.Features.utils.ItemUtil;
import org.bukkit.Bukkit;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;


public class PNJInventory {
    public static Inventory getBanqueInventory(Player p){
        Inventory inv = Bukkit.createInventory(p.getPlayer(), 54, "§6Banque");
        for(int i = 0; i < 54; i++){
            inv.setItem(i, ItemUtil.createItemStack(Material.GRAY_STAINED_GLASS_PANE, "§8-"));
            if (i > 8 && i%9 == 0 && i < 43){
                i += 7;
            }
        }
        inv.setItem(11, ItemUtil.createItemStack(Material.GOLD_NUGGET, 1, "§2Billet de 10 Ryôs", Arrays.asList(), "seisan", "ryos_billet"));
        inv.setItem(13, ItemUtil.createItemStack(Material.ARROW, ""));
        inv.setItem(15, ItemUtil.createItemStack(Material.GOLD_NUGGET, 10, "§6Ryô", Arrays.asList(), "seisan", "ryos"));

        inv.setItem(20, ItemUtil.createItemStack(Material.GOLD_NUGGET, 10, "§6Ryô", Arrays.asList(), "seisan", "ryos"));
        inv.setItem(22, ItemUtil.createItemStack(Material.ARROW, ""));
        inv.setItem(24, ItemUtil.createItemStack(Material.GOLD_NUGGET, 1, "§2Billet de 10 Ryôs", Arrays.asList(), "seisan", "ryos_billet"));

        inv.setItem(29, ItemUtil.createItemStack(Material.GOLD_NUGGET, 10, "§2Billet de 10 Ryôs", Arrays.asList(), "seisan", "ryos_billet"));
        inv.setItem(31, ItemUtil.createItemStack(Material.ARROW, ""));
        inv.setItem(33, ItemUtil.createItemStack(Material.GOLD_NUGGET, 1, "§2Liasse de 100 Ryôs", Arrays.asList(), "seisan", "ryos_liasse"));

        inv.setItem(38, ItemUtil.createItemStack(Material.GOLD_NUGGET, 1, "§2Liasse de 100 Ryôs", Arrays.asList(), "seisan", "ryos_liasse"));
        inv.setItem(40, ItemUtil.createItemStack(Material.ARROW, ""));
        inv.setItem(42, ItemUtil.createItemStack(Material.GOLD_NUGGET, 10, "§2Billet de 10 Ryôs", Arrays.asList(), "seisan", "ryos_billet"));


        return inv;
    }

    public static Inventory getVendeurJouet(Player p){
            Inventory inv = Bukkit.createInventory(p.getPlayer(), 45, "§6Vendeur de jouet");
            for(int i = 0; i < 45; i++){
                inv.setItem(i, ItemUtil.createItemStack(Material.GRAY_STAINED_GLASS_PANE, "§8-"));
                if (i > 8 && i%9 == 0 && i < 34){
                    i += 7;
                }
            }
            inv.setItem(11, ItemUtil.createItemStack(Material.PAPER, 1, "§4Boîte surprise Sankamaisu", Arrays.asList("§7Achat d'une boite Sankamaisu de §2la première génération.", "§7La boîte coûte 10 ryos."), "seisan", "boite_sankamaisu"));
            inv.setItem(15, ItemUtil.createItemStack(Material.PAPER,1, "§5Échange de figurine", Arrays.asList("§cHRP : §7Cliquez ici pour obtenir le menu vous permettant", "§7d'échanger cinq figurines contre une boîte."), "seisan", "boite_sankamaisu"));
            inv.setItem(31, ItemUtil.createItemStack(Material.GOLD_NUGGET, 1, "§6Solde de vos ryôs", Arrays.asList("§7Voici les ryôs en votre possession : ", "§7Solde : "+getRyos(p), "§cHRP : §7Si votre solde est erroné, contactez §9Isami §7ou un §4MJ§7."), "seisan", "ryos"));

            return inv;
    }

    public static Inventory getVendeurEncre(Player p){
        Inventory inv = Bukkit.createInventory(p.getPlayer(), 45, "§6Vendeur d'encre");
        for(int i = 0; i < 45; i++){
            inv.setItem(i, ItemUtil.createItemStack(Material.GRAY_STAINED_GLASS_PANE, "§8-"));
            if (i > 8 && i%9 == 0 && i < 34){
                i += 7;
            }
        }

        inv.setItem(11, ItemUtil.createItemStack(Material.INK_SAC, 1, "§8Achat d'encre Fuinjutsu", Arrays.asList("§7Achat d'un pot d'encre contenant dix doses.", "§7Le pot coûte 100 ryos.")));
        inv.setItem(15, ItemUtil.createItemStack(Material.INK_SAC, 1, "§8Achat d'encre Fuinjutsu", Arrays.asList("§7Achat d'un petit pot d'encre contenant une dose.", "§7Le pot coûte 12 ryos.")));
        inv.setItem(31, ItemUtil.createItemStack(Material.GOLD_NUGGET, 1, "§6Solde de vos ryôs", Arrays.asList("§7Voici les ryôs en votre possession : ", "§7Solde : "+getRyos(p), "§cHRP : §7Si votre solde est erroné, contactez §9Isami§7."), "seisan", "ryos"));

        return inv;
    }

    public static int getRyos(Player p) {
        int ryo = 0;
        ItemStack[] inventaire = p.getInventory().getContents();
        for(ItemStack item : inventaire) {
            if (item != null && item.getType() == Material.GOLD_NUGGET && item.hasItemMeta()) {
                ItemMeta meta = item.getItemMeta();
                if (meta != null && meta.hasDisplayName()) {
                    switch (meta.getDisplayName()) {
                        case "§6Ryô":
                            if (ItemUtil.hasTag(item, "seisan", "ryos")) {
                                ryo += item.getAmount();
                            }
                            break;
                        case "§2Billet de 10 Ryôs":
                            if (ItemUtil.hasTag(item, "seisan", "ryos_billet")) {
                                ryo += item.getAmount() * 10;
                            }
                            break;
                        case "§2Liasse de 100 Ryôs":
                            if (ItemUtil.hasTag(item, "seisan", "ryos_liasse")) {
                                ryo += item.getAmount() * 100;
                            }
                            break;
                    }
                }
            }
        }
        return ryo;
    }


    public static Inventory getEchange(Player p){
        Inventory inv = Bukkit.createInventory(p.getPlayer(), 9, "§6Échange de figurine");

        inv.setItem(0, ItemUtil.createItemStack(Material.GRAY_STAINED_GLASS_PANE, "§8-"));
        inv.setItem(8, ItemUtil.createItemStack(Material.GRAY_STAINED_GLASS_PANE, "§8-"));
        inv.setItem(6, ItemUtil.createItemStack(Material.GRAY_STAINED_GLASS_PANE, "§8-"));
        inv.setItem(7, ItemUtil.createItemStack(Material.GREEN_WOOL, "§5Valider l'item."));

        return inv;
    }
}
