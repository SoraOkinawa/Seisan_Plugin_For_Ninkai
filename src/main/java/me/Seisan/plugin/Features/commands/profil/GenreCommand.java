package me.Seisan.plugin.Features.commands.profil;

import me.Seisan.plugin.Features.PlayerData.PlayerInfo;
import me.Seisan.plugin.Features.objectnum.Gender;
import me.Seisan.plugin.Main.Command;
import org.bukkit.Bukkit;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;


public class GenreCommand extends Command {
    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        if(sender instanceof Player){
            if(sender.isOp()) {
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
        return new ArrayList<>();
    }
    @Override
    protected boolean isOpOnly() {
        return false;
    }
}
