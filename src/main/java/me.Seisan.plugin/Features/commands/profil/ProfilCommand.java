package me.Seisan.plugin.Features.commands.profil;

import me.Seisan.plugin.Features.Inventory.TrainInventory;
import me.Seisan.plugin.Features.PlayerData.PlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import me.Seisan.plugin.Main.Command;

import java.util.ArrayList;
import java.util.List;

public class ProfilCommand extends Command {
    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        if(sender instanceof Player) {
            Player p = (Player) sender;
            if (split.length == 0) {
                PlayerInfo pInfo = PlayerInfo.getPlayerInfo(p);
                p.openInventory(TrainInventory.getFichePerso(pInfo, p));
            } else {
                Player target = Bukkit.getPlayer(split[0]);
                if (target != null) {
                    PlayerInfo pInfo = PlayerInfo.getPlayerInfo(target);
                    if(pInfo.getMaskprofil() == 0 || sender.isOp()) {
                        p.openInventory(TrainInventory.getFichePerso(pInfo, p));
                    }
                    else {
                        sender.sendMessage("§cHRP : §7La fiche de ce personnage est inaccessible. §c[ANTI-SPOIL]");
                    }
                } else {
                    p.sendMessage(ChatColor.RED + "HRP : " + ChatColor.GRAY + " \"" + split[0] + "\" n'est pas connecté");
                }
            }
        }
    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split)
    {
        List<String> completion = new ArrayList();
        if(split.length == 1) {
            for(Player p : Bukkit.getOnlinePlayers()) {
                complete(completion, p.getName(), split[0]);
            }
        }
        return completion;
    }

    @Override
    protected boolean isOpOnly() {
        return false;
    }
}
