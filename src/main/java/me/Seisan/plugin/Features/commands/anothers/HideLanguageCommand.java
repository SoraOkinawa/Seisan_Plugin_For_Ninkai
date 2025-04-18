package me.Seisan.plugin.Features.commands.anothers;

import me.Seisan.plugin.Features.Chat.ChatFormat;
import me.Seisan.plugin.Main.Command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class HideLanguageCommand extends Command {

    private static List<UUID> hidden = new ArrayList<>();

    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {

        // /prefix <prefix>
        if ((sender instanceof Player)) {
            Player p = (Player) sender;

            // /prefix </> : Reset prefix
            if (args.length == 0) {
                if (hidden.contains(p.getUniqueId())) {
                    hidden.remove(p.getUniqueId());
                    p.sendMessage("§aLangues affichées.");
                } else {
                    hidden.add(p.getUniqueId());
                    p.sendMessage("§aLangues cachées.");
                }
            }

        }
    }

    public static boolean isPlayerHidingLanguage(Player p) {
        return hidden.contains(p.getUniqueId());
    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        return new ArrayList<>();
    }
}
