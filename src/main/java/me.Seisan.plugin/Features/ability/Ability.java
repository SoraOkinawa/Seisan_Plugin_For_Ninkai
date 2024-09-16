package me.Seisan.plugin.Features.ability;

import lombok.Getter;
import me.Seisan.plugin.Features.utils.ItemUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;

public class Ability {

    @Getter
    private String name; //Nom donné proprement (Exemple : Force 4)
    @Getter
    private String nameInPlugin; //Nom sans espace pour les commandes (Ex: Force 4 devient Force4)
    @Getter
    private ItemStack item; //Item représentatif de la compétence (Ex: Poudre rouge)
    @Getter
    private String type; // Force, Vitesse, Instinct, Perception de vitesse
    @Getter
    private int lvl; // Level
    @Getter
    private String description;
    @Getter
    private int pts;
    @Getter
    private int ptsnec;
    @Getter
    private String reqAbilities;
    @Getter
    private String givenAbilities;
    @Getter
    private String giveAbilities;
    @Getter
    private boolean giveAllowed;
    @Getter
    private String givenJutsu;
    @Getter
    private String lore;

    @Getter
    public static ArrayList<Ability> instanceList = new ArrayList<>();

    public Ability(String name, String nameInPlugin, Material itemType, String description, String type, int lvl, String tagkey, String tagvalue, int pts, int ptsnec, String reqAbilities, String givenAbilities, String giveAbilities, String lore, boolean giveAllowed, String givenJutsu){
        this.name = name;
        this.nameInPlugin = nameInPlugin;
        this.lore = lore == null || lore.equals("") || lore.equals(" ") ? "§8[§cHRP §7: Coming soon..." : lore;
        this.item = ItemUtil.createItemStack(itemType, 1, name, Arrays.asList(this.lore, "§7Cliquez ici pour obtenir les ", "§7informations sur cette compétence."), tagkey, tagvalue);
        this.type = type;
        this.lvl = lvl;
        this.description = description;
        this.pts = pts;
        this.ptsnec = ptsnec;
        this.reqAbilities = reqAbilities;
        this.givenAbilities = givenAbilities;
        this.giveAbilities = giveAbilities;
        this.giveAllowed = giveAllowed;
        this.givenJutsu = givenJutsu;
        instanceList.add(this);
    }

    public static Ability getByPluginName(String s){
        return instanceList.stream().filter(skill -> skill.nameInPlugin.equals(s)).findFirst().orElse(null);
    }

    public static Ability getByRealName(String s){
        return instanceList.stream().filter(skill -> ChatColor.stripColor(skill.name).equals(ChatColor.stripColor(s))).findFirst().orElse(null);
    }

    // BONUS ATTRIBUE (+ lvl)
}
