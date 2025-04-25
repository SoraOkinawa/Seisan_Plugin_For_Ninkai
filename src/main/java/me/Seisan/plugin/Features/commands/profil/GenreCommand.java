package me.Seisan.plugin.Features.commands.profil;

import me.Seisan.plugin.Features.PlayerData.PlayerInfo;
import me.Seisan.plugin.Features.objectnum.Gender;
import me.Seisan.plugin.Main;
import me.Seisan.plugin.Main.Command;
import org.bukkit.Bukkit;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;


public class GenreCommand extends Command {
    public static final String PERMISSION_FORCE = "ninkai.genre.force";
    
    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        if(sender instanceof Player){
            Player pSender = (Player) sender;
            if(pSender.hasPermission(PERMISSION_FORCE)) {
                if(split.length == 2) {
                    Player p = Bukkit.getPlayer(split[1]);
                    Gender gender = Gender.fromName(split[0]);
                    if(p != null) {
                        if(gender != null) {
                            PlayerInfo pInfo = PlayerInfo.getPlayerInfo(p);
                            pInfo.setGender(gender);
                            p.sendMessage("§cHRP : §7Le sexe de votre personnage a été changé. (" + gender.getName() +")");
                            sender.sendMessage("§cHRP : §7Vous avez choisi le sexe de "+p.getDisplayName()+". (" + gender.getName() +")");

                        }
                        else {
                            sender.sendMessage("§cHRP : §7/genre [sexe] [joueur]");
                            sender.sendMessage("§cHRP : §7Liste des sexe : Homme, Femme, Autre");
                        }
                    }
                    else {
                        sender.sendMessage("§cHRP : §7Le joueur n'est pas connecté.");
                    }
                }
                else {
                    sender.sendMessage("§cHRP : §7/genre [sexe] [joueur]");
                    sender.sendMessage("§cHRP : §7Liste des sexe : Homme, Femme, Autre");
                }
            }
            else {
                Player p = (Player)sender;
                PlayerInfo pInfo = PlayerInfo.getPlayerInfo(p);
                if(pInfo.getGender().getId() == -1) {
                    if (split.length == 1) {
                        Gender gender = Gender.fromName(split[0]);
                        if (gender != null) {
                            pInfo.setGender(gender);
                            sender.sendMessage("§cHRP : §7Vous avez choisi le sexe de votre personnage. (" + gender.getName() +")");
                        }
                    } else {
                        sender.sendMessage("§cHRP : §7/genre [sexe] (ATTENTION, INCHANGEABLE.)");
                        sender.sendMessage("§cHRP : §7Liste des sexe : Homme, Femme, Autre");
                    }
                }
                else {
                    sender.sendMessage("§cHRP : §7Le sexe de votre personnage est "+pInfo.getGender().getName());
                }
            }
        }
    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split)
    {
        Player pSender = (Player) sender;
        List<String> completion = new ArrayList<>();
        switch (split.length) {
            case 1:
                complete(completion, "Homme", split[0]);
                complete(completion, "Femme", split[0]);
                complete(completion, "Autre", split[0]);
                break;
            case 2:
                if (pSender.hasPermission(PERMISSION_FORCE))
                    for(Player p : Bukkit.getOnlinePlayers()) complete(completion, p.getName(), split[0]);
                break;
        }
        return completion;
    }
}
