package me.Seisan.plugin.Features.commands.anothers;


import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import me.Seisan.plugin.Main;
import me.Seisan.plugin.Main.Command;

import org.bukkit.command.CommandSender;

import org.bukkit.entity.Player;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ReloadSkinCommand extends Command {
    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        if(!(sender instanceof Player)) {
            return;
        }
        final Player p = (Player)sender;
        if(setSkin(p, p.getUniqueId().toString())) {
            p.sendMessage("§cHRP : §7Votre skin a été actualisé.");
        }
        else {
            p.sendMessage("§cHRP : §7Actualisation du skin erroné dû à Mojang.");
        }

    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split)
    {
        return new ArrayList<>();
    }

    public static boolean setSkin(Player p, String uuid) {
        try {
            HttpsURLConnection connection = (HttpsURLConnection) new URL(String.format("https://sessionserver.mojang.com/session/minecraft/profile/%s?unsigned=false", uuid)).openConnection();
            if (connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                URL url_1 = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
                InputStreamReader reader_1 = new InputStreamReader(url_1.openStream());
                JsonObject textureProperty = new JsonParser().parse(reader_1).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();
                String value = textureProperty.get("value").getAsString();
                String signature = textureProperty.get("signature").getAsString();
                PlayerProfile gp = p.getPlayerProfile();
                gp.setProperty(new ProfileProperty("textures", value, signature));
                p.setPlayerProfile(gp);
                return true;
            } else {
                Main.LOG.info("Connection could not be opened (Response code " + connection.getResponseCode() + ", " + connection.getResponseMessage() + ")");
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected boolean isOpOnly() {
        return false;
    }
}
