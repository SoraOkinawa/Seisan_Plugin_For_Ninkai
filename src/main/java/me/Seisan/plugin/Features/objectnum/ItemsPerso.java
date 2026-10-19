package me.Seisan.plugin.Features.objectnum;

import lombok.Getter;
import me.Seisan.plugin.Features.utils.ItemUtil;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;

import java.util.Arrays;
import java.util.List;

public enum ItemsPerso {

    MielArtisanat(1, "§fMiel artisanal §6Kamizuru", 1, Arrays.asList("§7500g de délice sucré.","§7Un miel de très bonne §8qualité§7, directement", "§7extrait des §6ruches §8de combat §6Kamizuru", "", "§7La §8principale §7source de §8revenu §7du clan","§7aux §eabeilles§7. Il a encore le goût du §8neuf§7", "", "§7Peut aussi servir de nourriture pour la", "§7plupart des §6abeilles claniques§7."), Material.POTION, "seisan", "miel", "color"),
    GeleeRoyale(2, "§fGelée royale §6Kamizuru", 1, Arrays.asList("§7100g de §8gelée§7.", "§7Une gelée §6royale §7de qualité §8artisanale.","§7soigneusement produite par les §eouvrières","§6Kamizuru§7.","","§7Directement produites par les §8ruches de","§8combat§7, son utilisation varie §9d'aseptisant","§9médicinal §7à comestible §8haute-gamme§7.","","§7C'est le produit le plus §8long §7et §8compliqué §7à","§7produire dans le domaine de §6l'apiculture§7,","§7ce qui justifie son §8haut prix§7"), Material.POTION, "seisan", "miel", "color");

    @Getter
    private int id;
    @Getter
    private String name;
    @Getter
    private int nb;
    @Getter
    private List<String> lore;
    @Getter
    private Material material;
    @Getter
    private String key;
    @Getter
    private String tag;
    @Getter
    private String color;
    ItemsPerso(int id, String name, int nb, List<String> lore, Material material, String key, String tag, String color) {
        this.name = name;
        this.id = id;
        this.nb = nb;
        this.lore = lore;
        this.material = material;
        this.key = key;
        this.tag = tag;
        this.color = color;
    }

    public static ItemStack correctID(int id) {
        for (ItemsPerso item : values()) {
            if (item.id == id) {
                ItemStack giveItem = new ItemStack(ItemUtil.createItemStack(item.material, item.nb, item.name, item.lore, item.key, item.tag));
                if(item.material == Material.POTION) {
                    PotionMeta meta = (PotionMeta)giveItem.getItemMeta();
                    if(meta != null) {
                        meta.setColor(Color.ORANGE);
                        giveItem.setItemMeta(meta);
                    }
                }
                return giveItem;
            }
        }
        return null;
    }
}