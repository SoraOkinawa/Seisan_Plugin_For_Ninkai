package me.Seisan.plugin.Features.commands.others;

import me.Seisan.plugin.Features.PlayerData.PlayerInfo;
import me.Seisan.plugin.Main;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import me.Seisan.plugin.Main.Command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;

import java.util.ArrayList;
import java.util.List;


public class NinkenCommand extends Command {
    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        if(!((split.length == 2 || split.length == 1) && (split[0].equals("size"))) && (split.length != 1 || (!split[0].equals("spawn") && !split[0].equals("switch") && !split[0].equals("IA") && !split[0].equals("tphere")))) {
            sender.sendMessage("§cHRP & usage : §7/ninken spawn : permet de faire spawn/despawn le Ninken.");
            sender.sendMessage("§cHRP & usage : §7/ninken switch : permet de jouer / ne plus jouer le Ninken.");
            sender.sendMessage("§cHRP & usage : §7/ninken IA : permet d'activer/désactiver l'IA du loup.");
            sender.sendMessage("§cHRP & usage : §7/ninken tphere : permet de téléporter en HRP votre loup sur vous.");
            return;
        }

        PlayerInfo pInfo;
        Player p = null;
        String target = "";
        if(split.length == 1) {
            pInfo = PlayerInfo.getPlayerInfo((Player) sender);
        }
        else {
            target = split[1];
            p = Bukkit.getPlayer(target);
            if (p == null) {
                return;
            }
            pInfo = PlayerInfo.getPlayerInfo(p);
        }

        if(!pInfo.getClan().getName().equals("Inuzuka")) {
            sender.sendMessage("§cHRP : §7Votre personnage n'est pas du clan Inuzuka. Vous ne pouvez donc pas réaliser cette commande.");
            return;
        }


