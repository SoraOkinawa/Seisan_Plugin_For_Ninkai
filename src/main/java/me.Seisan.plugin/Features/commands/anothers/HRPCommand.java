package me.Seisan.plugin.Features.commands.anothers;

import me.Seisan.plugin.Features.PlayerData.PlayerInfo;
import me.Seisan.plugin.Features.data.Config;
import me.Seisan.plugin.Features.objectnum.Clan;
import me.Seisan.plugin.Features.utils.ItemUtil;
import me.Seisan.plugin.Main;
import me.Seisan.plugin.Main.Command;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import de.themoep.inventorygui.*;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class HRPCommand extends Command {

    private static final Config hrpConfig = new Config("hrpChest.yml");

    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (sender instanceof Player) {

            Player p = (Player) sender;

            // /hrp : Ouvre le coffre HRP de base (Code Seisan de base)
            if (args.length == 0) {
                openInventoryFromChest(p, "hrpMainChest", "HRP", -1);
            }
            // /hrp clan : Ouvre le coffre HRP du clan de player
            else if (args.length == 1 && args[0].equalsIgnoreCase("clan")) {
                //Get player's clan
                String clan = PlayerInfo.getPlayerInfo(p).getClan().getName();
                //Open the inventory
                openInventoryFromChest(p, "hrpClanChest." + clan.toLowerCase(), clan, PlayerInfo.getPlayerInfo(p).getLvL(clan));

            }
            // /hrp poubelle : Ouvre la poubelle HRP (Code Seisan de base)
            else if (args.length == 1 && args[0].equalsIgnoreCase("poubelle")) {
                getPoubelleInventory(p);
            }
            // /hrp add : Ajoute un coffre aux coffres de HRP
            else if (args.length == 1 && args[0].equalsIgnoreCase("add") && p.isOp()) {
                Block b = p.getTargetBlock(null, 5);
                if (b.getType() == Material.CHEST) {
                    Location loc = b.getLocation();
                    hrpConfig.set("hrpMainChest." + loc.getWorld().getName() + "/" + loc.getBlockX() + "/" + loc.getBlockY() + "/" + loc.getBlockZ(), -1);
                    p.sendMessage(ChatColor.GREEN + "Coffre ajouté !");
                } else {
                    p.sendMessage(ChatColor.RED + "Vous devez regarder un coffre !");
                }
            }
            // /hrp remove : Supprime un coffre des coffres de HRP
            else if (args.length == 1 && args[0].equalsIgnoreCase("remove") && p.isOp()) {
                Block b = p.getTargetBlock(null, 5);
                if (b.getType() == Material.CHEST) {
                    Location loc = b.getLocation();
                    hrpConfig.set("hrpMainChest." + loc.getWorld().getName() + "/" + loc.getBlockX() + "/" + loc.getBlockY() + "/" + loc.getBlockZ(), null);

                    p.sendMessage(ChatColor.GREEN + "Coffre supprimé !");
                } else {
                    p.sendMessage(ChatColor.RED + "Vous devez regarder un coffre !");
                }

            }
            // /hrp list : Liste les coordonnées des coffres de HRP
            else if (args.length == 1 && args[0].equalsIgnoreCase("list") && p.isOp()) {
                p.sendMessage(ChatColor.GRAY + "Liste des coffres HRP :");
                hrpConfig.getKeys("hrpMainChest", true).forEach(key -> {
                    p.sendMessage(ChatColor.GRAY + " - " + key);
                });
            }

            // /hrp clan add <clan> <level> : Ajoute un coffre aux coffres de HRP du clan pour un niveau donné (MJ seulement)
            else if (args.length == 4 && args[0].equalsIgnoreCase("clan") && args[1].equalsIgnoreCase("add") && args[3].matches("[0-9]+") && p.isOp()) {
                Block b = p.getTargetBlock(null, 5);
                if (b.getType() == Material.CHEST) {
                    Location loc = b.getLocation();

                    hrpConfig.set("hrpClanChest." + args[2].toLowerCase() + "." + loc.getWorld().getName() + "/" + loc.getBlockX() + "/" + loc.getBlockY() + "/" + loc.getBlockZ(), Integer.parseInt(args[3]));

                    p.sendMessage(ChatColor.GREEN + "Coffre ajouté au clan " + args[2] + " !");
                } else {
                    p.sendMessage(ChatColor.RED + "Vous devez regarder un coffre !");
                }
            }
            // /hrp clan remove <clan> : Supprime un coffre des coffres de HRP du clan
            else if (args.length == 3 && args[0].equalsIgnoreCase("clan") && args[1].equalsIgnoreCase("remove") && p.isOp()) {
                Block b = p.getTargetBlock(null, 5);
                if (b.getType() == Material.CHEST) {
                    Location loc = b.getLocation();
                    hrpConfig.set("hrpClanChest." + args[2].toLowerCase() + "." + loc.getWorld().getName() + "/" + loc.getBlockX() + "/" + loc.getBlockY() + "/" + loc.getBlockZ(), null);

                    p.sendMessage(ChatColor.GREEN + "Coffre supprimé du clan " + args[2] + " !");
                } else {
                    p.sendMessage(ChatColor.RED + "Vous devez regarder un coffre !");
                }
            }
            // /hrp clan list <clan> : Liste les coordonnées des coffres de HRP du clan
            else if (args.length == 3 && args[0].equalsIgnoreCase("clan") && args[1].equalsIgnoreCase("list") && p.isOp()) {
                p.sendMessage(ChatColor.GRAY + "Liste des coffres HRP du clan " + args[2] + " :");
                hrpConfig.getKeys("hrpClanChest." + args[2].toLowerCase(), true).forEach(key -> {
                    p.sendMessage(ChatColor.GRAY + " - " + key + " : " + hrpConfig.getInt("hrpClanChest." + args[2] + "." + key) + " (niveau)");
                });
            }
            // /hrp clan list : Liste les clans ayant des coffres HRP
            else if (args.length == 2 && args[0].equalsIgnoreCase("clan") && args[1].equalsIgnoreCase("list") && p.isOp()) {
                p.sendMessage(ChatColor.GRAY + "Liste des clans ayant des coffres HRP :");
                hrpConfig.getKeys("hrpClanChest", false).forEach(key -> {
                    p.sendMessage(ChatColor.GRAY + " - " + key);
                });
            }
            // /hrp clan <clan> : Ouvre le coffre HRP du clan au level max
            else if (args.length == 2 && args[0].equalsIgnoreCase("clan") && p.isOp()) {
                openInventoryFromChest(p, "hrpClanChest." + args[1].toLowerCase(), args[1], -1);
            } else {
                p.sendMessage(ChatColor.RED + "Utilisation : /hrp [clan] [add|remove|list] [clan]");
            }
        }
    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {

        if (args.length == 1) {

            List<String> completions = new ArrayList<>();
            completions.add("clan");
            completions.add("poubelle");
            completions.add("add");
            completions.add("remove");
            return StringUtil.copyPartialMatches(args[0], completions, new ArrayList<>());
        } else if (args.length == 2 && args[0].equalsIgnoreCase("clan") && sender.isOp()) {
            List<String> completions = new ArrayList<>();
            completions.add("add");
            completions.add("remove");
            completions.add("list");

            for (Clan clan : Clan.allClans) {
                completions.add(clan.getName().toLowerCase());
            }

            return StringUtil.copyPartialMatches(args[1], completions, new ArrayList<>());
        } else if (args.length == 3 && args[0].equalsIgnoreCase("clan") && (args[1].equalsIgnoreCase("add") || args[1].equalsIgnoreCase("remove") || args[1].equalsIgnoreCase("list"))) {
            List<String> completions = new ArrayList<>();
            // Get all clan.name from enum Clam
            for (Clan clan : Clan.allClans) {
                completions.add(clan.getName().toLowerCase());
            }
            return StringUtil.copyPartialMatches(args[2], completions, new ArrayList<>());
        } else if (args.length == 4) {
            // Return 1 to 8
            List<String> completions = new ArrayList<>();
            for (int i = 1; i <= 8; i++) {
                completions.add(String.valueOf(i));
            }
            return StringUtil.copyPartialMatches(args[3], completions, new ArrayList<>());
        }
        return Collections.emptyList();
    }


    public static void openInventoryFromChest(Player p, final String chestPath, String invName, final int level) {
        // Get every items on the chests
        List<ItemStack> items = new ArrayList<>();
        // Check if key exists
        if (!hrpConfig.isInConfig(chestPath)) {
            p.sendMessage(ChatColor.RED + "Aucun coffre " + invName + " trouvé !");
            return;
        }

        hrpConfig.getKeys(chestPath, true).forEach(key -> {
            // Check if the value of the key is an int and if it's greater than the level
            if (level == -1 || hrpConfig.getInt(chestPath + "." + key) <= level) {
                ;
                Location loc = ItemUtil.stringToLocation(key);
                Block b = loc.getBlock();

                if (loc.getBlock().getType() == Material.CHEST) {

                    Chest c = (Chest) b.getState();

                    Inventory i = c.getBlockInventory();

                    Inventory iNeighbour = getNeighbourChest(c);

                    items.addAll(Arrays.asList(i.getContents()));
                    if (iNeighbour != null) {
                        items.addAll(Arrays.asList(iNeighbour.getContents()));
                    }
                }
            }
        });

        // Open the inventory
        openItemMenu(Main.plugin(), "§8HRP : §7Coffre HRP du " + invName, p, items);

    }

    //C'est un peu immonde
    public static Inventory getNeighbourChest(Chest c) {

        Location initLoc = c.getLocation();

        Location neigbordLocX1 = new Location(initLoc.getWorld(), initLoc.getX() - 1, initLoc.getY(), initLoc.getBlockZ());
        if (neigbordLocX1.getBlock().getType() == Material.CHEST) {
            return ((Chest) neigbordLocX1.getBlock().getState()).getBlockInventory();
        }

        Location neigbordLocX2 = new Location(initLoc.getWorld(), initLoc.getX() + 1, initLoc.getY(), initLoc.getBlockZ());
        if (neigbordLocX2.getBlock().getType() == Material.CHEST) {
            return ((Chest) neigbordLocX2.getBlock().getState()).getBlockInventory();
        }

        Location neigbordLocZ1 = new Location(initLoc.getWorld(), initLoc.getX(), initLoc.getY(), initLoc.getBlockZ() + 1);
        if (neigbordLocZ1.getBlock().getType() == Material.CHEST) {
            return ((Chest) neigbordLocZ1.getBlock().getState()).getBlockInventory();
        }

        Location neigbordLocZ2 = new Location(initLoc.getWorld(), initLoc.getX(), initLoc.getY(), initLoc.getBlockZ() - 1);
        if (neigbordLocZ2.getBlock().getType() == Material.CHEST) {
            return ((Chest) neigbordLocZ2.getBlock().getState()).getBlockInventory();
        }

        return null;

    }

    public static Inventory getPoubelleInventory(Player p) {
        return Bukkit.createInventory(p, 9, "§8HRP : §7Poubelle §4(VOUS PERDEZ VOS ITEMS DEDANS)");
    }


    @Override
    protected boolean isOpOnly() {
        return false;
    }

    public static void openItemMenu(Main plugin, String invName, Player p, List<ItemStack> items) {

        String[] setup = {
                "ggggggggg",
                "ggggggggg",
                "ggggggggg",
                "p       n",
        };

        InventoryGui gui = new InventoryGui(plugin, p, invName, setup);

        gui.setFiller(new ItemStack(Material.AIR, 1));

        GuiElementGroup group = new GuiElementGroup('g');

        for (ItemStack itemStack : items) {
            if (itemStack != null) {
                group.addElement(
                        (new StaticGuiElement('g', itemStack, itemStack.getAmount(),
                                click -> {
                                    p.getInventory().addItem(itemStack);
                                    return true;
                                }
                        )));
            }
        }

        gui.addElement(group);

        gui.addElement(new
                GuiPageElement('p', new ItemStack(Material.ARROW), GuiPageElement.PageAction.PREVIOUS, "Page d'avant (Actuelle : %page%)"));
        gui.addElement(new

                GuiPageElement('n', new ItemStack(Material.ARROW), GuiPageElement.PageAction.NEXT, "Page suivante (Actuelle : %page%)"));


        gui.show(p);
    }


}
