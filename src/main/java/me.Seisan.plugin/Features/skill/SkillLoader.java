package me.Seisan.plugin.Features.skill;

import me.Seisan.plugin.Features.PlayerData.PlayerInfo;
import me.Seisan.plugin.Features.data.Config;
import me.Seisan.plugin.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Helliot on 15/03/2018.
 */
public class SkillLoader {


    private static Config skillConfig = new Config("skills.yml");

    public static void loadSkillsFromConfig(){
            ArrayList<String> enabledSkills = skillConfig.getStringList("enabledSkills");

        if(enabledSkills != null && !enabledSkills.isEmpty()){
            readFile(skillConfig, enabledSkills);
        }else{
            System.out.println("[SeisanPlugin] Aucune technique n'a été activée car la liste est vide !");
            Bukkit.getServer().shutdown();
        }
    }

    public static boolean checkConfig(){
            try {
                Config newconfig = new Config("skills.yml"); //Reloading config file
                ArrayList<String> enabledAbility = newconfig.getStringList("enabledSkills");
                if(enabledAbility.isEmpty()) {
                    return false;
                }
                return readFile(newconfig, enabledAbility);
            }
            catch (Exception e) {
                e.printStackTrace();
                return false;
            }
    }

    private static boolean readFile(Config skillConfig, ArrayList<String> enabledSkills) {
        for(String skillName : enabledSkills){
            try {
                String name = skillConfig.getString(skillName + ".name");
                String nameInPlugin = skillConfig.getString(skillName + ".nameInPlugin");
                int manaCost = skillConfig.getInt(skillName + ".manaCost");
                boolean needMastery = skillConfig.getBoolean(skillName + ".needMastery");
                String levelName = skillConfig.getString(skillName + ".level");
                SkillLevel level = SkillLevel.getByCharName(levelName);
                String message = skillConfig.getString(skillName + ".message");
                String lore = skillConfig.getString(skillName + ".lore");
                String mudras = skillConfig.getString(skillName + ".mudras");
                ArrayList<String> commandList = skillConfig.getStringList(skillName + ".commandList");
                Material itemType = (Material.getMaterial(skillConfig.getString(skillName + ".itemType")) != null) ? Material.getMaterial(skillConfig.getString(skillName + ".itemType")) : Material.BOOK;
                boolean needTarget = skillConfig.getBoolean(skillName + ".needTarget");
                boolean canBeFullMaster = skillConfig.getBoolean(skillName + ".canBeFullMaster");
                String infoSup = skillConfig.getString(skillName+ ".infoSup");
                boolean targetVisibility = skillConfig.getBoolean(skillName+ ".skillVisibility");
                boolean publique = skillConfig.getBoolean(skillName+".publique");
                new Skill(name, nameInPlugin, "", manaCost, needMastery, level, message, lore, mudras, commandList, itemType, needTarget, canBeFullMaster, infoSup, targetVisibility, publique);
            }catch (Exception e){
                System.out.println("[SeisanPlugin] La technique " + skillName + " est mal configurée ! Explication de l'erreur:");
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    public static void reloadAllSkills(CommandSender p){
        HashMap<String,PlayerInfo> playerInfoList = (HashMap<String,PlayerInfo>) PlayerInfo.getInstanceList().clone();

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
        skillConfig = new Config("skills.yml"); //Reloading config file
        loadSkillsFromConfig();

        if(p != null)
            p.sendMessage(ChatColor.GREEN + "Jutsus sauvegardés ! \n" + ChatColor.GRAY + "Restitution des jutsus aux joueurs...");

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
