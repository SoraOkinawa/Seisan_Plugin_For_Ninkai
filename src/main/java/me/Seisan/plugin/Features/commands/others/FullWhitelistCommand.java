package me.Seisan.plugin.Features.commands.others;


import me.Seisan.plugin.Main;
import me.Seisan.plugin.Main.Command;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.bukkit.Bukkit.getServer;

public class FullWhitelistCommand extends Command {
    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {


        if(!(sender instanceof Player)) {
            return;
        }
        Player p = (Player)sender;
        JavaPlugin spigot = Main.plugin();
        try {
            File file = new File(spigot.getDataFolder().getCanonicalPath(),"whitelist.yaml");

            if(!file.exists()) {
                Main.LOG.info("§7Le fichier n'existe pas.");
                return;
            }

            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            String line;
            while((line = br.readLine()) != null) {
                p.chat("/whitelist add "+line);
            }
            fr.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split)
    {
        return new ArrayList<>();
    }
}
