package me.Seisan.plugin.Features.Inventory;

import me.Seisan.plugin.Features.PlayerData.PlayerInfo;
import me.Seisan.plugin.Features.ability.Ability;
import me.Seisan.plugin.Features.objectnum.ArtNinja;
import me.Seisan.plugin.Features.utils.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.bukkit.Material.WRITTEN_BOOK;

public class AbilityInventory {
    public static Inventory getAbilitiesInventory(PlayerInfo pInfo, String type, Player holder, int page){

        Inventory inv = Bukkit.createInventory(holder, 36, "§8Compétence : "+type);

        ArrayList<Ability> abilities = pInfo.getAbilities();
        boolean style = false;
        if(ArtNinja.getIDFromName(type) > 4) {
            style = true;
        }

        int nb = 0;
        abilities.sort(Comparator.comparing(Ability::getName));
        for(Ability ability : abilities){
            if(type.contains(ability.getType())) {
                nb++;
                if(nb <= page * 27 + 27 && nb > page * 27) {
                    inv.addItem(ability.getItem());
                }
            }
            if(style && ability.getType().contains("style_de_combat")) {
                nb++;
                if(nb <= page * 27 + 27 && nb > page * 27) {
                    inv.addItem(ability.getItem());
                }
            }
        }
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        if(skullMeta != null) {
            skullMeta.setOwningPlayer(pInfo.getPlayer());
            skullMeta.setDisplayName("§6Retourner au profil de " + pInfo.getPlayer().getDisplayName());
            skullMeta.setLore(Collections.singletonList("§7Page §6" + (page + 1)));
            skull.setItemMeta(skullMeta);
        }
        if (page > 0)
            inv.setItem(30, ItemUtil.createItemStack(Material.ARROW, "§6Page précédente"));

        if (page * 27 + 27 < nb)
            inv.setItem(32, ItemUtil.createItemStack(Material.ARROW, "§6Page suivante"));

        inv.setItem(31, skull);
        return inv;
    }

    public static void getAbilityToBook(Player p, Ability ability) {
        if(ability != null) {
            String[] desc = ability.getDescription().split(";");
            openInBook(p, desc);
        }
        else {
            System.out.println("Erreur lors de l'abilité");
        }
    }

    public static void openInBook(Player p, String[] desc) {
        ItemStack book = new ItemStack(WRITTEN_BOOK);
        List<String> pages = new ArrayList<>();
        Collections.addAll(pages, desc);
        BookMeta bookMeta = (BookMeta) book.getItemMeta();
        if(bookMeta != null) {
            bookMeta.setPages(pages);
            bookMeta.setAuthor("Serveur_one");
            bookMeta.setTitle("Titre 1");
            book.setItemMeta(bookMeta);
            p.openBook(book);
        }
        else {
            System.out.println("NULL");
        }
    }
}
