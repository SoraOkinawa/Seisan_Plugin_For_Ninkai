/*
 * Copyright 404Team (c) 2018. For all uses ask 404Team for approuval before.
 */
package me.Seisan.plugin.Features.commands.others;

import me.Seisan.plugin.Features.ability.AbilityLoader;
import me.Seisan.plugin.Features.skill.SkillLoader;
import me.Seisan.plugin.Main.Command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Helliot on 16/08/2018
 */
public class ReloadYMLCommand extends Command {
    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        if(split.length == 1) {
            switch (split[0]) {
                case "jutsu":
                    if(SkillLoader.checkConfig()) {
                        SkillLoader.reloadAllSkills(sender);
                    }
                    else {
                        sender.sendMessage("§4ATTENTION, JUTSU NON VALIDE. VOIR CONSOLE POUR DETAILS;");
                    }
                    break;
                case "ability":
                    if(AbilityLoader.checkConfig()) {
                        AbilityLoader.reloadAllAbility(sender);
                    }
                    else {
                        sender.sendMessage("§4ATTENTION, ABILITY NON VALIDE. VOIR CONSOLE POUR DETAILS;");
                    }
                    break;
                default:
                    sender.sendMessage(ChatColor.RED+"/reloadjutsu jutsu|ability");
            }
        }
        else {
            sender.sendMessage(ChatColor.RED+"/reloadyml justu");
            sender.sendMessage(ChatColor.RED+"/reloadyml ability");
        }
    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split)
    {
        return new ArrayList<>();
    }
}
