package me.Seisan.plugin.Features.Inventory;

import me.Seisan.plugin.Features.PlayerData.PlayerInfo;
import me.Seisan.plugin.Features.ability.Ability;
import me.Seisan.plugin.Features.utils.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Arrays;

public class CompetenceInventory {
    public static Inventory getCompetenceGenerale(PlayerInfo pInfo, Player p) {
        Inventory inv = Bukkit.createInventory(p, 54, "§8Apprentissage de compétences");
        /* Bordure */
        for (int i = 0; i < 54; i++) {
            inv.setItem(i, ItemUtil.createItemStack(Material.GRAY_STAINED_GLASS_PANE, "§8-"));
            if (i > 8 && i % 9 == 0 && i < 43) {
                i += 7;
            }
        }

        ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        if (skullMeta != null) {
            skullMeta.setOwningPlayer(pInfo.getPlayer());
            skullMeta.setDisplayName("§fPoints de compétence de " + pInfo.getPlayer().getDisplayName());
            ArrayList<String> lore = new ArrayList<>(Arrays.asList("§7Points de compétence: §f" + pInfo.getPoints()));
            lore.add("§7Points de compétence dépensés: §f"+pInfo.getPointsUsed());
            if(pInfo.getPointsAbilities() == null || pInfo.getPointsAbilities().equals(" ")) {
                lore.add("§7Aucun point bonus pour une compétence.");
            }
            else {
                lore.add("§7Point bonus dans les compétences suivantes :");
                for (String s : pInfo.getPointsAbilities().split(";")) {
                    Ability a = Ability.getByPluginName(s.split(",")[0]);
                    if (a != null) {
                        lore.add("§b- " + a.getName() + " §7(§6"+pInfo.getPointsToAbility(a.getNameInPlugin())+"§7)");
                    }
                }
            }
            skullMeta.setLore(lore);
            skull.setItemMeta(skullMeta);
        }
        inv.setItem(13, skull);

        inv.setItem(31, ItemUtil.createItemStack(Material.NETHER_STAR, 1, "§8Autres compétences", Arrays.asList("§7Cliquez pour davantage d'informations", "§7(Ceci n'affichagera que les compétences autres)")));
        inv.setItem(37, ItemUtil.createItemStack(Material.RED_DYE, 1, "§4Force", Arrays.asList("§7Cliquez pour davantage d'informations", "§7Niveau actuel : §f" + pInfo.getLvL("Force")), "ninkai", "force_icon"));
        inv.setItem(39, ItemUtil.createItemStack(Material.GREEN_DYE, 1, "§2Vitesse", Arrays.asList("§7Cliquez pour davantage d'informations", "§7Niveau actuel : §f" + pInfo.getLvL("Vitesse")), "ninkai", "vitesse_icon"));
        inv.setItem(41, ItemUtil.createItemStack(Material.YELLOW_DYE, 1, "§ePerception de la vitesse", Arrays.asList("§7Cliquez pour davantage d'informations", "§7Niveau actuel : §f" + pInfo.getLvL("Perception de la vitesse")), "ninkai", "perception_icon"));
        inv.setItem(43, ItemUtil.createItemStack(Material.BLUE_DYE, 1, "§9Instinct et expérience", Arrays.asList("§7Cliquez pour davantage d'informations", "§7Niveau actuel : §f" + pInfo.getLvL("Instinct et expérience")), "ninkai", "instinct_icon"));


        return inv;
    }

