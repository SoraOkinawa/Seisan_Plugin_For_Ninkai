package me.Seisan.plugin.Features.commands.others;


import me.Seisan.plugin.Features.PlayerData.PlayerInfo;
import me.Seisan.plugin.Features.utils.ItemUtil;
import me.Seisan.plugin.Main;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import me.Seisan.plugin.Main.Command;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class StealChakraCommand extends Command {
    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        if (sender instanceof ConsoleCommandSender) {
            // /stealjutsu origine cible montantvole montantrecup portee
            if(split.length == 5) {

                Player origine = Bukkit.getPlayer(split[0]);
                PlayerInfo origineInfo = PlayerInfo.getPlayerInfo(origine);
                Player target = Bukkit.getPlayer(split[1]);
                PlayerInfo targetInfo = PlayerInfo.getPlayerInfo(target);

                if (origine != null && origineInfo != null
                        && target != null && targetInfo != null
                        && StringUtils.isNumeric(split[2]) && StringUtils.isNumeric(split[3]) && StringUtils.isNumeric(split[4])) {

                    int montant_vole = Integer.parseInt(split[2]);
                    int montant_recup = Integer.parseInt(split[3]);
                    int portee = Integer.parseInt(split[4]);

                    Main.getStealChakra().put(origine.getName(), target.getName());

                    String message = "§cHRP : §7Un joueur a fait un jutsu de vol de chakra sur votre personnage. §6§bCliquez sur le message §r§7pour confirmer s'il est RP sinon, prévenez un MJ d'un abus.";
                    TextComponent messagecomponent = new TextComponent(message);
                    messagecomponent.setColor(net.md_5.bungee.api.ChatColor.getByChar('7'));
                    BaseComponent[] texte = new BaseComponent[]{
                            new TextComponent(ItemUtil.convertItemStackToJsonRegular(ItemUtil.createItemStack(Material.BOOK, 1, "§6Confirmer le vol de chakra RP."))) // The only element of the hover events basecomponents is the item json
                    };
                    messagecomponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, texte));
                    messagecomponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/stealchakra "+origine.getName()+ " "+target.getName() + " "+montant_vole+ " "+montant_recup+ " "+portee));

                    target.spigot().sendMessage(messagecomponent);
                    origine.sendMessage("§cHRP : §7La cible a reçu une demande de confirmation de vol de chakra.");

                } else {
                    sender.sendMessage("§cHRP : §7/stealjutsu (origine) (cible) (montant volé) (montant récup) (portée)");
                }
            }
            else {
                sender.sendMessage("§cHRP : §7/stealjutsu (origine) (cible) (montant volé) (montant récup) (portée)");
            }
        }
        else if(split.length == 5 ){
            Player origine = Bukkit.getPlayer(split[0]);
            PlayerInfo origineInfo = PlayerInfo.getPlayerInfo(origine);
            Player target = Bukkit.getPlayer(split[1]);
            PlayerInfo targetInfo = PlayerInfo.getPlayerInfo(target);
            if (origine != null && origineInfo != null && target != null && targetInfo != null && StringUtils.isNumeric(split[2])
                    && StringUtils.isNumeric(split[3]) && StringUtils.isNumeric(split[4])) {
                if(Main.getStealChakra().containsKey(origine.getName()) && Main.getStealChakra().get(origine.getName()).equals(target.getName())) {
                    int montant_vole = Integer.parseInt(split[2]);
                    int montant_recup = Integer.parseInt(split[3]);
                    int portee = Integer.parseInt(split[4]);
                    if (origineInfo.getPlayer().getLocation().distanceSquared(targetInfo.getPlayer().getLocation()) <= portee) {
                        if (montant_vole > targetInfo.getMana()) {
                            montant_vole = targetInfo.getMana();
                        }
                        if (montant_recup > montant_vole) {
                            montant_recup = montant_vole;
                        }
                        targetInfo.removeMana(montant_vole);
                        target.sendMessage("§b** Vous vous êtes fait voler " + montant_vole + " portions de chakra.");
                        origineInfo.addMana(montant_recup);
                        origine.sendMessage("§b** Vous avez volé et récupéré " + montant_recup + " portions de chakra.");
                        Main.getStealChakra().remove(origine.getName());
                    } else {
                        sender.sendMessage("§cHRP : §7Le joueur n'a pas la portée nécessaire pour voler.");
                        target.sendMessage("§cHRP : §7Le joueur était trop loin quand il a accepté, le vol ne s'est pas effectué.");
                        origine.sendMessage("§cHRP : §7Vous étiez trop loin quand vous avez accepté, le vol ne s'est pas effectué.");
                    }
                }
                else {
                    target.sendMessage("§cHRP : §7Vous avez déjà confirmé ou la confirmation n'est plus valide");
                    if(sender.isOp()) {
                        target.sendMessage("§cHRP : §7Commande exclusive à la console.");
                    }
                }
            }
            else {
                sender.sendMessage("§cHRP : Le vol ne s'est pas effectué car l'un des deux à déconnecté.");
                if(origine != null) {
                    sender.sendMessage("§cHRP : §7Celui qui a lancé le jutsu s'est déconnecté et le vol s'est mal passé.");
                }
            }
        }
    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        return new ArrayList<>();
    }

    @Override
    protected boolean isOpOnly() {
        return false;
    }
}
