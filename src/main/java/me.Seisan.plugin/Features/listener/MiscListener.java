package me.Seisan.plugin.Features.listener;

import me.Seisan.plugin.Features.Feature;
import me.Seisan.plugin.Features.Inventory.CasinoInventory;
import me.Seisan.plugin.Features.PlayerData.PlayerInfo;
import me.Seisan.plugin.Features.skill.Skill;
import me.Seisan.plugin.Features.skill.SkillMastery;
import me.Seisan.plugin.Features.utils.ItemUtil;
import me.Seisan.plugin.Main;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scoreboard.Score;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static me.Seisan.plugin.Features.listener.Listener.antimaccro;
import static me.Seisan.plugin.Features.listener.Listener.loccasino;
import static me.Seisan.plugin.Features.objectnum.Figurine.getFromLuck;

public class MiscListener extends Feature {

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void OnHitBlock(PlayerInteractEvent event) {
        // Si c'est un clic gauche
        if(event.getAction() == Action.LEFT_CLICK_BLOCK) {
    /*        if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.BARRIER) {
                Location locvase = (event.getClickedBlock().getLocation().add(0,-1,0));
                ItemFrame itemframe = getItemFrameAt(locvase);
                if(itemframe!= null) {
                    if(itemframe.getItem().getType() == Material.CLOCK) {
                        ItemStack vase = itemframe.getItem();
                        if(isVase(vase)) {
                            if(Bukkit.getScoreboardManager() != null) {
                                Score score = Bukkit.getScoreboardManager().getMainScoreboard().getObjective("vase").getScore("Veziah");
                                Location l = event.getClickedBlock().getLocation();
                                if (score.getScore() != 100000000) {
                                    if (l.getWorld() != null) {
                                        l.getWorld().spawnParticle(Particle.DAMAGE_INDICATOR, l.getX() + 0.5, l.getY() + 0.5, l.getZ() + 0.5, 5);
                                        l.getWorld().spawnParticle(Particle.SMOKE_NORMAL, l.getX() + 0.2, l.getY() + 0.2, l.getZ() + 0.2, 5);
                                        int value = Bukkit.getScoreboardManager().getMainScoreboard().getObjective("vase").getScore("Veziah").getScore();
                                        if(LimiteCoup(event.getPlayer())) {
                                            PlayerInfo playerInfo = PlayerInfo.getPlayerInfo(event.getPlayer());
                                            score.setScore(value + playerInfo.getCaract().getForce());
                                            switch (score.getScore()) {
                                                case 25000000:
                                                case 25000001:
                                                case 25000002:
                                                case 25000003:
                                                    itemframe.setItem(updateVase(vase, "vase1"));
                                                    // Un peu cassé
                                                    break;
                                                case 50000000:
                                                case 50000001:
                                                case 50000002:
                                                case 50000003:
                                                    itemframe.setItem(updateVase(vase, "vase2"));
                                                    // Un peu bcp cassé
                                                    break;
                                                case 75000000:
                                                case 75000001:
                                                case 75000002:
                                                case 75000003:
                                                    itemframe.setItem(updateVase(vase, "vase3"));
                                                    // Bcp cassé
                                                    break;
                                                case 99999999:
                                                case 99999998:
                                                case 99999997:
                                                case 99999996:
                                                    itemframe.setItem(updateVase(vase, "vase4"));
                                                    // CASsE
                                                    break;
                                            }
                                        }
                                    }
                                }
                                else {
                                    event.getPlayer().sendMessage(ChatColor.AQUA+"** Le vase se casse au prochain coup...");
                                    event.getPlayer().sendMessage(ChatColor.RED+"HRP : "+ ChatColor.GRAY+" Contactez Shikure en HRP.");
                                }
                            }
                        }
                    }
                }
            }*/
        }
        else if(event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.STONE_BUTTON){
            Location locclef = (event.getClickedBlock().getLocation().add(0,1,0));
            ItemFrame itemframe = getItemFrameAt(locclef);
            if(itemframe != null) {
                ItemStack itemStack = itemframe.getItem();
                if (itemStack.getType() == Material.CLOCK && ItemUtil.hasTag(itemStack, "seisan", "clef_or") && itemStack.hasItemMeta() && itemStack.getItemMeta() != null && itemStack.getItemMeta().hasDisplayName() && itemStack.getItemMeta().getDisplayName().equals("§6TENTEZ VOTRE CHANCE")) {
                    event.getPlayer().openInventory(CasinoInventory.getCasino(event.getPlayer()));
                    loccasino.put(event.getPlayer().getName(), locclef);
                }
            }
        }
    }

