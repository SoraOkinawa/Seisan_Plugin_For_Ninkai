package me.Seisan.plugin.Features.Barriere;

import me.Seisan.plugin.Features.PlayerData.PlayerInfo;
import me.Seisan.plugin.Features.ability.Ability;
import me.Seisan.plugin.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class BarriereLoader {

    public static void loadAllBarriereFromDB() {
        Main.LOG.info("Chargement des barrières depuis la base de données...");

        if (loadAll())
            Main.LOG.info("Chargement des barrières réussi.");
    }

    public static void updateOrInsert(String name, String nameInPlugin, String item, String type, int lvl, String tagkey, String tagvalue, String description, int pts, int ptsnec, String reqAbilities, String givenAbilities, String giveAbilities, Boolean giveAllowed, String givenJutsu, String lore) {
        if (isInserted(nameInPlugin))
            update(name, nameInPlugin, item, type, lvl, tagkey, tagvalue, description, pts, ptsnec, reqAbilities, givenAbilities, giveAbilities, giveAllowed, givenJutsu, lore);
        else
            insert(name, nameInPlugin, item, type, lvl, tagkey, tagvalue, description, pts, ptsnec, reqAbilities, givenAbilities, giveAbilities, giveAllowed, givenJutsu, lore);
    }

    public static void insert(String name, String nameInPlugin, String item, String type, int lvl, String tagkey, String tagvalue, String description, int pts, int ptsnec, String reqAbilities, String givenAbilities, String giveAbilities, Boolean giveAllowed, String givenJutsu, String lore) {
        try{
            PreparedStatement pst = Main.dbManager.getConnection().prepareStatement("INSERT INTO Abilities(name, nameInPlugin, item, type, lvl, tagkey, tagvalue, description, pts, ptsnec, reqAbilities, givenAbilities, giveAbilities, giveAllowed, givenJutsu, lore) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

            pst.setString(1, name);
            pst.setString(2, nameInPlugin);
            pst.setString(3, item);
            pst.setString(4, type);
            pst.setInt(5, lvl);
            pst.setString(6, tagkey);
            pst.setString(7, tagvalue);
            pst.setString(8, description);
            pst.setInt(9, pts);
            pst.setInt(10, ptsnec);
            pst.setString(11, reqAbilities);
            pst.setString(12, givenAbilities);
            pst.setString(13, giveAbilities);
            pst.setBoolean(14, giveAllowed);
            pst.setString(15, givenJutsu);
            pst.setString(16, lore); // Voie Ninja
            pst.executeUpdate();
            pst.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void update(String name, String nameInPlugin, String item, String type, int lvl, String tagkey, String tagvalue, String description, int pts, int ptsnec, String reqAbilities, String givenAbilities, String giveAbilities, Boolean giveAllowed, String givenJutsu, String lore) {
        try {
            PreparedStatement pst = Main.dbManager.getConnection().prepareStatement("UPDATE Abilities SET name = ?, item = ?, type = ?, lvl = ?, tagkey = ?, tagvalue = ?, description = ?, pts = ?, ptsnec = ?, reqAbilities = ?, givenAbilities = ?, giveAbilities = ?, giveAllowed = ?, givenJutsu = ?, lore = ? WHERE nameInPlugin = ?");

            pst.setString(1, name);
            pst.setString(2, item);
            pst.setString(3, type);
            pst.setInt(4, lvl);
            pst.setString(5, tagkey);
            pst.setString(6, tagvalue);
            pst.setString(7, description);
            pst.setInt(8, pts);
            pst.setInt(9, ptsnec);
            pst.setString(10, reqAbilities);
            pst.setString(11, givenAbilities);
            pst.setString(12, giveAbilities);
            pst.setBoolean(13, giveAllowed);
            pst.setString(14, givenJutsu);
            pst.setString(15, lore);
            pst.setString(16, nameInPlugin);

            pst.executeUpdate();
            pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean isInserted(String nameInPlugin){
        boolean insert = false;
        try {
            PreparedStatement pst = Main.dbManager.getConnection().prepareStatement("SELECT id FROM Abilities WHERE nameInPlugin = ?");
            pst.setString(1, nameInPlugin);
            pst.executeQuery();

            ResultSet result = pst.getResultSet();
            insert = result.next();
            pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return insert;
    }

    private static boolean loadAll() {
        try {
            PreparedStatement pst = Main.dbManager.getConnection()
                    .prepareStatement("SELECT * FROM Barrieres");

            pst.executeQuery();
            ResultSet result = pst.getResultSet();
            // - plugin-name
            // - Name
            // - Description
            // - Price
            // - IsPriceMultiplier
            // - Category
            // - Default
            // - PrepareTime
            // - Rank
            // - Secret
            // - Level

            while (result.next()) {
                String nameInPlugin = result.getString("nameInPlugin");
                String name = result.getString("name");
                String description = result.getString("description");
                int price = result.getInt("price");
                boolean isPriceMultiplier = result.getBoolean("isPriceMultiplier");
                String category = result.getString("category");
                boolean isDefault = result.getBoolean("isDefault");
                int prepareTime = result.getInt("prepareTime");
                String rank = result.getString("rank");
                boolean isSecret = result.getBoolean("isSecret");
                int level = result.getInt("level");

                new Barriere(nameInPlugin, name, description, price, isPriceMultiplier, category, isDefault, prepareTime, rank, isSecret, level);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            Main.LOG.log(Level.SEVERE, "Chargement des barrières échoué. Veuillez vérifier la console pour plus d'informations.");
            return false;
        }
        return true;
    }

    public static void reloadAll(CommandSender p){
        loadAll();
    }
}
