package me.Seisan.plugin.Features.Inventory;

import me.Seisan.plugin.Features.PlayerData.PlayerInfo;
import me.Seisan.plugin.Features.objectnum.ArtNinja;
import me.Seisan.plugin.Features.objectnum.ChakraType;
import me.Seisan.plugin.Features.skill.Skill;
import me.Seisan.plugin.Features.skill.SkillMastery;
import me.Seisan.plugin.Features.utils.ItemUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

/**
 * Created by Helliot on 10/02/2018.
 */

public class SkillInventory {

    public static Inventory getMainInventory(PlayerInfo pInfo, int page, String element){
        Inventory inv;
        if(element.equals("Fuinjutsu")) {
            inv = Bukkit.createInventory(pInfo.getPlayer(), 18, "§6Fuinjutsu : §7Choix du type de sceau");
            inv.setItem(1, ItemUtil.createItemStack(Material.SHIELD, 1, "§6Invocation éclair", Arrays.asList("§7Cliquez ici pour obtenir les informations", "§7sur les symboles d'invocations (§6Invocation éclair§7)")));
            inv.setItem(3, ItemUtil.createItemStack(Material.LEVER, 1, "§6Jôken", Arrays.asList("§7Cliquez ici pour obtenir les informations", "§7sur les symboles activeurs (§6Jôken§7).")));
            inv.setItem(5, ItemUtil.createItemStack(Material.IRON_SWORD, 1, "§6Saishô", Arrays.asList("§7Cliquez ici pour obtenir les informations", "§7sur les symboles principaux (§6Saishô§7).")));
            // inv.setItem(5, ItemUtil.createItemStack(Material.SHIELD, 1, "§6Kinaï", Arrays.asList("§7Cliquez ici pour obtenir les informations", "§7sur les symboles secondaires (§6Kinaï§7)")));
            inv.setItem(7, ItemUtil.createItemStack(Material.PAPER, 1, "§6Ninpo", Arrays.asList("§7Cliquez ici pour obtenir les informations", "§7sur les ninpos Fuinjutsu"), "ninkai", "ninpo_icon"));
            inv.setItem(13, ItemUtil.createItemStack(Material.ARROW, "§6Retour"));
        }
        else {
            inv = Bukkit.createInventory(pInfo.getPlayer(), 36, "§6Jutsu : " + element);

            ArrayList<Skill> skillList = new ArrayList<>(pInfo.getSkills().keySet());
            ArrayList<Skill> realSkillList = new ArrayList<>();
            /* Tri des jutsus en fonction de l'élément */
            if(element.equals("§7Autres")) {
                realSkillList = getOthersType(skillList, pInfo);
            }
            else {
                for (Skill skill : skillList) {
                    if (skill.getElement().equals(element)) {
                        realSkillList.add(skill);
                    }
                }
            }
            getListeSceauxJutsu(pInfo, page, inv, realSkillList);
            inv.setItem(35, ItemUtil.createItemStack(Material.ARROW, "§6Retour"));
        }
        return inv;
    }

    public static Inventory getMainInventory(PlayerInfo pInfo, int page){
        Inventory inv = Bukkit.createInventory(pInfo.getPlayer(), 36, "§6Jutsu : §7Tous");

        ArrayList<Skill> skillList = new ArrayList<>(pInfo.getSkills().keySet());
        getListeSceauxJutsu(pInfo, page, inv, skillList);
        return inv;
    }


