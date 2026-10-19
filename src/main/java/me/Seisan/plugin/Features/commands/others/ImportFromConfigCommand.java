package me.Seisan.plugin.Features.commands.others;

import me.Seisan.plugin.Features.ability.Ability;
import me.Seisan.plugin.Features.ability.AbilityLoader;
import me.Seisan.plugin.Features.ability.AbilityLoaderDB;
import me.Seisan.plugin.Features.data.Config;
import me.Seisan.plugin.Features.skill.SkillLoader;
import me.Seisan.plugin.Features.skill.TechniquesLoaderDB;
import me.Seisan.plugin.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class ImportFromConfigCommand extends Main.Command {
	@Override
	protected void myOnCommand(CommandSender sender, Command command, String label, String[] split) {
		if (split.length == 1) {
			if (!Main.dbManager.isConnected()) {
				sender.sendMessage("§4HRP : §cErreur de connexion à la base de données. Base de Données déconnectée. Import impossible.");
				return;
			}
			
			switch (split[0]) {
				case "jutsu":
					Config techniquesConfig = SkillLoader.skillConfig;
					TechniquesLoaderDB.insertToDBFromConfig(techniquesConfig, techniquesConfig.getStringList("enabledSkills"));
					TechniquesLoaderDB.reloadAll(sender);
					break;
				case "ability":
					Config abilityConfig = AbilityLoader.abilityConfig;
					AbilityLoaderDB.insertToDBFromConfig(abilityConfig, abilityConfig.getStringList("enabledAbilities"));
					AbilityLoaderDB.reloadAll(sender);
					break;
				default:
					sender.sendMessage("§4Usage : §c/importfromconfig <justu/ability>");
			}
		} else {
			sender.sendMessage("§4Usage : §c/importfromconfig <justu/ability>");
		}
	}
	
	@Override
	protected List<String> myOnTabComplete(CommandSender sender, Command command, String label, String[] split) {
		List<String> completion = new ArrayList<>();
		complete(completion, "jutsu", split[0]);
		complete(completion, "ability", split[0]);
		return completion;
		
	}
}
