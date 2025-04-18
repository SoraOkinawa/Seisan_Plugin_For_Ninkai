package me.Seisan.plugin.Features.commands.others;


import me.Seisan.plugin.Features.skill.Skill;
import me.Seisan.plugin.Features.utils.ItemUtil;
import org.bukkit.Material;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import me.Seisan.plugin.Main.Command;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ParcheminCommand extends Command {
    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        if(sender instanceof Player) {
            if (split.length != 0) {
                String nom_jutsu = split[0];
                Skill skill = Skill.getByPluginName(nom_jutsu);
                if (skill != null) {
                    GiveParchemin(skill, (Player)sender);
                    sender.sendMessage("§4HRP : §7 Le parchemin pour le jutsu "+skill.getName()+" a été ajouté à votre inventaire.");
                } else {
                    sender.sendMessage("§4HRP : §7Le nom du jutsu est incorrect. Veuillez vérifier l'écriture de ce dernier.");
                }
            } else {
                sender.sendMessage("§4Usage : §7/parchemin (nom_du_jutsu)");
            }
        }
        else {
            sender.sendMessage("Seul les joueurs peuvent utiliser la commande.");
        }
    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split)
    {
        List<String> completion = new ArrayList<>();
        if (split.length == 1) {
            for (Skill jutsu : Skill.getInstanceList()) {
                complete(completion, jutsu.getNameInPlugin(), split[0]);
            }
        }
        return  completion;
    }


    public static void GiveParchemin(Skill skill, Player player) {
        ItemStack parchemin = new ItemStack(skill.getItem());
        List<String> lore = Objects.requireNonNull(parchemin.getItemMeta()).getLore();
        assert lore != null;
        lore.add("§6Un parchemin pour l'apprentissage du jutsu.");
        parchemin.getItemMeta().setLore(lore);
        parchemin = ItemUtil.addTag(parchemin, "jutsu", "learn");
        player.getInventory().addItem(parchemin);
    }
}
