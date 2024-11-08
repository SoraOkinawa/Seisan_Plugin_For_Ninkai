package me.Seisan.plugin.Features.ability;

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
import java.util.*;
import java.util.logging.Level;

public class AbilityLoaderDB {

    public static void loadAllAbilitiesFromDB() {
        Main.LOG.info("Chargement des compétences depuis la base de données...");

        if (loadAllAbilities())
            Main.LOG.info("Chargement des compétences réussi.");
    }

    public static void saveInDb(String name, String nameInPlugin, String key, String description, String type, int lvl, String tagkey, String tagvalue, int pts, int ptsnec, String reqAbilities, String givenAbilities, String giveAbilities, Boolean giveAllowed, String givenJutsu, String lore) {
        if(name == null) name = "";
        if(nameInPlugin == null) nameInPlugin = "";
        if(key == null) key = "";
        if(type == null) type = "BOOK";
        if(tagkey == null) tagkey = "";
        if(tagvalue == null) tagvalue = "";
        if(description == null) description = "";
        // String reqAbilities, String givenAbilities, String giveAbilities, String lore
        if(reqAbilities == null) reqAbilities = "";
        if(givenAbilities == null) givenAbilities = "";
        if(giveAbilities == null) giveAbilities = "";
        if (giveAllowed == null) giveAllowed = false;
        if (givenJutsu == null) givenJutsu = "";
        if(lore == null) lore = "";

        if(isInsertDB(nameInPlugin)) {
            insertSkill(name, nameInPlugin, key, type, lvl, tagkey, tagvalue, description, pts, ptsnec, reqAbilities, givenAbilities, giveAbilities, giveAllowed, givenJutsu, lore);
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


    public static void insertSkill(String name, String nameInPlugin, String item, String type, int lvl, String tagkey, String tagvalue, String description, int pts, int ptsnec, String reqAbilities, String givenAbilities, String giveAbilities, Boolean giveAllowed, String givenJutsu, String lore) {
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

    public static boolean isInsertDB(String nameInPlugin){
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
