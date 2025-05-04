package me.Seisan.plugin.Features.commands.others;

import me.Seisan.plugin.Features.Sheets.SheetsReader;
import me.Seisan.plugin.Features.ability.AbilityLoaderDB;
import me.Seisan.plugin.Features.skill.TechniquesLoaderDB;
import me.Seisan.plugin.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SheetsToBddCommand extends Main.Command {
	@Override
	protected void myOnCommand(CommandSender sender, Command command, String label, String[] split) {
		if (split.length == 3) {
			try {
				switch (split[0]) {
					case "jutsu":
						addTechniques(sender, SheetsReader.ReadSheet(sender, split[1], split[2]));
						break;
					case "abilities":
						addAbilities(sender, SheetsReader.ReadSheet(sender, split[1], split[2]));
						break;
					case "test":
						displaySheet(sender, SheetsReader.ReadSheet(sender, split[1], split[2]));
						break;
				}
				
			}
			catch (Exception e) {e.printStackTrace();}
		}
	}
	
	private void addTechniques(CommandSender sender, List<List<Object>> values) {
		int updated = 0, inserted = 0;
		boolean result;
		for (List row : values) {
			result = TechniquesLoaderDB.insertOrUpdate(
					row.get(0).toString(),
					row.get(1).toString(),
					row.get(2).toString(),
					Boolean.parseBoolean((String) row.get(3)),
					Integer.parseInt((String) row.get(4)),
					Boolean.parseBoolean((String) row.get(5)),
					Boolean.parseBoolean((String) row.get(6)),
					Boolean.parseBoolean((String) row.get(7)),
					Boolean.parseBoolean((String) row.get(8)),
					Boolean.parseBoolean((String) row.get(9)),
					row.get(10).toString(),
					row.get(11).toString(),
					row.get(12).toString(),
					row.get(13).toString(),
					row.get(14).toString(),
					row.get(15).toString(),
					(row.size() > 16 ? row.get(16).toString() : "")
			);
			
			if (result) {
				sender.sendMessage("§aTechnique " + row.get(0) + " §r§6mise à jour§a.");
				updated++;
			} else {
				sender.sendMessage("§aTechnique " + row.get(0) + " §r§eajoutée à BDD.");
				inserted++;
			}
		}
		sender.sendMessage("§2§l" + inserted + " §r§atechniques rajoutées à la BDD.");
		sender.sendMessage("§2§l" + updated + " §r§atechniques mises à jour.");
	}
	
	private void addAbilities(CommandSender sender, List<List<Object>> values) {
		int updated = 0, inserted = 0;
		boolean result;
		for (List row : values) {
			result = AbilityLoaderDB.updateOrInsert(
					row.get(0).toString(),
					row.get(1).toString(),
					row.get(2).toString(),
					row.get(3).toString(),
					Integer.parseInt(row.get(4).toString()),
					row.get(5).toString(),
					row.get(6).toString(),
					row.get(7).toString(),
					Integer.parseInt(row.get(8).toString()),
					Integer.parseInt(row.get(9).toString()),
					row.get(10).toString(),
					row.get(11).toString(),
					row.get(12).toString(),
					Boolean.parseBoolean(row.get(13).toString()),
					row.get(14).toString(),
					row.get(15).toString()
			);
			
			if (result) {
				sender.sendMessage("§bCompétence " + row.get(0) + " §r§6mise à jour§b.");
				updated++;
			} else {
				sender.sendMessage("§bCompétence " + row.get(0) + " §r§eajoutée à BDD§b.");
				inserted++;
			}
		}
		sender.sendMessage("§3§l" + inserted + " §r§acompétences rajoutées à la BDD.");
		sender.sendMessage("§3§l" + updated + " §r§acompétences mises à jour.");
	}
	
	private void displaySheet(CommandSender sender, List<List<Object>> values) {
		String str = "";
		for (List row : values) {
			str = "";
			for (int i = 0; i < row.size(); i++) {
				str += row.get(i) + (i < row.size()-1 ? ";" : "");
			}
			sender.sendMessage(str);
		}
	}
	
	@Override
	protected List<String> myOnTabComplete(CommandSender sender, Command command, String label, String[] split) {
		List<String> completion = new ArrayList<>();
		switch (split.length) {
			case 1:
				complete(completion, "jutsu", split[0]);
				complete(completion, "abilities", split[0]);
				complete(completion, "test", split[0]);
				break;
			case 2:
				complete(completion,  "idGsheet", split[1]);
				break;
			case 3:
				complete(completion,  "plage", split[2]);
				break;
		}
		return completion;
	}
}
