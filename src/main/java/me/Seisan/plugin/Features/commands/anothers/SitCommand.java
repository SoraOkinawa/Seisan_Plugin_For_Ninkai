package me.Seisan.plugin.Features.commands.anothers;

import me.Seisan.plugin.Main.Command;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class SitCommand extends Command {

    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        Player player = Bukkit.getServer().getPlayer(sender.getName());
        if(player != null) {
            Vector direction = player.getLocation().getDirection();
            if (!player.getScoreboardTags().contains("lay")) {
                if (player.isOnGround()) {
                    if (player.getVehicle() == null) {
                        final ArmorStand vehicle = player.getWorld().spawn(player.getLocation().clone().subtract(0, 1.7, 0).setDirection(direction), ArmorStand.class);
                        vehicle.setGravity(false);
                        vehicle.setVisible(false);
                        vehicle.addPassenger(player);
                        vehicle.setCustomName("Sit");
                        player.getScoreboardTags().add("Assis");
                    } else {
                        player.sendMessage(ChatColor.RED + "HRP : " + ChatColor.GRAY + " Il est impossible de s'asseoir pour le moment.");
                        player.sendMessage(ChatColor.RED + "HRP : Vous êtes déjà assis, allongé ou dans un véhicule.");

                    }
                } else {
                    player.sendMessage(ChatColor.RED + "HRP : " + ChatColor.GRAY + " Il est impossible de s'asseoir pour le moment.");
                }
            }
        }
    }
    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split)
    {
        List<String> completion = new ArrayList();
        return completion;
    }

    @Override
    protected boolean isOpOnly() {
        return false;
    }
}
