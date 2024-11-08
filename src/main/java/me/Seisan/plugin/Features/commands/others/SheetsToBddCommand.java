package me.Seisan.plugin.Features.commands.others;

import me.Seisan.plugin.Features.Sheets.SheetsReader;
import me.Seisan.plugin.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class SheetsToBddCommand extends Main.Command {
	@Override
	protected void myOnCommand(CommandSender sender, Command command, String label, String[] split) {
		if (sender.isOp() && split.length == 2) {
			try {
				SheetsReader.ReadSheet(sender, split[0], split[1]);
			}
			catch (Exception e) {e.printStackTrace();}
		}
	}
	
	@Override
	protected List<String> myOnTabComplete(CommandSender sender, Command command, String label, String[] split) {
		return null;
	}
}