    public static Inventory getCompetenceType(PlayerInfo pInfo, Player p, String type) {
      //  "§8Apprentissage :
        Inventory inv = Bukkit.createInventory(p, 36, "§8Apprentissage : "+type);
        ArrayList<Ability> abilities = pInfo.getAbilities();
        ArrayList<Ability> listAbilities = Ability.getInstanceList();
        /* TODO : A OPTIMISER */
        for(Ability ability : listAbilities) {
            if(type.equals(ability.getType())) {
                if (ability.getPts() != -1 && !abilities.contains(ability)) {
                    ItemStack itemStack = new ItemStack(Material.AIR);
                    ArrayList<String> lore =  new ArrayList<>(Arrays.asList("§7Points nécessaires : §6"+ability.getPts(),"§7Points à dépenser antérieurement : §6"+ability.getPtsnec()));
                    if(!ability.getReqAbilities().equals("none")) {
                        lore.add("§7Compétence(s) nécessaires : ");
                        if(ability.getReqAbilities().contains(";")) {
                            for (String s : ability.getReqAbilities().split(";")) {
                                Ability a = Ability.getByPluginName(s);
                                if (a != null) {
                                    lore.add("§b- " + a.getName());
                                }
                            }
                        }
                        else {
                            Ability a = Ability.getByPluginName(ability.getReqAbilities());
                            if (a != null) {
                                lore.add("§b- " + a.getName());
                            }
                        }
                    }
                    else {
                        lore.add("§7Aucune compétence requise au préalable.");
                    }
                    // Il peut unlock l'abilité
                    if (ability.getPtsnec() > pInfo.getPointsUsed()) {
                        itemStack.setType(Material.BLACK_WOOL);
                        lore.add("§7Vous n'avez pas dépensé assez de point.(§6" + pInfo.getPointsUsed() + "§7/§c" + ability.getPtsnec() + "§7).");
                    } else if (!pInfo.hasReqAbilities(ability.getReqAbilities())) {
                        itemStack.setType(Material.BLACK_WOOL);
                        lore.add("§4Vous n'avez pas débloqué la compétence proposée.");
                    } else if (ability.getPts() <= pInfo.getPoints() + pInfo.getPointsToAbility(ability.getNameInPlugin())) {
                        itemStack.setType(Material.GREEN_WOOL);
                        lore.add("§2Vous avez assez de points. (§6" + pInfo.getPoints() + "§2(§e+ " + pInfo.getPointsToAbility(ability.getNameInPlugin()) + "§2)/§6" + ability.getPts() + "§2.");
                        lore.add(ability.getLore());
                    } else if (pInfo.getPointsToAbility(ability.getNameInPlugin()) > 0) {
                        itemStack.setType(Material.YELLOW_WOOL);
                        lore.add("§cVous n'avez pas assez de points. (§6" + pInfo.getPoints() + "§c(§e+ " + pInfo.getPointsToAbility(ability.getNameInPlugin()) + "§c)/§6" + ability.getPts() + "§c.");
                    }
                    else {
                        itemStack.setType(Material.RED_WOOL);
                        lore.add("§4Vous n'avez pas assez de points. (§6" + pInfo.getPoints() + "§4/§6"+ability.getPts()+"§4)");
                    }
                    ItemMeta meta = itemStack.getItemMeta();
                    if (meta != null) {
                        meta.setLore(lore);
                        meta.setDisplayName(ability.getName());
                        itemStack.setItemMeta(meta);
                        inv.addItem(itemStack);
                    }
                }
            }
        }
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        if(skullMeta != null) {
            skullMeta.setOwningPlayer(pInfo.getPlayer());
            skullMeta.setDisplayName("§6Retourner au menu");
            skull.setItemMeta(skullMeta);
        }
        inv.setItem(31, skull);
        return inv;
    }

    public static Inventory getConfirmationCompetence(PlayerInfo pInfo, Player p, String name) {
        Inventory inv = Bukkit.createInventory(p, 9, "§7Confirmation : "+name);
        inv.setItem(3, ItemUtil.createItemStack(Material.GREEN_WOOL, "§2Confirmer l'acquisition de compétence"));
        inv.setItem(4, ItemUtil.createItemStack(Material.BOOK, "§7Détails de la compétence"));
        inv.setItem(5, ItemUtil.createItemStack(Material.RED_WOOL, "§4Annuler l'acquisition de compétence"));
        return inv;
    }
}