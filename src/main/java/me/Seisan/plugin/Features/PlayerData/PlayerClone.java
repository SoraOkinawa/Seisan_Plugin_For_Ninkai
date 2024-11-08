package me.Seisan.plugin.Features.PlayerData;

import lombok.Getter;
import me.Seisan.plugin.Main;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCDataStore;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.citizensnpcs.api.npc.SimpleNPCDataStore;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.trait.TraitInfo;
import net.citizensnpcs.api.trait.TraitName;
import net.citizensnpcs.api.trait.trait.Equipment;
import net.citizensnpcs.api.util.NBTStorage;
import net.citizensnpcs.trait.Controllable;
import net.citizensnpcs.trait.SkinTrait;
import net.citizensnpcs.util.PlayerAnimation;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Helliot on 15/04/2018
 */
public class PlayerClone {

    @Getter
    private static NPCRegistry nindoRegistery;

    @Getter
    private static HashMap<Player, Integer> cloneTicket = new HashMap<>();

    private ArrayList<NPC> cloneList = new ArrayList<>();
    private PlayerInfo pInfo;
    private NPC cloneMedit;

    public PlayerClone(PlayerInfo pInfo){
        this.pInfo = pInfo;
    }

    public static void init(){

        if(CitizensAPI.getNamedNPCRegistry("Seisan") == null){
            try {
                NBTStorage storage = new NBTStorage(new File(Main.plugin().getDataFolder().getCanonicalPath() + "/NPC.yml"));
                NPCDataStore data = SimpleNPCDataStore.create(storage);
                CitizensAPI.createNamedNPCRegistry("Seisan", data);
            }catch(IOException e){
                e.printStackTrace();
                Bukkit.getServer().shutdown();
            }
        }

        CitizensAPI.getNamedNPCRegistry("Seisan").deregisterAll();
        nindoRegistery = CitizensAPI.getNamedNPCRegistry("Seisan");

        if(nindoRegistery == null){
            Bukkit.getServer().shutdown();
        }
    }

    public void destroyAllClones(){
        ArrayList<NPC> duplicate = (ArrayList<NPC>) cloneList.clone(); //Quelle ironie
        for(NPC npc : duplicate){
            cloneList.remove(npc);
            npc.destroy();
        }
        if(cloneMedit != null) {
            cloneMedit.destroy();
        }
        cloneMedit = null;
    }

    public NPC get(int id){
        if(id >= 0 && id <= cloneList.size() -1)
            return cloneList.get(id);
        else
            return null;
    }

    public void destroy(NPC npc){
        cloneList.remove(npc);
        npc.destroy();

        if(!cloneList.isEmpty()){
            for(int i = 0; i<cloneList.size(); i++){
                cloneList.get(i).setName(ChatColor.GRAY + "(" + i + ")");
            }
        }
    }

    public boolean own(NPC npc){
        return cloneList.contains(npc);
    }

    public boolean ownMedit(NPC npc) {
        return cloneMedit.equals(npc);
    }


    public NPC createClone(){
        PlayerInventory inventory = pInfo.getPlayer().getInventory();
        NPC npc =
                nindoRegistery
                .createNPC(
                EntityType.PLAYER,
                ChatColor.GRAY + "(" +
                cloneList.size() + ")");
        
        npc.getOrAddTrait(SkinTrait.class).setSkinName(pInfo.getPlayer().getName());
        
        ItemStack item = inventory.getHelmet();
        if (item != null) {
            npc.getTrait(Equipment.class).set(Equipment.EquipmentSlot.HELMET, item);
        }
        item = inventory.getChestplate();
        if (item != null) {
            npc.getTrait(Equipment.class).set(Equipment.EquipmentSlot.CHESTPLATE, item);
        }
        item = inventory.getLeggings();
        if (item != null) {
            npc.getTrait(Equipment.class).set(Equipment.EquipmentSlot.LEGGINGS, item);
        }
        item = inventory.getBoots();
        if (item != null) {
            npc.getTrait(Equipment.class).set(Equipment.EquipmentSlot.BOOTS, item);
        }
        item = inventory.getItemInMainHand();
        npc.getTrait(Equipment.class).set(Equipment.EquipmentSlot.HAND, item);
        item = inventory.getItemInOffHand();
            npc.getTrait(Equipment.class).set(Equipment.EquipmentSlot.OFF_HAND, item);

        npc.getTrait(Controllable.class).setEnabled(true);


        npc.spawn(pInfo.getPlayer().getLocation());

        cloneList.add(npc);
        return npc;
    }

    public static void put(Player p, int nbMax) {

        PlayerClone.getCloneTicket().put(p, nbMax);
        p.sendMessage(ChatColor.GREEN + "Vous êtes autorisé à créer " + nbMax + (nbMax > 1 ? " clones":" clone") + " maximum à l'aide de la commande " + ChatColor.GOLD + "/clone create " + ChatColor.GRAY + "nombre");

        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.plugin(), () -> {
            if (p.isOnline() && PlayerClone.getCloneTicket().containsKey(p)) {
                PlayerClone.getCloneTicket().remove(p);
                p.sendMessage(ChatColor.RED + "Le délai pour créer vos clones a été dépassé !");
            }
        }, 20 * 60 * 2L); //Le joueur a 2 minutes pour créer ses clones, afin d'éviter un abuse
    }

    public void createCloneMedit(){
        NPC npc =
                nindoRegistery.createNPC(
                EntityType.PLAYER,"§7[En médit.]");

        npc.getTrait(Controllable.class).setEnabled(false);
        npc.getTrait(SkinTrait.class).setSkinName(pInfo.getPlayer().getName(), true);
        Location l = pInfo.getPlayer().getLocation();
        npc.spawn(l);
        PlayerAnimation.SIT.play((Player) npc.getEntity());
        this.cloneMedit = npc;
    }

    public void deleteCloneMedit() {
        if(cloneMedit != null) {
            cloneMedit.destroy();
            cloneMedit = null;
        }

    }
}