   /*
   @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void LogCopy(InventoryCreativeEvent event) {
        System.out.println("aaaa");
        if(event.getWhoClicked().getGameMode() == GameMode.CREATIVE) {
            if(event.getCurrentItem() == null) {
                return;
            }
                System.out.println("§cHRP : §7" + event.getWhoClicked().getName() + " a copié " + event.getCursor().getType().name());

            if(event.getCursor().getType() == Material.GOLD_NUGGET) {
                for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                    if(p.isOp()) {
                        p.sendMessage("§cHRP : "+event.getWhoClicked().getName() + "§7 a copié " + event.getCursor().getType().name());
                    }
                }
            }
        }
    }*/
/*
    private boolean isVase(ItemStack vase) {
        boolean isVase = false;
            net.minecraft.server.v1_15_R1.ItemStack stack = CraftItemStack.asNMSCopy(vase);
            NBTTagCompound tag = stack.getTag() != null ? stack.getTag() : new NBTTagCompound();
            if(tag.hasKey("seisan") && tag.getString("seisan").contains("vase")) {
                // Blabla vérif du tag
                isVase = true;
        }
        return isVase;
    }

    private ItemStack updateVase(ItemStack vase, String newvase) {
        net.minecraft.server.v1_15_R1.ItemStack stack = CraftItemStack.asNMSCopy(vase);
        NBTTagCompound tag = stack.getTag() != null ? stack.getTag() : new NBTTagCompound();
        if(tag.hasKey("seisan") && tag.getString("seisan").contains("vase")) {
            // Blabla vérif du tag
            tag.setString("seisan", newvase);
        }
        stack.setTag(tag);
        return CraftItemStack.asBukkitCopy(stack);

    }*/


    @EventHandler(priority = EventPriority.HIGH)
    public void PickUpLantern(PlayerInteractEvent event) {
        Block b = event.getClickedBlock();
        Player p = event.getPlayer();
        if(b != null && b.getType() == Material.LANTERN) {
            if(event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
                ItemStack itemStack = new ItemStack((Material.LANTERN));
                if(event.getHand() == EquipmentSlot.HAND && p.getInventory().getItemInMainHand().getType() == Material.AIR) {
                    if(b.getType() == Material.LANTERN) {
                        b.setType(Material.AIR);
                        p.getInventory().setItemInMainHand(itemStack);
                        event.setCancelled(true);
                    }
                    return;
                }
                if(event.getHand() == EquipmentSlot.OFF_HAND && p.getInventory().getItemInOffHand().getType() == Material.AIR) {
                    if(b.getType() == Material.LANTERN) {
                        b.setType(Material.AIR);
                        p.getInventory().setItemInOffHand(itemStack);
                        event.setCancelled(true);
                    }
                }

            }
        }
    }


    private ItemFrame getItemFrameAt(Location l) {
        ItemFrame frame = null;
        if(l.getWorld() != null) {
            for (Entity entity : l.getWorld().getEntities()) {
                if(entity instanceof ItemFrame) {
                    if(entity.getLocation().getBlock().getLocation().equals(l)) {
                        frame = (ItemFrame)entity;
                    }
                }
            }
        }
        return frame;
    }

 /*   private boolean LimiteCoup(Player player) {
        boolean test = false;

        if(!antimaccro.containsKey(player.getName())) {
            antimaccro.put(player.getName(), 1);
            test = true;
        }
        else {
            int nbcoup = antimaccro.get(player.getName());
            if(nbcoup <= 15) {
                test = true;
                antimaccro.put(player.getName(), nbcoup+1);
            }
        }

        if(test) Bukkit.getScheduler().runTaskLater(Main.plugin(), () -> antimaccro.put(player.getName(), antimaccro.get(player.getName())-1), 20);
        return test;
    }
*/
    @EventHandler
    public void onDie(PlayerDeathEvent e){
        e.setDeathMessage("");
    }

