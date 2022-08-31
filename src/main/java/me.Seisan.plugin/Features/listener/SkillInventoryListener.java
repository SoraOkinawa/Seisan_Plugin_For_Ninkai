package me.Seisan.plugin.Features.listener;

import me.Seisan.plugin.Features.Feature;
import me.Seisan.plugin.Features.Inventory.SkillInventory;
import me.Seisan.plugin.Features.PlayerData.PlayerInfo;
import me.Seisan.plugin.Features.objectnum.RPRank;
import me.Seisan.plugin.Features.skill.Skill;
import me.Seisan.plugin.Features.skill.SkillMastery;
import me.Seisan.plugin.Features.utils.DiscordWebhook;
import me.Seisan.plugin.Features.utils.ItemUtil;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

import static me.Seisan.plugin.Features.commands.others.ParcheminCommand.GiveParchemin;


public class SkillInventoryListener extends Feature {


    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        if(e.getClickedInventory() != null && (e.getView().getTitle().startsWith("§6Jutsu : ") || e.getView().getTitle().startsWith("§6Sceaux : "))) {
            if (e.getWhoClicked() instanceof Player) {
                Player p = (Player) e.getWhoClicked();
                PlayerInfo pInfo = PlayerInfo.getPlayerInfo(p);
                Inventory inv = e.getClickedInventory();
                e.setCancelled(true);
                int slot = e.getSlot();
                if (slot == 35 && inv.getItem(slot) != null) {
                    p.openInventory(SkillInventory.getElementInventory(pInfo));
                    return;
                }
                if (slot == 30 && inv.getItem(slot) != null) {
                    int actualPage = Integer.parseInt(inv.getItem(31).getItemMeta().getDisplayName().substring(7));
                    if (e.getView().getTitle().startsWith("§6Sceaux : ")) {
                        p.openInventory(SkillInventory.getSceaux(pInfo, e.getView().getTitle().substring(11), actualPage - 1));
                    }
                    else if (!e.getView().getTitle().equals("§6Jutsu : §7Tous")) {
                        p.openInventory(SkillInventory.getMainInventory(pInfo, actualPage - 1, e.getView().getTitle().substring(10)));
                    } else {
                        p.openInventory(SkillInventory.getMainInventory(pInfo, actualPage - 1));

                    }
                    return;
                }

                if (slot == 32 && inv.getItem(slot) != null) {
                    int actualPage = Integer.parseInt(inv.getItem(31).getItemMeta().getDisplayName().substring(7));
                    if (e.getView().getTitle().startsWith("§6Sceaux : ")) {
                        p.openInventory(SkillInventory.getSceaux(pInfo, e.getView().getTitle().substring(11), actualPage + 1));
                    }
                    else if (!e.getView().getTitle().equals("§6Jutsu : §7Tous")) {
                        p.openInventory(SkillInventory.getMainInventory(pInfo, actualPage + 1, e.getView().getTitle().substring(10)));
                    } else {
                        p.openInventory(SkillInventory.getMainInventory(pInfo, actualPage + 1));
                    }
                    return;
                }


                if (inv.getItem(slot) != null) {
                    if (e.getClickedInventory().getType() == InventoryType.CHEST) {
                        String skillName = inv.getItem(slot).getItemMeta().getDisplayName();
                        Skill skill = Skill.getByRealName(skillName);
                        if (skill != null) {
                            p.openInventory(SkillInventory.getSkillSelectInventory(skill, pInfo));
                        }
                    }
                }
            }
        }else if(e.getClickedInventory() != null && e.getView().getTitle().startsWith("§8HRP : §7Don")) {
            if (!(e.getWhoClicked() instanceof Player)) {
                return;
            }
            e.setCancelled(true);
            Player p = (Player) e.getWhoClicked();
            int slot = e.getSlot();
            if(slot >= 0 && slot < 27) {
                Inventory inv = e.getClickedInventory();
                if(e.getClickedInventory().getType() != InventoryType.CHEST) {
                    return;
                }
                ItemStack item = inv.getItem(slot);
                if(item == null || item.getType() == Material.AIR) {
                    return;
                }
                p.getInventory().addItem(item);
                p.sendMessage("§cHRP : §7Vous vous êtes give un item du /hrp. Attention aux abus !");
                p.closeInventory();
            }
        }else if(e.getClickedInventory() != null && e.getView().getTitle().startsWith("§8HRP : §7Poubelle")) {
            if (!(e.getWhoClicked() instanceof Player)) {
                return;
            }
            if(e.getClickedInventory().getType() == InventoryType.CHEST) {
                if(e.getCurrentItem() != null) {
                    e.getCurrentItem().setAmount(0);
                }
            }
        } else if(e.getClickedInventory() != null && e.getView().getTitle().startsWith("§8Technique : ")) {
            if (e.getWhoClicked() instanceof Player) {
                e.setCancelled(true);
                Player p = (Player) e.getWhoClicked();

                /* Optional ...  = if(PlayerInfo.getPlayerInfo(p) != null) */
                Optional.ofNullable(PlayerInfo.getPlayerInfo(p)).ifPresent(pInfo -> {
                    Inventory inv = e.getClickedInventory();

                    ItemStack itemskillname = inv.getItem(2);
                    if (itemskillname == null) {
                        return;
                    }
                    ItemMeta im = itemskillname.getItemMeta();
                    if (im == null) {
                        return;
                    }
                    String skillName = im.getDisplayName();
                    /* Optional ... = if(Skill.getByRealName(skillname) != null) */
                    Optional.ofNullable(Skill.getByRealName(skillName)).ifPresent(skill -> {
                        int slot = e.getSlot();
                        switch (slot) {
                            case 0:
                                if (pInfo.getPlayer().isOp()) {
                                    GiveParchemin(skill, pInfo.getPlayer());
                                }
                                break;
                            case 2:
                                if (skill.getInfosup() == null) {
                                    p.sendMessage("§cHRP : §7Cette compétence n'a pas encore de description détaillée. Patience !");
                                } else {
                                    PlayerInfo.getAppareanceBook(skill.getInfosup().replace("%displayname%", p.getDisplayName()).split(";"), p);
                                }
                                break;
                            case 4:
                                pInfo.setCurrentSkill(skill);
                                break;
                            case 5:
                                if (pInfo.getVoieNinja().getName().contains("Ninjutsu") && pInfo.getLvL("Ninjutsu") >= 2) {
                                    if (pInfo.getMastery(skill) == SkillMastery.LEARNED && !skill.getMudras().equals("none")) {
                                        if (pInfo.getMana() >= skill.manaToTake(pInfo)) {
                                            if (skill.TryJutsuOneHand(skill, pInfo)) {
                                                skill.takeMana(pInfo);


                                                String message = "§7[Une main] §c** " + p.getDisplayName() + " §r§créalise la technique "+skillName;
                                                int range = 50;

                                                TextComponent messagecomponent = new TextComponent(message);
                                                Skill.affichejutsu(p, pInfo, range, messagecomponent, ItemUtil.createItemStack(Material.BOOK, 1, skillName, skill.getLore(p)), skill.isSkillVisibility(), skill.isNeedTarget(), pInfo.getTarget());

                                                //Lancement de la technique
                                                ArrayList<String> commandToRun = skill.getCommandList();
                                                skill.runCommands(commandToRun, 0, p);
                                                p.closeInventory();
                                            }
                                        } else {
                                            p.sendMessage("§cVous n'avez pas assez de chakra pour lancer la technique. (§b" + pInfo.getMana() + "/" + skill.getManaCost());
                                        }
                                    }
                                }
                                /*
                                    Le roll bonus est affiché ici.
                                */
                                break;
                            case 6:
                                if (inv.getItem(slot) != null && inv.getItem(slot).hasItemMeta() && inv.getItem(slot).getItemMeta().hasDisplayName() &&
                                        inv.getItem(slot).getItemMeta().getDisplayName().startsWith("§2Ajouter")) {
                                    if (pInfo.getFavoriteList().size() < 27) {
                                        pInfo.getFavoriteList().add(skill);
                                    } else {
                                        p.sendMessage(ChatColor.RED + "Vous ne pouvez pas avoir plus de 27 favoris");
                                    }
                                } else if (inv.getItem(slot) != null && inv.getItem(slot).hasItemMeta() && inv.getItem(slot).getItemMeta().hasDisplayName() &&
                                        inv.getItem(slot).getItemMeta().getDisplayName().startsWith("§4Retirer")) {
                                    pInfo.getFavoriteList().remove(skill);
                                }
                                p.openInventory(SkillInventory.getSkillSelectInventory(skill, pInfo));
                                break;
                            case 7:
                                p.openInventory(SkillInventory.getElementInventory(pInfo));
                                break;

                            default:
                                break;
                        }
                    });


                });
            }
        }else if(e.getClickedInventory() != null && e.getView().getTitle().equals("§6Fuinjutsu : §7Choix du type de sceau")) {
            if(e.getWhoClicked() instanceof Player) {
                Player p = (Player) e.getWhoClicked();
                PlayerInfo pInfo = PlayerInfo.getPlayerInfo(p);
                Inventory inv = e.getClickedInventory();
                e.setCancelled(true);

                int slot = e.getSlot();
                ItemStack item = inv.getItem(slot);
                if(item == null || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
                    return;
                }
                if(slot == 1 || slot == 3 /* || slot == 4 */ || slot == 5 || slot == 7) {
                    String type = ChatColor.stripColor(item.getItemMeta().getDisplayName());
                    ArrayList<Skill> listsceaux = SkillInventory.getTypeSceaux(pInfo.getSkills().keySet(), type);
                    if(listsceaux.size() == 0) {
                        p.sendMessage("§cHRP : §7Votre personnage n'a pas de symboles dans cette catégorie.");
                        return;
                    }
                    p.openInventory(SkillInventory.getSceaux(pInfo, type, 0));
                    return;
                }
                if(slot == 13) {
                    p.openInventory(SkillInventory.getElementInventory(pInfo));
                }
            }
        }
        else if(e.getClickedInventory() != null && e.getView().getTitle().equals("§8Techniques favorites")){
            if(e.getWhoClicked() instanceof Player) {
                Player p = (Player) e.getWhoClicked();
                PlayerInfo pInfo = PlayerInfo.getPlayerInfo(p);
                Inventory inv = e.getClickedInventory();
                e.setCancelled(true);

                int slot = e.getSlot();

                if(slot == 31 && inv.getItem(slot) != null) {
                    p.openInventory(SkillInventory.getElementInventory(pInfo));
                    return;
                }

                if (inv.getItem(slot) != null) {
                    if(e.getClickedInventory().getType() == InventoryType.CHEST) {
                        String skillName = inv.getItem(slot).getItemMeta().getDisplayName();
                        Skill skill = Skill.getByRealName(skillName);
                        if (skill != null) {
                            p.openInventory(SkillInventory.getSkillSelectInventory(skill, pInfo));
                        }
                    }
                }
            }
        }else if (e.getClickedInventory() != null && e.getView().getTitle().equals("§6Éléments")){
            if (e.getWhoClicked() instanceof Player){
                Player p = (Player) e.getWhoClicked();
                PlayerInfo pInfo = PlayerInfo.getPlayerInfo(p);
                Inventory inv = e.getClickedInventory();
                e.setCancelled(true);

                int slot = e.getSlot();
                ItemStack item = inv.getItem(slot);

                if(slot == 37 && inv.getItem(slot) != null){
                    if(!pInfo.getFavoriteList().isEmpty()) {
                        p.openInventory(SkillInventory.getFavoriteInventory(pInfo));
                    }else{
                        p.sendMessage(ChatColor.RED + "Vous n'avez aucune technique favorite !");
                    }
                    return;
                }

                if(slot == 48 && inv.getItem(slot) != null) {
                    p.openInventory(SkillInventory.getMainInventory(pInfo, 0, "§7Autres"));
                    return;
                }
                if(slot == 50 && inv.getItem(slot) != null) {
                    p.openInventory(SkillInventory.getMainInventory(pInfo, 0));
                    return;
                }
                if (item != null && item.getType() != Material.GRAY_STAINED_GLASS_PANE){
                    if(e.getClickedInventory().getType() == InventoryType.CHEST) {
                        String skillName = item.getItemMeta().getDisplayName();
                        skillName = ChatColor.stripColor(skillName);
                        /* Pour ne pas ouvrir l'inventaire en conséquence */
                        if (SkillInventory.GetJutsuType(pInfo.getSkills()).contains(skillName)) {
                            p.openInventory(SkillInventory.getMainInventory(pInfo, 0, skillName));
                        }
                        else {
                            p.sendMessage("§cVous n'avez pas de jutsu dans cette catégorie.");
                        }
                    }
                }
            }
        }
        else if(e.getClickedInventory() != null && e.getView().getTitle().startsWith("§7Bibliothèque du Cercle : ")) {
            Player p = (Player) e.getWhoClicked();
            Inventory inv = e.getClickedInventory();
            e.setCancelled(true);
            int slot = e.getSlot();
            ItemStack item = inv.getItem(slot);
            String name = e.getView().getTitle().substring(27);

            int actualPage = Integer.parseInt(inv.getItem(49).getItemMeta().getDisplayName().substring(7));
            if(slot == 47 && item != null && item.getType() == Material.ARROW) {
                p.openInventory(SkillInventory.getInventoryBB(p, actualPage-2, name));
            }
            else if(slot == 51 && item != null && item.getType() == Material.ARROW) {
                p.openInventory(SkillInventory.getInventoryBB(p, actualPage, name));
            }
            if(e.getClickedInventory() != null && e.getClickedInventory().getType() == InventoryType.CHEST) {
                if(item != null) {
                    String skillName = inv.getItem(slot).getItemMeta().getDisplayName();
                    Skill skill = Skill.getByRealName(skillName);
                    Player target = Bukkit.getPlayer(name);
                    if (skill != null && target != null) {
                        GiveParchemin(skill, p);
                        SendLog(p, skill, target);
                        p.closeInventory();
                    }
                    else if(name.equals("Informations") && skill != null) {
                        PlayerInfo.getAppareanceBook(skill.getInfosup().replace("%displayname%", p.getDisplayName()).split(";"), p);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInvDrag(InventoryDragEvent e){
        if(e.getView().getTitle().startsWith("§6Eléments") || e.getView().getTitle().startsWith("§6Jutsu : ") || e.getView().getTitle().equals("§6Fuinjutsu : §7Choix du type de sceau") || e.getView().getTitle().equals("§7Bibliothèque du Cercle : ") || e.getView().getTitle().startsWith("§8Don : §7")){
            e.setCancelled(true);
        }
    }

    private void SendLog(Player p, Skill skill, Player target)  {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        PlayerInfo targetInfo = PlayerInfo.getPlayerInfo(target);
        DiscordWebhook webhook;
        switch(targetInfo.getRank().id) {
            case 2:
                // La cible est un Chuunin
                webhook = new DiscordWebhook("https://discord.com/api/webhooks/969326292635902002/8KJhFrXl3YrkYtThWU9HOfxvZNp-TMc9z12C60NJrbIXgQ5tZhbR5i9VI-NZnUXzkEPJ");
                break;
            case 3:
            case 4:
            case 5:
                // La cible est un Juunin ou
                // La cible est un Sannin ou
                // La cible est un(e) chef(fe) de village
                webhook = new DiscordWebhook("https://discord.com/api/webhooks/969326562279317535/bjUbAIp3YgvO6MF7vNIkOFwFAnd2Tb7iv2EDhTT2N_YgJ3i3Dt_uIpjZ3XDtBNkD-UE5");
                break;
            default:
                // La cible est un(e) étudiant(e) ou
                // La cible est un Genin
                webhook = new DiscordWebhook("https://discord.com/api/webhooks/968955265019961364/c6YAnhHgEvq-ilqQUk5D71dr5MQsGxnoxTneKKikbazCnZHg3WsDHiJ9kBOycg1CzADu");
                break;
        }
        webhook.setContent(ChatColor.stripColor(target.getDisplayName())+" (`"+target.getName()+"`) : "+skill.getLevel().getName()+" - "+ChatColor.stripColor(skill.getName())+" - "+ dtf.format(now));
        webhook.setUsername(ChatColor.stripColor(p.getDisplayName()) + " ["+p.getName()+"]");
        try {
            webhook.execute();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
