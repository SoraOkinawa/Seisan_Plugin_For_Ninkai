package me.Seisan.plugin.Features.skill;

import org.bukkit.entity.Player;

import java.util.ArrayList;

public class SkillUtils {




    // format a string to an arraylist with each entry being a line, so each line is less than 50 characters, without skipping \n as new lore line
    public static ArrayList<String> formatToLore(String message, Player player) {
        // Split the message into lines
        String[] lines = message.split("\n");
        ArrayList<String> lore = new ArrayList<>();
        // split each line if more than 50 characters
        for (String line : lines) {
            lore.addAll(formatLore(line, player));
        }
        return lore;

    }

    // Copy past from Skill.java : ugly af
    public static ArrayList<String> formatLore(String message, Player player) {
        ArrayList<String> lore = new ArrayList<>();
        int taille = message.length();
        int tailledef = taille;
        int divi = 1;
        while (tailledef > 50) {
            divi++;
            tailledef = taille / divi;
        }
        int borneinf = 0;
        for (int i = 0; i < divi; i++) {
            int bornesupp = tailledef * (i + 1);
            while (bornesupp < message.length() && message.charAt(bornesupp) != ' ') {
                bornesupp++;
            }
            if (divi - 1 == i) {
                bornesupp = taille;
            }
            while (message.substring(borneinf, bornesupp).startsWith(" ")) {
                borneinf++;
            }
            lore.add("§7" + Skill.formatEncaMessage(message.substring(borneinf, bornesupp), player));
            borneinf = bornesupp;
        }
        return lore;
    }

}
