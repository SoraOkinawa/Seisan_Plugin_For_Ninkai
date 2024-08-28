package me.Seisan.plugin.Features.commands.anothers;

import me.Seisan.plugin.Features.PlayerData.PlayerInfo;
import me.Seisan.plugin.Features.commands.anothers.Commands;
import me.Seisan.plugin.Features.skill.SkillMastery;
import me.Seisan.plugin.Features.utils.ItemUtil;
import me.Seisan.plugin.Features.utils.Nickname;
import me.Seisan.plugin.Main.Command;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.world.level.IEntityAccess;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class TechniqueMJCommand extends Command {
    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        if (sender instanceof Player) {
            Player player = (Player) sender;//message d'erreur si il y a pas d'argument dans la commande !
            if (split.length == 0) {
                player.sendMessage("§4HRP : §7Il faut faire la commande /techniquemj <nom de la technique>");
            }
            //message si il y a argument / nom de la technique !
            if (split.length >= 1) {
                StringBuilder tc = new StringBuilder();
                for (String part : split) {
                    tc.append(part + " ");
                }
                String coconut = "&";
                String coco = coconut.replace("&", "§");
                World w = player.getWorld();
                for (Entity e : w.getPlayers()) {
                    if (e instanceof Player) {
                        if (e.getLocation().distance(player.getLocation()) <= 30) {
                            e.sendMessage("§c** " + player.getDisplayName() + "§créalise la technique " + coco + tc.toString());


                        }
                    }
                }
            }


        }


    }



@Override
protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String
        label, String[] split) {
    return List.of();
}
}




