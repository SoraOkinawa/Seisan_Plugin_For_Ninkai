package me.Seisan.plugin.Features.commands.others;


import me.Seisan.plugin.Features.Inventory.SkillInventory;
import me.Seisan.plugin.Features.PlayerData.PlayerInfo;
import me.Seisan.plugin.Features.data.PlayerDB;
import me.Seisan.plugin.Features.skill.Skill;
import me.Seisan.plugin.Features.skill.SkillManager;
import me.Seisan.plugin.Features.skill.SkillMastery;
import me.Seisan.plugin.Main;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import me.Seisan.plugin.Main.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class JutsuCommand extends Command {
    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        if(split.length == 0){
            if(sender instanceof Player){
                Player p = (Player) sender;
                PlayerInfo pInfo = PlayerInfo.getPlayerInfo(p);

                if(pInfo.getSkills().size() > 0)
                    p.openInventory(SkillInventory.getElementInventory(pInfo));

                else
                    p.sendMessage(ChatColor.RED + "Vous ne connaissez aucune technique !");
            }
        }else{
            if(SkillManager.isSkillEnabled()) {
                switch (split[0]) {
                    case "masteryall":
                        if(split.length == 2) {
                            if (sender instanceof Player && sender.isOp() && StringUtils.isNumeric(split[1]) && SkillMastery.getById(Integer.parseInt(split[1])) != null) {
                                Player p = (Player) sender;
                                PlayerInfo pInfo = PlayerInfo.getPlayerInfo(p);
                                SkillMastery mastery = SkillMastery.getById(Integer.parseInt(split[1]));
                                for (Skill skill : Skill.getInstanceList()) {
                                    if (!pInfo.getSkills().containsKey(skill) || pInfo.getMastery(skill) != mastery)
                                        pInfo.updateSkill(skill, mastery);
                                }
                            } else {
                                sender.sendMessage(ChatColor.RED + "Vous n'avez pas la permission d'utiliser cette commande");
                            }
                        }
                        break;
                    case "unlearn":
                       if (split.length == 3) {
                            if (sender.isOp()) {
                                String skillName = split[2];
                                Skill skill = Skill.getByPluginName(skillName);
                                if (skill != null) {
                                    Player target = sender.getServer().getPlayer(split[1]);
                                    if (target != null) {
                                        PlayerInfo tInfo = PlayerInfo.getPlayerInfo(target);

                                        if (tInfo.getSkills().containsKey(skill)) {
                                            tInfo.removeSkill(skill);
                                            sender.sendMessage(target.getDisplayName() + ChatColor.GREEN + " a oublié la technique: " + ChatColor.GOLD + skill.getName());
                                        } else {
                                            sender.sendMessage(target.getDisplayName() + ChatColor.RED + " ne connait pas cette technique !");
                                        }
                                    } else {
                                        sender.sendMessage(ChatColor.RED + "Le joueur " + split[1] + " n'est pas connecté !");
                                    }
                                } else {
                                    sender.sendMessage(ChatColor.RED + "La technique" + split[2] + " n'existe pas !");
                                }
                            } else {
                                sender.sendMessage(ChatColor.RED + "Vous n'avez pas la permission d'utiliser cette commande");
                            }
                       }
                        break;
                    case "unlearnall":
                        if (sender.isOp() && sender instanceof Player) {
                            Player p;
                            if (split.length == 1) {
                                p = (Player) sender;
                                PlayerInfo pInfo = PlayerInfo.getPlayerInfo(p);
                                HashMap<Skill, SkillMastery> skillSet = (HashMap<Skill, SkillMastery>) pInfo.getSkills().clone();
                                for (Skill skill : skillSet.keySet()) {
                                    pInfo.removeSkill(skill);
                                }
                                sender.sendMessage(ChatColor.RED + "Vous avez a oublié tous vos jutsus !");
                            } else if (split.length == 2) {
                                if (sender.getServer().getPlayer(split[1]) != null) {
                                    p = sender.getServer().getPlayer(split[1]);
                                    PlayerInfo pInfo = PlayerInfo.getPlayerInfo(p);
                                    HashMap<Skill, SkillMastery> skillSet = (HashMap<Skill, SkillMastery>) pInfo.getSkills().clone();
                                    for (Skill skill : skillSet.keySet()) {
                                        pInfo.removeSkill(skill);
                                    }
                                    sender.sendMessage(ChatColor.RED + "Le joueur " + split[1] + " a oublié tous ses jutsus !");
                                } else {
                                    sender.sendMessage(ChatColor.RED + "Le joueur " + split[1] + " n'est pas connecté !");
                                }
                            } else {
                                sender.sendMessage(ChatColor.RED + "WOLLEH TU FAIS QUOI AVEC LA COMMANDE ?");
                            }
                        } else {
                            sender.sendMessage(ChatColor.RED + "Vous n'avez pas la permission");
                        }
                        break;
                    case "select":
                        if (sender instanceof Player) {
                            if (split.length == 2) {
                                Player p = (Player) sender;
                                PlayerInfo pInfo = PlayerInfo.getPlayerInfo(p);
                                String skillName = split[1];

                                Skill skill = Skill.getByPluginName(skillName);

                                if (skill != null && pInfo.getSkills().containsKey(skill)) {
                                    p.openInventory(SkillInventory.getSkillSelectInventory(skill, pInfo));
                                } else {
                                    p.sendMessage(ChatColor.RED + "Vous ne connaissez pas la technique " + skillName);
                                }
                            } else {
                                sendHelpMessage(sender);
                            }
                        }
                        break;
                    case "list":
                        if (sender instanceof Player) {
                            Player p;
                            if(split.length == 2) {
                                p = Bukkit.getPlayer(split[1]);
                                if(p == null) {
                                    sender.sendMessage("§cHRP : §7Ce joueur n'est pas connecté.");
                                    return;
                                }
                            }
                            else {
                                p = (Player) sender;
                            }
                            PlayerInfo pInfo = PlayerInfo.getPlayerInfo(p);

                            if (pInfo.getSkills().size() > 0) {

                                String mess;
                                if(split.length != 2) {
                                    mess = "§6Liste de vos techniques§7: ";
                                }
                                else {
                                    mess = "§6Liste des techniques de "+p.getName()+": ";
                                }
                                int counter = mess.length();
                                for (Skill skill : pInfo.getSkills().keySet()) {
                                    String s = skill.getName();
                                    String toAppend = s + "§r,§7 ";
                                    counter += toAppend.length();

                                    if (counter > 220) {
                                        mess += "@";
                                        mess += toAppend;
                                        counter = 0;
                                    } else {
                                        mess = mess.concat(toAppend);
                                    }
                                }

                                mess = mess.substring(0, mess.length() - 4);
                                for (String s : mess.split("@")) {
                                    sender.sendMessage(s);
                                }
                            } else {
                                p.sendMessage(ChatColor.RED + "Vous ne connaissez aucune technique !");
                            }
                        }
                        break;
                    case "listall":
                        if (sender instanceof Player && sender.isOp()) {
                            String mess = "§6Liste des techniques§7: ";

                            int counter = mess.length();
                            for (Skill skill : Skill.getInstanceList()) {
                                String s = skill.getNameInPlugin();
                                String toAppend = s + "§r,§7 ";
                                counter += toAppend.length();

                                if (counter > 220) {
                                    mess += "@";
                                    mess += toAppend;
                                    counter = 0;
                                } else {
                                    mess += toAppend;
                                }
                            }

                            mess = mess.substring(0, mess.length() - 4);
                            for (String s : mess.split("@")) {
                                sender.sendMessage(s);
                            }
                        } else {
                            sender.sendMessage(ChatColor.RED + "Vous n'avez pas la permission !");
                        }
                        break;
                    case "learn":
                       if (split.length == 3 || split.length == 4) {
                            if (sender.isOp()) {
                                String skillName = split[2];
                                Skill skill = Skill.getByPluginName(skillName);
                                int nb = 0;
                                if (skill != null) {
                                    Player target = sender.getServer().getPlayer(split[1]);
                                    if (target == null) {
                                        sender.sendMessage(ChatColor.RED + "Le joueur " + split[1] + " n'est pas connecté !");
                                        return;
                                    }

                                    PlayerInfo tInfo = PlayerInfo.getPlayerInfo(target);
                                    if (tInfo.getSkills().containsKey(skill)) {
                                        sender.sendMessage(target.getDisplayName() + ChatColor.RED + " connnait déjà cette technique !");
                                        return;
                                    }
                                    if(split.length == 4) {
                                        if (!StringUtils.isNumeric(split[3])) {
                                            sender.sendMessage("§cHRP : §7Insérez un 0 (non maîtrisé), 1 (maîtrisé), ou 2 (maîtrisé à une main)");
                                            return;
                                        }
                                        nb = Integer.parseInt(split[3]);
                                        if (nb < 0 || nb > 2) {
                                            sender.sendMessage("§cHRP : §7Insérez un 0 (non maîtrisé), 1 (maîtrisé), ou 2 (maîtrisé à une main)");
                                            return;
                                        }
                                    }
                                    tInfo.updateSkill(skill, SkillMastery.getById(nb));
                                    sender.sendMessage(target.getDisplayName() + ChatColor.GREEN + " a appris la technique: " + ChatColor.GOLD + skill.getName()+ " §7en "+SkillMastery.getById(nb).getName());
                                } else {
                                    sender.sendMessage("§cHRP : §7La technique \"§6" + split[2] + "§7\" n'existe pas !");
                                }
                            } else {
                                sender.sendMessage(ChatColor.RED + "Vous n'avez pas la permission d'utiliser cette commande");
                            }
                        }
                        break;
                    case "learnall":
                        if (sender instanceof Player && sender.isOp()) {
                            Player p = (Player) sender;
                            PlayerInfo pInfo = PlayerInfo.getPlayerInfo(p);

                            for (Skill skill : Skill.getInstanceList()) {
                                if (!pInfo.getSkills().containsKey(skill))
                                    pInfo.updateSkill(skill, SkillMastery.UNLEARNED);
                            }
                        } else {
                            sender.sendMessage(ChatColor.RED + "Vous n'avez pas la permission d'utiliser cette commande");
                        }
                        break;
                    case "mastery":
                      /*  if (split.length == 3) {
                            if (sender instanceof Player && sender.isOp()) {
                                Player p = (Player) sender;
                                PlayerInfo playerInfo = PlayerInfo.getPlayerInfo(p);
                                Skill skill = Skill.getByPluginName(split[1]);
                                if (skill != null && playerInfo.getSkills().containsKey(skill)) {
                                    if (StringUtils.isNumeric(split[2]) && SkillMastery.getById(Integer.parseInt(split[2])) != null) {
                                        SkillMastery mastery = SkillMastery.getById(Integer.parseInt(split[2]));
                                        playerInfo.updateSkill(skill, mastery);
                                    } else {
                                        sendMastery(p);
                                    }
                                } else {
                                    p.sendMessage(ChatColor.RED + "Vous ne connaissez pas la technique indiquée !");
                                }
                            } else {
                                sender.sendMessage(ChatColor.RED + "Vous n'avez pas la permission d'utiliser cette commande");
                            }
                        } else*/
                        if (split.length == 4) {
                            if (sender.isOp()) {
                                Player target = sender.getServer().getPlayer(split[1]);
                                if (target != null) {
                                    PlayerInfo pInfo = PlayerInfo.getPlayerInfo(target);
                                    Skill skill = Skill.getByPluginName(split[2]);
                                    if (skill != null && pInfo.getSkills().containsKey(skill)) {
                                        SkillMastery mastery = SkillMastery.getById(Integer.parseInt(split[3]));
                                        if (StringUtils.isNumeric(split[3]) && mastery != null) {
                                            pInfo.updateSkill(skill, mastery);
                                            sender.sendMessage(target.getDisplayName() + ChatColor.GREEN + " a acquis la maitrise de la technique " + ChatColor.GOLD + skill.getName() +
                                                    ChatColor.GREEN + " au rang " + ChatColor.GOLD + mastery.getName());
                                        } else {
                                            sendMastery(sender);
                                        }
                                    } else {
                                        sender.sendMessage(ChatColor.RED + "Ce joueur ne connait pas la technique indiquée !");
                                    }
                                } else {
                                    sender.sendMessage(ChatColor.RED + "Le joueur " + split[1] + " n'est pas connecté !");
                                }
                            } else {
                                sender.sendMessage(ChatColor.RED + "Vous n'avez pas la permission d'utiliser cette commande");
                            }
                        }
                        break;
                    case "unselect":
                        if(!Main.getCurrentSelectSkill().containsKey(sender.getName())) {
                            sender.sendMessage("§cHRP : §7Vous n'avez pas de jutsu de sélectionné.");
                        }
                        else {
                            Player target = (Player)sender;
                            PlayerInfo pInfo = PlayerInfo.getPlayerInfo(target);

                            Bukkit.getScheduler().cancelTask(Main.getCurrentSelectSkill().get(sender.getName()));
                            Main.getCurrentSelectSkill().remove(sender.getName());

                            sender.sendMessage("§cHRP : §7Vous ne sélectionnez plus la technique : "+pInfo.getCurrentSkill().getName());
                            pInfo.setCurrentSkill(null);
                        }
                        break;
                    case "help":
                        sendHelpMessage(sender);
                        break;
                }
            }else{
                sender.sendMessage(ChatColor.RED + "Les jutsus sont désactivés pour le moment. Ils sont peut être en rechargement !");
            }
        }
    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split)
    {
        List<String> completion = new ArrayList();
        switch(split.length) {
            case 1:
                complete(completion, "select", split[0]);
                complete(completion, "unselect", split[0]);
                complete(completion, "list", split[0]);
                if (sender.isOp()) {
                    complete(completion, "learn", split[0]);
                    complete(completion, "learnall", split[0]);
                    complete(completion, "listall", split[0]);
                    complete(completion, "mastery", split[0]);
                    complete(completion, "unlearn", split[0]);
                    complete(completion, "unlearnall", split[0]);
                    complete(completion, "masteryall", split[0]);
                }
                break;
            case 2:
                if (split[0].equals("select")) {
                    PlayerInfo pInfo = PlayerInfo.getPlayerInfo((Player) sender);
                    for (Skill jutsu : pInfo.getSkills().keySet()) {
                        complete(completion, jutsu.getNameInPlugin(), split[1]);
                    }
                }
                if (sender.isOp()) {
                    if (split[0].equals("learn") || split[0].equals("unlearn") || split[0].equals("mastery")) {
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            complete(completion, p.getName(), split[1]);
                        }
                    }
                    if(split[0].equals("masteryall")) {
                        for(SkillMastery mastery : SkillMastery.values()) {
                            complete(completion, Integer.toString(mastery.getId()), split[1]);
                        }
                    }
                }
                break;
            case 3:
                Player p = Bukkit.getPlayer(split[1]);
                if (p != null) {
                    PlayerInfo pInfo = PlayerInfo.getPlayerInfo(p);
                    if (sender.isOp()) {
                        if (split[0].equals("learn")) {
                            for (Skill jutsu : Skill.getInstanceList()) {
                                complete(completion, jutsu.getNameInPlugin(), split[2]);
                            }
                        }
                        if (split[0].equals("unlearn") || split[0].equals("mastery")) {
                            for (Skill jutsu : pInfo.getSkills().keySet()) {
                                complete(completion, jutsu.getNameInPlugin(), split[2]);
                            }
                        }
                    }
                }
                break;
            case 4:
                if(split[0].equals("mastery") || split[0].equals("learn")) {
                    for(SkillMastery mastery : SkillMastery.values()) {
                        complete(completion, Integer.toString(mastery.getId()), split[1]);
                    }
                }
                break;
        }
        return completion;
    }

    @Override
    protected boolean isOpOnly() {
        return false;
    }

    private void sendMastery(CommandSender s){
        for(SkillMastery mastery : SkillMastery.values()){
            s.sendMessage(ChatColor.DARK_GRAY + "" + mastery.getId() + ChatColor.GRAY + ": " + ChatColor.GOLD + mastery.getName());
        }
    }

    private void sendHelpMessage(CommandSender s){
        ArrayList<String> helpList = new ArrayList<>();

        helpList.add("§6/jutsu §8- Permet d'accéder au sélecteur de jutsu");
        helpList.add("§6/jutsu §ehelp §8- Affiche ce message");
        helpList.add("§6/jutsu §eselect §7(nom) §8- Permet de sélectionner une technique donnée sans passer par l'inventaire");
        helpList.add("§6/jutsu §elist §8- Liste vos techniques");
        if(s.isOp()){
            helpList.add("§6/jutsu §elearn §7(nom) [joueur] §8- Permet d'apprendre une technique à soi même ou à un joueur (optionnel)");
            helpList.add("§6/jutsu §elearnall §8- Apprend toutes les techniques au lanceur de la commande");
            helpList.add("§6/jutsu §elistall §8- Liste les techniques existantes et activées");
            helpList.add("§6/jutsu §emastery §7(nom) (niveau) [joueur] §8- Permet de changer manuellement la maîtrise d'une technique (joueur optionnel)");
            helpList.add("§6/jutsu §eunlearn §7(nom) [joueur] §8- Permet d'oublier une technique");
            helpList.add("§6/jutsu §eunlearnall §7[joueur] §8- Permet d'oublier toutes les techniques");
            helpList.add("§6/jutsu §emasteryall §7(niveau) §8- Permet de mettre un niveau de maitrise à toutes les techniques");
        }

        for(String str : helpList){
            s.sendMessage(str);
        }
    }
}