    public static Inventory getSkillSelectInventory(Skill skill, PlayerInfo pInfo){
        Inventory inv = Bukkit.createInventory(pInfo.getPlayer(), 9, "§8Technique : " + skill.getName());

        SkillMastery mastery = pInfo.getMastery(skill);
        if(pInfo.getPlayer().isOp()) {
            inv.setItem(0, ItemUtil.createItemStack(Material.BOOK, 1, "§2Parchemin de la technique", Arrays.asList("§7Ce bouton permet de donner un parchemin", "§7pour le jutsu : "+skill.getName()), "ninkai", "rouleau_2"));
        }
        inv.setItem(1, ItemUtil.createItemStack(Material.CLOCK, 1, "§6Mudras de la technique :", getMudras(skill.getMudras())));
        inv.setItem(2, skill.getItem());
        if(ChatColor.stripColor(skill.getName()).startsWith("Fuinjutsu")) {
            int ink = skill.getInk(pInfo);
            inv.setItem(3, ItemUtil.createItemStack(Material.EXPERIENCE_BOTTLE, 1, "§6" + mastery.getName(), Arrays.asList("§7" + skill.getLevel().getName(), "§6Coût de scellemment: §7" + skill.manaToTake(pInfo), "§6Coût en encre : §7"+ink)));
        }
        else {
            ChakraType chakraType = ChakraType.fromName(skill.getElement());
            if(chakraType != ChakraType.NULL && pInfo.getReduc_ninjutsu() != 0)
                inv.setItem(3, ItemUtil.createItemStack(Material.EXPERIENCE_BOTTLE, 1, "§6" + mastery.getName(), Arrays.asList("§7" + skill.getLevel().getName(), "§6Coût: §7" + skill.manaToTake(pInfo), "§7Coût réel en chakra : §f"+ (skill.manaToTake(pInfo)+pInfo.getReduc_ninjutsu()))));
            else
                inv.setItem(3, ItemUtil.createItemStack(Material.EXPERIENCE_BOTTLE, 1, "§6" + mastery.getName(), Arrays.asList("§7" + skill.getLevel().getName(), "§6Coût: §7" + skill.manaToTake(pInfo))));
        }
        if(mastery != SkillMastery.UNLEARNED) {
            inv.setItem(4, ItemUtil.createItemStack(Material.PAPER, 1, "§aSélectionner cette technique", Arrays.asList(""), "ninkai", "ninpo_icon"));
        }
        else {
            inv.setItem(4, ItemUtil.createItemStack(Material.PAPER, 1, "§aSélectionner cette technique", Arrays.asList("§cAttention, il y a des risques d'échouer !")));
        }

        if(mastery == SkillMastery.UNLEARNED) {
            inv.setItem(5, ItemUtil.createItemStack(Material.ANVIL, 1, "§aLa technique n'est pas maîtrisée", Arrays.asList("§6Bonus: §7" + (pInfo.getRollBonus().get(skill) != null ? pInfo.getRollBonus().get(skill) : 0))));
        }else {
            if(pInfo.getVoieNinja().getName().contains("Ninjutsu") && pInfo.getLvL("Ninjutsu") >= 2 && !skill.getMudras().equals("none")) {
                if(mastery == SkillMastery.LEARNED) {
                    inv.setItem(5, ItemUtil.createItemStack(Material.ANVIL, 1, "§aLa technique n'est pas complètement maîtrisée.", Arrays.asList("§7Il vous est possible d'entraîner ce jutsu à une main.", "§cAttention, il y a des risques d'échouer !", "§6Bonus: §7" + (pInfo.getRollBonus().get(skill) != null ? pInfo.getRollBonus().get(skill) : 0))));
                }
                else {
                    inv.setItem(5, ItemUtil.createItemStack(Material.ANVIL, "§cLa technique est maîtrisée à une seule main."));
                }
            }
            else {
                inv.setItem(5, ItemUtil.createItemStack(Material.ANVIL, "§cLa technique est maîtrisée."));
            }
        }

        if(!pInfo.getFavoriteList().contains(skill))
            inv.setItem(6, ItemUtil.createRenamedWool(DyeColor.LIME, 1, "§2Ajouter aux favoris"));
        else
            inv.setItem(6, ItemUtil.createRenamedWool(DyeColor.RED, 1 ,"§4Retirer des favoris"));

        inv.setItem(7, ItemUtil.createItemStack(Material.ARROW, "§6Retour au menu"));

        return inv;
    }

    public static Inventory getFavoriteInventory(PlayerInfo pInfo){
        Inventory inv = Bukkit.createInventory(pInfo.getPlayer(), 36, "§8Techniques favorites");

        ArrayList<Skill> favList = pInfo.getFavoriteList();

        for(Skill skill : favList){
            inv.addItem(skill.getItem());
        }

        inv.setItem(31, ItemUtil.createItemStack(Material.ARROW, "§6Revenir au menu principal"));
        return inv;
    }

