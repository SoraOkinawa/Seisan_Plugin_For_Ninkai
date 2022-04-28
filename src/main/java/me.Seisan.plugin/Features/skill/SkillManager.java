package me.Seisan.plugin.Features.skill;

import lombok.Getter;
import lombok.Setter;
import me.Seisan.plugin.Features.Feature;
import me.Seisan.plugin.Features.PlayerData.PlayerInfo;
import me.Seisan.plugin.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

/**
 * Created by Helliot on 10/02/2018.
 */
public class SkillManager extends Feature {


    @Setter
    @Getter
    public static boolean skillEnabled = true;


    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        if(skillEnabled) {
            if (e.getHand() != null && e.getHand().equals(EquipmentSlot.HAND)) {
                if (e.getPlayer().isSneaking()) {
                    PlayerInfo pInfo = PlayerInfo.getPlayerInfo(e.getPlayer());
                    if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) {
                        if (pInfo.getCurrentSkill() != null) {
                            if(Main.getCurrentSelectSkill().containsKey(e.getPlayer().getName())) {
                                Bukkit.getScheduler().cancelTask(Main.getCurrentSelectSkill().get(e.getPlayer().getName()));
                                Main.getCurrentSelectSkill().remove(e.getPlayer().getName());
                            }
                            pInfo.getCurrentSkill().launch(pInfo);
                        }
                    }
                }
            }
        }else{
            e.getPlayer().sendMessage(ChatColor.RED + "Les jutsus sont désactivés pour le moment. Ils sont peut être en rechargement.");
        }
    }

    @EventHandler
    public void onInteractEntity(PlayerInteractAtEntityEvent e){
        if(skillEnabled) {
            if (e.getHand().equals(EquipmentSlot.HAND)) {
                if (e.getPlayer().isSneaking()) {
                    PlayerInfo pInfo = PlayerInfo.getPlayerInfo(e.getPlayer());
                    if (pInfo.getCurrentSkill() != null) {
                        Skill skill = pInfo.getCurrentSkill();
                        skill.launch(pInfo, e.getRightClicked());
                    }
                }
            }
        }else{
            e.getPlayer().sendMessage(ChatColor.RED + "Les jutsus sont désactivés pour le moment. Ils sont peut être en rechargement.");
        }
    }
}
