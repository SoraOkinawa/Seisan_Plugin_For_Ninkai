package me.Seisan.plugin.Features.objectnum;

import lombok.Getter;
import me.Seisan.plugin.Features.utils.ItemUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;

public enum Figurine {

    LameSolitaire("§4Lame Solitaire", 1, 20, 50, "lamesolitaire", "§6Bronze"),
    Oni("§4Démon", 2, 25, 20, "oni", "§7Argent"),
    EspritLupin("§4Esprit Lupin", 3, 40, 25, "espritlupin", "§6Bronze"),
    ArmureGardienne("§4Armure Gardienne", 4, 45, 10, "armuregardienne", "§7Argent"),
    ApprentiPando("§4Apprenti Pandô", 5, 65, 50, "apprentipando", "§6Bronze"),
    JoueurTambour("§4Joueur de Tambour", 6, 70, 20, "joueurtambour", "§7Argent"),
    Moine("§4Moine guerrier", 7, 79, 20, "moine", "§7Argent"),
    PrincesseMohiji("§4Princesse Mohiji", 8, 80, 3, "princessemohiji", "§eOr"),
    Lancier("§4Lancier", 9, 100, 25, "lancier", "§6Bronze"),
    Wakong("§4Wakong de Jade", 10, 101, 3, "wakongdejade", "§eOr");

    @Getter
    private String name;
    @Getter
    private int id;
    @Getter
    private int rate_obtain;
    @Getter
    private int rate_gold;
    @Getter
    private String tag;
    @Getter
    private String lore;

    Figurine(String name, int id, int rate_obtain, int rate_gold, String tag, String lore) {
        this.name = name;
        this.id = id;
        this.rate_obtain = rate_obtain;
        this.rate_gold = rate_gold;
        this.tag = tag;
        this.lore = lore;
    }

    public static ItemStack getFromLuck() {
        int luck = random(1, 101);
        for (Figurine figurine : values()) {
            if (figurine.rate_obtain >= luck) {
                return FigureToItem(figurine);
            }
        }
        return FigureToItem(Wakong);
    }

    public static ItemStack FigureToItem(Figurine figurine) {
        int rate_gold = random(1,1000);
        boolean bool = false;
        if(rate_gold <= figurine.getRate_gold()) {
            bool = true;
        }
        String tag = figurine.getId()+"_"+figurine.getTag();
        String lore = "§8Rang : "+figurine.getLore();
        String name = figurine.getName();
        if(bool) {
            tag = tag.concat("_gold");
            lore = "§8Rang : §g§eMythique";
            name = name.concat(" en or");
        }
        return ItemUtil.createItemStack(Material.GHAST_TEAR, 1, name, Collections.singletonList(lore), "ninkai", tag);
    }

    public static boolean isFigurine(ItemStack itemStack) {
        for(Figurine figurine : values()) {
            String tag = figurine.getId()+"_"+figurine.getTag();
            if(ItemUtil.hasTag(itemStack, "ninkai",tag)
                    || ItemUtil.hasTag(itemStack, "ninkai", tag.concat("_gold"))) {
                return true;
            }
        }
        return false;
    }


    public static int random(int min, int max) {
        return min + (int)(Math.random() * ((max - min) + 1));
    }
}