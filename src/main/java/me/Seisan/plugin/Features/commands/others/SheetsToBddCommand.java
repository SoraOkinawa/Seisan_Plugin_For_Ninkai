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
		if (sender.isOp() && split.length == 3) {
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
		for (List row : values) {
			TechniquesLoaderDB.insertOrUpdate(
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
			sender.sendMessage("§aTechnique " + row.get(0) + " §r§arajouté à la BDD ou mise à jour.");
		}
		sender.sendMessage("§2§l" + values.size() + " §r§atechniques rajoutées à la BDD ou mise à jour.");
	}
	
	private void addAbilities(CommandSender sender, List<List<Object>> values) {
		for (List row : values) {
			AbilityLoaderDB.updateOrInsert(
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
			sender.sendMessage("§aCompétence " + row.get(0) + " §r§arajouté à la BDD ou mise à jour.");
		}
		sender.sendMessage("§2§l" + values.size() + " §r§acompétences rajoutées à la BDD ou mise à jour.");
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
		List<String> completion = new ArrayList<>(Arrays.asList("jutsu", "abilities", "test"));
		if(split.length == 1) for(Player p : Bukkit.getOnlinePlayers()) complete(completion, p.getName(), split[0]);
		return completion;
	}
}
