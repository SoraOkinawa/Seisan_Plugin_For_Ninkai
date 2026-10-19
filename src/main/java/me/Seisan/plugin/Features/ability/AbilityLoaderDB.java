package me.Seisan.plugin.Features.ability;

import me.Seisan.plugin.Features.PlayerData.PlayerInfo;
import me.Seisan.plugin.Features.data.Config;
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

    public static void loadAllAbilitiesFromDB() {
        Main.LOG.info("Chargement des compétences depuis la base de données...");

        if (loadAll())
            Main.LOG.info("Chargement des compétences réussi.");
    }
    
    /**
     * Met à jour une compétence si elle est déjà dans la BDD. Sinon, elle l'insère.
     * @param name
     * @param nameInPlugin
     * @param item
     * @param type
     * @param lvl
     * @param tagkey
     * @param tagvalue
     * @param description
     * @param pts
     * @param ptsnec
     * @param reqAbilities
     * @param givenAbilities
     * @param giveAbilities
     * @param giveAllowed
     * @param givenJutsu
     * @param lore
     * @return true si la compétence a été update, false si elle a été insert
     */
    public static boolean updateOrInsert(String name, String nameInPlugin, String item, String type, int lvl, String tagkey, String tagvalue, String description, int pts, int ptsnec, String reqAbilities, String givenAbilities, String giveAbilities, Boolean giveAllowed, String givenJutsu, String lore) {
        if (isInserted(nameInPlugin)) {
            update(name, nameInPlugin, item, type, lvl, tagkey, tagvalue, description, pts, ptsnec, reqAbilities, givenAbilities, giveAbilities, giveAllowed, givenJutsu, lore);
            Main.LOG.info("Compétence " + name + " mise à jour.");
            return true;
        }
        else {
            insert(name, nameInPlugin, item, type, lvl, tagkey, tagvalue, description, pts, ptsnec, reqAbilities, givenAbilities, giveAbilities, giveAllowed, givenJutsu, lore);
            Main.LOG.info("Compétence " + name + " ajoutée.");
            return false;
        }
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
                    .prepareStatement("SELECT * FROM Abilities");
            
            pst.executeQuery();
            ResultSet result = pst.getResultSet();
            
            while (result.next()) {
                String name = result.getString("name");
                String nameInPlugin = result.getString("nameInPlugin");
                Material item = (Material.getMaterial(result.getString("item")) != null) ? Material.getMaterial(result.getString("item")) : Material.BOOK;
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
                String givenJutsu = result.getString("givenJutsu");
                
                new Ability(name, nameInPlugin, item, description, type, lvl, tagkey, tagvalue, pts, ptsnec, reqAbilities, givenAbilities, giveAbilities, lore, giveAllowed, givenJutsu);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Main.LOG.log(Level.SEVERE, "Chargement des compétences échoué. Veuillez vérifier la console pour plus d'informations.");
            return false;
        }
        return true;
    }

    public static void reloadAll(CommandSender p){
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
        loadAll();

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
    
    public static void createTable() {
        try {
            PreparedStatement pst = Main.dbManager.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Abilities(" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "name TEXT NOT NULL," +
                    "nameInPlugin TEXT NOT NULL," +
                    "item TEXT NOT NULL," +
                    "type TEXT NOT NULL," +
                    "lvl INT(11) NOT NULL," +
                    "tagkey TEXT NOT NULL," +
                    "tagvalue TEXT NOT NULL," +
                    "description TEXT NOT NULL," +
                    "pts INT(11) NOT NULL," +
                    "ptsnec INT(11) NOT NULL," +
                    "reqAbilities TEXT NOT NULL," +
                    "givenAbilities TEXT NOT NULL," +
                    "giveAbilities TEXT NOT NULL," +
                    "giveAllowed TINYINT(1) NOT NULL," +
                    "givenJutsu TEXT NOT NULL," +
                    "lore TEXT NOT NULL)");
            pst.executeUpdate();
            pst.close();
        } catch (SQLException e) {
	        e.printStackTrace();
        }
    }
    
    public static boolean insertToDBFromConfig(Config abilityConfig, ArrayList<String> enabledAbility) {
        for(String abilityName : enabledAbility){
            try {
                String name = abilityConfig.getString(abilityName + ".name");
                String nameInPlugin = abilityConfig.getString(abilityName + ".nameInPlugin");
                String itemType = abilityConfig.getString(abilityName + ".itemType");
                String type = abilityConfig.getString(abilityName+".type");
                String description = abilityConfig.getString(abilityName+".description");
                int lvl = abilityConfig.getInt(abilityName+".lvl");
                String tagkey = abilityConfig.getString(abilityName+".tagkey");
                String tagvalue = abilityConfig.getString(abilityName+".tagvalue");
                int pts = abilityConfig.getInt(abilityName+".pts");
                int ptsnec = abilityConfig.getInt(abilityName+".ptsnec");
                String reqAbilities = abilityConfig.getString(abilityName+".reqAbilities");
                String givenAbilities = abilityConfig.getString(abilityName+".givenAbilities");
                String giveAbilities = abilityConfig.getString(abilityName+".giveAbilities");
                String lore = abilityConfig.getString(abilityName+".lore");
                boolean giveAllowed = abilityConfig.getBoolean(abilityName+".giveAllowed");
                String givenJutsu = abilityConfig.getString(abilityName+".givenJutsu");

                updateOrInsert(name, nameInPlugin, itemType, type, lvl, tagkey, tagvalue, description, pts, ptsnec, reqAbilities, givenAbilities, giveAbilities, giveAllowed, givenJutsu, lore);
            }catch (Exception e){
                Main.LOG.info("La compétence " + abilityName + " est mal configurée ! Explication de l'erreur:");
                e.printStackTrace();
                return false;
            }
        }
        
        return true;
    }
}
