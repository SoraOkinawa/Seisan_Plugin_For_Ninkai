package me.Seisan.plugin.Features.commands.profil;

import me.Seisan.plugin.Features.PlayerData.PlayerInfo;
import me.Seisan.plugin.Features.ability.Ability;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import me.Seisan.plugin.Main.Command;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GiveCompetenceCommand extends Command {
    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        if(split.length != 2) {
            sender.sendMessage("§cHRP : §7/givecompetence (joueur) (competence)");
            return;
        }
        if(!(sender instanceof Player)) {
            sender.sendMessage("§cHRP : §7Commande uniquement via la console");
            return;
        }

        Player target = Bukkit.getPlayer(split[0]);
        Ability ability = Ability.getByPluginName(split[1]);

        if(target == null) {
            sender.sendMessage("§cHRP : §7La cible n'est pas connectée.");
            return;
        }

        if(ability == null) {
            sender.sendMessage("§cHRP : §7La compétence n'est pas reconnue. (Suivez bien l'auto completion)");
            return;
        }

        Player me = (Player)sender;
        PlayerInfo meInfo = PlayerInfo.getPlayerInfo(me);
        PlayerInfo targetInfo = PlayerInfo.getPlayerInfo(target);


        if(!meInfo.hasAbility(split[1])) {
            sender.sendMessage("§cHRP : §7Vous ne pouvez pas donner une technique que vous n'avez pas.");
            return;
        }

        if(targetInfo.hasAbility(split[1])) {
            sender.sendMessage("§cHRP : §7Vous ne pouvez pas lui enseigner une technique qu'il a déjà.");
            return;
        }

        if(!ability.isGiveAllowed()) {
            String name = ability.getName().split("-")[0];
            name = name.substring(0, name.length()-1);
            name = ChatColor.stripColor(name);
            if (name.equals(meInfo.getStyleCombat().getName())) {
                if (targetInfo.getStyleCombat() == meInfo.getStyleCombat() || targetInfo.getVoieNinja() == meInfo.getStyleCombat()) {
                    if (meInfo.getLvL(meInfo.getStyleCombat().getName()) >= 6) {
                        targetInfo.updateAbility(ability, sender);
                    }
                    else {
                        sender.sendMessage("§cHRP : §7Votre personnage n'est pas encore assez spécialisé.");
                    }
                    return;
                }
                sender.sendMessage("§cHRP : §7Son personnage ne partage pas vos arts ninjas.");
                return;
            }
            if (name.equals(meInfo.getVoieNinja().getName())) {
                if (targetInfo.getStyleCombat() == meInfo.getVoieNinja() || targetInfo.getVoieNinja() == meInfo.getVoieNinja()) {
                    if (meInfo.getLvL(meInfo.getVoieNinja().getName()) >= 6) {
                        targetInfo.updateAbility(ability, sender);
                    }
                    else {
                        sender.sendMessage("§cHRP : §7Votre personnage n'est pas encore assez spécialisé.");
                    }
                    return;
                }
                sender.sendMessage("§cHRP : §7Son personnage ne partage pas vos arts ninjas.");
                return;
            }
            if (name.equals(meInfo.getClan().getName())) {
                if (targetInfo.getClan() == meInfo.getClan()) {
                    if (meInfo.getLvL(meInfo.getClan().getName()) >= 6) {
                        targetInfo.updateAbility(ability, sender);
                    }
                    else {
                        sender.sendMessage("§cHRP : §7Votre personnage n'est pas encore assez spécialisé.");
                    }
                    return;
                }
                sender.sendMessage("§cHRP : §7Son personnage ne partage pas vos arts ninjas.");
                return;
            }
            sender.sendMessage("§cHRP : §7Cette technique ne peut être partagé ainsi.");
            return;
        }
        targetInfo.updateAbility(ability, sender);
    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split)
    {
        List<String> completion = new ArrayList<>();
        if(split.length == 1) {
            for(Player p : Bukkit.getOnlinePlayers()) {
                complete(completion, p.getName(), split[0]);
            }
        }
        if(split.length == 2) {
            PlayerInfo playerInfo = PlayerInfo.getPlayerInfo((Player)sender);
            for(Ability ability : Ability.getInstanceList()) {
                if(ability.isGiveAllowed() && playerInfo.hasAbility(ability.getNameInPlugin())) {
                    complete(completion, ability.getNameInPlugin(), split[1]);
                }
            }
            for(Ability ability : playerInfo.getAbilities()) {
                String name = ability.getName().split("-")[0];
                name = name.substring(0, name.length()-1);
                name = ChatColor.stripColor(name);
                if(name.equals(playerInfo.getStyleCombat().getName())) {
                    complete(completion, ability.getNameInPlugin(), split[1]);
                }
                if(name.equals(playerInfo.getVoieNinja().getName())) {
                    complete(completion, ability.getNameInPlugin(), split[1]);
                }
                if(name.equals(playerInfo.getClan().getName())) {
                    complete(completion, ability.getNameInPlugin(), split[1]);
                }
            }
        }
        return completion;
    }

    @Override
    protected boolean isOpOnly() {
        return false;
    }

}

