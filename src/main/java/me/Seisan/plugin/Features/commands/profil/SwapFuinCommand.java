package me.Seisan.plugin.Features.commands.profil;

import me.Seisan.plugin.Features.PlayerData.PlayerConfig;
import me.Seisan.plugin.Features.PlayerData.PlayerInfo;
import me.Seisan.plugin.Features.objectnum.ArtNinja;
import me.Seisan.plugin.Main.Command;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SwapFuinCommand extends Command {
    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        if(!(sender instanceof Player)) {
            return;
        }
//        Player p = (Player)sender;
//        PlayerInfo pInfo = PlayerInfo.getPlayerInfo(p);
//        PlayerConfig pConfig = PlayerConfig.getPlayerConfig(p);
//        if(pInfo.getVoieNinja() != ArtNinja.FUINJUTSU) {
//            p.sendMessage("§cHRP : §7Vous n'êtes pas Fuinjutsu.");
//            return;
//        }
//        if(pInfo.getLvL(pInfo.getVoieNinja().getName()) < 4) {
//            p.sendMessage("§cHRP : §7Vous n'êtes pas level 4 en Fuinjutsu.");
//            return;
//        }
//
//        pConfig.setSwapfuin(!pConfig.isSwapfuin());
//        if(pConfig.isSwapfuin()) {
//            p.sendMessage("§cHRP : §7Vous utilisez désormais vos feuilles Seju");
//        }
//        else {
//            p.sendMessage("§cHRP : §7Vous utilisez désormais votre encre Fuinjutsu");
//        }
    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split)
    {
        return new ArrayList<>();
    }


}
