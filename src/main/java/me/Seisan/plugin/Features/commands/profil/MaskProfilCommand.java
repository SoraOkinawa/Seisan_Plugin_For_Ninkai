package me.Seisan.plugin.Features.commands.profil;

import me.Seisan.plugin.Features.PlayerData.PlayerInfo;
import me.Seisan.plugin.Main.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MaskProfilCommand extends Command {
    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        PlayerInfo playerInfo = PlayerInfo.getPlayerInfo((Player)sender);
        if(playerInfo.getMaskprofil() == 0) {
            playerInfo.setMaskprofil(1);
            sender.sendMessage("§cHRP : §7Votre profil est maintenant masqué. [ANTI-SPOIL]");
        }
        else {
            playerInfo.setMaskprofil(0);
            sender.sendMessage("§cHRP : §7Votre profil est maintenant en libre accès.");

        }
    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split)
    {
        return new ArrayList<>();
    }
}
