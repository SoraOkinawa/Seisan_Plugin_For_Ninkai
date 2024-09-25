package me.Seisan.plugin.Features.commands.anothers;

import me.Seisan.plugin.Features.Chat.ChatFormat;
import me.Seisan.plugin.Features.PlayerData.PlayerInfo;
import me.Seisan.plugin.Features.commands.anothers.Commands;
import me.Seisan.plugin.Features.skill.Skill;
import me.Seisan.plugin.Features.skill.SkillLevel;
import me.Seisan.plugin.Features.skill.SkillMastery;
import me.Seisan.plugin.Features.utils.ItemUtil;
import me.Seisan.plugin.Features.utils.Nickname;
import me.Seisan.plugin.Main.Command;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.world.level.IEntityAccess;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.bukkit.ChatColor.COLOR_CHAR;


public class TechniqueMJCommand extends Command {

    private final Map<UUID, String[]> waitingForDescription = new HashMap<>();


    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        if (sender instanceof Player) {
            Player player = (Player) sender;//message d'erreur si il y a pas d'argument dans la commande
            if (split.length == 0) {
                player.sendMessage("§4HRP : §7Il faut faire la commande /techniquemj <nom de la technique> <niveau de la technique (si description nécessaire)>");
            }
            // /techniquemj <nom de la technique>
            else if (split.length == 1) {
                sendTechnique(player, split);
            }
            // /techniquemj <nom de la technique> <optionnel: niveau de la technique>
            else {
                // /techniquemj description <description> : Envoie la technique avec la description si le joueur a déjà rentré le nom et le niveau
                if (split[0].equals("description")) {
                    if (waitingForDescription.containsKey(player.getUniqueId())) {
                        String[] techniqueName = waitingForDescription.get(player.getUniqueId());
                        SkillLevel level = getLevelFromString(techniqueName[techniqueName.length - 1]);
                        techniqueName = Arrays.copyOfRange(techniqueName, 0, techniqueName.length - 1);

                        waitingForDescription.remove(player.getUniqueId());
                        sendTechniqueWithDescription(player, techniqueName, level, String.join(" ", Arrays.copyOfRange(split, 1, split.length)));
                    } else {
                        player.sendMessage("§4HRP : §7Il faut d'abord enregistrer le nom et le niveau de la technique avec /techniquemj <nom de la technique> <niveau de la technique>");
                    }

                }
                // /techniquemj <nom de la technique> <optionel:niveau de la technique>
                else {
                    SkillLevel level = getLevelFromString(split[split.length - 1]);

                    //Si le niveau est précisé, c'est qu'on veut une description, il faudra donc refaire la commande avec la description
                    if (level != SkillLevel.NULL) {
                        waitingForDescription.put(player.getUniqueId(), split);
                        player.sendMessage("§4HRP : §aNom et niveau de technique enregistrés, /techniquemj description <description> pour envoyer la technique");
                    } else {
                        sendTechnique(player, split);
                    }
                }
            }
        }
    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (args.length == 1 && waitingForDescription.containsKey(((Player) sender).getUniqueId())) {
            return StringUtil.copyPartialMatches(args[0], Collections.singletonList("description"), new ArrayList<>());
        } else if (args.length > 1 && !args[0].equalsIgnoreCase("description")) {

            List<String> completions = new ArrayList<>();
            for (SkillLevel skillLevel : SkillLevel.values()) {
                completions.add(skillLevel.getCharName());
            }
            return StringUtil.copyPartialMatches(args[args.length - 1], completions, new ArrayList<>());
        }
        return Collections.emptyList();
    }


    private SkillLevel getLevelFromString(String levelString) {
        //If the player is waiting for a description, set the description and send the technique
        SkillLevel level = SkillLevel.NULL;
        //If split ends with one of the skill levels, set the level to that skill level
        for (SkillLevel skillLevel : SkillLevel.values()) {
            if (levelString.equalsIgnoreCase(skillLevel.getCharName())) {
                level = skillLevel;
                break;
            }
        }
        return level;
    }

    private void sendTechnique(Player player, String[] split) {
        String techniqueName = translateHexColorCodes("#", "", String.join(" ", split).replace("&", "§"));
        for (Player p : player.getWorld().getPlayers()) {
            if (p.getWorld() == player.getWorld() && p.getLocation().distance(player.getLocation()) < 50)
                p.sendMessage("§c** " + player.getDisplayName() + " §créalise la technique " + techniqueName);
        }
    }

    private void sendTechniqueWithDescription(Player player, String[] techniqueName, SkillLevel level, String description) {

        String name = translateHexColorCodes(
                "#",
                "",
                String.join(" ", techniqueName).replace("&", "§")
        );
        String message = "";
        int range = 50;
        message += "§c** " + player.getDisplayName() + " §r§créalise la technique ";
        String newname = name;
        newname = newname.concat(" §f[" + level.getCharName() + "]");

        Skill.affichejutsu(
                player,
                PlayerInfo.getPlayerInfo(player),
                range,
                new TextComponent(message),
                new TextComponent(name),
                ItemUtil.createItemStack(Material.BOOK, 1, newname, formatLore(description, player)),
                true,
                false,
                null);
    }


    // Copy past from Skill.java : ugly af
    public ArrayList<String> formatLore(String message, Player player) {
        ArrayList<String> lore = new ArrayList<>();
        int taille = message.length();
        int tailledef = taille;
        int divi = 1;
        while (tailledef > 50) {
            divi++;
            tailledef = taille / divi;
        }
        int borneinf = 0;
        for (int i = 0; i < divi; i++) {
            int bornesupp = tailledef * (i + 1);
            while (bornesupp < message.length() && message.charAt(bornesupp) != ' ') {
                bornesupp++;
            }
            if (divi - 1 == i) {
                bornesupp = taille;
            }
            while (message.substring(borneinf, bornesupp).startsWith(" ")) {
                borneinf++;
            }
            lore.add("§7" + Skill.formatEncaMessage(message.substring(borneinf, bornesupp), player));
            borneinf = bornesupp;
        }
        return lore;
    }
    
    public static String translateHexColorCodes(String startTag, String endTag, String message) {
        final Pattern hexPattern = Pattern.compile(startTag + "([A-Fa-f0-9]{6})" + endTag);
        Matcher matcher = hexPattern.matcher(message);
        StringBuffer buffer = new StringBuffer(message.length() + 4 * 8);
        while (matcher.find()) {
            String group = matcher.group(1);
            matcher.appendReplacement(buffer, COLOR_CHAR + "x"
                    + COLOR_CHAR + group.charAt(0) + COLOR_CHAR + group.charAt(1)
                    + COLOR_CHAR + group.charAt(2) + COLOR_CHAR + group.charAt(3)
                    + COLOR_CHAR + group.charAt(4) + COLOR_CHAR + group.charAt(5)
            );
        }
        return matcher.appendTail(buffer).toString();
    }
}