        String name = sender.getName();
        switch (split[0]) {
            case "size":
                // Il fait la commande lui même
                if(split.length == 1) {
                    if (Main.getIDninkenFromNamePlayer().containsKey(name)) {
                        Entity entity = Bukkit.getServer().getEntity(Main.getIDninkenFromNamePlayer().get(name));
                        if(entity instanceof Wolf && !((Wolf)entity).isAdult()) {
                            ((Wolf) entity).setAdult();
                            for (Entity e : entity.getNearbyEntities(25, 25, 25)) {
                                if (e instanceof Player) {
                                    e.sendMessage("§b**"+entity.getCustomName()+"§b a reprit sa taille adulte.");
                                }
                            }
                        }
                    } else {
                        sender.sendMessage("§cHRP : §7Votre ninken n'étant pas spawn, il n'a pu grandir.");
                    }
                }
                else {
                    if(sender.isOp()) {
                        if (Main.getIDninkenFromNamePlayer().containsKey(target)) {
                            Entity entity = Bukkit.getServer().getEntity(Main.getIDninkenFromNamePlayer().get(target));
                            if (entity instanceof Wolf) {
                                ((Wolf) entity).setBaby();
                            }
                        } else {
                            p.sendMessage("§cHRP : §7Votre ninken n'étant pas spawn, il n'a pu rétrécir. Invoquez le avant le jutsu, la prochaine fois ! (ou assurez de ne pas switch)");
                        }
                    }
                }
                break;
            case "spawn":
                if(!Main.getIsSwitch().containsKey(name)) {
                    if (Main.getIDninkenFromNamePlayer().containsKey(name)) {
                        /* Despawn*/
                        Entity entity = Bukkit.getServer().getEntity(Main.getIDninkenFromNamePlayer().get(name));
                        if (entity != null) {
                            entity.remove();
                            sender.sendMessage("§cHRP : §7Votre Ninken a despawn.");
                            Main.getIDninkenFromNamePlayer().remove(name);
                        }
                    } else {
                        Wolf wolf = createWolf(pInfo);
                        Main.getIDninkenFromNamePlayer().put(name, wolf.getUniqueId());
                        sender.sendMessage("§cHRP : §7Votre Ninken a spawn.");
                    }
                }
                else {
                    sender.sendMessage("§cHRP : §7/ninken switch pour refaire spawn votre loup.");
                }
                break;
            case "switch":
                if(Main.getIDninkenFromNamePlayer().containsKey(name)) {
                    /* Il va se mettre sur le loup */
                    Entity entity = Bukkit.getServer().getEntity(Main.getIDninkenFromNamePlayer().get(name));
                    if (entity != null) {
                        NPC npc = pInfo.getPlayerClone().createClone();
                        ((Player) sender).teleport(entity);
                        String display = "displayer "+name+" Wolf";
                        if(!((Wolf)entity).isAdult()) {
                            display = display.concat(" setBaby true");
                        }
                        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), display);
                        Main.getIDninkenFromNamePlayer().remove(name);
                        Main.getIsSwitch().put(name, npc);
                        entity.remove();
                        sender.sendMessage("§cHRP : §7Vous incarnez désormais le Ninken de votre personnage.");
                    }
                }
                else if(Main.getIsSwitch().containsKey(name)) {
                    /* Il refait pop le loup */
                    NPC e = Main.getIsSwitch().get(name);
                    if(e != null) {
                        Wolf wolf = createWolf(pInfo);
                        Main.getIDninkenFromNamePlayer().put(name, wolf.getUniqueId());
                        ((Player) sender).teleport(e.getEntity());
                        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "undisplayer "+name);
                        pInfo.getPlayerClone().destroy(e);
                        Main.getIsSwitch().remove(name);
                        sender.sendMessage("§cHRP : §7Vous incarnez à nouveau votre personnage.");
                    }
                    else {
                        sender.sendMessage("Erreur.");
                    }
                }
                else {
                    sender.sendMessage("§cHRP : §7Faites déjà apparaître votre ninken.");
                }
                break;
            case "IA":
                if(!Main.getIDninkenFromNamePlayer().containsKey(name)) {
                    sender.sendMessage("§cHRP : §7Le Ninken de votre personnage n'a pas pop / est joué par vous.");
                    return;
                }
                Wolf wolf = (Wolf)Bukkit.getServer().getEntity(Main.getIDninkenFromNamePlayer().get(name));
                if(wolf !=null) {
                    if(wolf.hasAI()) {
                        wolf.setAI(false);
                        sender.sendMessage("§cHRP : §7L'IA de votre Ninken a été retirée.");
                    }
                    else {
                        sender.sendMessage("§cHRP : §7L'IA de votre Ninken a été remise.");
                        wolf.setAI(true);
                    }
                }
                break;
            case "tphere":
                if(!Main.getIDninkenFromNamePlayer().containsKey(name)) {
                    sender.sendMessage("§cHRP : §7Le Ninken de votre personnage n'a pas pop / est joué par vous.");
                    return;
                }
                Entity entity = Bukkit.getServer().getEntity(Main.getIDninkenFromNamePlayer().get(name));
                if (entity != null) {
                    entity.teleport((Player)sender);
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
    private Wolf createWolf(PlayerInfo pInfo) {
        Player p = pInfo.getPlayer();
        Wolf wolf = (Wolf) p.getWorld().spawnEntity(p.getLocation(), EntityType.WOLF);
        wolf.setAge(1);
        String name = pInfo.getAttributClan() == null ? "Ninken de " + p.getDisplayName() : pInfo.getAttributClan();
        wolf.setCustomName(name);
        wolf.setCustomNameVisible(true);
        wolf.setAgeLock(true);
        if (pInfo.getLvL(pInfo.getClan().getName()) < 4) {
            wolf.setBaby();
        } else {
            wolf.setAdult();
        }
        wolf.setInvulnerable(true);
        wolf.setAI(false);
        return wolf;
    }
}
