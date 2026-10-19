package me.Seisan.plugin.Features.commands.anothers;

import me.Seisan.plugin.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.Player;

public class SpeedCommand extends Main.Command {
	
	@Override
	protected void myOnCommand(CommandSender sender, Command command, String label, String[] split) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			
			if (split.length >= 1) {
				switch (split[0]) {
					case "get" :
						sender.sendMessage("§cHRP §7:\n- flySpeed : " + p.getFlySpeed() + "\n- walkSpeed : " + p.getWalkSpeed());
						break;
					case "reset":
						p.setFlySpeed(0.2f);
						p.setWalkSpeed(0.099986024f);
						break;
					default:
						float newSpeed = 0;
						try {
							newSpeed = Float.parseFloat(split[0]);
						} catch (NumberFormatException e) {
							sender.sendMessage("Usage : /speed <get|reset|nombre> [fly/walk]");
							return;
						}
						newSpeed = Math.min(1, Math.max(-1, newSpeed));
						boolean isFlying = p.isFlying();
						if (split.length == 2) {
							if (split[1].toLowerCase().equals("fly")) isFlying = true;
							else if (split[1].toLowerCase().equals("walk")) isFlying = false;
							else sender.sendMessage("§cHRP : Deuxième argument non reconnu, il sera ignoré. Usage : /speed <get|reset|nombre> [fly/walk]");
						}
						if (isFlying)
							p.setFlySpeed(newSpeed);
						else
							p.setWalkSpeed(newSpeed);
				}
			}
			else {
				sender.sendMessage("Usage : /speed <get|reset|nombre> [fly/walk]");
			}
		}
	}
	
//	private float lerpSpeed(float value) {
//		float a = 1f;
//		float b = 10f;
//		float c = 0.2f;
//		float d = 1f;
//		float result = c + ((value - a)*(d-c))/(b-a);;
//
//		return result;
//	}
	
	@Override
	protected List<String> myOnTabComplete(CommandSender sender, Command command, String label, String[] split) {
		List<String> completion = new ArrayList<>();
		switch (split.length) {
			case 1:
				complete(completion, "get", split[0]);
				complete(completion, "reset", split[0]);
				complete(completion, "nombreEntier", split[0]);
				break;
			case 2:
				complete(completion, "fly", split[0]);
				complete(completion, "walk", split[0]);
				break;
				
		}
		return completion;
	}
}
