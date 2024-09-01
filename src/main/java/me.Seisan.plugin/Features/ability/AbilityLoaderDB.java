package me.Seisan.plugin.Features.ability;

import me.Seisan.plugin.Features.PlayerData.PlayerInfo;
import me.Seisan.plugin.Features.data.Config;
import me.Seisan.plugin.Features.skill.Skill;
import me.Seisan.plugin.Features.skill.SkillLevel;
import me.Seisan.plugin.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;

public class AbilityLoaderDB {

    public static void LoadAllAbilitiesFromDB() {
        Main.LOG.info("Chargement des compétences depuis la base de données...");

        if (loadAllAbilities())
            Main.LOG.info("Chargement des compétences réussi.");
    }

    public static void saveInDb(String name, String nameInPlugin, String key, String description, String type, int lvl, String tagkey, String tagvalue, int pts, int ptsnec, String reqAbilities, String givenAbilities, String giveAbilities, String lore) {
        if(name == null) name = "";
        if(nameInPlugin == null) nameInPlugin = "";
        if(key == null) key = "";
        if(description == null) description = "";
        if(type == null) type = "BOOK";
        if(tagkey == null) tagkey = "";
        if(tagvalue == null) tagvalue = "";
        // String reqAbilities, String givenAbilities, String giveAbilities, String lore
        if(reqAbilities == null) reqAbilities = "";
        if(givenAbilities == null) givenAbilities = "";
        if(giveAbilities == null) giveAbilities = "";
        if(lore == null) lore = "";

        if(isInsertBDD(nameInPlugin)) {
            insertSkill(name, nameInPlugin, key, description, type, lvl, tagkey, tagvalue, pts, ptsnec, reqAbilities, givenAbilities, giveAbilities, lore);
        }
    }

    private static boolean loadAllAbilities() {
        try {
            PreparedStatement pst = Main.dbManager.getConnection()
                    .prepareStatement("SELECT * FROM Abilities");

            pst.executeQuery();
            ResultSet result = pst.getResultSet();

            while (result.next()) {
                String name = result.getString("name");
                String nameInPlugin = result.getString("nameInPlugin");
                Material itemType = (Material.getMaterial(result.getString("itemType")) != null) ? Material.getMaterial(result.getString("itemType")) : Material.BOOK;
                String description = result.getString("description");
                String type = result.getString("type");
                int lvl = result.getInt("lvl");
                String tagkey = result.getString("tagkey");
                String tagvalue = result.getString("tagvalue");
                int pts = result.getInt("pts");
                int ptsnec = result.getInt("ptsnec");
                String reqAbilities = result.getString("reqAbilities");
                String givenAbilities = result.getString("givenAbilities");
                String giveAbilities = result.getString("giveAbilities");
                String lore = result.getString("lore");
                boolean giveAllowed = result.getBoolean("giveAllowed");
                int resistance = result.getInt("resistance");
                String givenJutsu = result.getString("givenJutsu");

                new Ability(name, nameInPlugin, itemType, description, type, lvl, tagkey, tagvalue, pts, ptsnec, reqAbilities, givenAbilities, giveAbilities, lore, giveAllowed, resistance, givenJutsu);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Main.LOG.log(Level.SEVERE, "Chargement des compétences échoué. Veuillez vérifier la console pour plus d'informations.");
            return false;
        }
        return true;
    }


    public static void insertSkill(String name, String nameInPlugin, String key, String description, String type, int lvl, String tagkey, String tagvalue, int pts, int ptsnec, String reqAbilities, String givenAbilities, String giveAbilities, String lore) {
        try{
            PreparedStatement pst = Main.dbManager.getConnection().prepareStatement("INSERT INTO Skills(name, nameInPlugin, itemType, description, type, lvl, tagkey, tagvalue, pts, ptsnec, reqAbilities, givenAbilities, giveAbilities, lore) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

            pst.setString(1, name); //UUID
            pst.setString(2, nameInPlugin); //Mana
            pst.setString(3, key); // Manamission
            pst.setString(4, description); // Manamaze
            pst.setString(5, type); //CurrentSkill
            pst.setInt(6, lvl); //SkilList
            pst.setString(7, tagkey); //Rank
            pst.setString(8, tagvalue); //DisconnectTime
            //pts, ptsnec, reqAbilities, givenAbilities, giveAbilities, lore
            pst.setInt(9, pts); //RollBonus
            pst.setInt(10, ptsnec); //Clan
            pst.setString(11, reqAbilities); //Chakra Type
            pst.setString(12, givenAbilities); //Age
            pst.setString(13, giveAbilities); //Apparence
            pst.setString(14, lore); // Voie Ninja
            pst.executeUpdate();
            pst.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static boolean isInsertBDD(String nameInPlugin){
        boolean insert = false;
        try {
            PreparedStatement pst = Main.dbManager.getConnection()
                    .prepareStatement("SELECT id FROM Skills WHERE nameInPlugin = ?");
            pst.setString(1, nameInPlugin);
            pst.executeQuery();
            ResultSet result = pst.getResultSet();
            insert = result.next();
            pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return !insert;
    }

    public static void reloadAllAbility(CommandSender p){
        HashMap<String, PlayerInfo> playerInfoList = (HashMap<String, PlayerInfo>) PlayerInfo.getInstanceList().clone();

        if(p != null)
            p.sendMessage(ChatColor.GRAY + "Sauvegarde des compétences des joueurs connectés...");

        HashMap<String, String> savedAbilities = new HashMap<>();
        for(PlayerInfo pInfo : playerInfoList.values()) {
            String s = "";
            List<Ability> abilityList = pInfo.getAbilities();
            // si c'est un inventaire personnalisé
            if (pInfo.getPlayer().getOpenInventory().getTitle().startsWith("§")) {
                pInfo.getPlayer().closeInventory();
            }

            if(abilityList.size() > 0) {
                /* On enchaine le nom des compétences avec nom;nom2; */
                for (Ability ability : abilityList) {
                    if(ability != null && ability.getNameInPlugin() != null) {
                        s = s.concat(ability.getNameInPlugin() + ";");
                    }
                }

                if (s.length() > 0) {
                    s = s.substring(0, s.length() - 1);
                    savedAbilities.put(pInfo.getPlayer().getName(), s);
                }
            }
        }

        if(p != null)
            p.sendMessage(ChatColor.GREEN + "Compétences des joueurs connectés sauvegardés !\n" + ChatColor.GRAY + "Rechargement des compétences...");

        Ability.getInstanceList().clear();
        loadAllAbilities();

        if(p != null)
            p.sendMessage(ChatColor.GREEN + "Compétences sauvegardés ! \n" + ChatColor.GRAY + "Restitution des compétences aux joueurs...");

        for(Map.Entry<String, String> entry : savedAbilities.entrySet()){
            String playerName = entry.getKey();
            String playerAbilities  = entry.getValue();

            Player player = Bukkit.getPlayer(playerName);

            ArrayList<Ability> abilityList = new ArrayList<>();

            if(player != null){
                PlayerInfo pInfo = PlayerInfo.getPlayerInfo(player);


                for(String abilityName : playerAbilities.split(";")){
                    Ability ability = Ability.getByPluginName(abilityName);
                    if(ability != null){
                        abilityList.add(ability);
                    }
                }

                pInfo.setAbilities(abilityList);
            }
        }

        if(p!=null) {
            p.sendMessage(ChatColor.GREEN + "Fin du rechargement des compétences !");
        }
    }
}
