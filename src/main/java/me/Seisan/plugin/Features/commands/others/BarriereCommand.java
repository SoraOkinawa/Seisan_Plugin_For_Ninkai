package me.Seisan.plugin.Features.commands.others;

import de.themoep.inventorygui.GuiElementGroup;
import de.themoep.inventorygui.GuiPageElement;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import me.Seisan.plugin.Features.Barriere.Barriere;
import me.Seisan.plugin.Features.skill.Skill;
import me.Seisan.plugin.Main;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class BarriereCommand extends Main.Command {

    // Barriere's properties :
    // - Name
    // - Description
    // - Price
    // - IsPriceMultiplier
    // - Category
    // - Default
    // - PrepareTime
    // - Rank


    private Map<UUID, List<Skill>> barriere = new HashMap<>();

    /**
     * Usage : /barriere : Open item menu to select a skill and add it to the player's barriere list
     * Usage : /barriere add <skillName> : Add a skill to the player's barriere list
     * Usage : /barriere remove <skillName> : Remove a skill from the player's barriere list
     * Usage : /barriere list : List all the skills in the player's barriere list
     * Usage : /barriere clear : Clear the player's barriere list
     * Usage : /barriere lock : Lock the player's barriere list
     * Usage : /barriere envoie : Send the player's barriere list as a unique skill
     */


    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        Player player = (Player) sender;
        // Check if the player has barrieres in his skill list


        if (split.length == 0) {
            // Open item menu to select a skill and add it to the player's barriere list


        } else {
            switch (split[0]) {
                case "add":
                    // Add a skill to the player's barriere list
                    break;
                case "remove":
                    // Remove a skill from the player's barriere list
                    break;
                case "list":
                    // List all the skills in the player's barriere list
                    break;
                case "clear":
                    // Clear the player's barriere list
                    break;
                case "lock":
                    // Lock the player's barriere list
                    break;
                case "envoie":
                    // Send the player's barriere list as a unique skill
                    break;
            }
        }
    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        List<String> completion = new ArrayList();
        switch (split.length) {
            case 1:
                break;
        }
        return completion;
    }

    @Override
    protected boolean isOpOnly() {
        return false;
    }

    // Setups arrays for the barriere menu

    private static ArrayList<String[]> setupBarriereMenu(Barriere[] barriere) {
        // Create a list of all categories
        ArrayList<String> categories = new ArrayList<>();
        for (Barriere b : barriere) {
            if (!categories.contains(b.getCategory())) {
                categories.add(b.getCategory());
            }
        }
        // Create one list per category
        ArrayList<ArrayList<Barriere>> barriereByCategory = new ArrayList<>();
        for (String category : categories) {
            ArrayList<Barriere> barriereList = new ArrayList<>();
            for (Barriere b : barriere) {
                if (b.getCategory().equals(category)) {
                    barriereList.add(b);
                }
            }
            barriereByCategory.add(barriereList);
        }
        // Setup the menus
        ArrayList<String[]> menu = new ArrayList<>();
        for (ArrayList<Barriere> barriereList : barriereByCategory) {
            String[] layout;
            int line = 0;
            for (int i = 0; i < barriereList.size(); i+=8) {
                if (line == 0) {
                    layout = new String[]{"d", " ", "b", "b", "b", "b", "b", "b", "b" };
                } else {
                    layout = new String[]{" ", " ", "b", "b", "b", "b", "b", "b", "b" };
                }
                line++;
                if (line == 6) {
                    layout = new String[]{"p", " ", "r", " ", "v", " ", "l", " ", "n" };
                    line = 0;
                }
                menu.add(layout);
            }
        }
        // Menu must be a multiple of 6 lines
        int emptyLines = 6 - (menu.size() % 6);
        for (int i = 0; i < emptyLines; i++) {
            if(menu.size() % 6 == 0) {
                menu.add(new String[]{"p", " ", "r", " ", "v", " ", "l", " ", "n" });
            } else {
                menu.add(new String[]{" ", " ", " ", " ", " ", " ", " ", " ", " "});
            }
        }

        return menu;
    }




}
