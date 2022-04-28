package me.Seisan.plugin.Features.data;

import lombok.Getter;
import me.Seisan.plugin.Features.PlayerData.Meditation;
import me.Seisan.plugin.Features.utils.ItemUtil;
import me.Seisan.plugin.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MeditationDB {
    @Getter
    private DBManager data;

    MeditationDB(DBManager data){
        this.data = data;
    }

    public static void insertMedit(String uuid, String inventory, int xborder, int zborder, double xspawn, double yspawn, double zspawn, boolean hasarea) {
        try{
            PreparedStatement pst = Main.dbManager.getConnection().prepareStatement("INSERT INTO MeditationArea(uuid, inventory, xborder, zborder, xspawn, yspawn, zspawn,hasarea) VALUES(?,?,?,?,?,?,?,?)");

            pst.setString(1, uuid); //UUID
            pst.setString(2, inventory); //inventory
            pst.setInt(3, xborder); // xborder
            pst.setInt(4, zborder); // zbordr
            pst.setDouble(5, xspawn); // xspawn
            pst.setDouble(6, yspawn); //yspawn
            pst.setDouble(7, zspawn); // zspawn
            pst.setBoolean(8, hasarea); // hasarea
            pst.executeUpdate();
            pst.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public boolean isInsertMedit(String uuid){
        boolean insert = false;
        try {
            PreparedStatement pst = this.data.getConnection()
                    .prepareStatement("SELECT id FROM MeditationArea WHERE uuid = ?");
            pst.setString(1, uuid);
            pst.executeQuery();
            ResultSet result = pst.getResultSet();
            insert = result.next();
            pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return insert;
    }

    public void updateMedit(Meditation meditation){
        try {
            PreparedStatement pst = data.getConnection().prepareStatement("UPDATE MeditationArea SET inventory = ?, xborder = ?, zborder = ?, xspawn = ? ,yspawn = ?, zspawn = ?, hasarea = ? WHERE uuid = ?");

            pst.setString(1, meditation.getInventory());
            pst.setInt(2, meditation.getXborder());
            pst.setInt(3, meditation.getZborder());
            pst.setDouble(4, meditation.getXspawn());
            pst.setDouble(5, meditation.getYspawn());
            pst.setDouble(6, meditation.getZspawn());
            pst.setBoolean(7, meditation.isHasmedit());

            pst.setString(8, meditation.getNameplayer());

            pst.executeUpdate();
            pst.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public Meditation getMeditation(String uuid) {
        if (!isInsertMedit(uuid)) {
            insertMedit(uuid, ItemUtil.itemStackArrayToBase64(new ItemStack[]{new ItemStack(Material.DIRT)}), -1, -1, -1, -1, -1, false);
        }
        try {
            PreparedStatement pst = this.data.getConnection()
                    .prepareStatement("SELECT * FROM MeditationArea WHERE uuid = ?");
            pst.setString(1, uuid);
            pst.executeQuery();
            ResultSet result = pst.getResultSet();
            if (result.next()) {
                String inventory = result.getString("inventory");
                int xborder = result.getInt("xborder");
                int zborder = result.getInt("zborder");
                double xspawn = result.getDouble("xspawn");
                double yspawn = result.getDouble("yspawn");
                double zspawn = result.getDouble("zspawn");
                boolean hasMedit = result.getBoolean("hasarea");
                return new Meditation(uuid, inventory, xborder, zborder, xspawn, yspawn, zspawn, hasMedit);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean hasMeditation(String uuid) {
        try {
            PreparedStatement pst = this.data.getConnection()
                    .prepareStatement("SELECT hasarea FROM MeditationArea WHERE uuid = ?");
            pst.setString(1, uuid);
            pst.executeQuery();
            ResultSet result = pst.getResultSet();
            if (result.next())
                return result.getBoolean("hasarea");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
