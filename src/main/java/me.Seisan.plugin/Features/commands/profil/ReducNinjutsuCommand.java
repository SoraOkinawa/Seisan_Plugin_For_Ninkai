package me.Seisan.plugin.Features.commands.profil;

import me.Seisan.plugin.Features.PlayerData.PlayerInfo;
import me.Seisan.plugin.Main.Command;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ReducNinjutsuCommand extends Command {
    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        if(!sender.isOp()) return;
        if(split.length != 1) return;
        Player p = Bukkit.getPlayer(split[0]);
        if(p == null) { sender.sendMessage("§cHRP :§7Joueur non connecté"); return;}
        PlayerInfo.getPlayerInfo(p).setReduc_ninjutsu(0);
        p.sendMessage("§b** "+p.getDisplayName()+" §binitialise son optimisation élémentaire à 0.");
        for (Entity entity : p.getNearbyEntities(50, 50, 50)) {
            if (entity instanceof Player) {
                entity.sendMessage("§b** "+p.getDisplayName()+" §binitialise son optimisation élémentaire à 0.");
            }
        }
    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        return new ArrayList<>();
    }
}