    private static ArrayList<String> getMudras(String mudras) {
        ArrayList<String> s = new ArrayList<>();
        if(mudras.equals("none")) {
            s.add("§bIl n'y a aucun mûdra pour ce jutsu.");
        }
        else {
            String[] tabmudra = mudras.split(", ");
            s.add("§bIl y a "+tabmudra.length+" mûdras pour ce jutsu :");
            for(String mudra : tabmudra) {
                s.add("§b- "+mudra);
            }
        }
        return s;
    }

    public static Inventory getElementInventory(PlayerInfo pInfo){

        Inventory inv = Bukkit.createInventory(pInfo.getPlayer(), 54, "§6Éléments");
        int i;

        for(i = 0; i < 54; i++) {
            inv.setItem(i, ItemUtil.createItemStack(Material.GRAY_STAINED_GLASS_PANE, "§8-"));
            if(i > 8 && i%9 == 0 && i < 43) {
                i += 7;
            }
        }

        HashMap<Skill, SkillMastery> skills = pInfo.getSkills();
        ArrayList<String> typeJutsu = GetJutsuType(skills);
        String name_clan = ItemUtil.translateHexCodes(("&#"+pInfo.getClan().getColorHexa())
                .concat(ChatColor.stripColor(pInfo.getClan().getName())));
        inv.setItem(10, ItemUtil.createItemStack(Material.PAPER, 1, name_clan, Arrays.asList("§7Cliquez ici pour obtenir les jutsus", "§7relatifs à votre clan."), "ninkai", pInfo.getClan().getTag()));

        if(typeJutsu.contains("Ninpo")) {
            inv.setItem(11, ItemUtil.createItemStack(Material.PAPER, 1, "§7Ninpo", Arrays.asList("§7Cliquez ici pour obtenir les jutsus", "§7relatifs au ninpo"), "ninkai", "ninpo_icon"));
        }
        String name_voie = ItemUtil.translateHexCodes(("&#"+pInfo.getVoieNinja().getColorHexa())
                .concat(ChatColor.stripColor(pInfo.getVoieNinja().getName())));
        if(pInfo.getVoieNinja().getId() < 5) {
            inv.setItem(19, ItemUtil.createItemStack(Material.PAPER, 1, name_voie, Arrays.asList("§7Cliquez ici pour obtenir les jutsus", "§7relatifs à votre voie ninja."), "ninkai", pInfo.getVoieNinja().getName().toLowerCase() + "_scroll"));
        }
        else {
            inv.setItem(19, ItemUtil.createItemStack(Material.PAPER, 1, name_voie, Arrays.asList("§7Cliquez ici pour obtenir les jutsus", "§7relatifs à votre second style de combat."), "ninkai", "style_icon_2"));
        }
        String name_style = ItemUtil.translateHexCodes(("&#"+pInfo.getStyleCombat().getColorHexa())
                .concat(ChatColor.stripColor(pInfo.getStyleCombat().getName())));
        inv.setItem(28, ItemUtil.createItemStack(Material.PAPER, 1, name_style, Arrays.asList("§7Cliquez ici pour obtenir les jutsus", "§7relatifs à votre style de combat."), "ninkai", "style_icon"));
        inv.setItem(37, ItemUtil.createItemStack(Material.DIAMOND, "§6Favoris"));
        inv.setItem(48, ItemUtil.createItemStack(Material.DIAMOND, 1, "§6Jutsus non repertoriés", Arrays.asList("§7Cliquez ici pour obtenir les jutsus", "§7non repertoriés par les catégories", "§7ci-dessus. (Exemple : 4ème voie)")));
        inv.setItem(50, ItemUtil.createItemStack(Material.BOOK, 1, "§6Liste complète des jutsus", Arrays.asList("§7Cliquez ici pour retourner sur l'ancien", "§7affichage des jutsus de votre personnage."), "ninkai", "rouleau_1"));
        i = 12;
        for(int idChakra = 0; idChakra < 20; idChakra++) {
            ChakraType chakraType = ChakraType.fromId(idChakra);
            if(chakraType != null) {
                if(typeJutsu.contains(ChatColor.stripColor(chakraType.getName()))) {
                    String name = ItemUtil.translateHexCodes("&#"+chakraType.colorHexa.concat(ChatColor.stripColor(chakraType.getName())));
                    inv.setItem(i, ItemUtil.createItemStack(Material.DRAGON_BREATH, 1, name, Collections.singletonList("§7Vos techniques " + name), "ninkai", chakraType.getName().substring(2).toLowerCase().replace("ô", "o")));
                    i++;
                }
            }
            if(i%9 == 8) i += 3;
        }
        return inv;
    }


