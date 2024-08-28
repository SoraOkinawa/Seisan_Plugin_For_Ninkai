package me.Seisan.plugin.Features.data;

import lombok.Getter;
import me.Seisan.plugin.Features.PlayerData.Meditation;
import me.Seisan.plugin.Features.skill.Skill;
import me.Seisan.plugin.Features.skill.SkillLevel;
import me.Seisan.plugin.Features.utils.ItemUtil;
import me.Seisan.plugin.Main;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TechniquesDB {
    public static void insertTechnique(String name, String nameInPlugin, boolean enabled, int manaCost, boolean needMastery, boolean needTarget, boolean skillVisibility, boolean canBeFullMaster, boolean _public, String itemType, String level, String publicDescription, String privateDescription, String mudras) {
        try {
            PreparedStatement pst = Main.dbManager.getConnection().prepareStatement("INSERT INTO Techniques(name, nameInPlugin, enabled, manaCost, needMastery, needTarget, skillVisibility, canBeFullMaster, public, itemType, level, publicDescription, privateDescription, mudras, \n) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

            pst.setString(1, name);
            pst.setString(2, nameInPlugin);
            pst.setBoolean(3, enabled);
            pst.setInt(4, manaCost);
            pst.setBoolean(5, needMastery);
            pst.setBoolean(6, needTarget);
            pst.setBoolean(7, skillVisibility);
            pst.setBoolean(8, canBeFullMaster);
            pst.setBoolean(9, _public);
            pst.setString(10, itemType);
            pst.setString(11, level);
            pst.setString(12, publicDescription);
            pst.setString(13, privateDescription);
            pst.setString(14, mudras);

            pst.executeUpdate();
            pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void UpdateTechnique(String name, String nameInPlugin, boolean enabled, int manaCost, boolean needMastery, boolean needTarget, boolean skillVisibility, boolean canBeFullMaster, boolean _public, String itemType, String level, String publicDescription, String privateDescription, String mudras) {
        try {
            PreparedStatement pst = Main.dbManager.getConnection().prepareStatement("UPDATE Techniques SET name = ?, enabled = ?, manaCost = ?, needMaster = ?, needTarget = ?, skillVisibility = ?, canBeFullMaster = ?, public = ?, itemType = ?, level = ?, publicDescription = ?, privateDescription = ?, mudras = ? WHERE nameInPlugin = ?");

            pst.setString(1, name);
            pst.setBoolean(2, enabled);
            pst.setInt(3, manaCost);
            pst.setBoolean(4, needMastery);
            pst.setBoolean(5, needTarget);
            pst.setBoolean(6, skillVisibility);
            pst.setBoolean(7, canBeFullMaster);
            pst.setBoolean(8, _public);
            pst.setString(9, itemType);
            pst.setString(10, level);
            pst.setString(11, publicDescription);
            pst.setString(12, privateDescription);
            pst.setString(13, mudras);

            pst.setString(14, nameInPlugin);

            pst.executeUpdate();
            pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean isTechniqueInDb(String name) {
        boolean insert = false;
        try {
            PreparedStatement pst = Main.dbManager.getConnection().prepareStatement("SELECT id FROM Techniques WHERE name = ?");
            pst.setString(1, name);
            pst.executeQuery();

            ResultSet result = pst.getResultSet();
            insert = result.next();
            pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return insert;
    }

    public static Skill GetAllTechniques() {
        try {
            PreparedStatement pst = Main.dbManager.getConnection()
                    .prepareStatement("SELECT * FROM Techniques WHERE enabled = ?");

            pst.setBoolean(1, true);

            pst.executeQuery();
            ResultSet result = pst.getResultSet();

            while (result.next()) {
                String name = result.getString("name");
                String nameInPlugin = result.getString("nameInPlugin");
                int manaCost = result.getInt("manaCost");
                boolean needMastery = result.getBoolean("needMastery");
                SkillLevel level = SkillLevel.getByCharName(result.getString("level"));
                String publicDescription = result.getString("publicDescription");
                String privateDescription = result.getString("privateDescription");
                String mudras = result.getString("mudras");

                Material itemType = Material.getMaterial(result.getString("itemType")) != null ? Material.getMaterial(result.getString("itemType")) : Material.BOOK;
                boolean needTarget = result.getBoolean("needTarget");
                boolean canBeFullMaster = result.getBoolean("canBeFullMaster");
                String infoSup = "";
                boolean skillVisibility = result.getBoolean("skillVisibility");
                boolean _public = result.getBoolean("public");

                return new Skill(name, nameInPlugin, manaCost, needMastery, level, publicDescription, privateDescription, mudras, null, itemType, needTarget, canBeFullMaster, infoSup, skillVisibility, _public);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
