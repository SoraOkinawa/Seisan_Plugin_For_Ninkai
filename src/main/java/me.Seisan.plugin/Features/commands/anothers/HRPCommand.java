package me.Seisan.plugin.Features.commands.anothers;

import me.Seisan.plugin.Features.data.Config;
import me.Seisan.plugin.Features.objectnum.ItemsGive;
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
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static net.citizensnpcs.api.CitizensAPI.getDataFolder;

public class HRPCommand extends Command {

    private static Config hrpConfig = new Config("hrpChest.yml");

    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (sender instanceof Player) {

            Player p = (Player) sender;

            // /hrp : Ouvre le coffre HRP de base (Code Seisan de base)
            if (args.length == 0) {
                p.openInventory(getHRPInventory(p, 1));
            }
            // /hrp clan : Ouvre le coffre HRP du clan de player
            else if (args.length == 1 && args[0].equalsIgnoreCase("clan")) {
                p.openInventory(openClanInventory(args[1], 1));
            }
            // /hrp poubelle : Ouvre la poubelle HRP (Code Seisan de base)
            else if (args.length == 1 && args[0].equalsIgnoreCase("poubelle")) {
                p.openInventory(getPoubelleInventory(p));

            }
            // /hrp add : Ajoute un coffre aux coffres de HRP
            else if (args.length == 1 && args[0].equalsIgnoreCase("add")) {
                Block b = p.getTargetBlock(null, 5);
                if (b.getType() == Material.CHEST) {
                    Location loc = b.getLocation();
                    hrpConfig.set("hrpMainChest." + loc.getWorld().getName() + "/" + loc.getBlockX() + "/" + loc.getBlockY() + "/" + loc.getBlockZ(), true);


                    p.sendMessage(ChatColor.GREEN + "Coffre ajouté !");
                } else {
                    p.sendMessage(ChatColor.RED + "Vous devez regarder un coffre !");
                }
            }
            // /hrp add : Supprime un coffre des coffres de HRP
            else if (args.length == 1 && args[0].equalsIgnoreCase("remove")) {
                Block b = p.getTargetBlock(null, 5);
                if (b.getType() == Material.CHEST) {
                    Location loc = b.getLocation();
                    hrpConfig.set("hrpMainChest." + loc.getWorld().getName() + "/" + loc.getBlockX() + "/" + loc.getBlockY() + "/" + loc.getBlockZ(), null);

                    p.sendMessage(ChatColor.GREEN + "Coffre supprimé !");
                } else {
                    p.sendMessage(ChatColor.RED + "Vous devez regarder un coffre !");
                }

            }
            // /hrp add : Liste les coordonnées des coffres de HRP
            else if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
                p.sendMessage(ChatColor.GRAY + "Liste des coffres HRP :");
                hrpConfig.getKeys("hrpMainChest", true).forEach(key -> {
                    p.sendMessage(ChatColor.GRAY + " - " + key);
                });
            }

            // /hrp clan add <clan> : Ajoute un coffre aux coffres de HRP du clan
            else if (args.length == 3 && args[0].equalsIgnoreCase("clan") && args[1].equalsIgnoreCase("add")) {
                Block b = p.getTargetBlock(null, 5);
                if (b.getType() == Material.CHEST) {
                    Location loc = b.getLocation();

                    hrpConfig.set("hrpClanChest." + args[2] + "." + loc.getWorld().getName() + "/" + loc.getBlockX() + "/" + loc.getBlockY() + "/" + loc.getBlockZ(), true);

                    p.sendMessage(ChatColor.GREEN + "Coffre ajouté au clan " + args[2] + " !");
                } else {
                    p.sendMessage(ChatColor.RED + "Vous devez regarder un coffre !");
                }
            }
            // /hrp clan remove <clan> : Supprime un coffre des coffres de HRP du clan
            else if (args.length == 3 && args[0].equalsIgnoreCase("clan") && args[1].equalsIgnoreCase("remove")) {
                Block b = p.getTargetBlock(null, 5);
                if (b.getType() == Material.CHEST) {
                    Location loc = b.getLocation();
                    hrpConfig.set("hrpClanChest." + args[2] + "." + loc.getWorld().getName() + "/" + loc.getBlockX() + "/" + loc.getBlockY() + "/" + loc.getBlockZ(), null);

                    p.sendMessage(ChatColor.GREEN + "Coffre supprimé du clan " + args[2] + " !");
                } else {
                    p.sendMessage(ChatColor.RED + "Vous devez regarder un coffre !");
                }
            }
            // /hrp clan list <clan> : Liste les coordonnées des coffres de HRP du clan
            else if (args.length == 3 && args[0].equalsIgnoreCase("clan") && args[1].equalsIgnoreCase("list")) {
                p.sendMessage(ChatColor.GRAY + "Liste des coffres HRP du clan " + args[2] + " :");
                hrpConfig.getKeys("hrpClanChest." + args[2], true).forEach(key -> {
                    p.sendMessage(ChatColor.GRAY + " - " + key);
                });
            }
            // /hrp clan list : Liste les clans ayant des coffres HRP
            else if (args.length == 2 && args[0].equalsIgnoreCase("clan") && args[1].equalsIgnoreCase("list")) {
                p.sendMessage(ChatColor.GRAY + "Liste des clans ayant des coffres HRP :");
                hrpConfig.getKeys("hrpClanChest", true).forEach(key -> {
                    p.sendMessage(ChatColor.GRAY + " - " + key);
                });
            }
        }
    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        return new ArrayList<>();
    }


    public static Inventory getHRPInventory(Player p, int page) {
        Inventory inv = Bukkit.createInventory(p, 45, "§8HRP : §7Banque d'objets");

        for (int i = 0; i < 27 && i < ItemsGive.values().length; i++) {
            ItemsGive item = ItemsGive.values()[i];
            inv.setItem(i, ItemUtil.createItemStack(item.getMaterial(), item.getAmount(), item.getName().replace("%name%", p.getName()), Collections.singletonList(item.getLore()), item.getKeytag(), item.getValuetag()));
        }
        for (int i = 27; i < 36; i++) {
            inv.setItem(i, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
        }
        if (page > 1) {
            inv.setItem(39, ItemUtil.createItemStack(Material.ARROW, 1, "§6Retour", Collections.singletonList("§7Amène à la page " + (page - 1) + ".")));
        }
        inv.setItem(40, ItemUtil.createItemStack(Material.OAK_BUTTON, 1, "§6Page " + page));
        if (page - 1 < ItemsGive.values().length / 27) {
            inv.setItem(41, ItemUtil.createItemStack(Material.ARROW, 1, "§6Retour", Collections.singletonList("§7Amène à la page " + (page - 1) + ".")));
        }

        return inv;
    }

    private static Inventory openClanInventory(String clan, int page) {
        // TODO
        return null;
    }

    public static Inventory getPoubelleInventory(Player p) {
        return Bukkit.createInventory(p, 9, "§8HRP : §7Poubelle §4(VOUS PERDEZ VOS ITEMS DEDANS)");
    }


    @Override
    protected boolean isOpOnly() {
        return false;
    }


}