    public static ArrayList<Skill> getOthersType(ArrayList<Skill> skills, PlayerInfo pInfo) {
        ArrayList<Skill> listtype = new ArrayList<>();
        for(Skill s: skills) {
            String nature = s.getElement();
            if(nature.equals("Ninpo")) {
                continue;
            }
            if(Arrays.stream(ChakraType.values()).anyMatch(chakraType -> ChatColor.stripColor(chakraType.getName()).equals(nature))) {
                continue;
            }
            if(nature.equals(pInfo.getClan().getName())) {
                continue;
            }
            if(nature.equals(pInfo.getVoieNinja().getName())) {
                continue;
            }
            if(nature.equals(pInfo.getStyleCombat().getName())) {
                continue;
            }
            listtype.add(s);
        }
        return listtype;
    }

    public static ArrayList<String> GetJutsuType(HashMap<Skill, SkillMastery> skills) {
        ArrayList<String> listtype = new ArrayList<>();
        for(Skill s: skills.keySet()) {
            String nature = s.getElement();
            if(!listtype.contains(nature)) {
                listtype.add(nature);
            }
        }
        return listtype;
    }

    public static Inventory getSceaux(PlayerInfo pInfo, String type, int page) {
        Inventory inv = Bukkit.createInventory(pInfo.getPlayer(), 36,"§6Sceaux : "+type);
        ArrayList<Skill> listsceaux = getTypeSceaux(pInfo.getSkills().keySet(), type);
        getListeSceauxJutsu(pInfo, page, inv, listsceaux);
        inv.setItem(35, ItemUtil.createItemStack(Material.ARROW, "§6Retour"));

        return inv;
    }

    private static void getListeSceauxJutsu(PlayerInfo p, int page, Inventory inv, ArrayList<Skill> listsceaux) {
        listsceaux.sort(Comparator.comparing(Skill::getName));

        int slot = page * 27;
        for (int i = slot; i < slot + 27 && i < listsceaux.size(); i++) {
            ItemStack newitem = new ItemStack(listsceaux.get(i).getItem());
            ItemMeta meta = newitem.getItemMeta();
            assert meta != null;
            List<String> lore = meta.getLore();
            assert lore != null;
            String skillName = meta.getDisplayName();
            Skill skill = Skill.getByRealName(skillName);
            ChakraType chakraType = ChakraType.fromName(skill.getElement());
            lore.add("§7Coût chakra : §f"+ skill.manaToTake(p));
            if(chakraType != ChakraType.NULL && p.getReduc_ninjutsu() != 0)
                lore.add("§7Coût réel en chakra : §f"+ (skill.manaToTake(p)+p.getReduc_ninjutsu()));
            lore.add("§6"+p.getMastery(skill).getName());
            meta.setLore(lore);
            newitem.setItemMeta(meta);
            inv.addItem(newitem);
        }

        if (page > 0)
            inv.setItem(30, ItemUtil.createItemStack(Material.ARROW, "§6Page précédente"));

        if (page * 27 + 27 < listsceaux.size())
            inv.setItem(32, ItemUtil.createItemStack(Material.ARROW, "§6Page suivante"));

        inv.setItem(31, ItemUtil.createItemStack(Material.STONE_BUTTON, "§6Page " + page));
        inv.setItem(27, ItemUtil.createItemStack(Material.DIAMOND, "§6Favoris"));
    }

