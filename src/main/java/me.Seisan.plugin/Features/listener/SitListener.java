package me.Seisan.plugin.Features.listener;

import me.Seisan.plugin.Features.Feature;
import me.Seisan.plugin.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.block.data.type.Bed;
import org.bukkit.block.data.type.Slab;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.material.Banner;
import org.bukkit.util.Vector;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.util.List;

public class SitListener extends Feature {
    @EventHandler(priority = EventPriority.HIGH)
    public void removeArmorStandSit(EntityDismountEvent event) {
        if(event.getEntity() instanceof Player) {
            Entity vehicle = event.getDismounted();
            if (vehicle instanceof ArmorStand) {
                if (vehicle.getCustomName() != null) {
                    if (vehicle.getCustomName().equals("Sit")) {
                        event.getEntity().getScoreboardTags().remove("Assis");
                        vehicle.eject();
                        vehicle.remove();
                        tp((Player)event.getEntity(), event.getEntity().getLocation());
                        event.getEntity().getScoreboardTags().remove("Chaise");
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onTeleport(PlayerTeleportEvent e) {
        Entity vehicle = e.getPlayer().getVehicle();
        if (vehicle != null) {
            if (vehicle instanceof ArmorStand) {
                if (vehicle.getCustomName() != null) {
                    if (vehicle.getCustomName().equals("Sit")) {
                        e.getPlayer().getScoreboardTags().remove("Assis");
                        vehicle.remove();
                        e.getPlayer().getScoreboardTags().remove("Chaise");
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void ServerLoading(WorldLoadEvent event) {
        event.getWorld().getEntitiesByClass(ArmorStand.class).forEach(armorStand ->
        {
            String name = armorStand.getCustomName();
            if(name!=null)
            {
                if(name.equals("Sit")) {
                    armorStand.remove();
                }
            }
        });
    }
    @EventHandler(priority = EventPriority.MONITOR)
    public void PlayerQuitListener(PlayerQuitEvent event)
    {
        standUp(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void PlayerJoinListener(PlayerJoinEvent event) { standUp(event.getPlayer());}

    @EventHandler(priority = EventPriority.LOW)
    public void BlockBreakListener(BlockBreakEvent event) {
        boolean test = false;
        for (Entity entity : event.getBlock().getWorld().getNearbyEntities(event.getBlock().getLocation(),0.8, 2, 0.8)) {
            if (entity instanceof Player) {
                if (!entity.getScoreboardTags().isEmpty()) {
                    if (entity.getScoreboardTags().contains("Assis")) {
                        test = true;
                    }
                }
            }
        }
        if (test) {
            Player player = event.getPlayer();
            player.sendMessage(ChatColor.RED + "HRP : " + ChatColor.GRAY + "Vous ne pouvez pas détruire ce bloc, un joueur est assis non loin!");
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void Changement(PlayerSwapHandItemsEvent e) {
        Entity vehicle = e.getPlayer().getVehicle();
        if (vehicle != null) {
            if (vehicle instanceof ArmorStand) {
                if (vehicle.getCustomName() != null) {
                    if (vehicle.getCustomName().equals("Sit")) {
                        if(!e.getPlayer().getScoreboardTags().contains("Chaise")) {
                            e.getPlayer().getVehicle().getLocation().setDirection(e.getPlayer().getEyeLocation().getDirection());
                        }
                    }
                }
            }
        }
    }

    public static void clear() {
        for(Player p : Bukkit.getServer().getOnlinePlayers()) {
            standUp(p);
        }
    }

    private static void standUp(Player player)
    {
        Entity vehicle = player.getVehicle();
        if (vehicle != null) {
            if (vehicle instanceof ArmorStand) {
                if(vehicle.getCustomName() != null) {
                    if (vehicle.getCustomName().equals("Sit")) {
                        player.getScoreboardTags().remove("Assis");
                        player.getScoreboardTags().remove("Chaise");
                        player.leaveVehicle();
                        vehicle.remove();
                    }
                }
            }
        }
    }

    public boolean isDangerous(Material... materials)
    {
        for(Material material : materials)
        {
            if(material.isSolid() && !Material.PAINTING.equals(material) && !material.name().contains("SIGN") && !Material.VINE.equals(material) && !Material.LADDER.equals(material))
            {
                return true;
            }
        }
        return false;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onItemTouch(PlayerInteractEvent event)
    {
        if (EquipmentSlot.OFF_HAND.equals(event.getHand()))
        {
            return;
        }
        Action action = event.getAction();
        Block clickedBlock = event.getClickedBlock();

        final Player player = event.getPlayer();
        if(clickedBlock == null)
        {
            return;
        }
        Location blockLocation = clickedBlock.getLocation();
        if (!player.isSneaking() && action == Action.RIGHT_CLICK_BLOCK && (isBottomStair(clickedBlock) || isBottomHalfSlab(clickedBlock)) && event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.AIR) && (player.isOnGround() || player.isInsideVehicle()))
        {
            standUp(player);
            if(isDangerous(blockLocation.clone().add(0,2,0).getBlock().getType(),blockLocation.clone().add(0,1,0).getBlock().getType()))
            {
                player.sendMessage(ChatColor.RED + "HRP : Vous allez suffoquer si vous vous asseyez ici!");
                return;
            }
            if (player.getLocation().distance(blockLocation.clone().add(0.5,0,0.5)) > 1.5) {
                return;
            }
            Vector direction;
            if(isBottomHalfSlab(clickedBlock))
            {
                direction = player.getLocation().getDirection(); //direction for half slabs
            }
            else
            {
                direction = vectorize(((Stairs)(clickedBlock.getBlockData())).getFacing().getOppositeFace()); //direction for stairs
            }
            final ArmorStand vehicle = player.getWorld().spawn(blockLocation.clone().add(0.5, -1.2, 0.5).setDirection(direction),ArmorStand.class);
            vehicle.setGravity(false);
            vehicle.setVisible(false);
            //in case 2 players would want to stand on the same place.
            List<Entity> seats = vehicle.getNearbyEntities(0.2, 0.2, 0.2);
            if(seats.size()>1)
            {
                for(Entity seat : seats)
                {
                    if(!seat.getPassengers().isEmpty())
                    {
                        vehicle.remove();
                        player.sendMessage(ChatColor.RED + "HRP : §7Vous ne pouvez pas vous assoir a deux sur un meme siege!");
                        return;
                    }
                }
            }
            vehicle.addPassenger(player);
            vehicle.setCustomName("Sit");
            player.getScoreboardTags().add("Assis");
            if(!isBottomHalfSlab(clickedBlock)) {
                player.getScoreboardTags().add("Chaise");
            }

        }
    }


    private boolean isBottomStair(Block block)
    {
        if (block.getType().name().contains("STAIRS"))
        {
            return block.getBlockData().toString().contains("half=bottom");
        }
        return false;
    }

    private boolean isBottomHalfSlab(Block block)
    {
        if (block.getBlockData() instanceof Slab) {
            if (((Slab) block.getBlockData()).getType() == Slab.Type.BOTTOM) {
                return true;
            }
        }
        return (block.getBlockData() instanceof Bed);

    }

    private Vector vectorize(BlockFace face)
    {
        if(face == BlockFace.NORTH)
            return new Vector(0,0,-1);
        if(face == BlockFace.SOUTH)
            return new Vector(0,0,1);
        if(face == BlockFace.EAST)
            return new Vector(1,0,0);
        if(face == BlockFace.WEST)
            return new Vector(-1,0,0);
        if(face == BlockFace.NORTH_EAST)
            return new Vector(1,0,-1);
        if(face == BlockFace.NORTH_WEST)
            return new Vector(-1,0,-1);
        if(face == BlockFace.SOUTH_EAST)
            return new Vector(1,0,1);
        if(face == BlockFace.SOUTH_WEST)
            return new Vector(-1,0,1);
        return new Vector(0,0,0);
    }

    private static void tp(Player p, Location l) {
        Bukkit.getScheduler().runTaskLater(Main.plugin(), () -> {
            if(!Main.getInMedit().containsKey(p.getName())) {
                p.teleport(l.add(0, 1.2, 0));
            }
        }, 2);
    }

}
