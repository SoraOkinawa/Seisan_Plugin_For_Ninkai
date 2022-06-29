package me.Seisan.plugin.Features.commands.anothers;

import me.Seisan.plugin.Features.PlayerData.PlayerConfig;
import me.Seisan.plugin.Main;
import me.Seisan.plugin.Main.Command;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static me.Seisan.plugin.Features.commands.anothers.Commands.perms;

public class EncaCommand extends Command {
    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        if(split.length == 1) {
            Player target = Bukkit.getPlayer(split[0]);
            if (target == null) {
                sender.sendMessage("§7Le joueur n'est pas connecté.");
                return;
            }
            PlayerConfig pConfig = PlayerConfig.getPlayerConfig(target);
            pConfig.setEncamode(!pConfig.isEncamode());
            if (pConfig.isEncamode()) {
                sender.sendMessage(target.getDisplayName() + " §7est désormais en mode encadrant.");
                target.setPlayerListName("§7[Builder] " + target.getDisplayName());
                target.sendMessage("§cHRP : §7Vous êtes désormais en mode encadrant. Veuillez prendre en considération vos règles sur discord.");
            } else {
                sender.sendMessage(target.getDisplayName() + " §7n'est désormais plus en mode encadrant.");
                target.setPlayerListName(target.getDisplayName());
                target.sendMessage("§cHRP : §7Vous n'êtes plus en mode encadrant. Merci de votre contribution pour Seisan !");
            }
            newPermissionEncaMode(pConfig);
        }
    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split)
    {
        List<String> completion = new ArrayList();
        if (split.length == 1) {
            for (Player player : Main.plugin().getServer().getOnlinePlayers()) {
                complete(completion, player.getName(), split[0]);
            }
        }
        return completion;
    }

    public static void newPermissionEncaMode(PlayerConfig pConfig) {
        perms.get(pConfig.getPlayer().getUniqueId()).setPermission("minecraft.command.me", false);
        // C'est là pour l'auto complete
        pConfig.getPlayer().updateCommands();
        if (!pConfig.getPlayer().isOp()) {
            if (pConfig.isEncamode()) {
                perms.get(pConfig.getPlayer().getUniqueId()).setPermission("fawe.admin", true);
                perms.get(pConfig.getPlayer().getUniqueId()).setPermission("worldedit.*", true);
                perms.get(pConfig.getPlayer().getUniqueId()).setPermission("minecraft.command.gamemode", true);
                perms.get(pConfig.getPlayer().getUniqueId()).setPermission("minecraft.gamemode", true);
                perms.get(pConfig.getPlayer().getUniqueId()).setPermission("minecraft.command.effect", true);
                perms.get(pConfig.getPlayer().getUniqueId()).setPermission("multiverse.*", true);
                perms.get(pConfig.getPlayer().getUniqueId()).setPermission("clickwarp.*", true);
                perms.get(pConfig.getPlayer().getUniqueId()).setPermission("minecraft.command.give", true);
                perms.get(pConfig.getPlayer().getUniqueId()).setPermission("minecraft.command.teleport", true);
                perms.get(pConfig.getPlayer().getUniqueId()).setPermission("voxelsniper.*", true);
                perms.get(pConfig.getPlayer().getUniqueId()).setPermission("voxelsniper.sniper", true);
                perms.get(pConfig.getPlayer().getUniqueId()).setPermission("voxelsniper.undouser", true);
                perms.get(pConfig.getPlayer().getUniqueId()).setPermission("voxelsniper.ignorelimitations", true);
                perms.get(pConfig.getPlayer().getUniqueId()).setPermission("voxelsniper.goto", true);
                perms.get(pConfig.getPlayer().getUniqueId()).setPermission("voxelsniper.brush.*", true);
                perms.get(pConfig.getPlayer().getUniqueId()).setPermission("headdb.open", true);
                perms.get(pConfig.getPlayer().getUniqueId()).setPermission("headdb.phead", true);
                perms.get(pConfig.getPlayer().getUniqueId()).setPermission("minecraft.debugstick", true);
                perms.get(pConfig.getPlayer().getUniqueId()).setPermission("nicknames.*", true);
            } else {
                perms.get(pConfig.getPlayer().getUniqueId()).setPermission("fawe.admin", false);
                perms.get(pConfig.getPlayer().getUniqueId()).setPermission("worldedit.*", false);
                perms.get(pConfig.getPlayer().getUniqueId()).setPermission("minecraft.command.gamemode", false);
                perms.get(pConfig.getPlayer().getUniqueId()).setPermission("minecraft.gamemode", false);
                perms.get(pConfig.getPlayer().getUniqueId()).setPermission("minecraft.command.effect", false);
                perms.get(pConfig.getPlayer().getUniqueId()).setPermission("multiverse.*", false);
                perms.get(pConfig.getPlayer().getUniqueId()).setPermission("clickwarp.*", false);
                perms.get(pConfig.getPlayer().getUniqueId()).setPermission("minecraft.command.give", false);
                perms.get(pConfig.getPlayer().getUniqueId()).setPermission("minecraft.command.teleport", false);
                perms.get(pConfig.getPlayer().getUniqueId()).setPermission("voxelsniper.*", false);
                perms.get(pConfig.getPlayer().getUniqueId()).setPermission("voxelsniper.sniper", false);
                perms.get(pConfig.getPlayer().getUniqueId()).setPermission("voxelsniper.undouser", false);
                perms.get(pConfig.getPlayer().getUniqueId()).setPermission("voxelsniper.ignorelimitations", false);
                perms.get(pConfig.getPlayer().getUniqueId()).setPermission("voxelsniper.goto", false);
                perms.get(pConfig.getPlayer().getUniqueId()).setPermission("voxelsniper.brush.*", false);
                perms.get(pConfig.getPlayer().getUniqueId()).setPermission("headdb.open", false);
                perms.get(pConfig.getPlayer().getUniqueId()).setPermission("headdb.phead", false);
                perms.get(pConfig.getPlayer().getUniqueId()).setPermission("minecraft.debugstick", false);
                perms.get(pConfig.getPlayer().getUniqueId()).setPermission("nicknames.*", false);
            }
        }
    }
}