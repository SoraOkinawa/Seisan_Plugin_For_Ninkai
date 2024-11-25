package me.Seisan.plugin.Features.skill;

import me.Seisan.plugin.Features.PlayerData.PlayerInfo;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class TechniquesLoaderDB {
    public static void LoadAllTechniquesFromDB() {
        Main.LOG.info("Chargement des jutsu en base de données...");
        
        if (loadAll())
            Main.LOG.info("Chargement des techniques réussi.");
    }
    
    public static void insertOrUpdate(String name, String nameInPlugin, String category, boolean enabled, int manaCost, boolean needMastery, boolean needTarget, boolean skillVisibility, boolean canBeFullMaster, boolean _public, String itemType, String level, String message, String infoSup, String lore, String mudras, String commandList) {
        if (isInserted(nameInPlugin))
            update(name, nameInPlugin, category, enabled, manaCost, needMastery, needTarget, skillVisibility, canBeFullMaster, _public, itemType, level, message, infoSup, lore, mudras, commandList);
        else
            insert(name, nameInPlugin, category, enabled, manaCost, needMastery, needTarget, skillVisibility, canBeFullMaster, _public, itemType, level, message, infoSup, lore, mudras, commandList);
    }
    
    public static void insert(String name, String nameInPlugin, String category, boolean enabled, int manaCost, boolean needMastery, boolean needTarget, boolean skillVisibility, boolean canBeFullMaster, boolean _public, String itemType, String level, String message, String infoSup, String lore, String mudras, String commandList) {
        try {
            PreparedStatement pst = Main.dbManager.getConnection().prepareStatement("INSERT INTO Techniques(name, nameInPlugin, category, enabled, manaCost, needMastery, needTarget, skillVisibility, canBeFullMaster, public, itemType, level, message, infoSup, lore, mudras, commandList) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
    
            pst.setString(1, name);
            pst.setString(2, nameInPlugin);
            pst.setString(3, category);
            pst.setBoolean(4, enabled);
            pst.setInt(5, manaCost);
            pst.setBoolean(6, needMastery);
            pst.setBoolean(7, needTarget);
            pst.setBoolean(8, skillVisibility);
            pst.setBoolean(9, canBeFullMaster);
            pst.setBoolean(10, _public);
            pst.setString(11, itemType);
            pst.setString(12, level);
            pst.setString(13, message);
            pst.setString(14, infoSup);
            pst.setString(15, lore);
            pst.setString(16, mudras);
            pst.setString(17, commandList);
    
            pst.executeUpdate();
            pst.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void update(String name, String nameInPlugin, String category, boolean enabled, int manaCost, boolean needMastery, boolean needTarget, boolean skillVisibility, boolean canBeFullMaster, boolean _public, String itemType, String level, String message, String infoSup, String lore, String mudras, String commandList) {
        try {
            PreparedStatement pst = Main.dbManager.getConnection().prepareStatement("UPDATE Techniques SET name = ?, category = ?, enabled = ?, manaCost = ?, needMastery = ?, needTarget = ?, skillVisibility = ?, canBeFullMaster = ?, public = ?, itemType = ?, level = ?, message = ?, infoSup = ?, lore = ?, mudras = ?, commandList = ? WHERE nameInPlugin = ?");

            pst.setString(1, name);
            pst.setString(2, category);
            pst.setBoolean(3, enabled);
            pst.setInt(4, manaCost);
            pst.setBoolean(5, needMastery);
            pst.setBoolean(6, needTarget);
            pst.setBoolean(7, skillVisibility);
            pst.setBoolean(8, canBeFullMaster);
            pst.setBoolean(9, _public);
            pst.setString(10, itemType);
            pst.setString(11, level);
            pst.setString(12, message);
            pst.setString(13, infoSup);
            pst.setString(14, lore);
            pst.setString(15, mudras);
            pst.setString(16, commandList);
            pst.setString(17, nameInPlugin);

            pst.executeUpdate();
            pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean isInserted(String nameInPlugin) {
        boolean insert = false;
        try {
            PreparedStatement pst = Main.dbManager.getConnection().prepareStatement("SELECT id FROM Techniques WHERE nameInPlugin = ?");
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

    public static boolean loadAll() {
        try {
            PreparedStatement pst = Main.dbManager.getConnection()
                    .prepareStatement("SELECT * FROM Techniques WHERE enabled = ?");

            pst.setBoolean(1, true);

            pst.executeQuery();
            ResultSet result = pst.getResultSet();

            while (result.next()) {
                String name = result.getString("name");
                String nameInPlugin = result.getString("nameInPlugin");
                String category = result.getString("category");
                int manaCost = result.getInt("manaCost");
                boolean needMastery = result.getBoolean("needMastery");
                SkillLevel level = SkillLevel.getByCharName(result.getString("level"));
                String message = result.getString("message");
                String lore = result.getString("lore");
                String mudras = result.getString("mudras");
                
                String[] commandArray = result.getString("commandList").split(";");
                ArrayList<String> commandList = new ArrayList<>(Arrays.asList(commandArray));
    
                Material itemType = Material.getMaterial(result.getString("itemType")) != null ? Material.getMaterial(result.getString("itemType")) : Material.BOOK;
                boolean needTarget = result.getBoolean("needTarget");
                boolean canBeFullMaster = result.getBoolean("canBeFullMaster");
                String infoSup = result.getString("infoSup");
                boolean skillVisibility = result.getBoolean("skillVisibility");
                boolean _public = result.getBoolean("public");
                
                new Skill(name, nameInPlugin, category, manaCost, needMastery, level, message, lore, mudras, commandList, itemType, needTarget, canBeFullMaster, infoSup, skillVisibility, _public);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Main.LOG.log(Level.SEVERE, "Chargement des techniques échoué. Veuillez vérifier la console pour plus d'informations.");
            return false;
        }
        return true;
    }

    public static void reloadAll(CommandSender p) {
        HashMap<String, PlayerInfo> playerInfoList = (HashMap<String,PlayerInfo>) PlayerInfo.getInstanceList().clone();

        SkillManager.setSkillEnabled(false);
        if(p != null)
            p.sendMessage(ChatColor.GRAY + "Sauvegarde des jutsus des joueurs connectés...");

        HashMap<String, String> savedSkills = new HashMap<>();
        for(PlayerInfo pInfo : playerInfoList.values()){
            String s = "";
            HashMap<Skill, SkillMastery> skillList = pInfo.getSkills();
            if (pInfo.getPlayer().getOpenInventory().getTitle().startsWith("§")) {
                pInfo.getPlayer().closeInventory();
            }

            if(skillList.size() > 0) {
                for (Skill skill : skillList.keySet()) {
                    SkillMastery mastery = skillList.get(skill);

                    s = s.concat(skill.getNameInPlugin() + "," + mastery.getId() + ";");
                }

                if (s.length() > 0)
                    s = s.substring(0, s.length() - 1);

                s = s + "&";

                for (Skill skill : pInfo.getFavoriteList()) {
                    s = s.concat(skill.getNameInPlugin() + ",");
                }

                if (s.length() > 0) {
                    s = s.substring(0, s.length() - 1);
                    savedSkills.put(pInfo.getPlayer().getName(), s);
                }
            }
        }

        if(p != null)
            p.sendMessage(ChatColor.GREEN + "Jutsus des joueurs connectés sauvegardés !\n" + ChatColor.GRAY + "Rechargement des jutsus...");

        Skill.getInstanceList().clear();
        loadAll();

        if(p != null)
            p.sendMessage(ChatColor.GREEN + "Jutsus rechargés ! \n" + ChatColor.GRAY + "Restitution des jutsus aux joueurs...");

        for(Map.Entry<String, String> entry : savedSkills.entrySet()){
            String playerName = entry.getKey();
            String playerSkills = entry.getValue();

            Player player = Main.plugin().getServer().getPlayer(playerName);

            HashMap<Skill, SkillMastery> skillList = new HashMap<>();
            ArrayList<Skill> favoriteList = new ArrayList<>();

            if(player != null){
                PlayerInfo pInfo = PlayerInfo.getPlayerInfo(player);

                String skillListString = playerSkills;
                String favoriteListString = "";

                if(playerSkills.contains("&")) {
                    skillListString = playerSkills.split("&")[0];
                    favoriteListString = playerSkills.split("&")[1];
                }

                for(String skillString : skillListString.split(";")){
                    String skillName = skillString.split(",")[0];
                    String skillMastery = skillString.split(",")[1];

                    Skill skill = Skill.getByPluginName(skillName);
                    SkillMastery mastery = SkillMastery.getById(Integer.parseInt(skillMastery));
                    if(skill != null){
                        skillList.put(skill, mastery);
                    }
                }

                for(String skillName : favoriteListString.split(",")){
                    Skill skill = Skill.getByPluginName(skillName);
                    if(skill != null){
                        favoriteList.add(skill);
                    }
                }

                pInfo.setSkills(skillList);
                pInfo.setFavoriteList(favoriteList);
                if(Main.getCurrentSelectSkill().containsKey(player.getName())) {
                    Bukkit.getScheduler().cancelTask(Main.getCurrentSelectSkill().get(player.getName()));
                    Main.getCurrentSelectSkill().remove(player.getName());
                    pInfo.setCurrentSkill(null);
                }
            }
        }

        p.sendMessage(ChatColor.GREEN + "Fin du rechargement des jutsus !");
        SkillManager.setSkillEnabled(true);
    }
}
