package me.Seisan.plugin.Features.commands.anothers;

import me.Seisan.plugin.Main;
import me.Seisan.plugin.Main.Command;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class JumpUpDownCommand extends Command {
    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        // Il monte
        if(!(sender instanceof Player)) {
            return;
        }
        Player p = (Player)sender;
        Location l = p.getLocation();
        // est-ce qu'il est en jump et veut descendre
        if(Main.getIsJumping().containsKey(p.getName())) {
            Location block = Main.getIsJumping().get(p.getName());
            boolean notfind = true;
            for(int i = 2; i < 15 && notfind; ++i) {
                if(l.clone().subtract(0,i,0).getBlock().getType() != Material.AIR) {
                    notfind = false;
                    block.getBlock().setType(Material.AIR);
                    p.teleport(l.clone().subtract(0,i-1.5,0));
                }
            }
            if(notfind) {
                p.teleport(l.clone().subtract(0,12.5,0));
                block.getBlock().setType(Material.AIR);
            }

            Main.getIsJumping().remove(p.getName());
        }
        else {
            if(split.length != 1 || !StringUtils.isNumeric(split[0])) {
                p.sendMessage("§cHRP : §7/jumpupdown (nombre)");
                return;
            }

            int nb = Integer.parseInt(split[0]);
            if(nb < 0 || nb > 20) {
                p.sendMessage("§cHRP : §7Eh oh, mollo l'asticot. Entre 1 et 20, pas plus, pas moins.+");
                return;
            }
            if(l.clone().add(0,nb-1,0).getBlock().getType() != Material.AIR
                    || l.clone().add(0,nb,0).getBlock().getType() != Material.AIR
                    || l.clone().add(0,nb+1,0).getBlock().getType() != Material.AIR) {
                p.sendMessage("§cHRP : §7Il n'y a pas la place à "+nb+" blocs au dessus de vous.");
                return;
            }

            Main.getIsJumping().put(p.getName(), l.clone().add(0,nb-1,0));
            l.clone().add(0,nb-1,0).getBlock().setType(Material.BARRIER);
            p.teleport(l.clone().add(0,nb+1,0));
        }
    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split)
    {
        return new ArrayList<>();
    }


}
