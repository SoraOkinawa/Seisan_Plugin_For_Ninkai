package me.Seisan.plugin.Features.listener;

import me.Seisan.plugin.Features.Feature;
import me.Seisan.plugin.Features.Inventory.AbilityInventory;
import me.Seisan.plugin.Features.Inventory.CompetenceInventory;
import me.Seisan.plugin.Features.Inventory.TrainInventory;
import me.Seisan.plugin.Features.PlayerData.PlayerInfo;
import me.Seisan.plugin.Features.ability.Ability;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Objects;

/**
 * Created by Helliot on 30/04/2018
 */
public class TrainInventoryListener extends Feature {

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        if (e.getView().getTitle().startsWith("§8Fiche personnage de ")) {
            int slot = e.getSlot();
            Player p = (Player) e.getWhoClicked();
            e.setCancelled(true);
            ItemStack skull = e.getInventory().getItem(12);
            if (skull == null) {
                return;
            }
            SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
            if (skullMeta == null || skullMeta.getOwningPlayer() == null || skullMeta.getOwningPlayer().getName() == null) {
                return;
            }

            Player target = Bukkit.getPlayer(skullMeta.getOwningPlayer().getName());
            PlayerInfo pInfo = PlayerInfo.getPlayerInfo(target);
            if (slot == 12) {
                if (pInfo.getApparenceprofil() != null && !pInfo.getApparenceprofil().equals("")) {
                    PlayerInfo.getAppareanceBook(pInfo.getApparenceprofil().split(";"), p);
                } else {
                    p.closeInventory();
                    p.sendMessage("§cHRP : §7Ce joueur n'a pas d'apparence disponible sur son profil.");
                }
                return;
            }
            // 20 22 24
            if (slot == 20 && (p.isOp() || p.getName().equals(pInfo.getPlayer().getName()))) {
                p.openInventory(AbilityInventory.getAbilitiesInventory(pInfo, pInfo.getStyleCombat().getName(), p, 0));
                return;
            }
            if (slot == 22 && (p.isOp() || p.getName().equals(pInfo.getPlayer().getName()))) {
                p.openInventory(AbilityInventory.getAbilitiesInventory(pInfo, pInfo.getClan().getName(), p, 0));
                return;
            }
            if (slot == 24 && (p.isOp() || p.getName().equals(pInfo.getPlayer().getName()))) {
                p.openInventory(AbilityInventory.getAbilitiesInventory(pInfo, pInfo.getVoieNinja().getName(), p, 0));
                return;
            }
            if (slot == 31) {
                p.openInventory(AbilityInventory.getAbilitiesInventory(pInfo, "Autre", p, 0));
                return;
            }
            if (slot == 37) {
                p.openInventory(AbilityInventory.getAbilitiesInventory(pInfo, "Force", p, 0));
                return;
            }
            if (slot == 39) {
                p.openInventory(AbilityInventory.getAbilitiesInventory(pInfo, "Vitesse", p, 0));
                return;
            }
            if (slot == 41) {
                p.openInventory(AbilityInventory.getAbilitiesInventory(pInfo, "Perception de la vitesse", p, 0));
                return;
            }
            if (slot == 43) {
                p.openInventory(AbilityInventory.getAbilitiesInventory(pInfo, "Instinct et expérience", p, 0));
            }
        } else if (e.getView().getTitle().startsWith("§8Apprentissage de compétences")) {
            int slot = e.getSlot();
            Player p = (Player) e.getWhoClicked();
            e.setCancelled(true);
            PlayerInfo pInfo = PlayerInfo.getPlayerInfo(p);
            if (slot == 31) {
                p.openInventory(CompetenceInventory.getCompetenceType(pInfo, p, "Autre"));
                return;
            }
            if (slot == 37) {
                p.openInventory(CompetenceInventory.getCompetenceType(pInfo, p, "Force"));
                return;
            }
            if (slot == 39) {
                p.openInventory(CompetenceInventory.getCompetenceType(pInfo, p, "Vitesse"));
                return;
            }
            if (slot == 41) {
                p.openInventory(CompetenceInventory.getCompetenceType(pInfo, p, "Perception de la vitesse"));
                return;
            }
            if (slot == 43) {
                p.openInventory(CompetenceInventory.getCompetenceType(pInfo, p, "Instinct et expérience"));
            }

        } else if (e.getView().getTitle().startsWith("§8Apprentissage :")) {
            int slot = e.getSlot();
            if (slot >= 0 && slot < 36) {
                Player p = (Player) e.getWhoClicked();
                e.setCancelled(true);
                ItemStack item = e.getInventory().getItem(slot);
                if (item != null) {
                    if (item.getType() == Material.GREEN_WOOL) {
                        if (item.getItemMeta() != null) {
                            if (Ability.getByRealName(item.getItemMeta().getDisplayName()) != null) {
                                p.openInventory(CompetenceInventory.getConfirmationCompetence(PlayerInfo.getPlayerInfo(p), p, item.getItemMeta().getDisplayName()));
                            }
                        }
                    }
                    if (slot == 31) {
                        p.openInventory(CompetenceInventory.getCompetenceGenerale(PlayerInfo.getPlayerInfo(p), p));
                    }
                }
            }
        } else if (e.getView().getTitle().startsWith("§7Confirmation :")) {
            int slot = e.getSlot();
            if (slot >= 0 && slot < 9) {
                Player p = (Player) e.getWhoClicked();
                PlayerInfo pInfo = PlayerInfo.getPlayerInfo(p);
                e.setCancelled(true);
                ItemStack item = e.getInventory().getItem(slot);
                if (item != null) {
                    String name = e.getView().getTitle().substring(17);
                    if (item.getType() == Material.GREEN_WOOL) {
                        Ability ability = Ability.getByRealName(name);
                        if (ability != null) {
                            if (!pInfo.getAbilities().contains(ability)) {
                                pInfo.setPoints(pInfo.getPoints() + pInfo.getPointsToAbility(ability.getNameInPlugin()) - ability.getPts());
                                pInfo.deletePointstoAbilities(ability);
                                pInfo.updateAbility(ability);
                                p.closeInventory();
                            }
                        }
                    } else if (item.getType() == Material.BOOK) {
                        Ability ability = Ability.getByRealName(name);
                        if (ability != null) {
                            p.closeInventory();
                            AbilityInventory.openInBook(p, ability.getDescription().split(";"));
                        }
                    } else {
                        p.sendMessage("§cVous avez annulé l'obtention de la compétence : §6" + name);
                        p.closeInventory();
                    }
                }
            }
        } else if (e.getView().getTitle().startsWith("§8Compétence ")) {
            int slot = e.getSlot();
            Player p = (Player) e.getWhoClicked();
            e.setCancelled(true);

            if (slot == 31) {
                ItemStack skull = e.getInventory().getItem(31);
                if (skull == null) {
                    return;
                }
                SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
                if (skullMeta == null || skullMeta.getOwningPlayer() == null || skullMeta.getOwningPlayer().getName() == null) {
                    return;
                }

                Player target = Bukkit.getPlayer(skullMeta.getOwningPlayer().getName());
                if (target != null) {
                    PlayerInfo pInfo = PlayerInfo.getPlayerInfo(target);
                    p.openInventory(TrainInventory.getFichePerso(pInfo, p));
                }
                return;
            }

            if ((e.getInventory().getItem(30) != null && slot == 30) || (e.getInventory().getItem(32) != null && slot == 32)) {
                ItemStack skull = e.getInventory().getItem(31);
                if (skull == null) {
                    return;
                }
                SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
                if (skullMeta == null || skullMeta.getOwningPlayer() == null || skullMeta.getOwningPlayer().getName() == null || !skullMeta.hasLore()) {
                    return;
                }
                int page = Integer.parseInt(Objects.requireNonNull(skullMeta.getLore()).get(0).substring(skullMeta.getLore().get(0).length() - 1));
                Player target = Bukkit.getPlayer(skullMeta.getOwningPlayer().getName());
                if(target == null) { return; }
                PlayerInfo pInfo = PlayerInfo.getPlayerInfo(target);
                String type = e.getView().getTitle().replace("§8Compétence : ", "");
                if (slot == 30) {
                    page = page - 2;
                }
                p.openInventory(AbilityInventory.getAbilitiesInventory(pInfo, type, p, page));
                return;

            }

            ItemStack item = e.getInventory().getItem(slot);
            if (item != null && item.getItemMeta() != null) {
                AbilityInventory.getAbilityToBook(p, Ability.getByRealName(item.getItemMeta().getDisplayName()));
            }
        }
    }
}