    public static ArrayList<Skill> getTypeSceaux(Set<Skill> list, String type) {
        ArrayList<Skill> sceaux = new ArrayList<>();
        for (Skill skill : list) {
            if(skill.getName().split("-").length >= 2) {
                if (skill.getName().split("-")[1].contains(type)) {
                    sceaux.add(skill);
                }
            }
        }
        return sceaux;
    }

    public static Inventory getInventoryBB(Player p, int page, String name) {
        Inventory inv = Bukkit.createInventory(p, 54, "§7Bibliothèque du Cercle : "+name);
        ArrayList<Skill> skillList = new ArrayList<>(Skill.getInstanceList());
        ArrayList<Skill> realSkillList = new ArrayList<>();
        /* Tri des jutsus en fonction de si ils sont privatisés ou non */
        for (Skill skill : skillList) {
            if (skill.isPublique()) {
                realSkillList.add(skill);
            }
        }
        realSkillList.sort(Comparator.comparing(Skill::getName));
        int slot = page * 36;
        for(int i = slot; i < slot+36 && i < realSkillList.size(); i++) {
                ItemStack newitem = new ItemStack(realSkillList.get(i).getItem());
                ItemMeta meta = newitem.getItemMeta();
                assert meta != null;
                List<String> lore = meta.getLore();
                assert lore != null;
                String skillName = meta.getDisplayName();
                Skill skill = Skill.getByRealName(skillName);
                lore.add("§7Coût chakra : §f"+ skill.getManaCost());
                lore.add("§7Rang : §f"+ skill.getLevel());
                lore.add("§7Élément : §f"+ skill.getElement());
                meta.setLore(lore);
                newitem.setItemMeta(meta);
                inv.addItem(newitem);
        }

        for(int i = 36; i < 45; i++) {
            inv.setItem(i, ItemUtil.createItemStack(Material.GRAY_STAINED_GLASS_PANE, "§8-"));
        }

        if (page > 0)
            inv.setItem(47, ItemUtil.createItemStack(Material.ARROW, "§6Page précédente"));

        if (page * 36 + 36 < realSkillList.size())
            inv.setItem(51, ItemUtil.createItemStack(Material.ARROW, "§6Page suivante"));

        inv.setItem(49, ItemUtil.createItemStack(Material.STONE_BUTTON, "§6Page " + (page+1)));
        return inv;
    }

    public static Inventory getInventoryBBCheck(Player p, int page) {
        Inventory inv = Bukkit.createInventory(p, 54, "§7Bibliothèque du Cercle : Informations");
        ArrayList<Skill> skillList = new ArrayList<>(Skill.getInstanceList());
        ArrayList<Skill> realSkillList = new ArrayList<>();
        /* Tri des jutsus en fonction de si ils sont privatisés ou non */
        for (Skill skill : skillList) {
            if (skill.isPublique()) {
                realSkillList.add(skill);
            }
        }
        realSkillList.sort(Comparator.comparing(Skill::getName));
        int slot = page * 36;
        for(int i = slot; i < slot+36 && i < realSkillList.size(); i++) {
            ItemStack newitem = new ItemStack(realSkillList.get(i).getItem());
            ItemMeta meta = newitem.getItemMeta();
            assert meta != null;
            List<String> lore = meta.getLore();
            assert lore != null;
            String skillName = meta.getDisplayName();
            Skill skill = Skill.getByRealName(skillName);
            lore.add("§7Coût chakra : §f"+ skill.getManaCost());
            lore.add("§7Rang : §f"+     skill.getLevel());
            lore.add("§7Élément : §f"+ skill.getElement());
            meta.setLore(lore);
            newitem.setItemMeta(meta);
            inv.addItem(newitem);
        }

        for(int i = 36; i < 45; i++) {
            inv.setItem(i, ItemUtil.createItemStack(Material.GRAY_STAINED_GLASS_PANE, "§8-"));
        }

        if (page > 0)
            inv.setItem(47, ItemUtil.createItemStack(Material.ARROW, "§6Page précédente"));

        if (page * 36 + 36 < realSkillList.size())
            inv.setItem(51, ItemUtil.createItemStack(Material.ARROW, "§6Page suivante"));

        inv.setItem(49, ItemUtil.createItemStack(Material.STONE_BUTTON, "§6Page " + (page+1)));
        return inv;
    }
}