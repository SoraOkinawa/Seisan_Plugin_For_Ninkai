package me.Seisan.plugin.Features.listener;

import me.Seisan.plugin.Features.Feature;
import me.Seisan.plugin.Features.Inventory.PNJInventory;
import me.Seisan.plugin.Features.PlayerData.PlayerInfo;
import me.Seisan.plugin.Features.objectnum.Figurine;
import me.Seisan.plugin.Features.utils.Casino;
import me.Seisan.plugin.Features.utils.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

import static me.Seisan.plugin.Features.listener.Listener.loccasino;

public class PNJListener extends Feature {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEntityEvent e){
        Entity entity = e.getRightClicked();
        if (entity instanceof Villager) {
            if(entity.getCustomName() != null && entity.getCustomName().equals("§6Banquier")) {
                Player p = e.getPlayer();
                p.openInventory(PNJInventory.getBanqueInventory(p));
            }
            if(entity.getCustomName() != null && entity.getCustomName().equals("§6Vendeur de jouet")) {
                Player p = e.getPlayer();
                p.openInventory(PNJInventory.getVendeurJouet(p));
            }
            if(entity.getCustomName() != null && entity.getCustomName().equals("§6Vendeur d'encre")) {
                Player p = e.getPlayer();
                p.openInventory(PNJInventory.getVendeurEncre(p));
            }
        }
    }

    @EventHandler
    public void onHandle(InventoryDragEvent e) {
        if(e.getView().getTitle().equals("§6Banque") && e.getView().getTitle().equals("§6Vendeur de jouet") && e.getView().getTitle().equals("§6Casino") && e.getView().getTitle().equals("§6Vendeur d'encre")) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e){
        if (e.getClickedInventory() != null && e.getView().getTitle().equals("§6Banque")) {
            Inventory inv = e.getClickedInventory();
            e.setCancelled(true);
            int slot = e.getSlot();
            Player p = (Player) e.getWhoClicked();

            if (slot == 15 && inv.getItem(slot) != null){
                ItemStack[] ryo = p.getInventory().getContents();
                int amount = 0;
                ItemStack item = null;
                for (ItemStack itemactuel : ryo){
                    if (itemactuel != null){
                        if (itemactuel.getItemMeta().getDisplayName().equals("§2Billet de 10 Ryôs")){
                            if (itemactuel.getType() == Material.GOLD_NUGGET){
                                System.out.println(itemactuel.getItemMeta().getDisplayName());
                                if(ItemUtil.hasTag(itemactuel, "seisan", "ryos_billet")){
                                    if (itemactuel.getAmount() >= 1){
                                        amount = itemactuel.getAmount();
                                        item = itemactuel;
                                    }
                                }
                            }
                        }
                    }
                }
                if (item != null){
                    item.setAmount(amount - 1);
                    p.getInventory().addItem(ItemUtil.createItemStack(Material.GOLD_NUGGET, 10, "§6Ryô", Arrays.asList(), "seisan", "ryos"));
                }else {
                    p.sendMessage("§cHRP : §7Vous n'avez pas assez d'argent sur vous.");
                    p.closeInventory();
                }
            }

            if(slot == 24 && inv.getItem(slot) != null){
                ItemStack[] billet = p.getInventory().getContents();
                int amount = 0;
                ItemStack item = null;
                for (ItemStack itemactuel : billet){
                    if (itemactuel != null){
                        if (itemactuel.getType() == Material.GOLD_NUGGET){
                            if (itemactuel.getItemMeta().getDisplayName().equals("§6Ryô")){
                                if (ItemUtil.hasTag(itemactuel, "seisan", "ryos")){
                                    if (itemactuel.getAmount() >= 10){
                                        amount = itemactuel.getAmount();
                                        item = itemactuel;
                                    }
                                }
                            }
                        }
                    }
                }
                if (item != null){
                    item.setAmount(amount - 10);
                    p.getInventory().addItem(ItemUtil.createItemStack(Material.GOLD_NUGGET, 1, "§2Billet de 10 Ryôs", Arrays.asList(), "seisan", "ryos_billet"));
                }else {
                    p.sendMessage("§cHRP : §7Vous n'avez pas assez d'argent sur vous.");
                    p.closeInventory();
                }
            }
            if (slot == 33){
                ItemStack[] billet10 = p.getInventory().getContents();
                int amount = 0;
                ItemStack item = null;
                for (ItemStack itemactuel : billet10){
                    if (itemactuel != null){
                        if (itemactuel.getType() == Material.GOLD_NUGGET){
                            if (itemactuel.getItemMeta().getDisplayName().equals("§2Billet de 10 Ryôs")){
                                if (ItemUtil.hasTag(itemactuel, "seisan", "ryos_billet")){
                                    if (itemactuel.getAmount() >= 10){
                                        amount = itemactuel.getAmount();
                                        item = itemactuel;
                                    }
                                }
                            }
                        }
                    }
                }
                if (item != null){
                    item.setAmount(amount - 10);
                    p.getInventory().addItem(ItemUtil.createItemStack(Material.GOLD_NUGGET, 1, "§2Liasse de 100 Ryôs", Arrays.asList(), "seisan", "ryos_liasse"));
                }else {
                    p.sendMessage("§cHRP : §7Vous n'avez pas assez d'argent sur vous.");
                    p.closeInventory();
                }
            }
            if (slot == 42){
                ItemStack[] liasse = p.getInventory().getContents();
                int amount = 0;
                ItemStack item = null;
                for (ItemStack itemactuel : liasse){
                    if (itemactuel != null){
                        if (itemactuel.getType() == Material.GOLD_NUGGET){
                            if (itemactuel.getItemMeta().getDisplayName().equals("§2Liasse de 100 Ryôs")){
                                if (ItemUtil.hasTag(itemactuel, "seisan", "ryos_liasse")){
                                    if (itemactuel.getAmount() >= 1){
                                        amount = itemactuel.getAmount();
                                        item = itemactuel;
                                    }
                                }
                            }
                        }
                    }
                }
                if (item != null){
                    item.setAmount(amount - 1);
                    p.getInventory().addItem(ItemUtil.createItemStack(Material.GOLD_NUGGET, 10, "§2Billet de 10 Ryôs", Arrays.asList(), "seisan", "ryos_billet"));
                }else {
                    p.sendMessage("§cHRP : §7Vous n'avez pas assez d'argent sur vous.");
                    p.closeInventory();
                }
            }
        }
        else if(e.getClickedInventory() != null && e.getView().getTitle().equals("§6Vendeur de jouet")) {
            e.setCancelled(true);
            int slot = e.getSlot();
            Player p = (Player) e.getWhoClicked();

            switch(slot) {
                case 11:
                    if(PNJInventory.getRyos(p) >= 10) {
                        if(p.getInventory().firstEmpty() != -1) {
                            retirerRyo(10, p);
                            p.getInventory().addItem(ItemUtil.createItemStack(Material.PAPER, 1, "§4Boîte surprise Sankamaisu", Arrays.asList("§7Boite Sankamaisu de §2la première génération."), "seisan", "boite_sankamaisu"));
                        }
                        else {
                            p.sendMessage("§cHRP : §7Vous n'avez pas assez de place dans votre inventaire.");
                        }
                    }
                    else {
                        p.closeInventory();
                        p.sendMessage("§f<§7Vendeur de jouet §f[dit]> §aReviens plus tard quand tu auras de quoi payer ! Je te garde une boîte en réserve pour le jour où tu auras ta paie.");
                    }
                    break;
                case 15:
                    p.openInventory(PNJInventory.getEchange(p));
            }
        }
        else if(e.getView().getTitle().equals("§6Échange de figurine")) {
            int slot = e.getSlot();
            Player p = (Player) e.getWhoClicked();
            if(e.getClickedInventory() != null && e.getClickedInventory().getSize() == 9) {
                if (slot == 0 || slot == 6 || slot == 8 || slot == 7) e.setCancelled(true);
                if (slot == 7) {
                    boolean figurine = true;
                    for (int i = 1; i < 6 && figurine; i++) {
                        ItemStack itemStack = e.getClickedInventory().getItem(i);
                        if (itemStack == null || !Figurine.isFigurine(itemStack)) {
                            figurine = false;
                        }
                    }
                    if (figurine) {
                        for (int i = 1; i < 6; i++) {
                            ItemStack itemStack = e.getClickedInventory().getItem(i);
                            if (itemStack != null) {
                                itemStack.setAmount(itemStack.getAmount()-1);
                            }
                        }
                        p.getInventory().addItem(ItemUtil.createItemStack(Material.PAPER, 1, "§4Boîte surprise Sankamaisu", Arrays.asList("§7Boite Sankamaisu de §2la première génération."), "seisan", "boite_sankamaisu"));
                        p.closeInventory();
                        p.sendMessage("§cHRP : §7Vous avez échangé 5 figurines contre une seule.");
                    }
                }
            }
        }
        else if(e.getView().getTitle().equals("§6Casino")) {
            e.setCancelled(true);
            int slot = e.getSlot();
            Player p = (Player) e.getWhoClicked();
            if(p.getInventory().firstEmpty() == -1) {
                p.sendMessage("§cHRP : §7Vous n'avez pas assez de place dans votre inventaire.");
                return;
            }
            switch (slot) {
                case 11:
                    if (PNJInventory.getRyos(p) >= 10) {
                        lancerTirage(p, 1);
                    } else {
                        p.sendMessage("§b** Vous n'avez pas assez d'argent.");
                    }
                    break;
                case 13:
                    if (PNJInventory.getRyos(p) >= 50) {
                        lancerTirage(p, 5);
                    } else {
                        p.sendMessage("§b** Vous n'avez pas assez d'argent.");
                    }
                    break;
                case 15:
                    if (PNJInventory.getRyos(p) >= 100) {
                        lancerTirage(p, 10);
                    } else {
                        p.sendMessage("§b** Vous n'avez pas assez d'argent.");
                    }
                    break;
            }
        }
        else if(e.getView().getTitle().equals("§6Vendeur d'encre")) {
            e.setCancelled(true);
            int slot = e.getSlot();
            Player p = (Player) e.getWhoClicked();
            if(p.getInventory().firstEmpty() == -1) {
                p.sendMessage("§cHRP : §7Vous n'avez pas assez de place dans votre inventaire.");
                return;
            }
            if(slot == 11) {
                if (PNJInventory.getRyos(p) >= 100) {
                    for(int i = 0; i < 10; i++) { retirerRyo(10, p);}
                    PlayerInfo playerInfo = PlayerInfo.getPlayerInfo(p);
                    playerInfo.setInk(playerInfo.getInk()+10);
                    p.openInventory(PNJInventory.getVendeurEncre(p));
                }
                else {
                    p.sendMessage("§f<§8Vendeur d'encre§f[dit]> §aPas touche le fauché !");
                    p.closeInventory();
                }
            }
            else if(slot == 15) {
                if (PNJInventory.getRyos(p) >= 12) {
                    retirerRyo(10, p);
                    retirerRyo(2, p);
                    PlayerInfo playerInfo = PlayerInfo.getPlayerInfo(p);
                    playerInfo.setInk(playerInfo.getInk()+1);
                    p.openInventory(PNJInventory.getVendeurEncre(p));
                }
                else {
                    p.sendMessage("§f<§8Vendeur d'encre§f[dit]> §aPas touche le fauché !");
                    p.closeInventory();
                }
            }
        }
    }

    @EventHandler
    public void closeInventory(InventoryCloseEvent e) {
        if(e.getView().getTitle().equals("§6Échange de figurine") && e.getInventory().getSize() == 9) {
            for (ItemStack itemStack : e.getInventory().getContents()) {
                if (itemStack != null && itemStack.getType() != Material.GREEN_WOOL && itemStack.getType() != Material.GRAY_STAINED_GLASS_PANE) {
                    e.getPlayer().getWorld().dropItem(e.getPlayer().getLocation(), itemStack);
                }
            }
        }
    }

    public void retirerRyo(int nb, Player p) {
        int ryo_piece = 0;
        int ryo_billet = 0;
        int paye = nb;
        ItemStack[] inventaire = p.getInventory().getContents();
        for (ItemStack item : inventaire) {
            if (item != null && item.getType() == Material.GOLD_NUGGET && item.hasItemMeta()) {
                ItemMeta meta = item.getItemMeta();
                if (meta != null && meta.hasDisplayName()) {
                    switch (meta.getDisplayName()) {
                        case "§6Ryô":
                            if (ItemUtil.hasTag(item, "seisan", "ryos")) {
                                ryo_piece += item.getAmount();
                            }
                            break;
                        case "§2Billet de 10 Ryôs":
                            if (ItemUtil.hasTag(item, "seisan", "ryos_billet")) {
                                ryo_billet += item.getAmount();
                            }
                            break;
                    }
                }
            }
        }
        if (ryo_piece >= nb) {
            int i = 0;
            while (i < inventaire.length && paye != 0) {
                ItemStack item = inventaire[i];
                if (item != null && item.getType() == Material.GOLD_NUGGET && item.hasItemMeta()) {
                    ItemMeta meta = item.getItemMeta();
                    if (meta != null && meta.hasDisplayName() && meta.getDisplayName().equals("§6Ryô")) {
                        if (item.getAmount() > paye) {
                            inventaire[i].setAmount(item.getAmount() - paye);
                            paye = 0;
                        } else {
                            paye -= item.getAmount();
                            inventaire[i].setAmount(0);
                        }
                    }
                }
                i++;
            }
        } else if (ryo_billet >= nb/10) {
            int i = 0;
            while (i < inventaire.length && paye != 0) {
                ItemStack item = inventaire[i];
                if (item != null && item.getType() == Material.GOLD_NUGGET && item.hasItemMeta()) {
                    ItemMeta meta = item.getItemMeta();
                    if (meta != null && meta.hasDisplayName() && meta.getDisplayName().equals("§2Billet de 10 Ryôs")) {
                        inventaire[i].setAmount(item.getAmount() - 1);
                        if(nb < 10) {
                            p.getInventory().addItem(ItemUtil.createItemStack(Material.GOLD_NUGGET, 10 - nb, "§6Ryô", Arrays.asList(), "seisan", "ryos"));
                        }
                        paye = 0;
                    }
                }
                i++;
            }
        } else {
            int i = 0;
            while (i < inventaire.length && paye != 0) {
                ItemStack item = inventaire[i];
                if (item != null && item.getType() == Material.GOLD_NUGGET && item.hasItemMeta()) {
                    ItemMeta meta = item.getItemMeta();
                    if (meta != null && meta.hasDisplayName() && meta.getDisplayName().equals("§2Liasse de 100 Ryôs")) {
                        inventaire[i].setAmount(item.getAmount() - 1);
                        p.getInventory().addItem(ItemUtil.createItemStack(Material.GOLD_NUGGET, 100 - nb, "§6Ryô", Arrays.asList(), "seisan", "ryos"));
                        paye = 0;
                    }
                }
                i++;
            }
        }
    }

    public void lancerTirage(Player p, int nbtirage) {
        p.closeInventory();
        int total = 0;
        for(int i = 0; i < nbtirage; i++) {
            retirerRyo(10, p);
            Casino casino = Casino.getFromRoll();
            int nb = casino.getRyo();
            if (nb != 0 && nb < 100) {
                p.getInventory().addItem(ItemUtil.createItemStack(Material.GOLD_NUGGET, nb / 10, "§2Billet de 10 Ryôs", Arrays.asList(), "seisan", "ryos_billet"));
            }
            else {
                p.getInventory().addItem(ItemUtil.createItemStack(Material.GOLD_NUGGET, nb/100, "§2Liasse de 100 Ryôs", Arrays.asList(), "seisan", "ryos_liasse"));
            }
            total += nb;
            jouerparticule(p, casino);
        }
        p.sendMessage("§b** Vous avez gagné un total de "+total+" ryôs.");
    }

    private void jouerparticule(Player p, Casino casino) {
        Location l = loccasino.get(p.getName());
        if(l != null && l.getWorld() != null) {
            l.getWorld().spawnParticle(Particle.CRIT, l.getX() + 0.5, l.getY() + 0.5, l.getZ() + 0.5,  casino.getNbparticle(), casino.getTime(), casino.getTime(), casino.getTime());
            l.getWorld().spawnParticle(Particle.NOTE, l.getX() + 0.5, l.getY() + 0.5, l.getZ() + 0.5, casino.getNbparticle(), casino.getTime(), casino.getTime(), casino.getTime());
        }

    }
}