    @EventHandler
    public void MountNinken(PlayerInteractEntityEvent e) {
        String name = e.getPlayer().getName();
        if(e.getHand() == EquipmentSlot.HAND) {
            return;
        }
        if(e.getRightClicked() instanceof Wolf) {
            if (Main.getIDninkenFromNamePlayer().containsKey(name)) {
                if(e.getRightClicked().getUniqueId().equals(Main.getIDninkenFromNamePlayer().get(name))) {
                    e.getPlayer().sendMessage("§b** Votre Ninken est trop petit pour le monter.");
                }
            }
        }
    }

    @EventHandler
    public void onLearnJutsu(PlayerInteractEvent e) {
        if (e.getHand() != null && e.getHand().equals(EquipmentSlot.HAND)) {
            if (e.getPlayer().isSneaking()) {
                if(e.getItem() != null) {
                    ItemStack stack = e.getItem();
                    if(ItemUtil.hasTag(stack, "jutsu", "learn")) {
                        PlayerInfo pInfo = PlayerInfo.getPlayerInfo(e.getPlayer());
                        Skill skill = Skill.getByRealName(stack.getItemMeta().getDisplayName());
                        if(skill != null) {
                            if (!pInfo.getSkills().containsKey(skill)) {
                                pInfo.updateSkill(skill, SkillMastery.UNLEARNED);
                                pInfo.getPlayer().getInventory().setItemInMainHand(null);
                            }
                            else {
                                e.getPlayer().sendMessage("§cHRP : §7Vous connaissez déjà la technique "+skill.getName());
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onOpenItem(PlayerInteractEvent e) {
        if(e.getHand() != null && e.getHand().equals(EquipmentSlot.HAND)) {
            if(e.getPlayer().isSneaking()) {
                if(e.getItem() != null && e.getItem().getType() == Material.PAPER) {
                    ItemStack stack = e.getItem();
                    if(ItemUtil.hasTag(stack, "seisan", "boite_sankamaisu")) {
                        e.setCancelled(true);
                        if(e.getPlayer().getInventory().firstEmpty() != -1) {
                            if (e.getItem().getAmount() > 1) {
                                e.getItem().setAmount(e.getItem().getAmount() - 1);
                                e.getPlayer().getInventory().setItemInMainHand(e.getItem());
                            } else {
                                e.getPlayer().getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                            }
                            e.getPlayer().getInventory().addItem(getFromLuck());
                            e.getPlayer().updateInventory();
                        }
                        else {
                            e.getPlayer().sendMessage("§cHRP : §7Vous n'avez pas assez de place dans votre inventaire.");
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void commandPreprocess(PlayerCommandPreprocessEvent e) {
        String command = e.getMessage().split(" ")[0];
        command = command.substring(1);
        if(command.equalsIgnoreCase("reload")) {
            e.getPlayer().sendMessage(ChatColor.RED + "Le reload casse le plugin de Seisan, faites un /stop à la place");
            e.setCancelled(true);
        }else if(command.equalsIgnoreCase("help")){
            e.getPlayer().sendMessage(ChatColor.RED + "Bien tenté, mais non, pas de /help");
            e.setCancelled(true);
        }else if(command.equalsIgnoreCase("pl") || command.equalsIgnoreCase("plugin") || command.equalsIgnoreCase("plugins")){
            e.getPlayer().sendMessage("Plugins (1): " + ChatColor.GREEN + "Seisan");
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void consoleCommandPreprocess(ServerCommandEvent e) {
        if (e.getCommand().equalsIgnoreCase("reload")) {
            e.getSender().sendMessage(ChatColor.RED + "Le reload casse le plugin de Seisan, faites un /stop à la place");
            e.setCancelled(true);
        }
    }
}
