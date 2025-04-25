/*
 * Copyright 404Team (c) 2018. For all uses ask 404Team for approuval before.
 */
package me.Seisan.plugin.Features.commands.others;

import me.Seisan.plugin.Features.ability.AbilityLoaderDB;
import me.Seisan.plugin.Features.skill.TechniquesLoaderDB;
import me.Seisan.plugin.Main;
import me.Seisan.plugin.Main.Command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReloadDBCommand extends Command {
    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        if(split.length == 1) {
            if (!Main.dbManager.isConnected()) {
                sender.sendMessage("§4HRP : §cErreur de connexion à la base de données. Base de Données déconnectée. Reload impossible.");
                return;
            }
            
            switch (split[0]) {
                case "jutsu":
                    TechniquesLoaderDB.reloadAll(sender);
                    break;
                case "ability":
                    AbilityLoaderDB.reloadAll(sender);
                    break;
                default:
                    sender.sendMessage("§4Usage : §c/reloaddb <justu/ability> <gsheet ID> <plage>");
            }
        }
        else {
            sender.sendMessage("§4Usage : §c/reloaddb <justu/ability> <gsheet ID> <plage>");
        }
    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split)
    {
        List<String> completion = new ArrayList<>();
        complete(completion, "jutsu", split[0]);
        complete(completion, "ability", split[0]);
        return completion;
    }
}
