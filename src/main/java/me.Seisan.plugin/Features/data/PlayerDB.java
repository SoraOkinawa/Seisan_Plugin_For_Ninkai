package me.Seisan.plugin.Features.data;

import me.Seisan.plugin.Features.PlayerData.PlayerInfo;
import me.Seisan.plugin.Features.ability.Ability;
import me.Seisan.plugin.Features.objectnum.Clan;
import me.Seisan.plugin.Features.objectnum.RPRank;
import me.Seisan.plugin.Features.objectnum.ArtNinja;
import me.Seisan.plugin.Features.objectnum.CouleurChakra;
import me.Seisan.plugin.Features.objectnum.Teinte;
import me.Seisan.plugin.Features.objectnum.ChakraType;
import me.Seisan.plugin.Features.objectnum.Gender;
import me.Seisan.plugin.Features.skill.Skill;
import me.Seisan.plugin.Features.skill.SkillMastery;
import me.Seisan.plugin.Main;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by Helliot on 16/03/2018.
 */
public class PlayerDB {

    private DBManager data;

    PlayerDB(DBManager data){
        this.data = data;
    }

    public void insertPlayer(String uuid){
        try{
            PreparedStatement pst = data.getConnection().prepareStatement("INSERT INTO PlayerInfo(uuid, mana, manamission, manamaze, manabonus, currentSkill, knownSkills, rang, disconnectTime, rollBonus, clan, chakratype, age, appearence, voieNinja, styleCombat, abilities, aprofil, attributClan, points, pointsAbilities, delayPoints, resistance, ink, couleur, teint, oldpos, gender, fuin_paper, fuin_uzumaki, fuin_lastday, maskprofil, ticketmedit, minmedit, delayticketmedit, reduc_ninjutsu) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

            pst.setString(1, uuid); //UUID
            pst.setInt(2, 100); //Mana
            pst.setInt(3, 0); // Manamission
            pst.setInt(4, 0); // Manamaze
            pst.setInt(5, 0); // Manabonus
            pst.setString(6, null); //CurrentSkill
            pst.setString(7, null); //SkilList
            pst.setInt(8, 0); //Rank
            pst.setInt(9, 0); //DisconnectTime
            pst.setString(10, null); //RollBonus
            pst.setInt(11, -1); //Clan
            pst.setString(12, ""); //Chakra Type
            pst.setInt(13, 0); //Age
            pst.setString(14, ""); //Apparence
            pst.setInt(15, -1); // Voie Ninja
            pst.setInt(16, -1); // Style de combat
            pst.setString(17, "force_1;vitesse_1;perception_vitesse_3;"); // Abilities
            pst.setString(18, ""); // A profil
            pst.setString(19, ""); // Attribut de clan
            pst.setInt(20, 0); // Points
            pst.setString(21, ""); // Point abilities
            pst.setLong(22, PlayerInfo.getNextDimanche(LocalDateTime.now())); // Dernier diamanche
            pst.setString(23, ""); // Resistance
            pst.setInt(24, 0); // Ink
            pst.setInt(25, 0); // Couleur
            pst.setInt(26, 0); // Teinte
            pst.setString(27, ""); // Oldpos
            pst.setInt(28, -1); // Genre
            pst.setInt(29, 0); // Fuin_paper actuels
            pst.setInt(30, 0); // Fuin_uzumaki
            pst.setInt(31, PlayerInfo.getLastDay(LocalDateTime.now())); // Fuin_lastday
            pst.setInt(32, 0); // Maskprofil
            pst.setInt(33, 0); // ticket medit
            pst.setInt(34, 0); // minute passé dans le /medit
            pst.setInt(35, PlayerInfo.getNextThreeDays(LocalDateTime.now())); // delay ticket medit
            pst.setInt(36, 0); // delay ticket medit
            pst.executeUpdate();
            pst.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public boolean isInsert(String uuid){
        boolean insert = false;
        try {
            PreparedStatement pst = this.data.getConnection()
                    .prepareStatement("SELECT id FROM PlayerInfo WHERE uuid = ?");
            pst.setString(1, uuid);
            pst.executeQuery();
            ResultSet result = pst.getResultSet();
            insert = result.next();
            pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return !insert;
    }

    public void updatePlayer(PlayerInfo pInfo){
        String uuid = pInfo.getUuid();
        Main.getIsSaving().add(pInfo.getPlayer().getName());
        Bukkit.getScheduler().runTaskAsynchronously(Main.plugin(), () -> {
            try {
                PreparedStatement pst = data.getConnection().prepareStatement("UPDATE PlayerInfo SET mana = ?, manamission = ?, manamaze = ?, manabonus = ?, currentSkill = ?, knownSkills = ?, rang = ?, disconnectTime = ?, rollBonus = ?, favoriteSkills = ?, clan = ?, chakratype = ?, age = ?, appearence = ?, voieNinja = ?, styleCombat = ?, abilities = ?, aprofil = ?, attributClan = ?, points = ?, pointsAbilities = ?, delayPoints = ?, resistance = ?, ink = ?, couleur = ?, teint = ?, oldpos = ?, gender = ?, fuin_paper = ?, fuin_uzumaki = ?, fuin_lastday = ?, maskprofil = ?, ticketmedit = ?, minmedit = ?, delayticketmedit = ?, reduc_ninjutsu = ? WHERE uuid = ?");

                pst.setInt(1, pInfo.getMana());
                pst.setInt(2, pInfo.getNbmission());
                pst.setInt(3, pInfo.getManamaze());
                pst.setInt(4, pInfo.getManaBonus());
                if(pInfo.getCurrentSkill() != null)
                    pst.setString(5, pInfo.getCurrentSkill().getNameInPlugin());
                else
                    pst.setString(5, null);

                pst.setString(6, skillMapToString(pInfo));
                pst.setInt(7, pInfo.getRank().getId());
                pst.setLong(8, System.currentTimeMillis());
                pst.setString(9, rollBonusMapToString(pInfo));
                pst.setString(10, favoriteListToString(pInfo));
                pst.setInt(11, pInfo.getClan().getId());
                pst.setString(12, chakraMapToString(pInfo)); //Removing color code
                pst.setInt(13, pInfo.getAge());
                pst.setString(14, pInfo.getAppearance());
                pst.setInt(15, pInfo.getVoieNinja().getId());
                pst.setInt(16, pInfo.getStyleCombat().getId());
                pst.setString(17, abilitiesListToString(pInfo));
                pst.setString(18, pInfo.getApparenceprofil());
                pst.setString(19, pInfo.getAttributClan());
                pst.setInt(20, pInfo.getPoints());
                pst.setString(21, pInfo.getPointsAbilities());
                pst.setLong(22, pInfo.getDelayPoints()); //Delay A
                pst.setString(23, resMapToString(pInfo));
                pst.setInt(24, pInfo.getInk());
                pst.setInt(25, pInfo.getCouleurChakra().getId());
                pst.setInt(26, pInfo.getTeinte().getId());
                pst.setString(27, pInfo.getOldpos());
                pst.setInt(28, pInfo.getGender().getId());
                pst.setInt(29, pInfo.getFuin_paper());
                pst.setInt(30, pInfo.getFuin_uzumaki());
                pst.setInt(31, pInfo.getFuin_lastday());
                pst.setInt(32, pInfo.getMaskprofil());
                pst.setInt(33, pInfo.getTicketmedit());
                pst.setInt(34, pInfo.getMinmedit());
                pst.setInt(35, pInfo.getDelayTicketMedit());
                pst.setInt(36, pInfo.getReduc_ninjutsu());
                pst.setString(37, uuid);

                pst.executeUpdate();
                pst.close();
                Main.getIsSaving().remove(pInfo.getPlayer().getName());
            }catch (SQLException e){
                e.printStackTrace();
            }
        });
    }

    public ResultSet getPlayerInfo(String uuid) {
        try {
            PreparedStatement pst = this.data.getConnection()
                    .prepareStatement("SELECT * FROM PlayerInfo WHERE uuid = ?");
            pst.setString(1, uuid);
            pst.executeQuery();
            ResultSet result = pst.getResultSet();
            if (result.next())
                return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void loadPlayerFiche() {
        try {
            PreparedStatement pst = this.data.getConnection()
                    .prepareStatement("SELECT * FROM PlayerFiche");
            pst.executeQuery();
            ResultSet result = pst.getResultSet();
            while (result.next()) {
                String name = result.getString("name");
                Main.getFicheMJ().put(name, loadPlayerInfo(name, result));
            }
            System.out.println(Main.getFicheMJ().size()+" fiches personnages ont été chargées.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isInsertFichePerso(String id){
        boolean insert = false;
        try {
            PreparedStatement pst = this.data.getConnection()
                    .prepareStatement("SELECT id FROM PlayerFiche WHERE name = ?");
            pst.setString(1, id);
            pst.executeQuery();
            ResultSet result = pst.getResultSet();
            insert = result.next();
            pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return insert;
    }

    public void saveFichePerso() {
        for(PlayerInfo pInfo : Main.getFicheMJ().values()) {
            if (!isInsertFichePerso(pInfo.getId())) {
                insertFichePerso(pInfo.getId());
            }

            try {
                PreparedStatement pst = data.getConnection().prepareStatement("UPDATE PlayerFiche SET mana = ?, manamission = ?, manamaze = ?, manabonus = ?, currentSkill = ?, knownSkills = ?, rang = ?, disconnectTime = ?, rollBonus = ?, favoriteSkills = ?, clan = ?, chakratype = ?, age = ?, appearence = ?, voieNinja = ?, styleCombat = ?, abilities = ?, aprofil = ?, attributClan = ?, points = ?, pointsAbilities = ?, delayPoints = ?, resistance = ?, ink = ?, couleur = ?, teint = ?, oldpos = ?, gender = ?, fuin_paper = ?, fuin_uzumaki = ?, fuin_lastday = ?, maskprofil = ?, ticketmedit = ?, minmedit = ?, delayticketmedit = ?, reduc_ninjutsu = ? WHERE name = ?");

                pst.setInt(1, pInfo.getMana());
                pst.setInt(2, pInfo.getNbmission());
                pst.setInt(3, pInfo.getManamaze());
                pst.setInt(4, pInfo.getManaBonus());
                if (pInfo.getCurrentSkill() != null)
                    pst.setString(5, pInfo.getCurrentSkill().getNameInPlugin());
                else
                    pst.setString(5, null);

                pst.setString(6, skillMapToString(pInfo));
                pst.setInt(7, pInfo.getRank().getId());
                pst.setLong(8, System.currentTimeMillis());
                pst.setString(9, rollBonusMapToString(pInfo));
                pst.setString(10, favoriteListToString(pInfo));
                pst.setInt(11, pInfo.getClan().getId());
                pst.setString(12, chakraMapToString(pInfo)); //Removing color code
                pst.setInt(13, pInfo.getAge());
                pst.setString(14, pInfo.getAppearance());
                pst.setInt(15, pInfo.getVoieNinja().getId());
                pst.setInt(16, pInfo.getStyleCombat().getId());
                pst.setString(17, abilitiesListToString(pInfo));
                pst.setString(18, pInfo.getApparenceprofil());
                pst.setString(19, pInfo.getAttributClan());
                pst.setInt(20, pInfo.getPoints());
                pst.setString(21, pInfo.getPointsAbilities());
                pst.setLong(22, pInfo.getDelayPoints()); //Delay A
                pst.setString(23, resMapToString(pInfo));
                pst.setInt(24, pInfo.getInk());
                pst.setInt(25, pInfo.getCouleurChakra().getId());
                pst.setInt(26, pInfo.getTeinte().getId());
                pst.setString(27, pInfo.getOldpos());
                pst.setInt(28, pInfo.getGender().getId());
                pst.setInt(29, pInfo.getFuin_paper());
                pst.setInt(30, pInfo.getFuin_uzumaki());
                pst.setInt(31, pInfo.getFuin_lastday());
                pst.setInt(32, pInfo.getMaskprofil());
                pst.setInt(33, pInfo.getTicketmedit());
                pst.setInt(34, pInfo.getMinmedit());
                pst.setInt(35, pInfo.getTicketmedit());
                pst.setInt(36, pInfo.getReduc_ninjutsu());
                pst.setString(37, pInfo.getId());

                pst.executeUpdate();
                pst.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void insertFichePerso(String id) {
        try{
            PreparedStatement pst = data.getConnection().prepareStatement("INSERT INTO PlayerFiche(name, mana, manamission, manamaze, manabonus, currentSkill, knownSkills, `rang`, disconnectTime, rollBonus, clan, chakratype, age, appearence, voieNinja, styleCombat, abilities, aprofil, attributClan, points, pointsAbilities, delayPoints, resistance, ink, couleur, teint, oldpos, gender, fuin_paper, fuin_uzumaki, fuin_lastday, maskprofil, ticketmedit, minmedit, delayticketmedit, reduc_ninjutsu) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

            pst.setString(1, id); //UUID
            pst.setInt(2, 100); //Mana
            pst.setInt(3, 0); // Manamission
            pst.setInt(4, 0); // Manamaze
            pst.setInt(5, 0); // Manamaze
            pst.setString(6, null); //CurrentSkill
            pst.setString(7, null); //SkilList
            pst.setInt(8, 0); //Rank
            pst.setInt(9, 0); //DisconnectTime
            pst.setString(10, null); //RollBonus
            pst.setInt(11, -1); //Clan
            pst.setString(12, ""); //Chakra Type
            pst.setInt(13, 0); //Age
            pst.setString(14, ""); //Apparence
            pst.setInt(15, -1); // Voie Ninja
            pst.setInt(16, -1); // Style de combat
            pst.setString(17, "force_1;vitesse_1;perception_vitesse_3;"); // Abilities
            pst.setString(18, ""); // A profil
            pst.setString(19, ""); // Attribut de clan
            pst.setInt(20, 0); // Points
            pst.setString(21, ""); // Point abilities
            pst.setLong(22, PlayerInfo.getNextDimanche(LocalDateTime.now())); // Dernier diamanche
            pst.setString(23, ""); // Resistance
            pst.setInt(24, 0); // Ink
            pst.setInt(25, 0); // Couleur
            pst.setInt(26, 0); // Teinte
            pst.setString(27, ""); // Oldpos
            pst.setInt(28, -1); // Genre
            pst.setInt(29, 0); // Fuin_paper actuels
            pst.setInt(30, 0); // Fuin_uzumaki
            pst.setInt(31, PlayerInfo.getLastDay(LocalDateTime.now())); // Fuin_lastday
            pst.setInt(32, 0); // Maskprofil
            pst.setInt(33, 0); // ticket medit
            pst.setInt(34, 0); // minmedit
            pst.setInt(35, 0); // delayticketmedit
            pst.setInt(36, 0); // reduc_ninjutsu
            pst.executeUpdate();
            pst.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    private PlayerInfo loadPlayerInfo(String name, ResultSet set) throws SQLException {
        int mana = set.getInt("mana");
        int manamission = set.getInt("manamission");
        int manamaze = set.getInt("manamaze");
        int manabonus = set.getInt("manabonus");
        RPRank rank = RPRank.getById(set.getInt("rang"));
        HashMap<Skill, SkillMastery> skills = stringToSkillMap(set.getString("knownSkills"));
        HashMap<Skill, Integer> rollBonus = stringToRollBonusMap(set.getString("rollBonus"));
        ArrayList<Skill> favoriteList = stringToFavoriteList(set.getString("favoriteSkills"));
        Clan clan = Clan.getFromID(set.getInt("clan"));
        HashMap<ChakraType, Integer> chakraType = stringToChakraMap(set.getString("chakratype"));
        int age = set.getInt("age");
        String apparence = set.getString("appearence");
        ArtNinja voieNinja = ArtNinja.getFromID(set.getInt("voieNinja"));
        ArtNinja styleCombat = ArtNinja.getFromID(set.getInt("styleCombat"));
        ArrayList<Ability> abilities = stringToAbilitiesList(set.getString("abilities"));
        String apparenceprofil = set.getString("aprofil");
        String attributClan = set.getString("attributClan");
        int points = set.getInt("points");
        String pointsAbilities = set.getString("pointsAbilities");
        long delayPoints = set.getLong("delayPoints");
        HashMap<String, Integer> resistance = stringToResistance(set.getString("resistance"));
        int ink = set.getInt("ink");
        CouleurChakra couleurChakra = CouleurChakra.getFromID(set.getInt("couleur"));
        Teinte teinte = Teinte.getFromID(set.getInt("teint"));
        String oldpos = set.getString("oldpos");
        Gender gender = Gender.getFromID(set.getInt("gender"));
        int fuin_paper = set.getInt("fuin_paper");
        int fuin_uzumaki = set.getInt("fuin_uzumaki");
        int fuin_lastday = set.getInt("fuin_lastday");
        int maskprofil = set.getInt("maskprofil");
        int ticketmedit = set.getInt("ticketmedit");
        int minmedit = set.getInt("minmedit");
        int delayticketmedit = set.getInt("delayticketmedit");
        int reduc_ninjutsu = set.getInt("reduc_ninjutsu");
        return new PlayerInfo(name, mana, manamission, manamaze, manabonus, rank, skills, rollBonus, favoriteList, clan, chakraType, age, apparence, voieNinja, styleCombat, abilities, apparenceprofil, attributClan, points, pointsAbilities, delayPoints, resistance, ink, couleurChakra, teinte, oldpos, gender, fuin_paper, fuin_uzumaki, fuin_lastday, maskprofil, ticketmedit, minmedit, delayticketmedit, reduc_ninjutsu);
    }

    public void loadData(Player p){
        Main.loadingList.add(p.getName());
        p.sendMessage(ChatColor.DARK_GRAY + "Veuillez patienter pendant le chargement de vos données !");
        if(isInsert(p.getUniqueId().toString())) {
            insertPlayer(p.getUniqueId().toString());
        }


        Bukkit.getScheduler().runTaskAsynchronously(Main.plugin(), () -> {
            ResultSet set = getPlayerInfo(p.getUniqueId().toString());
            try {
                int mana = set.getInt("mana");
                int manamission = set.getInt("manamission");
                int manamaze = set.getInt("manamaze");
                int manabonus = set.getInt("manabonus");
                RPRank rank = RPRank.getById(set.getInt("rang"));
                HashMap<Skill, SkillMastery> skills = stringToSkillMap(set.getString("knownSkills"));
                HashMap<Skill, Integer> rollBonus = stringToRollBonusMap(set.getString("rollBonus"));
                ArrayList<Skill> favoriteList = stringToFavoriteList(set.getString("favoriteSkills"));
                Clan clan = Clan.getFromID(set.getInt("clan"));
                HashMap<ChakraType, Integer> chakraType = stringToChakraMap(set.getString("chakratype"));
                int age = set.getInt("age");
                String apparence = set.getString("appearence");
                ArtNinja voieNinja = ArtNinja.getFromID(set.getInt("voieNinja"));
                ArtNinja styleCombat = ArtNinja.getFromID(set.getInt("styleCombat"));
                ArrayList<Ability> abilities = stringToAbilitiesList(set.getString("abilities"));
                String apparenceprofil = set.getString("aprofil");
                String attributClan = set.getString("attributClan");
                int points = set.getInt("points");
                String pointsAbilities = set.getString("pointsAbilities");
                long delayPoints = set.getLong("delayPoints");
                HashMap<String, Integer> resistance = stringToResistance(set.getString("resistance"));
                int ink = set.getInt("ink");
                CouleurChakra couleurChakra = CouleurChakra.getFromID(set.getInt("couleur"));
                Teinte teinte = Teinte.getFromID(set.getInt("teint"));
                String oldpos = set.getString("oldpos");
                Gender gender = Gender.getFromID(set.getInt("gender"));
                int fuin_paper = set.getInt("fuin_paper");
                int fuin_uzumaki = set.getInt("fuin_uzumaki");
                int fuin_lastday = set.getInt("fuin_lastday");
                int maskprofil = set.getInt("maskprofil");
                int ticketMedit = set.getInt("ticketmedit");
                int minMedit = set.getInt("minmedit");
                int delayTicketMedit = set.getInt("delayticketmedit");
                int reduc_ninjutsu = set.getInt("reduc_ninjutsu");
                PlayerInfo pInfo = new PlayerInfo(p, mana, manamission, manamaze, manabonus, rank, skills, rollBonus, favoriteList, clan, chakraType, age, apparence, voieNinja, styleCombat, abilities, apparenceprofil, attributClan, points, pointsAbilities, delayPoints, resistance, ink, couleurChakra, teinte, oldpos, gender, fuin_paper, fuin_uzumaki, fuin_lastday, maskprofil, ticketMedit, minMedit,delayTicketMedit, reduc_ninjutsu);

                long disconnectTime = set.getLong("disconnectTime");
                long timeDisconnected = System.currentTimeMillis() - disconnectTime;
                long minutesDisconnected = TimeUnit.MILLISECONDS.toMinutes(timeDisconnected);
                double manaRegenNumber = round(minutesDisconnected / 2.0, 0);

                int manaAdd = pInfo.getMaxMana() / 100;
                int totalMana = (int) (manaRegenNumber * manaAdd);
                pInfo.addMana(totalMana);


                Main.loadingList.remove(p.getName());
                p.sendMessage(ChatColor.DARK_GRAY + "Vos données ont été chargées correctement ! \n"
                        + ChatColor.GRAY + "Bienvenue sur " + ChatColor.RED + "Seisan " + p.getDisplayName() + ChatColor.GRAY + ",\n" +
                        ChatColor.GRAY + "Votre rang est: " + ChatColor.GOLD + rank.getDisplayName() + "\n" +
                        ChatColor.GRAY + "Votre clan est: " + ChatColor.GOLD + clan.getName() + "\n" +
                        ChatColor.GRAY + "Votre style de combat est: " + ChatColor.GOLD + styleCombat.getName());
                if (voieNinja.getId() < 5) {
                    p.sendMessage(ChatColor.GRAY + "Votre voie ninja est: " + ChatColor.GOLD + voieNinja.getName());
                } else {
                    p.sendMessage(ChatColor.GRAY + "Votre second style de combat est: " + ChatColor.GOLD + voieNinja.getName());
                }
            }catch (Exception e){
                e.printStackTrace();
                Bukkit.getScheduler().scheduleSyncDelayedTask(Main.plugin(), () -> {
                    Main.loadingList.remove(p.getName());
                    p.kickPlayer(ChatColor.RED + "Une erreur est survenue pendant le chargement de vos donnéees !");
                });
            }
        });
    }

    private static String skillMapToString(PlayerInfo pInfo){
        String s = "";
        HashMap<Skill, SkillMastery> skillList = pInfo.getSkills();

        for(Skill skill : skillList.keySet()){
            SkillMastery mastery = skillList.get(skill);

            s = s.concat(skill.getNameInPlugin() + "," + mastery.getId() + ";");
        }

        if(s.length() > 0)
            s = s.substring(0, s.length() - 1);

        return s;
    }

    private static HashMap<Skill, SkillMastery> stringToSkillMap(String s){
        HashMap<Skill, SkillMastery> map = new HashMap<>();

        if(s == null || s.length() == 0)
            return map;

        for(String str : s.split(";")){
            Skill skill = Skill.getByPluginName(str.split(",")[0]);
            SkillMastery mastery = SkillMastery.getById(Integer.parseInt(str.split(",")[1]));

            if(skill != null && mastery != null){
                map.put(skill, mastery);
            }
        }

        return map;
    }

    private static HashMap<ChakraType, Integer> stringToChakraMap(String s){
        HashMap<ChakraType, Integer> map = new HashMap<>();

        if(s == null || s.length() == 0)
            return map;

        for(String str : s.split(";")){
            ChakraType chakraType = ChakraType.fromName(str.split("_")[0]);
            int prc = 0;
            if(str.contains("_"))
                prc = Integer.parseInt(str.split("_")[1]);

            if(chakraType != null){
                map.put(chakraType, prc);
            }
        }

        return map;
    }

    private static String chakraMapToString(PlayerInfo pInfo){
        String s = "";
        HashMap<ChakraType, Integer> chakraTypeIntegerHashMap = pInfo.getChakraType();

        for(ChakraType chakraType : chakraTypeIntegerHashMap.keySet()){
            int prct = chakraTypeIntegerHashMap.get(chakraType);
            s = s.concat(chakraType.getName().substring(2) + "_" + prct + ";");
        }

        if(s.length() > 0)
            s = s.substring(0, s.length() - 1);

        return s;
    }

    private static String resMapToString(PlayerInfo pInfo){
        String s = "";
        HashMap<String, Integer> skillList = pInfo.getResistancebonus();

        for(String str : skillList.keySet()){
            int nb = skillList.get(str);
            s = s.concat(str + ","+nb+";");
        }

        if(s.length() > 0)
            s = s.substring(0, s.length() - 1);

        return s;
    }

    private static HashMap<String, Integer> stringToResistance(String s){
        HashMap<String, Integer> map = new HashMap<>();

        if(s == null || s.length() == 0)
            return map;

        for(String str : s.split(";")){
            String raison = str.split(",")[0];
            int nb = Integer.parseInt(str.split(",")[1]);

            if(raison != null){
                map.put(raison, nb);
            }
        }

        return map;
    }

    private static String rollBonusMapToString(PlayerInfo pInfo){
        String s = "";
        HashMap<Skill, Integer> map = pInfo.getRollBonus();

        for(Skill skill : map.keySet()){
            s = s.concat(skill.getNameInPlugin() + "," + map.get(skill) + ";");
        }

        return s;
    }

    private static HashMap<Skill, Integer> stringToRollBonusMap(String s){
        HashMap<Skill, Integer> map = new HashMap<>();

        if (s == null || s.length() == 0)
            return map;

        for(String str : s.split(";")){
            Skill skill = Skill.getByPluginName(str.split(",")[0]);
            int bonus = StringUtils.isNumeric(str.split(",")[1]) ? Integer.parseInt(str.split(",")[1]) : 0;

            if(skill != null)
                map.put(skill, bonus);
        }

        return map;
    }

    private static String favoriteListToString(PlayerInfo pInfo){
        String s = "";

        ArrayList<Skill> favList = pInfo.getFavoriteList();

        for(Skill skill : favList){
            s = s.concat(skill.getNameInPlugin() + ";");
        }

        return s;
    }

    private static ArrayList<Skill> stringToFavoriteList(String s){
        ArrayList<Skill> list = new ArrayList<>();

        if(s == null || s.length() == 0){
            return list;
        }

        for(String str : s.split(";")){
            Skill skill = Skill.getByPluginName(str);

            if(skill != null)
                list.add(skill);
        }

        return list;
    }


    private static ArrayList<Ability> stringToAbilitiesList(String s) {
        ArrayList<Ability> list = new ArrayList<>();
        if(s == null || s.equals("")) {
            list.add(Ability.getByPluginName("vitesse_1"));
            list.add(Ability.getByPluginName("force_1"));
            list.add(Ability.getByPluginName("perception_vitesse_3"));
            return list;
        }

        for(String str : s.split(";")) {
            Ability ability = Ability.getByPluginName(str);

            if(ability != null) {
                list.add(ability);
            }
        }

        // Petit débuggage provisoire
        Ability a = Ability.getByPluginName("perception_vitesse_3");
        if(!list.contains(a))
            list.add(a);

        return list;
    }

    private static String abilitiesListToString(PlayerInfo pInfo){
        String s = "";

        ArrayList<Ability> abilities = pInfo.getAbilities();

        for(Ability ability : abilities){
            s = s.concat(ability.getNameInPlugin() + ";");
        }

        return s;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
