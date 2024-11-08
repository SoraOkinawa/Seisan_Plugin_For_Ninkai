package me.Seisan.plugin.Features.commands.others;

import me.Seisan.plugin.Features.PlayerData.Meditation;
import me.Seisan.plugin.Features.PlayerData.PlayerInfo;
import me.Seisan.plugin.Features.utils.MeditationArea;
import me.Seisan.plugin.Main;
import me.Seisan.plugin.Main.Command;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MeditationCommand extends Command {

    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        if (sender instanceof Player) {

            Player p = (Player) sender;
            PlayerInfo pInfo = PlayerInfo.getPlayerInfo(p);

            if (split.length == 0) {
                // Il est en train de méditer, il ressort
                if (Main.getInMedit().contains(p.getName())) {

                    Main.getInMedit().remove(p.getName());
                    Main.getInBulleMedit().remove(p.getName());
                    exitMedit(p.getName(), false);

                } else {
                    // Il a la compétence, on l'envoit dans la méditation
                    if (pInfo.hasAbility("meditation_ouverte")) {


                        pInfo.getPlayerClone().createCloneMedit();
                        if (pInfo.getOldpos() == null || pInfo.getOldpos().equals("") || pInfo.getOldpos().equals(" "))
                            pInfo.setOldpos(p.getWorld().getName() + ";" + p.getLocation().getX() + ";" + p.getLocation().getY() + ";" + p.getLocation().getZ());

                        // X est dans la bulle de X (la sienne, du coup)
                        Main.getInBulleMedit().put(p.getName(), p.getName());
                        Meditation m = Meditation.getMeditationFromUUID(p.getUniqueId().toString());
                        if (m == null) {
                            m = Main.dbManager.getMeditationDB().getMeditation(p.getUniqueId().toString());
                        }
                        if (!m.isHasmedit()) MeditationArea.initMeditation(m);


                        MeditationArea.switchInventory(p);
                        p.setGameMode(GameMode.CREATIVE);
                        // TP
                        Location location_spawn = MeditationArea.getSpawn(m);
                        if (location_spawn == null) {
                            p.sendMessage("§cHRP : §7Erreur lors du chargement.");
                            return;
                        }
                        p.teleport(location_spawn);
                        // On le met dans la fiche médit
                        Main.getInMedit().add(p.getName());

                        sendMessage(p);
                    } else {
                        // Il n'a pas la compétence
                        p.sendMessage("§cHRP : §7Vous n'avez pas appris à méditer.");
                    }
                }
            } else if (split.length == 1) {

                switch (split[0]) {
                    case "list":
                        if (sender.isOp()) {
                            sender.sendMessage("§cHRP : §7Voici les différentes personnes en méditation :");
                        }
                        break;
                    case "setspawn":
                        p = (Player) sender;
                        if (!Main.getInBulleMedit().containsKey(p.getName()) || !Main.getInBulleMedit().get(p.getName()).equals(p.getName())) {
                            sender.sendMessage("§cHRP : §7Vous n'êtes pas dans votre bulle de méditation.");
                            return;
                        }

                        p = (Player) sender;
                        MeditationArea.changeSpawn(p);
                        sender.sendMessage("§b** Vous vous synchronisez avec votre environnement et vous liez cet emplacement comme point origine");

                        break;
                    default:
                        sender.sendMessage("§cHRP : §7/meditation");
                        sender.sendMessage("§cHRP : §7/meditation setspawn");
                        sender.sendMessage("§cHRP : §7/meditation accept pseudoMC");
                        sender.sendMessage("§cHRP : §7/meditation deny pseudoMC");
                        if (sender.isOp()) {
                            sender.sendMessage("§cHRP : §7/meditation list");
                        }
                        break;
                }
            } else if (split.length == 2) {
                Player target;

                switch (split[0]) {
                    case "accept":
                        if (!Main.getAskMedit().containsKey(split[1])) {
                            sender.sendMessage("§cHRP : §7Ce joueur n'a pas demandé à se lier à vous ou le lien a été interrompu.");
                            return;
                        }
                        target = Bukkit.getPlayer(split[1]);
                        if (target == null) {
                            Main.getAskMedit().remove(split[1]);
                            sender.sendMessage("§cHRP : §7Le joueur s'est déconnecté entre temps et l'opération ne peut aboutir.");
                            return;
                        }

                        PlayerInfo tInfo = PlayerInfo.getPlayerInfo(target);
                        tInfo.getPlayerClone().createCloneMedit();
                        if (tInfo.getOldpos() == null || tInfo.getOldpos().equals("") || tInfo.getOldpos().equals(" "))
                            tInfo.setOldpos(target.getWorld().getName() + ";" + target.getLocation().getX() + ";" + target.getLocation().getY() + ";" + target.getLocation().getZ());

                        Meditation m = Meditation.getMeditationFromUUID(p.getUniqueId().toString());
                        if (m == null) {
                            m = Main.dbManager.getMeditationDB().getMeditation(p.getUniqueId().toString());
                        }
                        Location location_spawn = MeditationArea.getSpawn(m);
                        if (location_spawn == null) {
                            target.sendMessage("§cHRP : §7Erreur lors du chargement.");
                            return;
                        }
                        target.teleport(location_spawn);
                        Main.getInMedit().add(target.getName());
                        Main.getInBulleMedit().put(target.getName(), p.getName());

                        MeditationArea.switchInventory(target);
                        target.setGameMode(GameMode.CREATIVE);

                        Main.getAskMedit().remove(target.getName());
                        sendMessage(target);

                        break;
                    case "deny":
                        if (!Main.getAskMedit().containsKey(split[1])) {
                            sender.sendMessage("§cHRP : §7Ce joueur n'a pas demandé à se lier à vous ou le lien a été interrompu.");
                            return;
                        }
                        target = Bukkit.getPlayer(split[1]);
                        if (target == null) {
                            Main.getAskMedit().remove(split[1]);
                            sender.sendMessage("§cHRP : §7Le joueur s'est déconnecté entre temps et l'opération ne peut aboutir.");
                            return;
                        }

                        Main.getAskMedit().remove(target.getName());
                        sender.sendMessage("§b** Vous avez refusé de former un lien de méditation avec " + target.getDisplayName());
                        target.sendMessage("§b** " + p.getDisplayName() + " a refusé de former un lien de méditation avec vous.");
                        break;
                    default:
                        break;
                }
            }
        }
    }


    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String
            label, String[] split) {
        List<String> completion = new ArrayList();
        switch (split.length) {
            case 1:
                complete(completion, "accept", split[0]);
                complete(completion, "deny", split[0]);
                complete(completion, "setspawn", split[0]);
                if (sender.isOp()) {
                    complete(completion, "list", split[0]);
                }
                break;
            case 2:
                if (sender.isOp()) {
                    if (split[0].equals("accept") || split[0].equals("deny")) {
                        for (String name : Main.askMedit.keySet()) {
                            if (Main.askMedit.get(name).equals(sender.getName())) {
                                complete(completion, name, split[1]);
                            }
                        }
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

    public static void exitMedit(String name, boolean crash) {
        Player p = Bukkit.getPlayer(name);
        if (p != null) {
            if (!p.isOp()) {
                if (p.isFlying()) {
                    p.setFlying(false);
                }
                p.setAllowFlight(false);
            }
            if (p.isInsideVehicle()) {
                p.eject();
            }

            Location l = p.getLocation().clone();
            PlayerInfo pInfo = PlayerInfo.getPlayerInfo(p);
            if (pInfo.getPlayerClone() != null)
                pInfo.getPlayerClone().deleteCloneMedit();
            String[] oldpos = pInfo.getOldpos().split(";");


            p.setGameMode(GameMode.SURVIVAL);
            Main.dbManager.getMeditationDB().getMeditation(p.getUniqueId().toString());
            MeditationArea.switchInventory(p);
            Bukkit.getScheduler().runTaskLater(Main.plugin(), () -> {
                p.teleport(new Location(Bukkit.getWorld(oldpos[0]),
                        Double.parseDouble(oldpos[1]),
                        Double.parseDouble(oldpos[2]),
                        Double.parseDouble(oldpos[3])));
                pInfo.setOldpos("");
            }, 20);

            if (!crash) {
                if (l.getWorld() == null)
                    return;
                for (Entity e : l.getWorld().getNearbyEntities(l, 20, 20, 20)) {
                    if (e instanceof Player) {
                        e.sendMessage("§b** " + p.getDisplayName() + " §best sorti de son état méditatif.");
                    }
                }

                p.sendMessage("§b** Vous êtes revenu à vous, soudainement.");
                Main.getIdMedit().remove(name);
            }
        }
    }

    private void sendMessage(Player p) {
        TextComponent message = new TextComponent("§b** Votre personnage entre dans un état méditatif. **");
        p.spigot().sendMessage(message);
    }
}

