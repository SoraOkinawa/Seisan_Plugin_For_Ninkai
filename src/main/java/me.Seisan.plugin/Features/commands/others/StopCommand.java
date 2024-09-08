package me.Seisan.plugin.Features.commands.others;

import me.Seisan.plugin.Features.PlayerData.PlayerInfo;
import me.Seisan.plugin.Main;
import me.Seisan.plugin.Main.Command;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

import static me.Seisan.plugin.Main.dbManager;


public class StopCommand extends Command {
    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        final Server server = sender.getServer();
        System.out.println("Closing the server");
        Main.serverOpen = false;
        System.out.println("Kicking all players");
        for (Player p : sender.getServer().getOnlinePlayers()) {
            p.kickPlayer(ChatColor.RED + "Ninkai redémarre. Veuillez prévenir un Administrateur si le serveur s'est fermé inopinément.");
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.plugin(), (Runnable) new BukkitRunnable() {
            @Override
            public void run() {
                server.shutdown();
            }
        }, 20 * 6L);
    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        return new ArrayList<>();
    }

}
