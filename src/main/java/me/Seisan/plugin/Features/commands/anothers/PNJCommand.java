package me.Seisan.plugin.Features.commands.anothers;

import me.Seisan.plugin.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

import java.util.ArrayList;
import java.util.List;

public class PNJCommand extends Main.Command {
    @Override
    protected void myOnCommand(CommandSender sender, Command command, String label, String[] split) {
        if (sender.isOp()) {
            if(split.length == 1) {
                Player p = (Player) sender;
                Villager villager;
                switch(split[0]) {
                    case "banquier":
                        villager = (Villager) p.getWorld().spawnEntity(p.getLocation(), EntityType.VILLAGER);
                        villager.setInvulnerable(true);
                        villager.setAI(false);
                        villager.setCustomName("§6Banquier");

                        p.sendMessage("Le banquier a été créé avec succès.");
                        break;
                    case "jouet":
                        villager = (Villager) p.getWorld().spawnEntity(p.getLocation(), EntityType.VILLAGER);
                        villager.setInvulnerable(true);
                        villager.setAI(false);
                        villager.setCustomName("§6Vendeur de jouet");

                        p.sendMessage("Le vendeur de jouet a été créé avec succès.");
                        break;
                    case "encre":
                        villager = (Villager) p.getWorld().spawnEntity(p.getLocation(), EntityType.VILLAGER);
                        villager.setInvulnerable(true);
                        villager.setAI(false);
                        villager.setCustomName("§6Vendeur d'encre");

                        p.sendMessage("Le vendeur d'encre a été créé avec succès.");
                }
            }
        }
    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, Command command, String label, String[] split) {
        List<String> completion = new ArrayList();
        if (split.length == 1) {
            complete(completion, "jouet", split[0]);
            complete(completion, "banquier", split[0]);
            complete(completion, "encre", split[0]);
        }
        return completion;
    }
}
