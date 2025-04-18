package me.Seisan.plugin.Features.commands.profil;


import me.Seisan.plugin.Features.PlayerData.PlayerInfo;
import me.Seisan.plugin.Features.objectnum.CouleurChakra;
import me.Seisan.plugin.Features.objectnum.Teinte;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import me.Seisan.plugin.Main.Command;

public class ColorChakraCommand extends Command {
    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        // /colorchakra (joueur) (couleur)
        if (split.length == 2) {
            Player p = Bukkit.getPlayer(split[0]);
            if (p == null) {
                sender.sendMessage("§cHRP : §7Le joueur n'est pas connecté.");
                return;
            }
            PlayerInfo pInfo = PlayerInfo.getPlayerInfo(p);
            CouleurChakra couleur = CouleurChakra.fromName(split[1]);
            Teinte teinte = Teinte.fromName(split[1]);
            if (couleur != null) {
                pInfo.setCouleurChakra(couleur);
                sender.sendMessage("§cHRP : §7La couleur de chakra du personnage du joueur est désormais : " + couleur.getName());
            } else if (teinte != null) {
                pInfo.setTeinte(teinte);
                sender.sendMessage("§cHRP : §7La teinte de chakra du personnage du joueur est désormais : " + teinte.getName());
            } else {
                sender.sendMessage("§cHRP : §7/colorchakra (joueur) (couleur)");
                sender.sendMessage("§cHRP : §7Les couleurs sont sur votre gdoc è_é");
            }
        } else {
            sender.sendMessage("§cHRP : §7/colorchakra (joueur) (couleur)");
            sender.sendMessage("§cHRP : §7Les couleurs sont sur votre gdoc è_é");
        }
    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split)
    {
        return new ArrayList<>();
    }
}