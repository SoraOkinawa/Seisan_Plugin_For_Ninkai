package me.Seisan.plugin.Features.commands.anothers;

import me.Seisan.plugin.Features.PlayerData.PlayerConfig;
import me.Seisan.plugin.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static me.Seisan.plugin.Features.commands.anothers.Commands.perms;

public class FixPermCommand extends Main.Command {
	@Override
	protected void myOnCommand(CommandSender sender, Command command, String label, String[] split) {
		Player player = (Player) sender;
		if (split.length == 1) {
			Player target = Bukkit.getPlayer(split[0]);
			PlayerConfig pConfig = PlayerConfig.getPlayerConfig(target);
			
			perms.get(pConfig.getPlayer().getUniqueId()).unsetPermission("fawe.admin");
			perms.get(pConfig.getPlayer().getUniqueId()).unsetPermission("worldedit.*");
			perms.get(pConfig.getPlayer().getUniqueId()).unsetPermission("minecraft.command.gamemode");
			perms.get(pConfig.getPlayer().getUniqueId()).unsetPermission("minecraft.gamemode");
			perms.get(pConfig.getPlayer().getUniqueId()).unsetPermission("minecraft.command.effect");
			perms.get(pConfig.getPlayer().getUniqueId()).unsetPermission("multiverse.*");
			perms.get(pConfig.getPlayer().getUniqueId()).unsetPermission("clickwarp.*");
			perms.get(pConfig.getPlayer().getUniqueId()).unsetPermission("minecraft.command.give");
			perms.get(pConfig.getPlayer().getUniqueId()).unsetPermission("minecraft.command.teleport");
			perms.get(pConfig.getPlayer().getUniqueId()).unsetPermission("voxelsniper.*");
			perms.get(pConfig.getPlayer().getUniqueId()).unsetPermission("voxelsniper.sniper");
			perms.get(pConfig.getPlayer().getUniqueId()).unsetPermission("voxelsniper.undouser");
			perms.get(pConfig.getPlayer().getUniqueId()).unsetPermission("voxelsniper.ignorelimitations");
			perms.get(pConfig.getPlayer().getUniqueId()).unsetPermission("voxelsniper.goto");
			perms.get(pConfig.getPlayer().getUniqueId()).unsetPermission("voxelsniper.brush.*");
			perms.get(pConfig.getPlayer().getUniqueId()).unsetPermission("headdb.open");
			perms.get(pConfig.getPlayer().getUniqueId()).unsetPermission("headdb.phead");
			perms.get(pConfig.getPlayer().getUniqueId()).unsetPermission("minecraft.debugstick");
			perms.get(pConfig.getPlayer().getUniqueId()).unsetPermission("nicknames.*");
			
			player.sendMessage("§c[HRP] §aPermissions reset avec succès.");
		} else {
			player.sendMessage("§c[HRP] Usage : /fixperm <player>");
		}
	}
	
	@Override
	protected List<String> myOnTabComplete(CommandSender sender, Command command, String label, String[] split) {
		List<String> completion = new ArrayList();
		if (split.length == 1) {
			for (Player player : Main.plugin().getServer().getOnlinePlayers()) {
				complete(completion, player.getName(), split[0]);
			}
		}
		return completion;	}
}
