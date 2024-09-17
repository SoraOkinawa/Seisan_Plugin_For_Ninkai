package me.Seisan.plugin.Features.listener;


import me.Seisan.plugin.Features.Feature;
import me.Seisan.plugin.Features.PlayerData.PlayerInfo;
import me.Seisan.plugin.Main;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.Controllable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.Objects;


public class CloneListener extends Feature {


    @EventHandler
    public void onCloneClick(NPCRightClickEvent e){
        NPC npc = e.getNPC();
        Player p = e.getClicker();
        PlayerInfo pInfo = PlayerInfo.getPlayerInfo(p);

        if(npc.getTrait(Controllable.class).isEnabled() && !pInfo.getPlayerClone().own(npc)){
//            p.sendMessage(ChatColor.RED + "Vous ne pouvez pas controler un clone qui ne vous appartient pas !");
            Location loc = p.getLocation();

            if(npc.getEntity() instanceof Player) {
                Player playernpc = (Player)npc.getEntity();
                if (playernpc.getPassengers().isEmpty()) { //Si faux, le propriétaire du clone est déjà dessus, pas besoin d'eject
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Main.plugin(), () -> {
                        playernpc.eject();
                        p.teleport(loc); //On retp le joueur à sa position initiale
                    }, 1L);
                }
            }
        }
        else if(npc.getName().equals("§7[En médit.]")) {

            // Il a pas la compétence
            if(!pInfo.hasAbility("meditation_ouverte")) {
                return;
            }

            String target_namemc = Main.getInBulleMedit().keySet().stream()
                    .filter(s -> Main.getInBulleMedit().get(s).equals(s))
                    .filter(s -> PlayerInfo.getPlayerInfo(Objects.requireNonNull(Bukkit.getPlayer(s)))
                            .getPlayerClone().ownMedit(npc)).findFirst().orElse(null);


            if(target_namemc == null) {
                p.sendMessage("§b** Il est impossible de se lier à la méditation de cette personne...");
                return;
            }

            Player target = Bukkit.getPlayer(target_namemc);
            if(target == null) {
                p.sendMessage("§b** Il est impossible de se lier à la méditation de cette personne...");
                return;
            }

            target.sendMessage("§b** "+p.getDisplayName()+" cherche à rejoindre votre bulle de méditation.");
            target.sendMessage("§cHRP : §7Pour accepter l'accès : /meditation accept "+p.getName());
            target.sendMessage("§cHRP : §7Pour refuser l'accès : /meditation deny "+p.getName());
            p.sendMessage("§b** Vous cherchez à vous lier à la bulle de méditation de "+target.getDisplayName()+". Attendez qu'il forme le lien entre vous et lui.");
            Main.getAskMedit().put(p.getName(), target_namemc);
        }
    }

}