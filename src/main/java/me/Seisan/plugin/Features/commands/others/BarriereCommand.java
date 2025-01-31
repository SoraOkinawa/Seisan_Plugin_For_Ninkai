package me.Seisan.plugin.Features.commands.others;

import me.Seisan.plugin.Features.Barriere.Barriere;
import me.Seisan.plugin.Features.PlayerData.PlayerInfo;
import me.Seisan.plugin.Features.skill.Skill;
import me.Seisan.plugin.Features.skill.SkillLevel;
import me.Seisan.plugin.Features.utils.ItemUtil;
import me.Seisan.plugin.Main;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;


import java.util.*;

import static me.Seisan.plugin.Features.commands.anothers.TechniqueMJCommand.translateHexColorCodes;

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
                        if(split.length < 2) {
                            player.sendMessage("Usage : /barriere add <skillName>");
                            return;
                        }
                        // Add a barriere to the player's barriere list if list is not locked
                        if (!lockedBarriereList.contains(player.getUniqueId())) {
                            Barriere barriere = getBarriereByName(split[1], knownBarriere);
                            if (barriere != null) {
                                addBarriere(player, barriere);
                            } else {
                                player.sendMessage("Barriere not found");
                            }
                        } else {
                            player.sendMessage("Barrière verouillée. Impossible d'ajouter une nouvelle barrière.");
                        }

                        break;
                    case "remove":
                        if(split.length < 2) {
                            player.sendMessage("Usage : /barriere remove <skillName>");
                            return;
                        }
                        // Remove a skill from the player's barriere list if list is not locked
                        if (!lockedBarriereList.contains(player.getUniqueId())) {
                            Barriere barriere = getBarriereByName(split[1], knownBarriere);
                            if (barriere != null) {
                                removeBarriere(player, barriere);
                            } else {
                                player.sendMessage("Barriere not found");
                            }
                        } else {
                            player.sendMessage("Barrière verouillée. Impossible de retirer une barrière.");
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
                            player.sendMessage("Barrière verouillée. Impossible de retirer une barrière.");
                        }
                        break;
                    case "lock":
                        // Lock the player's barriere list if the prepare time is supperior than 0
                        if(getMaxPrepareTime(barriereList.get(player.getUniqueId())) > 0) {
                            lockBarriere(player);
                        } else {
                            player.sendMessage("Barrière instanée. Pas besoin de la verouiller.");
                        }
                        break;
                        case "cancel":
                        // Cancel the player's barriere list if list is locked
                        if (lockedBarriereList.contains(player.getUniqueId())) {
                            barriereList.get(player.getUniqueId()).clear();
                            lockedBarriereList.remove(player.getUniqueId());
                            // Send the cancel message
                            String cancelPhrase = "§c** " + player.getDisplayName() + " §r§cannule la préparation de la barrière.";
                            player.sendMessage(cancelPhrase);
                            for (Entity entity : player.getNearbyEntities(50, 50, 50)) {
                                if (entity instanceof Player) {
                                    entity.sendMessage(cancelPhrase);
                                }
                            }
                        } else {
                            player.sendMessage("Aucune barrière en cours de préparation.");
                        }
                        break;
                    case "envoie":
                        // Send the player's barriere list as a unique skill
                        envoieBarriere(player, knownBarriere);
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
                completion.add("add");
                completion.add("remove");
                completion.add("list");
                completion.add("clear");
                completion.add("lock");
                completion.add("envoie");
                completion.add("cancel");
                return StringUtil.copyPartialMatches(split[0], completion, new ArrayList<>());

            case 2:
                // if add, add only the barriere from the player's voieNinja level
                if (split[0].equals("add") || split[0].equals("remove")) {
                    Player player = (Player) sender;
                    PlayerInfo pInfo = PlayerInfo.getPlayerInfo(player);
                    int barriereLevel = pInfo.getLvL("Houjutsu"); //beurk hardcoded
                    ArrayList<Barriere> knownBarriere = new ArrayList<>();
                    for (Barriere b : Barriere.instanceList) {
                        if (b.getLevel() <= barriereLevel) {
                            completion.add(b.getNameInPlugin());
                        }
                    }
                }
                return StringUtil.copyPartialMatches(split[1], completion, new ArrayList<>());
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
        player.sendMessage("§7 - Barriere " + barriere.getName() + " ajoutée");
    }

    // remove from the player's barriere list
    private void removeBarriere(Player player, Barriere barriere) {
        UUID uuid = player.getUniqueId();
        if (barriereList.containsKey(uuid)) {
            barriereList.get(uuid).remove(barriere);
            player.sendMessage("§7 - Barriere " + barriere.getName() + " retirée");
        }
    }

    // list all the skills in the player's barriere list
    private void listBarriere(Player player) {
        UUID uuid = player.getUniqueId();
        if (barriereList.containsKey(uuid)) {
            for (Barriere barriere : barriereList.get(uuid)) {
                player.sendMessage("§7 - " + barriere.getName());
            }
            int cost = getBarriereCost(barriereList.get(uuid));
            player.sendMessage("§7 - Coût " + cost + " chakra.\nTemps de préparation : " + getMaxPrepareTime(barriereList.get(uuid)) + "tours.");
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
            player.sendMessage("§7 - Vérouillage de la barrière, coût " + cost + ".\nTemps de préparation : " + getMaxPrepareTime(barriereList.get(uuid)) + "tours.");
            String startPhrase = "§c** " + player.getDisplayName() + " §r§ccommence à préparer une barrière pendant " + getMaxPrepareTime(barriereList.get(uuid)) + " tours.";
            player.sendMessage(startPhrase);
            for (Entity entity : player.getNearbyEntities(50, 50, 50)) {
                if (entity instanceof Player) {
                    entity.sendMessage(startPhrase);
                }
            }
        }
    }

    // send the player's barriere list as a unique skill
    private void envoieBarriere(Player player, List<Barriere> knownBarriere) {
        UUID uuid = player.getUniqueId();
        if (barriereList.containsKey(uuid)) {
            // Create a new skill with the player's barriere list
            List<Barriere> barriere = barriereList.get(uuid);

            // Send the barriere message
            sendBarriereMessage(player, barriere, knownBarriere);

            // Clear the player's barriere list
            barriereList.get(uuid).clear();
            // unlock the player's barriere list
            lockedBarriereList.remove(uuid);
        }
    }


    private void sendBarriereMessage(Player player, List<Barriere> barrieres, List<Barriere> knownBarriere) {

        // Get all categories of barrieres in the player's barriere list
        List<String> categories = getBarriereCategories(knownBarriere);

        // For all categories of the player's category, get the default barriere if he has nothing in that category
        for (String category : categories) {
            if (getBarriereByCategory(barrieres, category).size() == 0) {
                barrieres.add(Barriere.getDefaultBarriereByCategory(category));
            }
        }

        // get the mana cost
        int cost = getBarriereCost(barrieres);
        // Remove the cost from the player's mana
        PlayerInfo pInfo = PlayerInfo.getPlayerInfo(player);
        pInfo.removeMana(cost);

        // Get the barriere from category "forme" and "taille", with default if nothing

        Barriere forme;
        // Techniquement il peut y avoir qu'une seule barrière de cette catégorie mais un peu lasdeg
        if (getBarriereByCategory(barrieres, "forme").size() > 0) {
            forme = getBarriereByCategory(barrieres, "forme").get(0);
        } else {
            forme = getDefaultBarriere(barrieres, "forme");
        }

        Barriere taille;
        // Techniquement il peut y avoir qu'une seule barrière de cette catégorie mais un peu lasdeg
        if (getBarriereByCategory(barrieres, "taille").size() > 0) {
            taille = getBarriereByCategory(barrieres, "taille").get(0);
        } else {
            taille = getDefaultBarriere(barrieres, "taille");
        }

        // Do a puce list of all descriptions of the barrieres
        List<String> descriptions = new ArrayList<>();
        for (Barriere b : barrieres) {
            // Only if invisible is false
            if (!b.isInvisibleWhenDefault())
                descriptions.add(b.getDescription());
        }
        // Descriptions should start with "- " and end with "\n"
        String description = "- " + String.join("\n- ", descriptions);
        // add preparation time at the begening of descrition if more than 0
        if (getMaxPrepareTime(barrieres) > 0) {
            description = "Préparé pendant : " + getMaxPrepareTime(barrieres) + " tours.\n" + description;
        }

        // Barriere name
        String barriereName = "Houjutsu - " + taille.getName() + " barrière " + forme.getName();

        // Barriere rank
        SkillLevel rank = getMaxRank(barrieres);

        // Send the barriere message
        sendTechniqueWithDescription(player, barriereName, rank, description);

    }

    private void sendTechniqueWithDescription(Player player, String techniqueName, SkillLevel level, String description) {

        String name = translateHexColorCodes(
                "#",
                "",
                String.join(" ", techniqueName).replace("&", "§")
        );
        String message = "";
        int range = 50;
        message += "§c** " + player.getDisplayName() + " §r§créalise la technique ";
        String newname = name;
        newname = newname.concat(" §f[" + level.getCharName() + "]");

        Skill.affichejutsu(
                player,
                PlayerInfo.getPlayerInfo(player),
                range,
                new TextComponent(message),
                new TextComponent(name),
                ItemUtil.createItemStack(Material.BOOK, 1, newname, formatToLore(description, player)),
                true,
                false,
                null);
    }

    // format a string to an arraylist with each entry being a line, so each line is less than 50 characters, without skipping \n as new lore line
    public ArrayList<String> formatToLore(String message, Player player) {
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
    public ArrayList<String> formatLore(String message, Player player) {
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

    private void openBarriereMenu() {

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
        float cost = 0;
        List<Float> multipliers = new ArrayList<>();
        for (Barriere b : barriere) {
            if (b.isPriceMultiplier()) {
                multipliers.add(b.getPrice());
            } else {
                cost += b.getPrice();
            }
        }
        double endMultiplier = 100;
        for (Float multiplier : multipliers) {
            endMultiplier += (multiplier-1)*100;
        }
        cost *= endMultiplier/100;
        return Math.round(cost);
    }

    // get all catergorie of barrieres in the player's barriere list
    private List<String> getBarriereCategories(List<Barriere> barriere) {
        List<String> categories = new ArrayList<>();
        for (Barriere b : barriere) {
            if (!categories.contains(b.getCategory())) {
                categories.add(b.getCategory());
            }
        }
        return categories;
    }

    // get all barriere of a category in the player's barriere list
    private List<Barriere> getBarriereByCategory(List<Barriere> barriere, String category) {
        List<Barriere> barriereList = new ArrayList<>();
        for (Barriere b : barriere) {
            if (b.getCategory().equals(category)) {
                barriereList.add(b);
            }
        }
        return barriereList;
    }

    // get the default barriere in one category in the player's barriere list
    private Barriere getDefaultBarriere(List<Barriere> barriere, String category) {
        for (Barriere b : barriere) {
            if (b.getCategory().equals(category) && b.isDefault()) {
                return b;
            }
        }
        return null;
    }

    public static ArrayList<Barriere> getDefaultBarrieres(ArrayList<Barriere> barrieres) {
        ArrayList<Barriere> defaultBarrieres = new ArrayList<>();
        for (Barriere barriere : barrieres) {
            if (barriere.isDefault()) {
                defaultBarrieres.add(barriere);
            }
        }
        return defaultBarrieres;
    }

    //get max rank of a barriere
    private SkillLevel getMaxRank(List<Barriere> barriere) {
        SkillLevel maxRank = SkillLevel.NULL;
        for (Barriere b : barriere) {
            if (SkillLevel.getByCharName(b.getRank()).getLevelOrder() < maxRank.getLevelOrder()) {
                maxRank = SkillLevel.getByCharName(b.getRank());
            }
        }
        return maxRank;
    }


}
