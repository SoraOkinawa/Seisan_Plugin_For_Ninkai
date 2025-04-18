package me.Seisan.plugin.Features.commands.anothers;

import me.Seisan.plugin.Main.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

import static me.Seisan.plugin.Features.commands.anothers.Commands.getRandom;
import static me.Seisan.plugin.Features.commands.anothers.Commands.phraseantonin;

public class AntoninCommand extends Command {
    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        sender.sendMessage("§7Antonin vous chuchote : §i"+phraseantonin.get(getRandom(0, phraseantonin.size()-1)));
    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        return new ArrayList<>();
    }
}
