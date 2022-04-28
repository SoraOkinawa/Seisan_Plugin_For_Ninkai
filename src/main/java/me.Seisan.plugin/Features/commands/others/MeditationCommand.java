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

    private static int FINISHMEDIT = 12;
    int STARTINGMEDIT = 0;
    int CANSTOPMEDIT = 3;

    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        if(split.length == 0 && sender instanceof Player) {
            Player p = (Player)sender;
            PlayerInfo pInfo = PlayerInfo.getPlayerInfo(p);
            // Il est en train de méditer
            if(Main.getInMedit().containsKey(p.getName())) {
                // Il a passer le temps
                if(Main.getInMedit().get(p.getName()) >= CANSTOPMEDIT) {
                    Main.getInMedit().remove(p.getName());
                    Main.getInBulleMedit().remove(p.getName());
                    exitMedit(p.getName(), false);
                }
                else {
                    p.sendMessage("§b** Votre personnage est coincé dans son état méditatif.");
                    p.sendMessage("§cHRP : §7Il reste AU MAXIMUM "+(15-(Main.getInMedit().get(p.getName())*5))+" minutes de méditation pour sortir.");
                }
            }
            else {
                // Si il est dans la zone et ne médite pas
                if(isInRect(p)) {
                    // Il a la compétence
                    if(pInfo.hasAbility("meditation_ouverte")) {
                        int ticket = pInfo.getTicketmedit();
                        if (ticket > 0 || pInfo.getManamaze() == 300) {
                            // On lui retire un ticket
                            pInfo.setTicketmedit(ticket - 1);
                            pInfo.getPlayerClone().createCloneMedit();
                            if (pInfo.getOldpos() == null || pInfo.getOldpos().equals("") || pInfo.getOldpos().equals(" "))
                                pInfo.setOldpos(p.getWorld().getName() + ";" + p.getLocation().getX() + ";" + p.getLocation().getY() + ";" + p.getLocation().getZ());
                            p.teleport(new Location(Bukkit.getWorld("meditation"), -2, 41, 5));
                            // On le met dans la fiche médit
                            Main.getInMedit().put(p.getName(), STARTINGMEDIT);
                            // On l'autorise à voler à 50 de mana de laby
                            if(pInfo.getManamaze() >= 50) p.setAllowFlight(true);
                            // On lance la verif de toutes les 5 minutes
                            Main.dbManager.getMeditationDB().getMeditation(p.getUniqueId().toString());
                            MeditationArea.switchInventory(p);
                            verifMedit(p.getName());
                            sendMessage(p);

                        } else {
                            p.sendMessage("§b** Vous a déjà médité il y a peu de temps et ne pouvez méditer de nouveau pour le moment.");
                        }
                    }
                    else {
                        p.sendMessage("§b** Votre personnage n'a pas reçu l'enseignement nécessaire pour méditer.");
                    }
                }
                else {
                    p.sendMessage("§b** Vous n'êtes pas au bon endroit pour méditer.");
                }
            }
        }
        if(split.length == 1) {
            Player p;
            PlayerInfo pInfo;
            switch (split[0]) {
                case "bulle":
                    if(!(sender instanceof Player)) {
                        return;
                    }
                    p = (Player)sender;
                    pInfo = PlayerInfo.getPlayerInfo(p);

                    if(pInfo.getManamaze() < 100) {
                        p.sendMessage("§b** Vous ne pouvez pas encore créer votre propre bulle de méditation, continuez de vous entrainer");
                        return;
                    }

                    if(Main.getInMedit().containsKey(p.getName())) {
                        p.sendMessage("§b** Vous méditez déjà.");
                        return;
                    }


                    if(!isInRect(p)) {
                        p.sendMessage("§b** Vous n'êtes pas au bon endroit pour méditer.");
                        return;
                    }

                    // Il a pas la compétence
                    if(!pInfo.hasAbility("meditation_ouverte")) {
                        p.sendMessage("§b** Votre personnage n'a pas reçu l'enseignement nécessaire pour méditer.");
                        return;
                    }

                    int ticket = pInfo.getTicketmedit();
                    if (ticket == 0 && pInfo.getManamaze() != 300) {
                        p.sendMessage("§b** Vous a déjà médité il y a peu de temps et ne pouvez méditer de nouveau pour le moment.");
                        return;
                    }

                    // Il a sa bulle
                    pInfo.setTicketmedit(ticket - 1);
                    pInfo.getPlayerClone().createCloneMedit();
                    if (pInfo.getOldpos() == null || pInfo.getOldpos().equals("") || pInfo.getOldpos().equals(" "))
                        pInfo.setOldpos(p.getWorld().getName() + ";" + p.getLocation().getX() + ";" + p.getLocation().getY() + ";" + p.getLocation().getZ());

                    // X est dans la bulle de X (la sienne, du coup)
                    Main.getInBulleMedit().put(p.getName(), p.getName());
                    Meditation m = Meditation.getMeditationFromUUID(p.getUniqueId().toString());
                    if(m == null) {
                        m = Main.dbManager.getMeditationDB().getMeditation(p.getUniqueId().toString());
                    }
                    if(!m.isHasmedit()) MeditationArea.initMeditation(m);


                    MeditationArea.switchInventory(p);
                    p.setGameMode(GameMode.CREATIVE);
                    // TP
                    Location location_spawn = MeditationArea.getSpawn(m);
                    if(location_spawn == null) {
                        p.sendMessage("§cHRP : §7Erreur lors du chargement.");
                        return;
                    }
                    p.teleport(location_spawn);
                    // On le met dans la fiche médit
                    Main.getInMedit().put(p.getName(), STARTINGMEDIT);
                    // On lance la verif de toutes les 5 minutes

                    verifMedit(p.getName());
                    sendMessage(p);
                    
                    break;
                case "list":
                    if(sender.isOp()) {
                        sender.sendMessage("§cHRP : §7Voici les différentes personnes en méditation :");
                        for(String name : Main.inMedit.keySet()) {
                            sender.sendMessage("§7"+name+" médite depuis au moins "+(Main.inMedit.get(name)*5)+" minute(s).");
                        }
                    }
                    break;
                case "setspawn":
                    if(!(sender instanceof Player)) {
                        return;
                    }

                    p = (Player)sender;
                    if(!Main.getInBulleMedit().containsKey(p.getName()) || !Main.getInBulleMedit().get(p.getName()).equals(p.getName())) {
                        sender.sendMessage("§cHRP : §7Vous n'êtes pas dans votre bulle de méditation.");
                        return;
                    }

                    p = (Player)sender;
                    MeditationArea.changeSpawn(p);
                    sender.sendMessage("§b** Vous vous synchronisez avec votre environnement et vous liez cet emplacement comme point origine");

                    break;
                default:
                    sender.sendMessage("§cHRP : §7/meditation bulle");
                    sender.sendMessage("§cHRP : §7/meditation setspawn");
                    sender.sendMessage("§cHRP : §7/meditation accept pseudoMC");
                    sender.sendMessage("§cHRP : §7/meditation deny pseudoMC");
                    if(sender.isOp()) {
                        sender.sendMessage("§cHRP : §7/meditation list");
                    }
                    break;
            }
        }
        else if(split.length == 2) {
            Player target;
            Player p = (Player)sender;
            switch (split[0]) {
                case "accept":
                    if(!Main.getAskMedit().containsKey(split[1])) {
                        sender.sendMessage("§cHRP : §7Ce joueur n'a pas demandé à se lier à vous ou le lien a été interrompu.");
                        return;
                    }
                    target = Bukkit.getPlayer(split[1]);
                    if(target == null) {
                        Main.getAskMedit().remove(split[1]);
                        sender.sendMessage("§cHRP : §7Le joueur s'est déconnecté entre temps et l'opération ne peut aboutir.");
                        return;
                    }
                    if(!isInRect(target)) {
                        Main.getAskMedit().remove(split[1]);
                        target.sendMessage("§b** Vous n'êtes pas au bon endroit pour méditer.");
                        sender.sendMessage("§b** Vous avez perdu le lien suite à un manque de contact physique.");
                        return;
                    }

                    PlayerInfo tInfo = PlayerInfo.getPlayerInfo(target);
                    int ticket = tInfo.getTicketmedit();
                    if (ticket == 0 && tInfo.getManamaze() != 300) {
                        target.sendMessage("§b** Vous   a déjà médité il y a peu de temps et ne pouvez méditer de nouveau pour le moment.");
                        sender.sendMessage("§b** Vous avez perdu le lien suite à un manque de repos de votre partenaire.");
                        return;
                    }
                    tInfo.setTicketmedit(ticket - 1);
                    tInfo.getPlayerClone().createCloneMedit();
                    if (tInfo.getOldpos() == null || tInfo.getOldpos().equals("") || tInfo.getOldpos().equals(" "))
                        tInfo.setOldpos(target.getWorld().getName() + ";" + target.getLocation().getX() + ";" + target.getLocation().getY() + ";" + target.getLocation().getZ());


                    Meditation m = Meditation.getMeditationFromUUID(p.getUniqueId().toString());
                    if(m == null) {
                        m = Main.dbManager.getMeditationDB().getMeditation(p.getUniqueId().toString());
                    }
                    Location location_spawn = MeditationArea.getSpawn(m);
                    if(location_spawn == null) {
                        target.sendMessage("§cHRP : §7Erreur lors du chargement.");
                        return;
                    }

                    target.teleport(location_spawn);
                    Main.getInMedit().put(target.getName(), STARTINGMEDIT);
                    Main.getInBulleMedit().put(target.getName(), p.getName());

                    MeditationArea.switchInventory(target);
                    if(tInfo.getManamaze() >= 50) {
                        target.setGameMode(GameMode.CREATIVE);
                    }
                    else {
                        target.setGameMode(GameMode.SURVIVAL);
                    }


                    Main.getAskMedit().remove(target.getName());
                    verifMedit(target.getName());
                    sendMessage(target);

                    break;
                case "deny":
                    if(!Main.getAskMedit().containsKey(split[1])) {
                        sender.sendMessage("§cHRP : §7Ce joueur n'a pas demandé à se lier à vous ou le lien a été interrompu.");
                        return;
                    }
                    target = Bukkit.getPlayer(split[1]);
                    if(target == null) {
                        Main.getAskMedit().remove(split[1]);
                        sender.sendMessage("§cHRP : §7Le joueur s'est déconnecté entre temps et l'opération ne peut aboutir.");
                        return;
                    }

                    Main.getAskMedit().remove(target.getName());
                    sender.sendMessage("§b** Vous avez refusé de former un lien de méditation avec "+target.getDisplayName());
                    target.sendMessage("§b** "+p.getDisplayName()+" a refusé de former un lien de méditation avec vous.");
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split)
    {
        List<String> completion = new ArrayList();
        switch (split.length) {
            case 1:
                complete(completion, "bulle", split[0]);
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

    public static void verifMedit(String name) {
        int id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.plugin(), () -> {
            Player p = Bukkit.getPlayer(name);
            if(p != null) {
                int min = Main.getInMedit().get(name)+1;
                if(min == FINISHMEDIT) {
                    Main.getInMedit().remove(name);
                    Main.getInBulleMedit().remove(name);
                    exitMedit(name, false);
                }
                else {
                    Main.getInMedit().put(name, min);
                }
            }
        }, 60 * 5 * 20L, 60 * 5 * 20L); // check tous les 5 minutes
        Main.getIdMedit().put(name, id);
    }

    public static void exitMedit(String name, boolean crash) {
        Player p = Bukkit.getPlayer(name);
        if(p != null) {
            if(!p.isOp()) {
                if(p.isFlying()) {
                    p.setFlying(false);
                }
                p.setAllowFlight(false);
            }
            if(p.isInsideVehicle()) {
                p.eject();
            }

            Location l = p.getLocation().clone();
            PlayerInfo pInfo = PlayerInfo.getPlayerInfo(p);
            if(pInfo.getPlayerClone() != null)
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

            // Le bonus à ceux qui sont restés
            if(!crash) {
                if(l.getWorld() == null)
                    return;
                for(Entity e : l.getWorld().getNearbyEntities(l, 20, 20, 20)) {
                    if(e instanceof Player) {
                        e.sendMessage("§b** "+p.getDisplayName()+" §best sorti de son état méditatif.");
                    }
                }
                int nb = pInfo.getManamaze()/4 + 15;
                if((pInfo.getManamaze()-10)%40 == 0)
                    nb++;
                pInfo.setMinmedit(pInfo.getMinmedit()+15);

                // Si il dépasse le quota
                if(nb <= pInfo.getMinmedit()) {
                    pInfo.setMinmedit(pInfo.getMinmedit()-nb);
                    if(pInfo.getManamaze() < 300) pInfo.setManamaze(pInfo.getManamaze()+10);
                }
                p.sendMessage("§b** Vous êtes revenu à vous, soudainement.");
                Bukkit.getScheduler().cancelTask(Main.getIdMedit().get(name));
                Main.getIdMedit().remove(name);
                pInfo.updateChakra();
            }
            else {
                pInfo.getPlayer().sendMessage("§cHRP : §7Un ticket vous a été remboursé pour votre déconnexion.");
                pInfo.setTicketmedit(pInfo.getTicketmedit()+1);
            }
        }
    }

    public boolean isInRect(Player player)
    {
        double[] dim = new double[2];

        dim[0] = -4902;
        dim[1] = -4930;
        Arrays.sort(dim);
        if(player.getLocation().getX() > dim[1] || player.getLocation().getX() < dim[0])
            return false;

        dim[0] = -3791;
        dim[1] = -3769;
        Arrays.sort(dim);
        if(player.getLocation().getZ() > dim[1] || player.getLocation().getZ() < dim[0])
            return false;

        dim[0] = 115;
        dim[1] = 130;
        Arrays.sort(dim);
        return !(player.getLocation().getY() > dim[1]) && !(player.getLocation().getY() < dim[0]);
    }

    private void sendMessage(Player p) {
        TextComponent message = new TextComponent( "§b** Votre personnage entre dans un état méditatif. §7[Cliquez ici]");
        message.setClickEvent( new ClickEvent( ClickEvent.Action.OPEN_URL, "https://docs.google.com/document/d/1y3APJYQopanQBzjcPKtG_obD2ZBSV2lOPb4VEapme5Q/edit" ) );
        message.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder( "§6Cliquez pour davantage d'informations." ).create() ) );
        p.spigot().sendMessage(message);
    }
}

