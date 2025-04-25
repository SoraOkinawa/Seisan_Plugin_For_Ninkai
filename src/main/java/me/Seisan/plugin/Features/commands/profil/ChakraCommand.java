package me.Seisan.plugin.Features.commands.profil;


import de.tr7zw.nbtapi.NBT;
import me.Seisan.plugin.Features.PlayerData.PlayerInfo;
import me.Seisan.plugin.Features.objectnum.ChakraType;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import me.Seisan.plugin.Main.Command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChakraCommand extends Command {
    public static final String PERMISSION_INFO              = "ninkai.chakra.info";
    public static final String PERMISSION_ROLL              = "ninkai.chakra.roll";
    public static final String PERMISSION_ADD               = "ninkai.chakra.add";
    public static final String PERMISSION_REMOVE            = "ninkai.chakra.remove";
    public static final String PERMISSION_SET               = "ninkai.chakra.set";
    public static final String PERMISSION_SET_MAX           = "ninkai.chakra.set.max";
    public static final String PERMISSION_TYPE_ADD          = "ninkai.chakra.type.add";
    public static final String PERMISSION_TYPE_REMOVE       = "ninkai.chakra.type.remove";
    public static final String PERMISSION_MISSION_GET       = "ninkai.chakra.mission.get";
    public static final String PERMISSION_MISSION_ADD       = "ninkai.chakra.mission.add";
    public static final String PERMISSION_MISSION_REMOVE    = "ninkai.chakra.mission.remove";
    public static final String PERMISSION_PASSIF_GET        = "ninkai.chakra.passif.get";
    public static final String PERMISSION_PASSIF_ADD        = "ninkai.chakra.passif.add";
    public static final String PERMISSION_PASSIF_REMOVE     = "ninkai.chakra.passif.remove";
    public static final String PERMISSION_COMPLEMENT_ADD    = "ninkai.chakra.complement.add";
    public static final String PERMISSION_COMPLEMENT_REMOVE = "ninkai.chakra.complement.remove";
    
    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        if (split.length == 3) {
            if (split[0].equals("mission")) {
                if ("get".equals(split[1]) && sender.hasPermission(PERMISSION_MISSION_GET)) {
                    Player p = Bukkit.getServer().getPlayer(split[2]);
                    if (p != null) {
                        PlayerInfo playerInfo = PlayerInfo.getPlayerInfo(p);
                        sender.sendMessage("§cHRP : " + p.getDisplayName() + "§7 a obtenu §6" + playerInfo.getManaMission() + " §7chakra en récompense de ses §6"+ playerInfo.getNbmission()+" §7mission(s).");
                    } else {
                        sender.sendMessage("§cHRP : §7Le joueur est inconnu ou n'est pas connecté.");
                    }
                } else {
                    sendHelpList(sender);
                }
            } else if (split[0].equals("passif")) {
                if (split[1].equals("get") && sender.hasPermission(PERMISSION_PASSIF_GET)) {
                    Player p = Bukkit.getServer().getPlayer(split[2]);
                    if (p != null) {
                        PlayerInfo playerInfo = PlayerInfo.getPlayerInfo(p);
                        sender.sendMessage("§cHRP : §7Le joueur §c" + p.getName() + " §7possède §a" + playerInfo.getPassiveMana() + " §7portions de chakra passif.");
                    } else
                        sender.sendMessage("§cHRP : §7Le joueur est inconnu ou n'est pas connecté.");
                } else
                    sendHelpList(sender);
            }
            else {
                Player target = Bukkit.getPlayer(split[1]);
                String nameType = split[2];
                int prct = 0;
                if(split[2].contains("_")) {
                    nameType = split[2].split("_")[0];
                    if(!StringUtils.isNumeric(split[2].split("_")[1])) {
                        sender.sendMessage("§cHRP : §7La seconde partie se doit d'être un chiffre.");
                        return;
                    }
                    prct = Integer.parseInt(split[2].split("_")[1]);
                    if(prct < 0 || prct > 75 || prct%25 != 0) {
                        sender.sendMessage("§cHRP : §7La seconde partie se doit d'être un multiple de 25.");
                        return;
                    }
                }
                ChakraType type = ChakraType.fromName(nameType);
                if (target != null) {
                    PlayerInfo targetInfo = PlayerInfo.getPlayerInfo(target);
                    switch (split[0]) {
                        case "addtype":
                            if (sender.hasPermission(PERMISSION_TYPE_ADD)) {
                                if (type != null) {
                                    if (type == ChakraType.NULL) {
                                        sender.sendMessage("§cHRP : §7La nature n'existe pas ou le nombre suivant n'est pas 25 ou 50");
                                        return;
                                    }
                                    if (!targetInfo.hasChakra(type) || (targetInfo.hasChakra(type) && targetInfo.getChakraType().get(type) != prct)) {
                                        targetInfo.addChakraType(type, prct);
                                        sender.sendMessage(ChatColor.GREEN + "La nature de chakra de " + target.getDisplayName() + ChatColor.GREEN + " est désormais composée du " + type.getName());
                                    } else {
                                        sender.sendMessage("§cHRP : §7Le joueur a déjà la nature.");
                                    }
                                } else {
                                    sendHelpList(sender);
                                }
                            }
                            break;
                        case "removetype":
                            if (sender.hasPermission(PERMISSION_TYPE_REMOVE)) {
                                if (type != null) {
                                    if (targetInfo.hasChakra(type)) {
                                        targetInfo.removeChakraType(type);
                                        sender.sendMessage(ChatColor.GREEN + "La nature de chakra de " + target.getDisplayName() + ChatColor.GREEN + " n'est désormais plus composée du " + type.getName());
                                    } else {
                                        sender.sendMessage("§2Eh... Il n'a pas la nature... tu ne peux pas lui retirer...");
                                    }
                                } else {
                                    sendHelpList(sender);
                                }
                            }
                            break;
                        case "add":
                            if (sender.hasPermission(PERMISSION_ADD)) {
                                if (StringUtils.isNumeric(split[2])) {
                                    int amount = Integer.parseInt(split[2]);
                                    targetInfo.addMana(amount);
                                    sender.sendMessage(target.getDisplayName() + ChatColor.GREEN + " a gagné " + ChatColor.GOLD + amount + ChatColor.GREEN + " point(s) de chakra.");
                                } else {
                                    sendHelpList(sender);
                                }
                            }
                            break;
                        case "remove":
                            if (sender.hasPermission(PERMISSION_REMOVE)) {
                                if (StringUtils.isNumeric(split[2])) {
                                    int amount = Integer.parseInt(split[2]);
                                    targetInfo.removeMana(amount);
                                    sender.sendMessage(target.getDisplayName() + ChatColor.GREEN + " a perdu " + ChatColor.GOLD + amount + ChatColor.GREEN + " point(s) de chakra.");
                                } else {
                                    sendHelpList(sender);
                                }
                            }
                            break;
                        case "setmax":
                            if (sender.hasPermission(PERMISSION_SET_MAX)) {
                                if (StringUtils.isNumeric(split[2])) {
                                    int amount = Integer.parseInt(split[2]);
                                    if (amount % 10 == 0) {
                                        targetInfo.setMaxMana(amount);
                                        sender.sendMessage(target.getDisplayName() + ChatColor.GREEN + " a désormais " + ChatColor.GOLD + amount + ChatColor.GREEN + " de chakra max.");
                                    } else {
                                        sender.sendMessage(ChatColor.RED + "Le nombre que vous donnez doit être un multiple de 10");
                                    }
                                } else {
                                    sendHelpList(sender);
                                }
                            }
                            break;
                        case "set":
                            if (sender.hasPermission(PERMISSION_SET)) {
                                if (StringUtils.isNumeric(split[2])) {
                                    int amount = Integer.parseInt(split[2]);
                                    targetInfo.setMana(amount);
                                    sender.sendMessage(target.getDisplayName() + ChatColor.GREEN + " a désormais " + ChatColor.GOLD + amount + ChatColor.GREEN + " point(s) de chakra.");
                                } else {
                                    sendHelpList(sender);
                                }
                            }
                            break;
                        default:
                            sendHelpList(sender);
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "Le joueur est inconnu ou n'est pas connecté !");
                }
            }
        } else if (split.length == 2) {
            if (sender.hasPermission(PERMISSION_INFO)) {
                Player target = sender.getServer().getPlayer(split[1]);
                if (target != null) {
                    PlayerInfo targetInfo = PlayerInfo.getPlayerInfo(target);
                    if ("info".equals(split[0])) {
                        sender.sendMessage(target.getDisplayName() + ChatColor.GRAY + ": §6" + targetInfo.getMana() + "§7/§6" + targetInfo.getMaxMana());
                    } else {
                        sendHelpList(sender);
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "Le joueur est inconnu ou n'est pas connecté !");
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Vous n'avez pas la permission !");
            }
        }else if(split.length == 1){
            if ("roll".equals(split[0]) && sender.hasPermission(PERMISSION_ROLL)) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    PlayerInfo pInfo = PlayerInfo.getPlayerInfo(p);

                    if (pInfo.getChakraType() == null || pInfo.getChakraType().equals("§7Non défini") || pInfo.getChakraType().equals("") || pInfo.getChakraType().isEmpty()) {

                        ItemStack item = p.getEquipment().getItemInMainHand();
                        boolean isChakraPaper = NBT.get(item, nbt -> (boolean) nbt.getBoolean("ChakraPaper"));

                        if (isChakraPaper) {
                            Random r = new Random();
                            int i = r.nextInt(5);
                            String encaMessage = "";
                            switch (i) {
                                case 0:
                                    pInfo.addChakraType(ChakraType.RAITON, 0);
                                    encaMessage = "§b** Le papier se froisse **";
                                    break;
                                case 1:
                                    pInfo.addChakraType(ChakraType.DOTON, 0);
                                    encaMessage = "§b** Le papier se salit avant de s'effriter **";
                                    break;
                                case 2:
                                    pInfo.addChakraType(ChakraType.KATON, 0);
                                    encaMessage = "§b** Le papier s'enflamme **";
                                    break;
                                case 3:
                                    pInfo.addChakraType(ChakraType.FUUTON, 0);
                                    encaMessage = "§b** Le papier se coupe en deux **";
                                    break;
                                case 4:
                                    pInfo.addChakraType(ChakraType.SUITON, 0);
                                    encaMessage = "§b** Le papier devient humide **";
                                    break;
                            }

                            p.sendMessage(encaMessage);
                            for (Entity e : p.getNearbyEntities(20, 20, 20)) {
                                if (e instanceof Player) {
                                    e.sendMessage(encaMessage);
                                }
                            }
                        } else {
                            p.sendMessage(ChatColor.RED + "Vous devez avoir un papier à chakra en main");
                        }
                    } else {
                        p.sendMessage(ChatColor.RED + "Votre nature de chakra est déjà définie.");
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "Player command only");
                }
            } else {
                sendHelpList(sender);
            }

        } else if(split.length == 4) {
            if(split[0].equals("mission")) {
                Player p = Bukkit.getServer().getPlayer(split[2]);
                switch (split[1]) {
                    case "add":
                        if(sender.hasPermission(PERMISSION_MISSION_ADD) && p != null) {
                            int amount;
                            if (!StringUtils.isNumeric(split[3])) {
                                amount = 1;
                            }
                            else {
                                amount = Integer.parseInt(split[3]);
                            }
                            PlayerInfo playerInfo = PlayerInfo.getPlayerInfo(p);
                            int newamount = amount + playerInfo.getNbmission();
                            playerInfo.setNbmission(newamount);
                            playerInfo.updateChakra();
                            playerInfo.ajoutInstinct();
                            sender.sendMessage(p.getDisplayName()+"§7 a désormais §6"+playerInfo.getManaMission()+"§7/200 de chakra de mission grâce à ses §6"+playerInfo.getNbmission()+"§7 missions.");
                            sender.sendMessage("§4N'oubliez pas de mettre à jour le document des missions !");
                        }
                        else {
                            sender.sendMessage("§cHRP : §7Le joueur est inconnu ou n'est pas connecté");
                        }
                        break;
                    case "remove":
                        if(sender.hasPermission(PERMISSION_MISSION_REMOVE) && p != null) {
                            int amount;
                            if (!StringUtils.isNumeric(split[3])) {
                                amount = -1;
                            }
                            else {
                                amount = -Integer.parseInt(split[3]);
                            }
                            PlayerInfo playerInfo = PlayerInfo.getPlayerInfo(p);
                            int newamount = amount + playerInfo.getNbmission();
                            playerInfo.setNbmission(newamount);
                            playerInfo.updateChakra();
                            playerInfo.ajoutInstinct();
                            sender.sendMessage(p.getDisplayName()+"§7 a désormais §6"+newamount+"§7/200 de chakra de mission grâce à ses §6"+playerInfo.getNbmission()+".");
                            sender.sendMessage("§4N'oubliez pas de mettre à jour le document des missions !");
                        }
                        else {
                            sender.sendMessage("§cHRP : §7Le joueur est inconnu ou n'est pas connecté");
                        }
                        break;
                    default:
                        sendHelpList(sender);
                }
            }
            else if(split[0].equals("complement")) {
                if(split[1].equals("add") && sender.hasPermission(PERMISSION_COMPLEMENT_ADD)) {
                    Player p = Bukkit.getServer().getPlayer(split[2]);
                    if(p == null) {
                        sender.sendMessage("§cHRP : §7Le joueur est inconnu ou n'est pas connecté.");
                        return;
                    }
                    PlayerInfo pInfo = PlayerInfo.getPlayerInfo(p);
                    if (!StringUtils.isNumeric(split[3])) {
                        sender.sendMessage("§cHRP : §7Le chiffre n'est pas entier et positif");
                        return;
                    }
                    int amount = Integer.parseInt(split[3]);
                    pInfo.setManaBonus(pInfo.getManaBonus()+amount);
                    p.sendMessage("§cHRP : §7Votre personnage a désormais un complément de "+pInfo.getManaBonus()+" portions de chakra.");
                    sender.sendMessage("§cHRP : §7"+p.getName()+" §7a désormais un complément de "+pInfo.getManaBonus()+" portions de chakra.");
                    pInfo.updateChakra();

                }
                else if(split[1].equals("remove") && sender.hasPermission(PERMISSION_COMPLEMENT_REMOVE)) {
                    Player p = Bukkit.getServer().getPlayer(split[2]);
                    if(p == null) {
                        sender.sendMessage("§cHRP : §7Le joueur est inconnu ou n'est pas connecté.");
                        return;
                    }
                    PlayerInfo pInfo = PlayerInfo.getPlayerInfo(p);
                    if (!StringUtils.isNumeric(split[3])) {
                        sender.sendMessage("§cHRP : §7Le chiffre n'est pas entier et positif");
                        return;
                    }
                    int amount = Integer.parseInt(split[3]);
                    pInfo.setManaBonus(pInfo.getManaBonus()-amount);
                    p.sendMessage("§cHRP : §7Votre personnage a désormais un complément de "+pInfo.getManaBonus()+" portions de chakra.");
                    sender.sendMessage("§cHRP : §7"+p.getName()+" §7a désormais un complément de "+pInfo.getManaBonus()+" portions de chakra.");
                    pInfo.updateChakra();
                }
            }
            else if(split[0].equals("passif") && ((split[1].equals("add") && sender.hasPermission(PERMISSION_PASSIF_ADD))) || (split[1].equals("remove") && sender.hasPermission(PERMISSION_PASSIF_REMOVE))) {
                Player p = Bukkit.getServer().getPlayer(split[2]);
                if (p != null) {
                    PlayerInfo playerInfo = PlayerInfo.getPlayerInfo(p);
                    int amount;
                    int newamount;
                    if (!StringUtils.isNumeric(split[3])) {
                        sender.sendMessage("§cHRP : §7Montant de chakra incorrect. Veuillez indiquer un chiffre.");
                    } else {
                        amount = Integer.parseInt(split[3]);
                        newamount = amount;
                        
                        if (split[1].equals("add")) newamount = amount + playerInfo.getPassiveMana();
                        if (split[1].equals("remove")) newamount = Math.max(amount - playerInfo.getPassiveMana(), 0);
    
                        playerInfo.setPassiveMana(newamount);
                        playerInfo.updateChakra();
                        playerInfo.ajoutInstinct();
                    }
                } else {
                    sender.sendMessage("§cHRP : §7Le joueur est inconnu ou n'est pas connecté");
                }
            }
            else {
                sendHelpList(sender);
            }
        }
        else {
            sendHelpList(sender);
        }
    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split)
    {
        List<String> completion = new ArrayList();
        switch (split.length) {
            case 1 :
                if (sender.hasPermission(PERMISSION_ROLL)) complete(completion, "roll", split[0]);
                if (sender.hasPermission(PERMISSION_ADD)) complete(completion, "add", split[0]);
                if (sender.hasPermission(PERMISSION_REMOVE)) complete(completion, "remove", split[0]);
                if (sender.hasPermission(PERMISSION_SET_MAX)) complete(completion, "setmax", split[0]);
                if (sender.hasPermission(PERMISSION_SET)) complete(completion, "set", split[0]);
                if (sender.hasPermission(PERMISSION_TYPE_ADD)) complete(completion, "addtype", split[0]);
                if (sender.hasPermission(PERMISSION_TYPE_REMOVE)) complete(completion, "removetype", split[0]);
                if (sender.hasPermission(PERMISSION_MISSION_ADD) || sender.hasPermission(PERMISSION_MISSION_REMOVE) || sender.hasPermission(PERMISSION_MISSION_GET)) complete(completion, "mission", split[0]);
                if (sender.hasPermission(PERMISSION_PASSIF_ADD) || sender.hasPermission(PERMISSION_PASSIF_REMOVE) || sender.hasPermission(PERMISSION_PASSIF_GET)) complete(completion, "passif", split[0]);
                if (sender.hasPermission(PERMISSION_COMPLEMENT_ADD) || sender.hasPermission(PERMISSION_COMPLEMENT_REMOVE)) complete(completion, "complement", split[0]);
                break;
            case 2 :
                if(split[0].equals("mission") || split[0].equals("passif") || split[0].equals("complement")) {
                    if ((split[0].equals("mission") && sender.hasPermission(PERMISSION_MISSION_ADD)) ||
                        (split[0].equals("passif") && sender.hasPermission(PERMISSION_PASSIF_ADD)) ||
                        (split[0].equals("complement") && sender.hasPermission(PERMISSION_COMPLEMENT_ADD)))
                    complete(completion, "add", split[1]);
                    
                    if ((split[0].equals("mission") && sender.hasPermission(PERMISSION_MISSION_REMOVE)) ||
                        (split[0].equals("passif") && sender.hasPermission(PERMISSION_PASSIF_REMOVE)) ||
                        (split[0].equals("complement") && sender.hasPermission(PERMISSION_COMPLEMENT_REMOVE)))
                    complete(completion, "remove", split[1]);
                    
                    if ((split[0].equals("mission") && sender.hasPermission(PERMISSION_MISSION_GET)) ||
                        (split[0].equals("passif") && sender.hasPermission(PERMISSION_PASSIF_GET)))
                    complete(completion, "get", split[1]);
                }
                else if(split[0].equals("addtype") || split[0].equals("removetype") || split[0].equals("add") ||
                        split[0].equals("remove") || split[0].equals("setmax") || split[0].equals("set")) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        complete(completion, p.getName(), split[1]);
                    }
                }
                break;
            case 3 :
                if(split[0].equals("mission") || split[0].equals("passif") || split[0].equals("complement")) {
                    if(split[1].equals("add") || split[1].equals("remove") || split[1].equals("get")) {
                        for(Player p : Bukkit.getOnlinePlayers()) {
                            complete(completion, p.getName(), split[2]);
                        }
                    }
                }
                else if(split[0].equals("addtype") || split[0].equals("removetype")) {
                    Player p = Bukkit.getPlayer(split[1]);
                    if(p != null) {
                        for(ChakraType chakraType : ChakraType.values()) {
                            complete(completion, chakraType.name.substring(2), split[2]);
                        }
                    }
                }
                break;
        }
        return completion;
    }

    private void sendHelpList(CommandSender sender) {
        ArrayList<String> helpList = new ArrayList<>();

        helpList.add("§6/chakra §eroll §8- Permet de découvrir sa nature première de chakra");
        if (sender.isOp()) {
            helpList.add("§6/chakra §eadd §7(nombre) (joueur) §8- Permet d'ajouter du chakra à un joueur (Ne peut dépasser le maximum)");
            helpList.add("§6/chakra §eremove §7(nombre) (joueur) §8- Permet de retirer du chakra à un joueur (Ne peut arriver en dessous de 0)");
            helpList.add("§6/chakra §esetmax §7(nombre) (joueur) §8- Permet de changer le chakra maximal d'un joueur");
            helpList.add("§6/chakra §eset §7(nombre) (joueur) §8- Permet de mettre le chakra d'un joueur à une valeur précise (Peut dépasser le maximum)");
            helpList.add("§6/chakra §einfo §7(joueur) §8- Permet d'avoir les infos de chakra d'un joueur");
            helpList.add("§6/chakra §eaddtype §7(type) (joueur) §8- Permet d'ajouter une nature de chakra d'un joueur");
            helpList.add("§6/chakra §eremovetype §7(type) (joueur) §8- Permet de retirer une nature de chakra d'un joueur");
            helpList.add("§6/chakra §emission §7add (joueur) (nb)§8- Permet d'ajouter du chakra de mission.");
            helpList.add("§6/chakra §emission §7remove (joueur) (nb) §8- Permet de retirer du chakra de mission.");
            helpList.add("§6/chakra §emission §7get (joueur) §8- Permet de connaître le chakra de mission.");
            helpList.add("§6/chakra §ecomplement §7add (joueur) (nb) §8- Ajoute du chakra maximum au joueur");
            helpList.add("§6/chakra §ecomplement §7remove (joueur) (nb) §8- Retire du chakra maximum au joueur");
            helpList.add("§6/chakra §epassif §7add (joueur) (nombre) §8- Permet d'ajouter du chakra passif.");
            helpList.add("§6/chakra §epassif §7remove (joueur) (nombre) §8- Permet de retirer du chakra passif.");
            helpList.add("§6/chakra §epassif §7get (joueur) §8- Permet de connaître le chakra passif d'un joueur.");
        }

        for(String s : helpList){
            sender.sendMessage(s);
        }
    }


}