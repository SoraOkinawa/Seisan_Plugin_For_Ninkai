package me.Seisan.plugin.Features.Inventory;


import me.Seisan.plugin.Features.PlayerData.PlayerConfig;
import me.Seisan.plugin.Features.PlayerData.PlayerInfo;
import me.Seisan.plugin.Features.objectnum.ChakraType;
import me.Seisan.plugin.Features.utils.ItemUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by Helliot on 27/04/2018
 */
public class TrainInventory {

    public static Inventory getFichePerso(PlayerInfo pInfo, Player holder){
        Inventory inv = Bukkit.createInventory(holder, 54, "§8Fiche personnage de " + pInfo.getPlayer().getDisplayName());
        PlayerConfig playerConfig = PlayerConfig.getPlayerConfig(pInfo.getPlayer());

        ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        if(skullMeta != null) {
            skullMeta.setOwningPlayer(pInfo.getPlayer());
            skullMeta.setDisplayName("§6Fiche personnage de " + pInfo.getPlayer().getDisplayName());
            String sexe = "§7Sexe : §f"+pInfo.getGender().getName();
            String age = "§7Âge : §f" + pInfo.getAge();
            String rank = "§7Rang : §f"+pInfo.getRank().displayName;
            String jutsuPoints = "§7Points de Technique : §f" + pInfo.getJutsuPoints();
            if(playerConfig.isRyoji()) rank = rank.concat("§7 (Ryoji)");
            skullMeta.setLore(Arrays.asList(sexe, age, rank, jutsuPoints));
            skull.setItemMeta(skullMeta);
        }


        /* Bordure */
        for(int i = 0; i < 54; i++) {
            inv.setItem(i, ItemUtil.createItemStack(Material.GRAY_STAINED_GLASS_PANE, 1, "§8-"));
            if(i > 8 && i%9 == 0 && i < 43) {
                i += 7;
            }
        }

        /* ITEM */
        String clan = pInfo.getClan().getName();
        String style = pInfo.getStyleCombat().getName();
        String voie = pInfo.getVoieNinja().getName();
        String name_clan = ItemUtil.translateHexCodes(("&#"+pInfo.getClan().getColorHexa())
                .concat(ChatColor.stripColor(pInfo.getClan().getName())));
        String name_style = ItemUtil.translateHexCodes(("&#"+pInfo.getStyleCombat().getColorHexa())
                .concat(ChatColor.stripColor(pInfo.getStyleCombat().getName())));
        String name_voie = ItemUtil.translateHexCodes(("&#"+pInfo.getVoieNinja().getColorHexa())
                .concat(ChatColor.stripColor(pInfo.getVoieNinja().getName())));
        String chakra, chakramission, passivechakra, chakrabonus, chakramax, chakrarank, chakracolor, chakralevel, mission;
        List<String> chakralore;
        if(pInfo.getPlayer().getName().equals(holder.getName()) || holder.isOp()) {
            chakra = "§7Nature de chakra : ";
            chakramax = "§7Chakra : §f"+pInfo.getMana()+"§7/§f"+pInfo.getMaxMana();
            chakramission = "§7Chakra obtenu en mission : §f"+pInfo.getManaMission()+"§7/§f200";
            passivechakra = "§7Chakra obtenu passivement : §f"+pInfo.getPassiveMana()+"§7/§f300";
            mission = "§7Nombre de point de MJ : §f"+pInfo.getNbmission();
            chakrabonus = "§7Chakra complèmentaire : §f"+pInfo.getManaBonus();
            chakrarank = "§7Chakra obtenu grâce à votre rang : §f"+pInfo.getRank().getChakraRank();
            chakracolor = "§7Couleur de chakra : "+pInfo.getCouleurChakra().getName() + " "+pInfo.getTeinte().getName();
            chakralevel = "§7Niveau de transparence du chakra : §f"+pInfo.getTransparence();
            chakralore = new ArrayList<>(Arrays.asList(chakramax, chakramission, passivechakra, mission, chakrabonus, chakrarank, chakracolor, chakralevel, chakra));

            for(ChakraType chakraType : pInfo.getChakraType().keySet()) {
                chakralore.add("§7- "+chakraType.name+ " §7(-§6"+pInfo.getChakraType().get(chakraType)+"§7%)");
            }
        }
        else {
            chakra = "§7Nature non perceptible.";
            chakramax = "§7Quantité de chakra non perceptible";
            chakralore = Arrays.asList(chakra, chakramax);
        }
        List<String> clanlore;
        if(pInfo.getClan().getName().equals("Inuzuka")) {
            String name = pInfo.getAttributClan() == null ? "§7Sans nom de Ninken" : "§7Nom du Ninken : §6"+pInfo.getAttributClan();
            clanlore = Arrays.asList(name_clan,"§7Niveau : §f"+pInfo.getLvlHint(clan), name);
        }
        else if(clan.equals("Sabaku")) {
            String name = pInfo.getAttributClan() == null ? "§7Type de sable : §cNon défini." : "§7Type de sable : "+pInfo.getAttributClan();
            clanlore = Arrays.asList(name_clan, "§7Niveau : §f"+pInfo.getLvlHint(clan), name);
        }
        else {
            clanlore = Arrays.asList(name_clan,"§7Niveau : §f"+pInfo.getLvlHint(clan));
        }
        inv.setItem(12, skull);
        inv.setItem(14, ItemUtil.createItemStack(Material.BLUE_DYE, 1, "§6Informations sur le chakra", chakralore, "ninkai", "chakra_icon"));
        inv.setItem(20, ItemUtil.createItemStack(Material.PAPER, 1, "§6Style de combat", Arrays.asList(name_style,"§7Niveau : §f"+pInfo.getLvlHint(style)), "ninkai", pInfo.getStyleCombat().getTag()));
        inv.setItem(22, ItemUtil.createItemStack(Material.PAPER, 1, "§6Clan", clanlore, "ninkai", pInfo.getClan().getTag()));
        if(pInfo.getVoieNinja().getId() == 1) {
            inv.setItem(24, ItemUtil.createItemStack(Material.END_CRYSTAL, 1, "§6Voie ninja", Arrays.asList(name_voie,"§7Niveau : §f"+pInfo.getLvlHint(voie))));
        }
        else if(pInfo.getVoieNinja().isVoieNinja()){
            inv.setItem(24, ItemUtil.createItemStack(Material.PAPER, 1, "§6Voie ninja", Arrays.asList(name_voie,"§7Niveau : §f"+pInfo.getLvlHint(voie)), "ninkai", pInfo.getVoieNinja().getTag()));
        }
        else {
            inv.setItem(24, ItemUtil.createItemStack(Material.PAPER,1, "§6Second style de combat", Arrays.asList(name_voie,"§7Niveau : §f"+pInfo.getLvlHint(voie)), "ninkai", pInfo.getVoieNinja().getTag()));
        }
        inv.setItem(31, ItemUtil.createItemStack(Material.NETHER_STAR, 1, "§8Autres compétences", Arrays.asList("§7Cliquez pour davantage d'informations","§7(Ceci n'affichagera que les compétences autres)")));

        inv.setItem(37, ItemUtil.createItemStack(Material.RED_DYE, 1, "§4Force",Arrays.asList("§7Cliquez pour davantage d'informations","§7Niveau : §f"+pInfo.getLvL("Force")), "ninkai", "force_icon"));
        inv.setItem(39, ItemUtil.createItemStack(Material.GREEN_DYE, 1, "§2Vitesse",Arrays.asList("§7Cliquez pour davantage d'informations","§7Niveau : §f"+pInfo.getLvL("Vitesse")), "ninkai", "vitesse_icon"));
        inv.setItem(41, ItemUtil.createItemStack(Material.YELLOW_DYE, 1, "§ePerception de la vitesse",Arrays.asList("§7Cliquez pour davantage d'informations","§7Niveau : §f"+pInfo.getLvL("Perception de la vitesse")), "ninkai", "perception_icon"));
        inv.setItem(43, ItemUtil.createItemStack(Material.BLUE_DYE, 1, "§9Instinct et expérience",Arrays.asList("§7Cliquez pour davantage d'informations","§7Niveau : §f"+pInfo.getLvL("Instinct et expérience")), "ninkai", "instinct_icon"));

        return inv;
    }
}
