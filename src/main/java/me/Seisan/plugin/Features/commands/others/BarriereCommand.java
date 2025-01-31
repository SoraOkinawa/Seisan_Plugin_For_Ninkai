package me.Seisan.plugin.Features.commands.others;

import de.themoep.inventorygui.GuiElementGroup;
import de.themoep.inventorygui.GuiPageElement;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import me.Seisan.plugin.Features.Barriere.Barriere;
import me.Seisan.plugin.Features.PlayerData.PlayerInfo;
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


    private Map<UUID, List<Barriere>> barriereList = new HashMap<>();
    private List<UUID> lockedBarriereList = new ArrayList<>();

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
        PlayerInfo pInfo = PlayerInfo.getPlayerInfo(player);
        if(pInfo.getVoieNinja().getIdentifiant().equals("houjutsu")) { //beurk hardcoded
            //Level of the voieNinja is defined by the skill level
            int barriereLevel = pInfo.getLvL("Houjutsu"); //beurk hardcoded
            ArrayList<Barriere> knownBarriere = new ArrayList<>();
            for (Barriere b : Barriere.instanceList) {
                if (b.getLevel() <= barriereLevel) {
                    knownBarriere.add(b);
                }
            }


            if (split.length == 0) {
                // Open item menu to select a skill and add it to the player's barriere list

            } else {
                switch (split[0]) {
                    case "add":
                        // Add a barriere to the player's barriere list if list is not locked
                        if (!lockedBarriereList.contains(player.getUniqueId())) {
                            Barriere barriere = getBarriereByName(split[1], knownBarriere);
                            if (barriere != null) {
                                addBarriere(player, barriere);
                            } else {
                                player.sendMessage("Barriere not found");
                            }
                        } else {
                            player.sendMessage("Barriere list is locked");
                        }

                        break;
                    case "remove":
                        // Remove a skill from the player's barriere list if list is not locked
                        if (!lockedBarriereList.contains(player.getUniqueId())) {
                            Barriere barriere = getBarriereByName(split[1], knownBarriere);
                            if (barriere != null) {
                                removeBarriere(player, barriere);
                            } else {
                                player.sendMessage("Barriere not found");
                            }
                        } else {
                            player.sendMessage("Barriere list is locked");
                        }

                        break;
                    case "list":
                        // List all the skills in the player's barriere list
                        listBarriere(player);
                        break;
                    case "clear":
                        // Clear the player's barriere list if list is not locked
                        if (!lockedBarriereList.contains(player.getUniqueId())) {
                            clearBarriere(player);
                        } else {
                            player.sendMessage("Barriere list is locked");
                        }
                        break;
                    case "lock":
                        // Lock the player's barriere list
                        lockBarriere(player);
                        break;
                    case "envoie":
                        // Send the player's barriere list as a unique skill
                        envoieBarriere(player);
                        break;
                }
            }
        }
    }

    //TODO : TAB COMPLETION
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

    // add to the player's barriere list
    private void addBarriere(Player player, Barriere barriere) {
        UUID uuid = player.getUniqueId();
        if (!barriereList.containsKey(uuid)) {
            barriereList.put(uuid, new ArrayList<>());
        }
        barriereList.get(uuid).add(barriere);
        player.sendMessage("Barriere " + barriere.getName() + " ajoutée");
    }

    // remove from the player's barriere list
    private void removeBarriere(Player player, Barriere barriere) {
        UUID uuid = player.getUniqueId();
        if (barriereList.containsKey(uuid)) {
            barriereList.get(uuid).remove(barriere);
            player.sendMessage("Barriere " + barriere.getName() + " retirée");
        }
    }

    // list all the skills in the player's barriere list
    private void listBarriere(Player player) {
        UUID uuid = player.getUniqueId();
        if (barriereList.containsKey(uuid)) {
            for (Barriere barriere : barriereList.get(uuid)) {
                player.sendMessage(barriere.getName());
            }
        }
    }

    // clear the player's barriere list
    private void clearBarriere(Player player) {
        UUID uuid = player.getUniqueId();
        if (barriereList.containsKey(uuid)) {
            barriereList.get(uuid).clear();
        }
    }

    // lock the player's barriere list
    private void lockBarriere(Player player) {
        UUID uuid = player.getUniqueId();
        if (barriereList.containsKey(uuid)) {
            lockedBarriereList.add(uuid);
            int cost = getBarriereCost(barriereList.get(uuid));
            player.sendMessage("Vérouillage de la barrière, coût " + cost + ".\nTemps de préparation : " + getMaxPrepareTime(barriereList.get(uuid)) + "tours.");
        }
    }

    // send the player's barriere list as a unique skill
    private void envoieBarriere(Player player) {
        UUID uuid = player.getUniqueId();
        if (barriereList.containsKey(uuid)) {
            // Create a new skill with the player's barriere list
            List<Barriere> barriere = barriereList.get(uuid);
            // TODO : Send a message to the player with all the barrieres
            player.sendMessage("Barriere envoyée");
            // Clear the player's barriere list
            barriereList.get(uuid).clear();
            // unlock the player's barriere list
            lockedBarriereList.remove(uuid);
        }
    }




    // Setups arrays for the barriere menu
    private static ArrayList<String[]> setupBarriereMenu(ArrayList<Barriere> barriere) {
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


    //get a barrier by its name from a list
    private Barriere getBarriereByName(String name, List<Barriere> barriere) {
        for (Barriere b : barriere) {
            if (b.getName().equals(name)) {
                return b;
            }
        }
        return null;
    }

    // calculate max preparation time
    private int getMaxPrepareTime(List<Barriere> barriere) {
        int maxPrepareTime = 0;
        for (Barriere b : barriere) {
            if (b.getPrepareTime() > maxPrepareTime) {
                maxPrepareTime = b.getPrepareTime();
            }
        }
        return maxPrepareTime;
    }

    // calculate cost of the barriere
    private int getBarriereCost(List<Barriere> barriere) {
        int cost = 0;
        List<Double> multipliers = new ArrayList<>();
        for (Barriere b : barriere) {
            if (b.isPriceMultiplier()) {
                multipliers.add(b.getPrice());
            } else {
                cost += b.getPrice();
            }
        }
        double endMultiplier = 100;
        for (Double multiplier : multipliers) {
            endMultiplier += (multiplier-1)*100;
        }
        cost *= endMultiplier/100;
        return cost;
    }

}
