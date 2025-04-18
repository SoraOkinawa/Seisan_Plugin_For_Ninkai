package me.Seisan.plugin.Features.commands.profil;


import me.Seisan.plugin.Features.Inventory.CompetenceInventory;
import me.Seisan.plugin.Features.PlayerData.PlayerInfo;
import me.Seisan.plugin.Main.Command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CompetenceCommand extends Command {
    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        Player p = (Player) sender;
        PlayerInfo pInfo = PlayerInfo.getPlayerInfo(p);
        p.openInventory(CompetenceInventory.getCompetenceGenerale(pInfo, p));
    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split)
    {
        return new ArrayList<>();
    }
}
