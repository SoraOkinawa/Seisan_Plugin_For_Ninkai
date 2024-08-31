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
            String rank = "§7Rang : §f"+pInfo.getRank().displayName;
            if(playerConfig.isRyoji()) rank = rank.concat("§7 (Ryoji)");
            skullMeta.setLore(Arrays.asList("§7Sexe : §f"+pInfo.getGender().getName(),"§7Âge : §f" + pInfo.getAge(), rank,"§7Nombre de dose d'encre : §f"+pInfo.getInk()));
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
        String chakra, chakramission, chakramaze, chakrabonus, ticketmedit, paliermedit, chakramax, chakrarank, chakracolor, chakralevel, mission;
        List<String> chakralore;
        if(pInfo.getPlayer().getName().equals(holder.getName()) || holder.isOp()) {
            chakra = "§7Nature de chakra : ";
            chakramax = "§7Chakra : §f"+pInfo.getMana()+"§7/§f"+pInfo.getMaxMana();
            chakramission = "§7Chakra obtenu en mission : §f"+pInfo.getManaMission()+"§7/§f200";
            mission = "§7Nombre de mission effectuées : §f"+pInfo.getNbmission();
            chakrabonus = "§7Chakra complèmentaire : §f"+pInfo.getManaBonus();
            chakramaze = "§7Chakra obtenu en méditation : §f"+pInfo.getManamaze()+"§7/§f300";
            ticketmedit = "§7Ticket(s) de méditation : §f"+pInfo.getTicketmedit()+"/2";
            int nb = pInfo.getManamaze()/4 + 15;
            if((pInfo.getManamaze()-10)%40 == 0)
                nb++;
            paliermedit = "§7Palier d'évolution pour la méditation : §f"+pInfo.getMinmedit()+"/"+nb;
            chakrarank = "§7Chakra obtenu grâce à votre rang : §f"+pInfo.getRank().getChakraRank();
            chakracolor = "§7Couleur de chakra : "+pInfo.getCouleurChakra().getName() + " "+pInfo.getTeinte().getName();
            chakralevel = "§7Niveau de transparence du chakra : §f"+pInfo.getTransparence();
            chakralore = new ArrayList<>(Arrays.asList(chakramax, chakramission, mission, chakramaze, chakrabonus, ticketmedit, paliermedit, chakrarank, chakracolor, chakralevel, chakra));

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
        inv.setItem(14, ItemUtil.createItemStack(Material.BLUE_DYE, 1, "§6Informations sur le chakra", chakralore, "seisan", "chakra_icon"));
        inv.setItem(20, ItemUtil.createItemStack(Material.PAPER, 1, "§6Style de combat", Arrays.asList(name_style,"§7Niveau : §f"+pInfo.getLvlHint(style)), "seisan", "style_icon"));
        inv.setItem(22, ItemUtil.createItemStack(Material.PAPER, 1, "§6Clan", clanlore, "seisan", pInfo.getClan().getTag()));
        if(pInfo.getVoieNinja().getId() == -1 || pInfo.getVoieNinja().getId() == 0) {
            inv.setItem(24, ItemUtil.createItemStack(Material.END_CRYSTAL, 1, "§6Voie ninja", Arrays.asList(name_voie,"§7Niveau : §f"+pInfo.getLvlHint(voie))));
        }
        else if(pInfo.getVoieNinja().getId() < 5){
            List<String> lore = Arrays.asList(name_voie,"§7Niveau : §f"+pInfo.getLvlHint(voie));
            if(pInfo.getVoieNinja().getId() == 1) {
                int lvl = pInfo.getLvL(pInfo.getVoieNinja().getName());
                if(lvl >= 4) {
                    lvl-=2;
                    PlayerConfig pConfig = PlayerConfig.getPlayerConfig(pInfo.getPlayer());
                    String mode = pConfig.isSwapfuin() ? "Feuilles Seji" : "Encre Fuinjutsu";
                    lore = Arrays.asList("§7"+voie,
                            "§7Niveau : §f"+pInfo.getLvlHint(voie),
                            /* "§7Feuilles de Seji : §f"+pInfo.getFuin_paper(), */
                            "§7Capacité de l'assembleur Uzumaki : §f"+pInfo.getFuin_uzumaki()+" §7/ §f"+lvl*lvl,
                            "§7Mode de consommation : §6"+mode);
                }
            }
            inv.setItem(24, ItemUtil.createItemStack(Material.PAPER, 1, "§6Voie ninja", lore, "seisan", voie.toLowerCase()+"_scroll"));
        }
        else {
            inv.setItem(24, ItemUtil.createItemStack(Material.PAPER,1, "§6Second style de combat", Arrays.asList(name_voie,"§7Niveau : §f"+pInfo.getLvlHint(voie)), "seisan", "style_icon_2"));
        }
        inv.setItem(31, ItemUtil.createItemStack(Material.NETHER_STAR, 1, "§8Autres compétences", Arrays.asList("§7Cliquez pour davantage d'informations","§7(Ceci n'affichagera que les compétences autres)")));

        inv.setItem(37, ItemUtil.createItemStack(Material.RED_DYE, 1, "§4Force",Arrays.asList("§7Cliquez pour davantage d'informations","§7Niveau : §f"+pInfo.getLvL("Force")), "seisan", "force_icon"));
        inv.setItem(39, ItemUtil.createItemStack(Material.GREEN_DYE, 1, "§2Vitesse",Arrays.asList("§7Cliquez pour davantage d'informations","§7Niveau : §f"+pInfo.getLvL("Vitesse")), "seisan", "vitesse_icon"));
        inv.setItem(41, ItemUtil.createItemStack(Material.YELLOW_DYE, 1, "§ePerception de la vitesse",Arrays.asList("§7Cliquez pour davantage d'informations","§7Niveau : §f"+pInfo.getLvL("Perception de la vitesse")), "seisan", "perception_icon"));
        inv.setItem(43, ItemUtil.createItemStack(Material.BLUE_DYE, 1, "§9Instinct et expérience",Arrays.asList("§7Cliquez pour davantage d'informations","§7Niveau : §f"+pInfo.getLvL("Instinct et expérience")), "seisan", "instinct_icon"));

        return inv;
    }
}
