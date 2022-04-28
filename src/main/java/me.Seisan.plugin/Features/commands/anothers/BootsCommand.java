package me.Seisan.plugin.Features.commands.anothers;

import me.Seisan.plugin.Features.PlayerData.PlayerInfo;
import me.Seisan.plugin.Main.Command;
import org.bukkit.ChatColor;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class BootsCommand extends Command {

    //final String BOOTS_TAG = ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + ChatColor.BOLD + "CHAKRA_BOOTS" + ChatColor.DARK_GRAY + "] ";

    @Override
    protected void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            PlayerInfo playerInfo = PlayerInfo.getPlayerInfo(p);
            if (playerInfo.hasAbility("concentration_chakra")) {
                if (p.getInventory().getBoots() == null) {
                    ItemStack customBoots = new ItemStack(Material.CHAINMAIL_BOOTS);
                    ItemMeta customB = customBoots.getItemMeta();
                    if(customB != null) {
                        customB.setDisplayName("§4Bottes de chakra");
                        customB.setUnbreakable(true);
                        customB.addEnchant(Enchantment.FROST_WALKER, 1, true);
                        customB.addEnchant(Enchantment.BINDING_CURSE, 1, true);
                        if(playerInfo.hasAbility("plume")) {
                            customB.addEnchant(Enchantment.PROTECTION_FALL, 10, true);
                        }
                        customB.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                        customBoots.setItemMeta(customB);
                        p.getInventory().setBoots(customBoots);
                    }
                    p.updateInventory();
                    if(playerInfo.getClan().getId() == 18 && playerInfo.getLvL(playerInfo.getClan().getName()) >= 4) {
                        p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 999999, 14, false, false));
                    }
                    else {
                        p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 999999, playerInfo.getCaract().getForce(), false, false));
                    }
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999, playerInfo.getCaract().getVitesse(), false, false));
                } else {
                    p.removePotionEffect(PotionEffectType.JUMP);
                    p.removePotionEffect(PotionEffectType.SPEED);
                    p.getInventory().setBoots(null);
                }
            } else {
                sender.sendMessage("§b**Vous n'avez pas la compétence adéquate pour insuffler du chakra dans vos pieds !");
            }
        }
    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        return null;
    }

    @Override
    protected boolean isOpOnly() {
        return false;
    }
}
