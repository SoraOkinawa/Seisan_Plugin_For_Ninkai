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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AbilityLoader {
    private static Config abilityConfig = new Config("abilities.yml");

    public static void loadAbilitiesFromConfig(){
        ArrayList<String> enabledAbility = abilityConfig.getStringList("enabledAbilities");

        if(!enabledAbility.isEmpty()){
            readFile(abilityConfig, enabledAbility);
        }else{
            Main.LOG.info("Aucune compétence n'a été activée car la liste est vide !");
            Bukkit.shutdown();
        }
    }

    public static boolean checkConfig(){
        try {
            Config newconfig = new Config("abilities.yml"); //Reloading config file
            ArrayList<String> enabledAbility = newconfig.getStringList("enabledAbilities");
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

    private static boolean readFile(Config abilityConfig, ArrayList<String> enabledAbility) {
        for(String abilityName : enabledAbility){
            try {
                String name = abilityConfig.getString(abilityName + ".name");
                String nameInPlugin = abilityConfig.getString(abilityName + ".nameInPlugin");
                Material itemType = (Material.getMaterial(abilityConfig.getString(abilityName + ".itemType")) != null) ? Material.getMaterial(abilityConfig.getString(abilityName + ".itemType")) : Material.BOOK;
                String description = abilityConfig.getString(abilityName+".description");
                String type = abilityConfig.getString(abilityName+".type");
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
                int resistance = abilityConfig.getInt(abilityName+".resistance");
                String givenJutsu = abilityConfig.getString(abilityName+".givenJutsu");
                // TODO : Faire la même chose pour les jutsus et """proprement"""
              //  saveBDD(name, nameInPlugin, itemType.getKey().getKey(), description, tagkey, lvl, tagkey, tagvalue, pts, ptsnec, reqAbilities, givenAbilities, giveAbilities, lore);
                new Ability(name, nameInPlugin, itemType, description, type, lvl, tagkey, tagvalue, pts, ptsnec, reqAbilities, givenAbilities, giveAbilities, lore, giveAllowed, resistance, givenJutsu);
            }catch (Exception e){
                Main.LOG.info("La compétence " + abilityName + " est mal configurée ! Explication de l'erreur:");
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    @SuppressWarnings("unchecked")
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
        abilityConfig = new Config("abilities.yml"); //Reloading config file
        loadAbilitiesFromConfig();

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
