package me.Seisan.plugin.Features.commands.profil;

import me.Seisan.plugin.Features.PlayerData.PlayerInfo;
import me.Seisan.plugin.Features.ability.Ability;
import me.Seisan.plugin.Features.skill.Skill;
import me.Seisan.plugin.Main.Command;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class AbilitiesCommand extends Command {
    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (args.length == 3) {
            Player p = Bukkit.getPlayer(args[1]);
            PlayerInfo playerInfo;
            if (p != null) {
                playerInfo = PlayerInfo.getPlayerInfo(p);
            } else {
                sender.sendMessage(ChatColor.RED+"Le joueur n'est pas connecté");
                return;
            }
            Ability ability = Ability.getByPluginName(args[2]);
            if(ability == null) {
                sender.sendMessage(ChatColor.RED+"L'abilité n'est pas reconnu");
                return;
            }
            switch (args[0]) {
                case "learn":
                    if(!playerInfo.getAbilities().contains(ability)) {
                        playerInfo.updateAbility(ability, sender);
                        playerInfo.addgiveAbilities(ability.getGiveAbilities());
                        playerInfo.updateChakra();
                    }
                    else {
                        sender.sendMessage(ChatColor.RED+"Le joueur a déjà la compétence.");
                    }
                    break;
                case "unlearn":
                    if(playerInfo.getAbilities().contains(ability)) {
                        playerInfo.removeAbility(ability, sender);
                        playerInfo.removegiveAbilities(ability.getGiveAbilities());
                        playerInfo.updateChakra();
                    }
                    else {
                        sender.sendMessage(ChatColor.RED+"Le joueur n'a pas la compétence.");
                    }
                    break;
                default:
                    SendHelp((Player)sender);
            }
        } else {
            if (args.length == 2) {
                if("unlearnall".equals(args[0])) {
                    Player target = Bukkit.getPlayer(args[1]);
                    if(target != null) {
                        PlayerInfo pInfo = PlayerInfo.getPlayerInfo(target);
                        pInfo.getAbilities().clear();
                        pInfo.updateAbility(Ability.getByPluginName("vitesse_1"));
                        pInfo.updateAbility(Ability.getByPluginName("force_1"));
                        pInfo.updateAbility(Ability.getByPluginName("perception_vitesse_3"));
                        if(!target.getName().equals(sender.getName())) {
                            sender.sendMessage(ChatColor.RED+"HRP : "+target.getDisplayName()+ChatColor.GRAY+ "a eu ses compétences de réinitialisées");
                        }
                        target.sendMessage(ChatColor.RED+"HRP : "+ChatColor.GRAY+"Vos compétences ont été réinitialisées.");
                        pInfo.updateChakra();
                    }
                    else {
                        sender.sendMessage(ChatColor.RED+"Le joueur n'est pas connecté");
                    }
                } else {
                    SendHelp((Player)sender);
                }
            } else {
                if (args.length == 1) {
                    if ("learnall".equals(args[0])) {
                        PlayerInfo pInfo = PlayerInfo.getPlayerInfo((Player) sender);
                        for (Ability newability : Ability.instanceList) {
                            if (!pInfo.getAbilities().contains(newability))
                                pInfo.updateAbility(newability);
                        }
                    } else {
                        SendHelp((Player) sender);
                    }
                } else {
                    SendHelp((Player) sender);
                }
            }
        }
    }

    private void SendHelp(Player p) {
        p.sendMessage(ChatColor.RED+"HRP : "+ChatColor.GRAY+"/abilities §2learn §9joueur §babilityesname");
        p.sendMessage(ChatColor.RED+"HRP : §7/abilities §2remove §9joueur §babilityesname");
        p.sendMessage(ChatColor.RED+"HRP : §7/abilities §2learnall §8Ne fonctionne que sur soi-même");
        p.sendMessage(ChatColor.RED+"HRP : "+ChatColor.GRAY+"/abilities §2unlearnall §9joueur");

    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split)
    {
        // abilities (1 - learn ou remove) (2 - joueur) (3 - ability)
        // abilities (1 - learnall)
        // abilities (1 - unlearnall) (2 - joueur)
        List<String> completion = new ArrayList();
        switch(split.length) {
            case 1:
                complete(completion, "learn", split[0]);
                complete(completion, "unlearn", split[0]);
                complete(completion, "learnall", split[0]);
                complete(completion, "unlearnall", split[0]);
                break;
            case 2:
                if(split[0].equals("learn") || split[0].equals("unlearn")) {
                    for(Player p : Bukkit.getOnlinePlayers()) {
                        complete(completion, p.getName(), split[1]);
                    }
                }
                break;
            case 3:
                Player target = Bukkit.getPlayer(split[1]);
                if(target != null) {
                    if (split[0].equals("learn")) {
                        for (Ability ability : Ability.getInstanceList()) {
                            complete(completion, ability.getNameInPlugin(), split[2]);
                        }
                    }
                    if (split[0].equals("unlearn")) {
                        PlayerInfo pInfo = PlayerInfo.getPlayerInfo(target);
                        for (Ability ability : pInfo.getAbilities()) {
                            complete(completion, ability.getNameInPlugin(), split[2]);
                        }
                    }
                }
            }
        return completion;
    }

}
