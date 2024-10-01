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
    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        if (split.length == 3) {
            if (sender.isOp()) {
                if (split[0].equals("mission")) {
                    if ("get".equals(split[1])) {
                        Player p = Bukkit.getServer().getPlayer(split[2]);
                        if (p != null) {
                            PlayerInfo playerInfo = PlayerInfo.getPlayerInfo(p);
                            sender.sendMessage("§cHRP : " + p.getDisplayName() + "§7 a obtenu §6" + playerInfo.getManaMission() + " §7chakra en récompense de ses §6"+ playerInfo.getNbmission()+" §7mission(s).");
                        } else {
                            sender.sendMessage("§cHRP : §7Le joueur n'est pas connecté.");
                        }
                    } else {
                        sendHelpList(sender);
                    }
                } else {
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
                                break;
                            case "removetype":
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
                                break;
                            case "add":
                                if (StringUtils.isNumeric(split[2])) {
                                    int amount = Integer.parseInt(split[2]);
                                    targetInfo.addMana(amount);
                                    sender.sendMessage(target.getDisplayName() + ChatColor.GREEN + " a gagné " + ChatColor.GOLD + amount + ChatColor.GREEN + " point(s) de chakra.");
                                } else {
                                    sendHelpList(sender);
                                }
                                break;
                            case "remove":
                                if (StringUtils.isNumeric(split[2])) {
                                    int amount = Integer.parseInt(split[2]);
                                    targetInfo.removeMana(amount);
                                    sender.sendMessage(target.getDisplayName() + ChatColor.GREEN + " a perdu " + ChatColor.GOLD + amount + ChatColor.GREEN + " point(s) de chakra.");
                                } else {
                                    sendHelpList(sender);
                                }
                                break;
                            case "setmax":
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
                                break;
                            case "set":
                                if (StringUtils.isNumeric(split[2])) {
                                    int amount = Integer.parseInt(split[2]);
                                    targetInfo.setMana(amount);
                                    sender.sendMessage(target.getDisplayName() + ChatColor.GREEN + " a désormais " + ChatColor.GOLD + amount + ChatColor.GREEN + " point(s) de chakra.");
                                } else {
                                    sendHelpList(sender);
                                }
                                break;
                            default:
                                sendHelpList(sender);
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "Le joueur n'est pas connecté !");
                    }
                }
            }
        } else if (split.length == 2) {
            if (sender.isOp()) {
                Player target = sender.getServer().getPlayer(split[1]);
                if (target != null) {
                    PlayerInfo targetInfo = PlayerInfo.getPlayerInfo(target);
                    if ("info".equals(split[0])) {
                        sender.sendMessage(target.getDisplayName() + ChatColor.GRAY + ": §6" + targetInfo.getMana() + "§7/§6" + targetInfo.getMaxMana());
                    } else {
                        sendHelpList(sender);
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "Le joueur n'est pas connecté !");
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Vous n'avez pas la permission !");
            }
        }else if(split.length == 1){
            if ("roll".equals(split[0])) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    PlayerInfo pInfo = PlayerInfo.getPlayerInfo(p);

                    sender.sendMessage("" + pInfo.getChakraType());

                    if (pInfo.getChakraType() == null || pInfo.getChakraType().equals("§7Non défini") || pInfo.getChakraType().equals("") || pInfo.getChakraType().isEmpty()) {

                        ItemStack item = p.getEquipment().getItemInMainHand();
                        boolean isChakraPaper = NBT.get(item, nbt -> (boolean) nbt.getBoolean("ChakraPaper"));

                        sender.sendMessage("" + isChakraPaper);

                        if (isChakraPaper) {
                            Random r = new Random();
                            int i = r.nextInt(4) + 1;
                            String encaMessage = "";
                            switch (i) {
                                case 1:
                                    pInfo.addChakraType(ChakraType.RAITON, 0);
                                    encaMessage = "§b** Le papier se froisse **";
                                    break;
                                case 2:
                                    pInfo.addChakraType(ChakraType.DOTON, 0);
                                    encaMessage = "§b** Le papier se salit avant de s'effriter **";
                                    break;
                                case 3:
                                    pInfo.addChakraType(ChakraType.KATON, 0);
                                    encaMessage = "§b** Le papier s'enflamme **";
                                    break;
                                case 4:
                                    pInfo.addChakraType(ChakraType.FUUTON, 0);
                                    encaMessage = "§b** Le papier se coupe en deux **";
                                    break;
                                case 5:
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
                        if(p != null) {
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
                            sender.sendMessage("§cHRP : §7Le joueur n'est pas connecté");
                        }
                        break;
                    case "remove":
                        if(p != null) {
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
                            sender.sendMessage("§cHRP : §7Le joueur n'est pas connecté");
                        }
                        break;
                    default:
                        sendHelpList(sender);
                }
            }
            else if(split[0].equals("complement")) {
                if(split[1].equals("add")) {
                    Player p = Bukkit.getServer().getPlayer(split[2]);
                    if(p == null) {
                        sender.sendMessage("§cHRP : §7Le joueur n'est pas connecté.");
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
                else if(split[1].equals("remove")) {
                    Player p = Bukkit.getServer().getPlayer(split[2]);
                    if(p == null) {
                        sender.sendMessage("§cHRP : §7Le joueur n'est pas connecté.");
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
                complete(completion, "roll", split[0]);
                if(sender.isOp()) {
                    complete(completion, "add", split[0]);
                    complete(completion, "remove", split[0]);
                    complete(completion, "setmax", split[0]);
                    complete(completion, "set", split[0]);
                    complete(completion, "addtype", split[0]);
                    complete(completion, "removetype", split[0]);
                    complete(completion, "mission", split[0]);
                    complete(completion, "complement", split[0]);
                }
                break;
            case 2 :
                if(sender.isOp()) {
                    if(split[0].equals("mission") || split[0].equals("meditation") || split[0].equals("complement")) {
                        complete(completion, "add", split[1]);
                        complete(completion, "remove", split[1]);
                        if(split[0].equals("mission")) {
                            complete(completion, "get", split[1]);
                        }
                    }
                    else if(split[0].equals("addtype") || split[0].equals("removetype") || split[0].equals("add") ||
                            split[0].equals("remove") || split[0].equals("setmax") || split[0].equals("set")) {
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            complete(completion, p.getName(), split[1]);
                        }
                    }
                }
                break;
            case 3 :
                if(sender.isOp()) {
                    if(split[0].equals("mission") || split[0].equals("meditation") || split[0].equals("complement")) {
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
                }
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
            helpList.add("§6/chakra §ecomplement §7set (joueur) (nb) §8- Ajoute ou retire du chakra maximum au joueur");
            helpList.add("§6/chakra §emeditation §7add (joueur) §8- Permet d'ajouter le chakra de meditation (le palier).");
            helpList.add("§6/chakra §emeditation §7remove (joueur) §8- Permet de retirer le chakra de meditation (le palier).");
        }

        for(String s : helpList){
            sender.sendMessage(s);
        }
    }

    @Override
    protected boolean isOpOnly() {
        return false;
    }
}